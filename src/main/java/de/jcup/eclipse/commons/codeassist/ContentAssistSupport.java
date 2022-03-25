package de.jcup.eclipse.commons.codeassist;

import java.util.Collection;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.contentassist.ICompletionProposal;

/**
 * A content assist support is a simplification word eclipse content assist usage.
 * @author Albert Tregnaghi
 *
 */
public interface ContentAssistSupport {

	/**
	 * Will e.g. build a new simple word list
	 */
	void startAssistSession();
	
	
	/**
	 * Will e.g. clean simple word list again
	 */
	void endAssistSession();


	/**
	 * Calculates proposals
	 * @param document original eclipse editor document
	 * @param source already fetched source
	 * @param offset
	 * @return proposals
	 */
	Collection<? extends ICompletionProposal> calculate(IDocument document, int offset);

}
