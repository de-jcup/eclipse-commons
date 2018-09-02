package testcase.de.jcup.eclipse.commons;

import org.eclipse.core.resources.IFile;

import de.jcup.eclipse.commons.tasktags.AbstractConfigurableTaskTagsSupportProvider;

public class TestcaseTaskTagsSupportProvider extends AbstractConfigurableTaskTagsSupportProvider{

	public TestcaseTaskTagsSupportProvider(TestcaseActivator plugin) {
		super(plugin, plugin.getPluginId());
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
	public boolean isFileHandled(IFile file) {
		return file.getFileExtension().equals("testcase");
	}

}
