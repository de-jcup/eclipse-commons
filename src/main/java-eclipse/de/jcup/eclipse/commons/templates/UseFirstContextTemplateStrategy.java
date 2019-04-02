package de.jcup.eclipse.commons.templates;

import java.util.List;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.templates.Template;

public class UseFirstContextTemplateStrategy implements TemplateStrategy{

    private TemplateSupportConfig config;

    public UseFirstContextTemplateStrategy(TemplateSupportConfig config) {
        this.config=config;
    }
    
    @Override
    public String getContextType(IDocument document, IRegion region) {
        List<String> contentTypes = config.getContextTypes();
        if (contentTypes==null) {
            throw new IllegalStateException("getContentTypes() returns null: not correct implemented!");
        }
        return contentTypes.get(0);
    }

    @Override
    public Integer getRelevance(Template template, String prefix) {
        return null;
    }

    @Override
    public String extractPrefix(ITextViewer viewer, int offset) {
        return null;
    }
}
