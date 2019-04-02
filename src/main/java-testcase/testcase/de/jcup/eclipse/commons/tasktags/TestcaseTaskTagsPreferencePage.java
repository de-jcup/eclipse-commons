package testcase.de.jcup.eclipse.commons.tasktags;

import de.jcup.eclipse.commons.tasktags.AbstractTaskTagsPreferencePage;
import testcase.de.jcup.eclipse.commons.TestcaseActivator;

public class TestcaseTaskTagsPreferencePage extends AbstractTaskTagsPreferencePage{

	public TestcaseTaskTagsPreferencePage() {
		super(TestcaseActivator.getDefault().getTaskSupportProvider(), "Testcase todos","Define your todos inside the testcase");
	}

}
