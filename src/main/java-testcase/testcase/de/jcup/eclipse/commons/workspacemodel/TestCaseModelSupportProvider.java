package testcase.de.jcup.eclipse.commons.workspacemodel;

import org.eclipse.core.resources.IFile;

import de.jcup.eclipse.commons.PluginContextProvider;
import de.jcup.eclipse.commons.workspacemodel.AbstractConfigurableModelBuilderSupportProvider;
import de.jcup.eclipse.commons.workspacemodel.ModelBuilder;
import testcase.de.jcup.eclipse.commons.keywords.TestCaseKeyword;
import testcase.de.jcup.eclipse.commons.keywords.TestCaseKeywords;

public class TestCaseModelBuilderSupportProvider extends AbstractConfigurableModelBuilderSupportProvider<TestCaseModel>{


	public TestCaseModelBuilderSupportProvider(PluginContextProvider provider) {
		super(provider);
	}

	@Override
	public boolean isFileHandled(IFile file) {
		String fileExtension = file.getFileExtension();
		if (fileExtension==null) {
			return false;
		}
		if (fileExtension.contentEquals("testcase")) {
			return true;
		}
		return false;
	}

	@Override
	public String getModelName() {
		return "Testcase project model";
	}

	@Override
	public boolean isLineCheckforModelNessary(String line, int lineNumber, String[] lines) {
		String lowerCase = line.toLowerCase();
		for (TestCaseKeyword keyword: TestCaseKeywords.values()) {
			if (lowerCase.startsWith(keyword.getText())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public ModelBuilder<TestCaseModel> createBuilder() {
		return new TestCaseModelBuilder();
	}

	@Override
	public boolean isProjectModelBuilderSupportEnabled() {
		return true;
	}

	

}
