package testcase.de.jcup.eclipse.commons;

import de.jcup.eclipse.commons.tasktags.AbstractTaskTagsPreferencePage;

public class TestcaseTaskTagsPreferencePage extends AbstractTaskTagsPreferencePage{

	public TestcaseTaskTagsPreferencePage() {
		super(TestcaseActivator.getDefault().getTaskSupportProvider(), "Testcase todos","Define your todos inside the testcase");
	}

}
