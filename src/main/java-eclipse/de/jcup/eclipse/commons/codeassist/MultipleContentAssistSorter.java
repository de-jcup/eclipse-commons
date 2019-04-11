package de.jcup.eclipse.commons.codeassist;

import java.util.List;

import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.ICompletionProposal;

public interface MultipleContentAssistSorter {

    List<ICompletionProposal> sortProposals(ITextViewer viewer, int offset, List<ICompletionProposal> list);

}
