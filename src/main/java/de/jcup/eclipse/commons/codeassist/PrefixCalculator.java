package de.jcup.eclipse.commons.codeassist;

public interface PrefixCalculator {
    

    /**
     * Resolves text before given offset. 
     * E.g. when cursor is before "abcd" an empty string will be returned
     * 
     * @param source
     * @param offset
     * @return text, never <code>null</code>
     */
    public String calculate(String source, int offset);
}
