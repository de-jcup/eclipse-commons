package de.jcup.eclipse.commons.codeassist;

import java.util.Collections;
import java.util.List;

public class SimpleWordProposalProvider implements ProposalProvider{

	private String word;
	
	public SimpleWordProposalProvider(String word){
		this.word=word;
	}
	
	@Override
	public String getLabel() {
		return word;
	}

	
	@Override
	public List<String> getCodeTemplate() {
		return Collections.singletonList(word+" ");
	}

}
