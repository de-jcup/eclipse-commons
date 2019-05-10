package testcase.de.jcup.eclipse.commons.workspacemodel;

import java.util.Map;

import org.eclipse.core.resources.IResource;

import de.jcup.eclipse.commons.ui.EclipseUtil;
import de.jcup.eclipse.commons.workspacemodel.AbstractModelBuilder;
import de.jcup.eclipse.commons.workspacemodel.ModelUpdateAction;
import testcase.de.jcup.eclipse.commons.TestcaseActivator;

/**
 * This is an example for a project model builder. This builder collects
 * in every file with ".testcase" file extensions the lines where wellknown
 * keywords reside.<br><br>
 * Inside it's just a simple map .
 * @author albert
 *
 */
public class TestCaseModelBuilder extends AbstractModelBuilder<TestCaseModel> {

	@Override
	public TestCaseModel create() {
		return new TestCaseModel();
	}

	@Override
	public void updateImpl(TestCaseModel model, ModelUpdateAction action) {
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
