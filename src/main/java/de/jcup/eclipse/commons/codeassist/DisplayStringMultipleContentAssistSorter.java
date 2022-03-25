package de.jcup.eclipse.commons.codeassist;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.ICompletionProposal;

public class DisplayStringMultipleContentAssistSorter implements MultipleContentAssistSorter{

    private PrefixCalculator calculator;
    
    public DisplayStringMultipleContentAssistSorter() {
        this(null);
        
    }
    
    public DisplayStringMultipleContentAssistSorter(PrefixCalculator calculator) {
        if (calculator==null) {
            calculator=new DefaultPrefixCalculator();
        }
        this.calculator=calculator;
    }

    @Override
    public List<ICompletionProposal> sortProposals(ITextViewer viewer, int offset, List<ICompletionProposal> list) {
        if (list==null) {
            return null; // we do not repair
        }
        if (viewer==null) {
            return list;
        }
        if (offset<0) {
            return list;
        }
        IDocument document = viewer.getDocument();
        if (document==null) {
            return list;
        }
        String text = document.get();
        String calculatedPrefix = calculator.calculate(text, offset);
        return sortProposals(list, calculatedPrefix);
    }

    

    private List<ICompletionProposal> sortProposals(List<ICompletionProposal> list, String calculatedPrefix) {
        RelevanceCalculator calculator = new DefaultRelevanceCalculator(calculatedPrefix);
        
        TreeMap<Integer,List<ICompletionProposal>> map = new TreeMap<>();
        
        List<ICompletionProposal> sorted = new ArrayList<ICompletionProposal>();
        for (ICompletionProposal proposal: list) {
            if (proposal==null) {
                continue;
            }
            String display = proposal.getDisplayString();
            int relevance = calculator.calculate(display);
            
            map.computeIfAbsent(relevance, key->new ArrayList<>()).add(proposal);
            
        }
        for (Integer key: map.descendingKeySet()) {
            sorted.addAll(map.get(key));
        }
        
        return sorted;
    }



}
