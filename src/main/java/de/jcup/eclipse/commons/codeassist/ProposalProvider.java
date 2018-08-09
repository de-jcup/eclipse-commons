package de.jcup.eclipse.commons.codeassist;

import java.util.List;

public interface ProposalProvider {

	public String getLabel();
	
	public List<String> getCodeTemplate();
}
