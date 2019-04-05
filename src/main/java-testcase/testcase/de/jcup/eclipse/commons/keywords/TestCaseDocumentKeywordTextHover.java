package testcase.de.jcup.eclipse.commons.keywords;

import de.jcup.eclipse.commons.WhitespaceWordEndDetector;
import de.jcup.eclipse.commons.keyword.DocumentKeywordTextHover;

public class TestCaseDocumentKeywordTextHover extends DocumentKeywordTextHover{

    public TestCaseDocumentKeywordTextHover() {
        super(new TestCaseTooltipTextSupportPreferences(), new WhitespaceWordEndDetector());
    }

}
