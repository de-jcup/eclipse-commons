package testcase.de.jcup.eclipse.commons.tasktags;

import org.eclipse.core.resources.IFile;

import de.jcup.eclipse.commons.tasktags.AbstractConfigurableTaskTagsSupportProvider;
import testcase.de.jcup.eclipse.commons.TestcaseActivator;

public class TestcaseTaskTagsSupportProvider extends AbstractConfigurableTaskTagsSupportProvider{

	public TestcaseTaskTagsSupportProvider(TestcaseActivator plugin) {
		super(plugin);
	}

	@Override
	public boolean isLineCheckforTodoTaskNessary(String line, int lineNumber, String[] lines) {
		if (line==null){
			return false;
		}
		return line.startsWith("!!");
	}

	@Override
	public String getTodoTaskMarkerId() {
		return "testcase.de.jcup.eclipse.commons.task";
	}

	@Override
	public boolean isFileHandled(IFile file) {
		if (file==null){
			return false;
		}
		String fileExtension = file.getFileExtension();
		if (fileExtension==null){
			return false;
		}
		return fileExtension.equals("testcase");
	}

}
