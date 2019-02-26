package testcase.de.jcup.eclipse.commons;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IResource;

public class TestCaseProjectModel {

	
	private Map<IResource, StringBuilder> map = new HashMap<>();
	
	public Map<IResource, StringBuilder> getMap() {
		return map;
	}

}
