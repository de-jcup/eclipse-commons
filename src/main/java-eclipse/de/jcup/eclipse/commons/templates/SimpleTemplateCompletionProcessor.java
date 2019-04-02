package de.jcup.eclipse.commons.templates;

import java.util.List;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.templates.Template;
import org.eclipse.jface.text.templates.TemplateCompletionProcessor;
import org.eclipse.jface.text.templates.TemplateContextType;
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

    SimpleTemplateCompletionProcessor(TemplateSupport support) {
        if (support==null) {
            throw new IllegalArgumentException("support may not be null!");
        }
        this.support = support;
        this.config=support.getConfig();
        this.provider=support.getProvider();
    }


    @Override
    protected String extractPrefix(ITextViewer viewer, int offset) {
        TemplateStrategy strategy = getStrategy();
        String prefix = strategy.extractPrefix(viewer, offset);
        if (prefix!=null) {
            return prefix;
        }
        return super.extractPrefix(viewer, offset);
    }

    @Override
    protected int getRelevance(Template template, String prefix) {
        TemplateStrategy strategy = getStrategy();
        Integer relevance= strategy.getRelevance(template, prefix);
        if (relevance!=null) {
            return relevance.intValue();
        }
        return super.getRelevance(template, prefix);
    }

    @Override
    protected Template[] getTemplates(String contextTypeId) {
        if (config.isCompletionDisabled()) {
            return NO_TEMPLATES;
        }
        return support.getTemplateStore().getTemplates(contextTypeId);
    }

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
        if (strategy==null) {
            strategy= config.createStrategy();
            if (strategy==null) {
                throw new IllegalStateException(config.getClass().getName()+" does wrong implement createStrategy()! May never return null. Destroy method (uses default) or implement correctly");
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