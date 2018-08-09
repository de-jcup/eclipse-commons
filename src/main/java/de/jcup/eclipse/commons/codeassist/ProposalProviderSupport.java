package de.jcup.eclipse.commons.codeassist;

import java.util.Set;

/**
 * A word completion is able to calculate words for code assistence
 * @author Albert Tregnaghi
 *
 */
public interface ProposalProviderSupport {

	/**
	 * Calculates the resulting proposals for given offset.
	 * 
	 * @param source
	 * @param offset
	 * @return proposals, never <code>null</code>
	 */
	Set<ProposalProvider> calculate(String source, int offset);

	/**
	 * Reset complet
	 * 
	 */
	void reset();

	String getTextbefore(String source, int offset);

}