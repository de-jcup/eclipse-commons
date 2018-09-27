package testcase.de.jcup.eclipse.commons;

import de.jcup.eclipse.commons.keyword.TooltipTextSupport;

public enum TestCaseKeywords implements TestCaseKeyword{

	WORD1,
	WORD2,
	WORD3//no keyword
	;

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

}
