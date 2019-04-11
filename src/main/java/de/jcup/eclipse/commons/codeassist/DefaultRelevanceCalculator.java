package de.jcup.eclipse.commons.codeassist;

public class DefaultRelevanceCalculator implements RelevanceCalculator {

    private String prefix;
    private String prefixIgnoreCase;

    public DefaultRelevanceCalculator(String prefix ) {
        this.prefix=prefix;
        this.prefixIgnoreCase=prefix.toLowerCase();
    }
    
    @Override
    public int calculate(String word) {
        if (prefix==null || prefix.isEmpty()) {
            return RelevanceConstants.MATCHES_ONLY_BECAUSE_EMPTY; 
        }
        if (word==null) {
            return RelevanceConstants.DOES_NOT_MATCH;
        }
        /* - NOT ignore case */
        if (word.equals(prefix)) {
            return RelevanceConstants.MATCHES_FULL; 
        }
        if (word.startsWith(prefix)) {
            return RelevanceConstants.MATCHES_START; // show those which are starting with at first
        }

        /* - ignore case */
        String wordIgnoreCase = word.toLowerCase();
        if (wordIgnoreCase.equals(prefixIgnoreCase)) {
            return RelevanceConstants.MATCHES_FULL_WHEN_IGNORE_CASE; 
        }
        if (wordIgnoreCase.startsWith(prefixIgnoreCase)) {
            return RelevanceConstants.MATCHES_START_WHEN_IGNORE_CASE; // show those which are starting with at first
        }
        /* - NOT ignore case */
        if (word.contains(prefix)) {
            return RelevanceConstants.MATCHES_NOT_AT_START_BUT_INSIDE; // show those which contains somewhere are next
        }
        /* - ignore case */
        if (wordIgnoreCase.contains(prefixIgnoreCase)) {
            return RelevanceConstants.MATCHES_NOT_AT_START_BUT_INSIDE_WHEN_IGNORE_CASE; // show those which contains somewhere are next
        }
        
        // we return 0 - means will not be added (see caller code above)
        return RelevanceConstants.DOES_NOT_MATCH;
    }
}
