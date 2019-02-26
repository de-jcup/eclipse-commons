package testcase.de.jcup.eclipse.commons;

import java.util.Map;

import org.eclipse.core.resources.IResource;

import de.jcup.eclipse.commons.projectmodelbuilder.AbstractProjectModelBuilder;
import de.jcup.eclipse.commons.projectmodelbuilder.ModelUpdateAction;

public class TestCaseProjectModelBuilder extends AbstractProjectModelBuilder<TestCaseProjectModel> {

	@Override
	public TestCaseProjectModel create() {
		return new TestCaseProjectModel();
	}

	@Override
	public void doUpdate(TestCaseProjectModel model, ModelUpdateAction action) {
		IResource resource = action.getResource();
		switch (action.getType()) {
		case ADD:
			/* each line containing a keyword will have been called. So we append all lines.
			 * Model will just contain information about testscase-files having keywords inside */
			Map<IResource, StringBuilder> map = model.getMap();
			StringBuilder sb = map.get(resource);
			if (sb==null) {
				sb = new StringBuilder();
				map.put(resource, sb);
			}
			sb.append(action.getLine());
			sb.append("\n");
			break;
		case DELETE:
			model.getMap().remove(resource);
			break;
		default:
			break;

		}

	}

}
