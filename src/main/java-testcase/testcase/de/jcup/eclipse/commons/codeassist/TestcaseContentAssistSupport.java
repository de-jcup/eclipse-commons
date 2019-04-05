package testcase.de.jcup.eclipse.commons.codeassist;


import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.graphics.Image;

import de.jcup.eclipse.commons.PluginContextProvider;
import de.jcup.eclipse.commons.codeassist.ProposalInfoProvider;
import de.jcup.eclipse.commons.codeassist.ProposalProviderContentAssistSupport;
import de.jcup.eclipse.commons.codeassist.SimpleWordCodeCompletion;
import de.jcup.eclipse.commons.keyword.TooltipTextSupport;
import testcase.de.jcup.eclipse.commons.other.TestcaseLabelProvider;

public class TestcaseContentAssistSupport extends ProposalProviderContentAssistSupport{

    private TestcaseLabelProvider labelProvider = new TestcaseLabelProvider();
    
    public TestcaseContentAssistSupport(PluginContextProvider provider) {
        super(provider, new SimpleWordCodeCompletion());
    }
    
    @Override
    protected ProposalInfoProvider createProposalInfoBuilder() {
        return new ProposalInfoProvider() {
            
            @Override
            public Object getProposalInfo(IProgressMonitor monitor, Object target) {
                if (! (target instanceof String)){
                    return null;
                }
                String word = (String) target;
                return TooltipTextSupport.getTooltipText(word.toLowerCase());
            }

            @Override
            public Image getImage(Object target) {
                if (! (target instanceof String)){
                    return null;
                }
                return labelProvider.getImage(target);
            }
        };
    }
    
    
}