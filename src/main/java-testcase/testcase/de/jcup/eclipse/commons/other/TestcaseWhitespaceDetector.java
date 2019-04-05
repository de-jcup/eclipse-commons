package testcase.de.jcup.eclipse.commons.other;

import org.eclipse.jface.text.rules.IWhitespaceDetector;

public class TestcaseWhitespaceDetector implements IWhitespaceDetector {

	@Override
	public boolean isWhitespace(char c) {
		return (c == ' ' || c == '\t' || c == '\n' || c == '\r');
	}
}