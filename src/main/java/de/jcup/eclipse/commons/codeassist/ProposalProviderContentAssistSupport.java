package de.jcup.eclipse.commons.codeassist;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.contentassist.ICompletionProposal;

import de.jcup.eclipse.commons.PluginContextProvider;
import de.jcup.eclipse.commons.codeassist.ProposalProvider;
import de.jcup.eclipse.commons.codeassist.ProposalProviderSupport;

public abstract class ProposalProviderContentAssistSupport implements ContentAssistSupport{

	protected ProposalProviderSupport completion;
	protected PluginContextProvider pluginContextProvider;


	public ProposalProviderContentAssistSupport(PluginContextProvider provider, ProposalProviderSupport completion){
		this.completion=completion;
		this.pluginContextProvider=provider;
	}
	
	protected PluginContextProvider getPluginContextProvider() {
		return pluginContextProvider;
	}
	/**
	 * Will build a new simple word list
	 */
	public void startAssistSession(){
		completion.reset();
		
		prepareCompletion(completion);
	}
	
	/**
	 * Implementation can add additional words, set a word list bilder etc.
	 * @param completion
	 */
	protected void prepareCompletion(ProposalProviderSupport completion){
		
	}
	
	/**
	 * Can be overridden by implementations to provide proposal information
	 * @return
	 */
	protected ProposalInfoProvider createProposalInfoBuilder(){
		return null;
	}
	
	/**
	 * Will clean simple word list again
	 */
	public void endAssistSession(){
		completion.reset();
	}


	@Override
	public Collection<? extends ICompletionProposal> calculate(IDocument document, int offset) {
		String source = document.get();
		Set<ProposalProvider> proposalProviders = completion.calculate(source, offset);
		if (proposalProviders==null){
			return Collections.emptyList();
		}
		ProposalInfoProvider proposalInfoProvider = createProposalInfoBuilder();

		List<ICompletionProposal> result = new ArrayList<>(proposalProviders.size());
		for (ProposalProvider proposalProvider : proposalProviders) {
			result.add(new SimpleCompletionProposal(completion,pluginContextProvider,document, offset, proposalProvider,proposalInfoProvider));
		}

		return result;
	}
}
