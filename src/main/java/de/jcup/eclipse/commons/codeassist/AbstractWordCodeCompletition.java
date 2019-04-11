package de.jcup.eclipse.commons.codeassist;

public abstract class AbstractWordCodeCompletition implements ProposalProviderSupport {

    protected PrefixCalculator prefixCalculator;
    
    public AbstractWordCodeCompletition(){
        this.prefixCalculator=createPrefixCalculator();
    }
    
	protected PrefixCalculator createPrefixCalculator() {
        return new DefaultPrefixCalculator();
    }

    /**
	 * Resolves text before given offset. 
	 * E.g. when cursor is before "abcd" an empty string will be returned
	 * 
	 * @param source
	 * @param offset
	 * @return text, never <code>null</code>
	 */
	public String getTextbefore(String source, int offset) {
		return prefixCalculator.calculate(source, offset);
	}
	
	

}