package testcase.de.jcup.eclipse.commons;

import java.util.Arrays;
import java.util.List;

import org.eclipse.jface.text.templates.Template;

import de.jcup.eclipse.commons.templates.TemplateStrategy;
import de.jcup.eclipse.commons.templates.TemplateSupportConfig;

public class TestTemplateSupportConfig implements TemplateSupportConfig {

    @Override
    public String getTemplatesKey() {
        return "testcase.de.jcup.eclipse.commons.testcase.templates";
    }

    @Override
    public List<String> getContextTypes() {
        return Arrays.asList("commons.testcase.type1");
    }

    @Override
    public TemplateStrategy getStrategy() {
        return null;
    }

    @Override
    public String getTemplateImageName(Template template) {
        return null;
    }

}
