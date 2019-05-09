package de.jcup.eclipse.commons;

import static org.junit.Assert.*;

import org.junit.Test;

public class SimpleStringUtilsTest {

    @Test
    public void trim_right_hiho_multiple_ways() {
        assertEquals("hiho",SimpleStringUtils.trimRight("hiho"));
        assertEquals("hiho",SimpleStringUtils.trimRight("hiho "));
        assertEquals("hiho",SimpleStringUtils.trimRight("hiho     "));
        assertEquals("hiho",SimpleStringUtils.trimRight("hiho\n    "));
    }
    
    @Test
    public void trim_right_empty() {
        assertEquals("",SimpleStringUtils.trimRight(""));
        assertEquals("",SimpleStringUtils.trimRight(" "));
        assertEquals("",SimpleStringUtils.trimRight("\n"));
    }
    @Test
    public void trim_right_null() {
        assertEquals(null,SimpleStringUtils.trimRight(null));
    }

    @Test
    public void short_string_null_1_returns_empty_string() {
        assertEquals("", SimpleStringUtils.shortString(null, 1));
    }
    
    @Test
    public void short_string_a_0_returns_empty_string() {
        assertEquals("", SimpleStringUtils.shortString("a", 0));
    }
    
    @Test
    public void short_string_a_1_returns_a_string() {
        assertEquals("a", SimpleStringUtils.shortString("a", 1));
    }
    
    @Test
    public void short_string_a_2_returns_a_string() {
        assertEquals("a", SimpleStringUtils.shortString("a", 2));
    }
    
    @Test
    public void short_string_12345678901_10_returns_1234567_dot_dot_dot_string() {
        assertEquals("1234567...", SimpleStringUtils.shortString("12345678901", 10));
    }
    
    @Test
    public void short_string_1234567890_10_returns_1234567890_string() {
        assertEquals("1234567890", SimpleStringUtils.shortString("1234567890", 10));
    }

    @Test
    public void null_equals_null__is_true() {
        assertTrue(SimpleStringUtils.equals(null, null));
    }
    
    @Test
    public void a_equals_a__is_true() {
        assertTrue(SimpleStringUtils.equals("a", "a"));
    }
    
    @Test
    public void a_equals_null__is_false() {
        assertFalse(SimpleStringUtils.equals("a", null));
    }
    
    @Test
    public void a_equals_b__is_false() {
        assertFalse(SimpleStringUtils.equals("a","b"));
    }
    
    @Test
    public void b_equals_a__is_false() {
        assertFalse(SimpleStringUtils.equals("b","a"));
    }
    
    @Test
    public void null_equals_a__is_false() {
        assertFalse(SimpleStringUtils.equals(null,"a"));
    }


}
