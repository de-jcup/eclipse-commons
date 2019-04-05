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

    @Override
    public int compareTo(ProposalProvider o) {
        if (o==null) {
            return 1;
        }
        String label = getLabel();
        String label2 = o.getLabel();
        if (label==null) {
            if (label2==null){
                return 0;
            }
            return -1;
        }
        if (label2==null){
            return 1;
        }
        return label.compareTo(label2);
    }

}
