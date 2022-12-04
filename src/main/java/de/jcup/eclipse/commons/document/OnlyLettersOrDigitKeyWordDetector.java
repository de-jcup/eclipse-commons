package de.jcup.eclipse.commons.document;
import org.eclipse.jface.text.rules.IWordDetector;

public class OnlyLettersOrDigitKeyWordDetector implements IWordDetector {

    @Override
    public boolean isWordStart(char c) {
        if (!Character.isLetterOrDigit(c)) {
            return false;
        }
        return true;
    }

    @Override
    public boolean isWordPart(char c) {
        if (!Character.isLetterOrDigit(c)) {
            return false;
        }
        return true;
    }
}
