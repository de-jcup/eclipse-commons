package de.jcup.eclipse.commons.templates;

import java.util.List;

import org.eclipse.jface.text.templates.Template;

public interface TemplateSupportConfig {

    public String getTemplatesKey();

    /**
     * 
     * @return context types, never <code>null</code> and contains at least one entry
     */
    public List<String> getContextTypes();
    
    /**
     * @return strategy or <code>null</code>
     */
    public TemplateStrategy getStrategy();

    /**
     * @param template the template
     * @return image name or <code>null</code>
     */
    public String getTemplateImageName(Template template);
}
