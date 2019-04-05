package testcase.de.jcup.eclipse.commons.other;

import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.WhitespaceRule;


public class TestcaseDefaultTextScanner extends RuleBasedScanner {

	public TestcaseDefaultTextScanner() {
		IRule[] rules = new IRule[1];
		rules[0] = new WhitespaceRule(new TestcaseWhitespaceDetector());

		setRules(rules);
	}
}
