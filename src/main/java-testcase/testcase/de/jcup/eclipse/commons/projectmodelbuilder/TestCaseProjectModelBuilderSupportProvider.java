package testcase.de.jcup.eclipse.commons.projectmodelbuilder;

import org.eclipse.core.resources.IFile;

import de.jcup.eclipse.commons.PluginContextProvider;
import de.jcup.eclipse.commons.projectmodelbuilder.AbstractConfigurableProjectModelBuilderSupportProvider;
import de.jcup.eclipse.commons.projectmodelbuilder.ProjectModelBuilder;
import testcase.de.jcup.eclipse.commons.keywords.TestCaseKeyword;
import testcase.de.jcup.eclipse.commons.keywords.TestCaseKeywords;

public class TestCaseProjectModelBuilderSupportProvider extends AbstractConfigurableProjectModelBuilderSupportProvider<TestCaseProjectModel>{


	public TestCaseProjectModelBuilderSupportProvider(PluginContextProvider provider) {
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
	public ProjectModelBuilder<TestCaseProjectModel> createBuilder() {
		return new TestCaseProjectModelBuilder();
	}

	@Override
	public boolean isProjectModelBuilderSupportEnabled() {
		return true;
	}

	

}
