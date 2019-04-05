package testcase.de.jcup.eclipse.commons.keywords;

import de.jcup.eclipse.commons.keyword.DocumentKeyWord;
import de.jcup.eclipse.commons.keyword.TooltipTextSupportPreferences;

public class TestCaseTooltipTextSupportPreferences implements TooltipTextSupportPreferences{
    @Override
    public boolean areTooltipsForKeyWordsEnabled() {
        return true;
    }

    @Override
    public String getCommentColorWeb() {
        return "#00ff00";
    }

    @Override
    public DocumentKeyWord[] getAllKeywords() {
        return TestCaseKeywords.values();
    }
    
    @Override
    public boolean isIgnoreCaseOnKeywordText() {
        return true;
    }
}
