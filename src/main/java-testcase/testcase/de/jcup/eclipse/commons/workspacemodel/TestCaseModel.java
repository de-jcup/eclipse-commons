package testcase.de.jcup.eclipse.commons.workspacemodel;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IResource;

public class TestCaseModel {

	
	private Map<IResource, StringBuilder> map = new HashMap<>();
	
	public Map<IResource, StringBuilder> getMap() {
		return map;
	}

}
