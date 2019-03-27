package de.jcup.eclipse.commons.templates;

import java.io.IOException;
import java.util.List;

import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.templates.Template;
import org.eclipse.jface.text.templates.TemplateCompletionProcessor;
import org.eclipse.jface.text.templates.TemplateContextType;
import org.eclipse.jface.text.templates.persistence.TemplateStore;
import org.eclipse.swt.graphics.Image;
import org.eclipse.text.templates.ContextTypeRegistry;
import org.eclipse.ui.editors.text.templates.ContributionContextTypeRegistry;
import org.eclipse.ui.editors.text.templates.ContributionTemplateStore;
import org.osgi.service.prefs.BackingStoreException;

import de.jcup.eclipse.commons.PluginContextProvider;
import de.jcup.eclipse.commons.ui.EclipseUtil;

public class SimpleTemplateCompletionProcessor extends TemplateCompletionProcessor {

   
    private TemplateSupport support;
    private TemplateSupportConfig config;
    private PluginContextProvider provider;

    SimpleTemplateCompletionProcessor(TemplateSupport support) {
        if (support==null) {
            throw new IllegalArgumentException("support may not be null!");
        }
        this.support = support;
        this.config=support.getConfig();
        this.provider=support.getProvider();
    }

    private static final String DEFAULT_IMAGE = "template.png"; //$NON-NLS-1$

    @Override
    protected String extractPrefix(ITextViewer viewer, int offset) {
        TemplateStrategy strategy = config.getStrategy();
        if (strategy != null) {
            return strategy.extractPrefix(viewer, offset);
        }
        return super.extractPrefix(viewer, offset);
    }

    @Override
    protected int getRelevance(Template template, String prefix) {
        TemplateStrategy strategy = config.getStrategy();
        if (strategy != null) {
            return strategy.getRelevance(template, prefix);
        }
        return super.getRelevance(template, prefix);
    }

    @Override
    protected Template[] getTemplates(String contextTypeId) {
        return support.getTemplateStore().getTemplates(contextTypeId);
    }

    @Override
    protected TemplateContextType getContextType(ITextViewer viewer, IRegion region) {
        String id = null;

        TemplateStrategy strategy = config.getStrategy();
        if (strategy != null) {
            IDocument document = viewer.getDocument();
            id = strategy.getContextType(document, region);
        } else {
            List<String> contextTypes = config.getContextTypes();
            if (contextTypes != null && !contextTypes.isEmpty()) {
                id = contextTypes.get(0);
            }
        }
        if (id == null) {
            return null;
        }
        return support.getContextTypeRegistry().getContextType(id);
    }

    @Override
    protected Image getImage(Template template) {
        ImageRegistry registry = provider.getActivator().getImageRegistry();
        String imageName = config.getTemplateImageName(template);
        if (imageName == null) {
            imageName = DEFAULT_IMAGE;
        }
        Image image = registry.get(imageName);
        if (image == null) {
            ImageDescriptor desc = EclipseUtil.createImageDescriptor("icons/" + imageName, provider.getPluginID());
            registry.put(imageName, desc);
            image = registry.get(imageName);
        }
        return image;
    }

    
}