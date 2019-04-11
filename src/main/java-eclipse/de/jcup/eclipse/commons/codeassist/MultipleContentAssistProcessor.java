package de.jcup.eclipse.commons.codeassist;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.contentassist.IContextInformationValidator;

public class MultipleContentAssistProcessor implements IContentAssistProcessor, IContextInformationValidator{

    private List<IContentAssistProcessor> assistProcessors;
    private IContentAssistProcessor validatingAssistProcessor;
    private char[] completionProposalsAutoActivationCharacers;
    private char[] contextInformationAutoActivationCharacters;
    private MultipleContentAssistSorter sorter;
    
    public MultipleContentAssistProcessor(IContentAssistProcessor validatingAssistProcessor, IContentAssistProcessor ...others ){
        this.assistProcessors = new ArrayList<>();
        this.validatingAssistProcessor=validatingAssistProcessor;
        this.assistProcessors.add(validatingAssistProcessor);
        for (IContentAssistProcessor o: others) {
            assistProcessors.add(o);
        }
        completionProposalsAutoActivationCharacers= createCompletionProposalAutoActivationCharacters();
        contextInformationAutoActivationCharacters=createContextInformationAutoActivationCharacters();
        this.sorter=createSorter();
    }
    
    protected MultipleContentAssistSorter createSorter() {
        return new DisplayStringMultipleContentAssistSorter();
    }
    
    @Override
    public ICompletionProposal[] computeCompletionProposals(ITextViewer viewer, int offset) {
        List<ICompletionProposal> list = new ArrayList<ICompletionProposal>();
        for (IContentAssistProcessor proc: assistProcessors) {
            ICompletionProposal[] computed = proc.computeCompletionProposals(viewer, offset);
            for (ICompletionProposal p: computed) {
                list.add(p);
            }
        }
        if (sorter!=null) {
            list=sorter.sortProposals(viewer,offset, list);
        }
        return list.toArray(new ICompletionProposal[list.size()]);
    }

    @Override
    public IContextInformation[] computeContextInformation(ITextViewer viewer, int offset) {
        List<IContextInformation> list = new ArrayList<IContextInformation>();
        for (IContentAssistProcessor proc: assistProcessors) {
            IContextInformation[] computed = proc.computeContextInformation(viewer, offset);
            if (computed==null) {
                continue;
            }
            for (IContextInformation p: computed) {
                list.add(p);
            }
        }
        return (IContextInformation[]) list.toArray();
    }

    @Override
    public char[] getCompletionProposalAutoActivationCharacters() {
        return completionProposalsAutoActivationCharacers;
    }

    @Override
    public char[] getContextInformationAutoActivationCharacters() {
        return contextInformationAutoActivationCharacters;
    }

    private char[] createCompletionProposalAutoActivationCharacters() {
        Set<Character> list = new LinkedHashSet<>();
        for (IContentAssistProcessor proc: assistProcessors) {
            char[] computed = proc.getCompletionProposalAutoActivationCharacters();
            if (computed==null || computed.length<1) {
                continue;
            }
            for (char p: computed) {
                list.add(p);
            }
        }
        return fromSetToArray(list);
    }
    private char[] createContextInformationAutoActivationCharacters() {
        Set<Character> list = new LinkedHashSet<>();
        for (IContentAssistProcessor proc: assistProcessors) {
            char[] computed = proc.getContextInformationAutoActivationCharacters();
            if (computed==null || computed.length<1) {
                continue;
            }
            for (char p: computed) {
                list.add(p);
            }
        }
        return fromSetToArray(list);
    }

    private char[] fromSetToArray(Set<Character> list) {
        char[] result = new char[list.size()];
        int i=0;
        for (Character c: list) {
            result[i++]=c.charValue();
        }
        return result;
    }

    @Override
    public String getErrorMessage() {
        for (IContentAssistProcessor proc: assistProcessors) {
            String message = proc.getErrorMessage();
            if (message!=null) {
                return message;
            }
        }
        return null;
    }

    @Override
    public IContextInformationValidator getContextInformationValidator() {
        return validatingAssistProcessor.getContextInformationValidator();
    }

    @Override
    public void install(IContextInformation info, ITextViewer viewer, int offset) {
       for (IContentAssistProcessor proc: assistProcessors) {
           proc.getContextInformationValidator().install(info, viewer, offset);
       }
        
    }

    @Override
    public boolean isContextInformationValid(int offset) {
        boolean valid=false;
        for (IContentAssistProcessor proc: assistProcessors) {
            valid=valid || proc.getContextInformationValidator().isContextInformationValid(offset);
            if (valid) {
                break;
            }
        }
        return valid;
    }
    
}
