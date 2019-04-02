package de.jcup.eclipse.commons.templates;

import java.util.Collections;
import java.util.List;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.templates.Template;

public interface TemplateSupportConfig {

    /**
     * @return key used for template storage etc., may not be <code>null</code>
     */
    public String getTemplatesKey();

    /**
     * 
     * @return context types, never <code>null</code> and must contain at least one entry
     */
    public List<String> getContextTypes();
    
    /**
     * Optional
     * @return content types suported by template. per default always {@link IDocument#DEFAULT_CONTENT_TYPE}
     */
    public default List<String>  getContentTypes() {
        return Collections.singletonList(IDocument.DEFAULT_CONTENT_TYPE);
    }
    
    /**
     * Optional, default implementation returns a {@link UseFirstContextTemplateStrategy}
     * @return strategy never <code>null</code>
     */
    public default TemplateStrategy createStrategy() {
        return new UseFirstContextTemplateStrategy(this);
    }

    /**
     * Resolve template image path (e.g. "icons/template.png") - used for image resolving 
     * @param template the template
     * @return image name or <code>null</code> - when <code>null</code> then a shared image will be used
     */
    public String getTemplateImagePath(Template template);

    /**
     * Optional, enabled per default. Interesting when templating shall be turned off 
     * @return <code>true</code> when completion shall be turned off
     */
    public default boolean isCompletionDisabled() {
        return false;
    }
}
