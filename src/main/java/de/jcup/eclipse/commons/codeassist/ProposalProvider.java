package de.jcup.eclipse.commons.codeassist;

import java.util.List;

public interface ProposalProvider extends Comparable<ProposalProvider>{

	public String getLabel();
	
	public List<String> getCodeTemplate();
}
