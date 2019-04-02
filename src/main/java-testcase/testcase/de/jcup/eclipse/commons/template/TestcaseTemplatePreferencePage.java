package testcase.de.jcup.eclipse.commons.template;

import de.jcup.eclipse.commons.templates.TemplateSupportPreferencePage;
import testcase.de.jcup.eclipse.commons.TestcaseActivator;

public class TestcaseTemplatePreferencePage extends TemplateSupportPreferencePage{

    public TestcaseTemplatePreferencePage() {
        super(TestcaseActivator.getDefault().getTemplateSupportProvider());
    }

}
