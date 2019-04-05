package testcase.de.jcup.eclipse.commons.keywords;

import java.util.ArrayList;
import java.util.List;

import de.jcup.eclipse.commons.keyword.TooltipTextSupport;

public enum TestCaseKeywords implements TestCaseKeyword{

    WORDS,
	WORD1,
	WORD2,
	WORD3// this keyword has no own tooltip file
	;
    private List<String> code = new ArrayList<>();
    
    private TestCaseKeywords() {
        code.add("This is an example");
        code.add("------------------");
        code.add(name());
    }

	@Override
	public String getText() {
		return name().toLowerCase();
	}

	@Override
	public boolean isBreakingOnEof() {
		return false;
	}

	@Override
	public String getTooltip() {
		return TooltipTextSupport.getTooltipText(getText());
	}

	@Override
	public String getLinkToDocumentation() {
		return "https://github.com/de-jcup/eclipse-commons";
	}

    @Override
    public List<String> getCodeTemplate() {
        return code;
    }

}
