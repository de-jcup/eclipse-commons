package de.jcup.eclipse.commons.codeassist;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class DefaultPrefixCalculatorTest {

    private DefaultPrefixCalculator calculatorToTest;

    @Before
    public void before() {
        calculatorToTest = new DefaultPrefixCalculator();
    }

    @Test
    public void test_abc__different_index() {
        /* prepare */
        String text = "abc";

        /* execute + test */
        assertEquals("", calculatorToTest.calculate(text, 0));
        assertEquals("a", calculatorToTest.calculate(text, 1));
        assertEquals("ab", calculatorToTest.calculate(text, 2));
        assertEquals("abc", calculatorToTest.calculate(text, 3));
    }

    @Test
    public void test_abc_newline_def__different_index() {
        /* prepare */
        String text = "abc\ndef";

        /* execute + test */
        assertEquals("", calculatorToTest.calculate(text, 0));
        assertEquals("a", calculatorToTest.calculate(text, 1));
        assertEquals("ab", calculatorToTest.calculate(text, 2));
        assertEquals("abc", calculatorToTest.calculate(text, 3));
    
        assertEquals("", calculatorToTest.calculate(text, 4));
        assertEquals("d", calculatorToTest.calculate(text, 5));
    
    }

}
