package testcase.de.jcup.eclipse.commons;

import de.jcup.eclipse.commons.templates.TemplateSupportPreferencePage;

public class TestcaseTemplatePreferencePage extends TemplateSupportPreferencePage{

    public TestcaseTemplatePreferencePage() {
        super(TestcaseActivator.getDefault().getTemplateSupportProvider());
    }

}
