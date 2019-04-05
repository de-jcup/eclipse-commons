package testcase.de.jcup.eclipse.commons.codeassist;
import de.jcup.eclipse.commons.codeassist.SupportableContentAssistProcessor;
import testcase.de.jcup.eclipse.commons.TestcaseActivator;

public class TestcaseContentAssistProcessor extends SupportableContentAssistProcessor {

    public TestcaseContentAssistProcessor(){
        super(new TestcaseContentAssistSupport(TestcaseActivator.getDefault()));
    }
}