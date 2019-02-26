package de.jcup.eclipse.commons.projectmodelbuilder;

import org.eclipse.core.resources.IResource;

public interface ModelUpdateAction {
	
	public enum ActionType{
		ADD,
		
		DELETE,
	}

	public ActionType getType();
	
	public IResource getResource();
	
	/* line to inspect*/
	public String getLine() ;
	
	public int getLineNumber() ;
	
	public String getMessage() ;
}
