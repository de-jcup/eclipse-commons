package de.jcup.eclipse.commons.templates;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.templates.Template;

public interface TemplateStrategy {

    /**
     * Resolves template context type
     * @param document
     * @param region
     * @return context type to use for this region in document, never <code>null</code>
     */
    public String getContextType(IDocument document, IRegion region);

    /**
     * Resolves template relevance 
     * @param template
     * @param prefix
     * @return relevance or <code>null</code> when default shall be used
     */
    public Integer getRelevance(Template template, String prefix);

    /**
     * Extract a prefix
     * @param viewer
     * @param offset
     * @return extreacted prefix or <code>null</code> when default shall be used
     */
    public String extractPrefix(ITextViewer viewer, int offset);

}
