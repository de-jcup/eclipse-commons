package de.jcup.eclipse.commons.templates;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.templates.Template;
import org.eclipse.jface.text.templates.TemplateCompletionProcessor;
import org.eclipse.jface.text.templates.TemplateContext;
import org.eclipse.jface.text.templates.TemplateContextType;
import org.eclipse.jface.text.templates.TemplateException;
import org.eclipse.jface.text.templates.TemplateProposal;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import de.jcup.eclipse.commons.PluginContextProvider;
import de.jcup.eclipse.commons.ui.EclipseUtil;

public class SimpleTemplateCompletionProcessor extends TemplateCompletionProcessor {

    private static final Template[] NO_TEMPLATES = new Template[] {};

    private TemplateSupport support;
    private TemplateSupportConfig config;
    private PluginContextProvider provider;
    private TemplateStrategy strategy;

    private static final class ProposalComparator implements Comparator<ICompletionProposal> {
        @Override
        public int compare(ICompletionProposal o1, ICompletionProposal o2) {
            int r1 = o1 instanceof TemplateProposal ? ((TemplateProposal) o1).getRelevance() : 0;
            int r2 = o2 instanceof TemplateProposal ? ((TemplateProposal) o2).getRelevance() : 0;
            return r2 - r1;
        }
    }

    private static final Comparator<ICompletionProposal> proposalComparator = new ProposalComparator();

    SimpleTemplateCompletionProcessor(TemplateSupport support) {
        if (support == null) {
            throw new IllegalArgumentException("support may not be null!");
        }
        this.support = support;
        this.config = support.getConfig();
        this.provider = support.getProvider();
    }

    @Override
    public ICompletionProposal[] computeCompletionProposals(ITextViewer viewer, int offset) {
        /*
         * This is nearly as origin code, but blow we check if matching > 0. If not we
         * do not add the proposal which removes unwanted parts
         */
        ITextSelection selection = (ITextSelection) viewer.getSelectionProvider().getSelection();
        if (selection == null) {
            return new ICompletionProposal[0];
        }
        // adjust offset to end of normalized selection
        if (selection.getOffset() == offset) {
            offset = selection.getOffset() + selection.getLength();
        }

        String prefix = extractPrefix(viewer, offset);
        Region region = new Region(offset - prefix.length(), prefix.length());
        TemplateContext context = createContext(viewer, region);
        if (context == null) {
            return new ICompletionProposal[0];
        }

        context.setVariable("selection", selection.getText()); // name of the selection variables {line, word}_selection //$NON-NLS-1$

        Template[] templates = getTemplates(context.getContextType().getId());

        List<ICompletionProposal> matches = new ArrayList<>();
        for (Template template : templates) {
            try {
                context.getContextType().validate(template.getPattern());
            } catch (TemplateException e) {
                continue;
            }
            if (template.matches(prefix, context.getContextType().getId())) {
                int relevance = getRelevance(template, prefix);
                /* here is the change: only when relevance >0 add the match! */
                if (relevance > RelevanceConstants.DOES_NOT_MATCH ) {
                    matches.add(createProposal(template, context, (IRegion) region, relevance));
                }
            }
        }

        Collections.sort(matches, proposalComparator);

        return matches.toArray(new ICompletionProposal[matches.size()]);
    }

    @Override
    protected String extractPrefix(ITextViewer viewer, int offset) {
        TemplateStrategy strategy = getStrategy();
        String prefix = strategy.extractPrefix(viewer, offset);
        if (prefix != null) {
            return prefix;
        }
        return super.extractPrefix(viewer, offset);
    }

    @Override
    protected int getRelevance(Template template, String prefix) {
        TemplateStrategy strategy = getStrategy();
        Integer relevance = strategy.getRelevance(template, prefix);
        if (relevance != null) {
            return relevance.intValue();
        }

        if (prefix == null || prefix.isEmpty()) {
            return RelevanceConstants.MATCHES_ONLY_BECAUSE_EMPTY; // no prefix, means showing all templates
        }
        String templateName = template.getName();
        if (templateName == null) {
            templateName = "";
        }
        if (config.isIgnoringCaseAtTemplateNames()) {
            templateName=templateName.toLowerCase();
            prefix = prefix.toLowerCase();
        }
        
        if (templateName.equals(prefix)) {
            return RelevanceConstants.MATCHES_FULL; 
        }
        if (templateName.startsWith(prefix)) {
            return RelevanceConstants.MATCHES_START; // show those which are starting with at first
        }
        if (templateName.contains(prefix)) {
            return RelevanceConstants.MATCHES_NOT_AT_START_BUT_INSIDE; // show those which contains somewhere are next
        }
        // we return 0 - means will not be added (see caller code above)
        return RelevanceConstants.DOES_NOT_MATCH;
    }

    @Override
    protected Template[] getTemplates(String contextTypeId) {
        if (config.isCompletionDisabled()) {
            return NO_TEMPLATES;
        }
        return support.getTemplateStore().getTemplates(contextTypeId);
    }

    @SuppressWarnings("deprecation")
    @Override
    protected TemplateContextType getContextType(ITextViewer viewer, IRegion region) {
        if (config.isCompletionDisabled()) {
            return null;
        }
        TemplateStrategy strategy = getStrategy();
        IDocument document = viewer.getDocument();
        String id = strategy.getContextType(document, region);
        return support.getContextTypeRegistry().getContextType(id);
    }

    private TemplateStrategy getStrategy() {
        if (strategy == null) {
            strategy = config.createStrategy();
            if (strategy == null) {
                throw new IllegalStateException(config.getClass().getName() + " does wrong implement createStrategy()! May never return null. Destroy method (uses default) or implement correctly");
            }
        }

        return strategy;
    }

    @Override
    protected Image getImage(Template template) {
        ImageRegistry registry = provider.getActivator().getImageRegistry();
        String imagePath = config.getTemplateImagePath(template);
        if (imagePath == null) {
            return PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FILE);
        }
        Image image = registry.get(imagePath);
        if (image == null) {
            ImageDescriptor desc = EclipseUtil.createImageDescriptor(imagePath, provider.getPluginID());
            registry.put(imagePath, desc);
            image = registry.get(imagePath);
        }
        return image;
    }

}