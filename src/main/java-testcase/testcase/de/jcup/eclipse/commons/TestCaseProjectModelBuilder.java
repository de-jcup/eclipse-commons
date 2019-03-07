package testcase.de.jcup.eclipse.commons;

import java.util.Map;

import org.eclipse.core.resources.IResource;

import de.jcup.eclipse.commons.projectmodelbuilder.AbstractProjectModelBuilder;
import de.jcup.eclipse.commons.projectmodelbuilder.ModelUpdateAction;
import de.jcup.eclipse.commons.ui.EclipseUtil;

public class TestCaseProjectModelBuilder extends AbstractProjectModelBuilder<TestCaseProjectModel> {

	@Override
	public TestCaseProjectModel create() {
		return new TestCaseProjectModel();
	}

	@Override
	public void updateImpl(TestCaseProjectModel model, ModelUpdateAction action) {
		IResource resource = action.getResource();
		switch (action.getType()) {
		case ADD:
		    EclipseUtil.logInfo("Action:"+action.getType(), TestcaseActivator.getDefault());
			/* each line containing a keyword will have been called. So we append all lines.
			 * Model will just contain information about testcase-files having keywords inside */
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
		    EclipseUtil.logInfo("Action:"+action.getType(), TestcaseActivator.getDefault());
			model.getMap().remove(resource);
			break;
		default:
		    EclipseUtil.logInfo("Action [unsupported]:"+action.getType(), TestcaseActivator.getDefault());
			break;

		}

	}

}
