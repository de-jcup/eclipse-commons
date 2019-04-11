package de.jcup.eclipse.commons.codeassist;

import static org.junit.Assert.*;

import org.junit.Test;

public class DefaultRelevanceCalculatorTest {

    @Test
    public void test_prefix1_ignore_case() {
        /* prepare */
        DefaultRelevanceCalculator c = new DefaultRelevanceCalculator("prefix1");
        
        /* execute + test */
        assertEquals(RelevanceConstants.MATCHES_FULL, c.calculate("prefix1"));
        assertEquals(RelevanceConstants.MATCHES_FULL_WHEN_IGNORE_CASE, c.calculate("Prefix1"));
        assertEquals(RelevanceConstants.MATCHES_START, c.calculate("prefix12"));
        assertEquals(RelevanceConstants.MATCHES_START_WHEN_IGNORE_CASE, c.calculate("Prefix12"));
        assertEquals(RelevanceConstants.MATCHES_NOT_AT_START_BUT_INSIDE, c.calculate("isprefix12"));
        assertEquals(RelevanceConstants.MATCHES_NOT_AT_START_BUT_INSIDE_WHEN_IGNORE_CASE, c.calculate("isPrefix12"));
        assertEquals(RelevanceConstants.DOES_NOT_MATCH, c.calculate("pre"));
        assertEquals(RelevanceConstants.DOES_NOT_MATCH, c.calculate(""));
        
    }
    
    
    @Test
    public void test_empty() {
        /* prepare */
        DefaultRelevanceCalculator c = new DefaultRelevanceCalculator("");
        
        /* execute + test */
        assertEquals(RelevanceConstants.MATCHES_ONLY_BECAUSE_EMPTY, c.calculate("prefix1"));
        assertEquals(RelevanceConstants.MATCHES_ONLY_BECAUSE_EMPTY, c.calculate("Prefix1"));
        assertEquals(RelevanceConstants.MATCHES_ONLY_BECAUSE_EMPTY, c.calculate("Prefix12"));
        assertEquals(RelevanceConstants.MATCHES_ONLY_BECAUSE_EMPTY, c.calculate("isPrefix12"));
        assertEquals(RelevanceConstants.MATCHES_ONLY_BECAUSE_EMPTY, c.calculate("pre"));
        assertEquals(RelevanceConstants.MATCHES_ONLY_BECAUSE_EMPTY, c.calculate(""));
        
    }

}
