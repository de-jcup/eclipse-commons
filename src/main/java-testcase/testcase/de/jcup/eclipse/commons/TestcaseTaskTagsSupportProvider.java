package testcase.de.jcup.eclipse.commons;

import java.util.ArrayList;
import java.util.List;

import de.jcup.eclipse.commons.tasktags.AbstractConfigurableTaskTagsSupportProvider;

public class TestcaseTaskTagsSupportProvider extends AbstractConfigurableTaskTagsSupportProvider{
	private List<String> fileextensions = new ArrayList<>();
									// file and open it

	public TestcaseTaskTagsSupportProvider(TestcaseActivator plugin) {
		super(plugin, plugin.getPluginId());
		fileextensions.add("testcase"); // to test we must add a "xyz.testcase"
	}

	@Override
	public boolean isLineCheckforTodoTaskNessary(String line, int lineNumber, String[] lines) {
		return line.startsWith("!!");
	}

	@Override
	public String getTodoTaskMarkerId() {
		return "testcase.de.jcup.eclipse.commons.task";
	}

	@Override
	public List<String> getTodoTaskFileExtensions() {
		return fileextensions;
	}

}
