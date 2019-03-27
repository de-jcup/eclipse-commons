package de.jcup.eclipse.commons.templates;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.templates.Template;

public interface TemplateStrategy {

    public String getContextType(IDocument document, IRegion region);

    public int getRelevance(Template template, String prefix);

    public String extractPrefix(ITextViewer viewer, int offset);

}
