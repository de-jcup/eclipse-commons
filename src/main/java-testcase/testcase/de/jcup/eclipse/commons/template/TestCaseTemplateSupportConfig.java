package testcase.de.jcup.eclipse.commons.template;

import java.util.Arrays;
import java.util.List;

import org.eclipse.jface.text.templates.Template;

import de.jcup.eclipse.commons.templates.TemplateSupportConfig;

public class TestCaseTemplateSupportConfig implements TemplateSupportConfig {

    @Override
    public String getTemplatesKey() {
        return "testcase.de.jcup.eclipse.commons.testcase.templates";
    }

    @Override
    public List<String> getContextTypes() {
        return Arrays.asList("testcase.template.context.alpha");
    }

    @Override
    public String getTemplateImagePath(Template template) {
        return null;
    }

}
