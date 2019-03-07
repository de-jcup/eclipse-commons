package de.jcup.eclipse.commons.replacetabbyspaces;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class TabReplaceBySpacesStringBuilderTest {

    private TabReplaceBySpacesStringBuilder builderToTest;

    @Before
    public void before() {
        builderToTest = new TabReplaceBySpacesStringBuilder();
    }
    
    @Test
    public void createBlockReplacement_indent_3_lines_4_spaces() {
        /* prepare */
        StringBuilder sb1 = new StringBuilder();
        sb1.append("line1\n");
        sb1.append("line2\n");
        sb1.append("line3\n");
        String lineBlock = sb1.toString();
        
        int numSpaces=4;
        
        boolean doIndent=true;
        
        /* execute*/
        String replacement = builderToTest.createBlockReplacement(doIndent, numSpaces, lineBlock);
        
        /* test */
        StringBuilder sb2 = new StringBuilder();
        sb2.append("    line1\n");
        sb2.append("    line2\n");
        sb2.append("    line3"); // <-- last line has no \n
        String expected = sb2.toString();
        
        assertEquals(expected,replacement);
    }
    
    @Test
    public void createBlockReplacement_indent_3_lines_6_spaces() {
        /* prepare */
        StringBuilder sb1 = new StringBuilder();
        sb1.append("line1\n");
        sb1.append("line2\n");
        sb1.append("line3\n");
        String lineBlock = sb1.toString();
        
        int numSpaces=6;
        
        boolean doIndent=true;
        
        /* execute*/
        String replacement = builderToTest.createBlockReplacement(doIndent, numSpaces, lineBlock);
        
        /* test */
        StringBuilder sb2 = new StringBuilder();
        sb2.append("      line1\n");
        sb2.append("      line2\n");
        sb2.append("      line3"); // <-- last line has no \n
        String expected = sb2.toString();
        
        assertEquals(expected,replacement);
    }
    
    @Test
    public void createBlockReplacement_outdent_3_lines_before_6_spaces() {
        /* prepare */
        StringBuilder sb1 = new StringBuilder();
        sb1.append("      line1\n");
        sb1.append("      line2\n");
        sb1.append("      line3\n"); 
        
        String lineBlock = sb1.toString();
        
        int numSpaces=3;
        
        boolean doIndent=false;
        
        /* execute*/
        String replacement = builderToTest.createBlockReplacement(doIndent, numSpaces, lineBlock);
        
        /* test */
        StringBuilder sb2 = new StringBuilder();
        sb2.append("   line1\n");
        sb2.append("   line2\n");
        sb2.append("   line3");// <-- last line has no \n
        
        String expected = sb2.toString();
        
        assertEquals(expected,replacement);
    }
    @Test
    public void createBlockReplacement_outdent_4_lines_before_10_spaces() {
        /* prepare */
        StringBuilder sb1 = new StringBuilder();
        sb1.append("          line1\n");
        sb1.append("          line2\n");
        sb1.append("          line3\n"); 
        
        String lineBlock = sb1.toString();
        
        int numSpaces=4;
        
        boolean doIndent=false;
        
        /* execute*/
        String replacement = builderToTest.createBlockReplacement(doIndent, numSpaces, lineBlock);
        
        /* test */
        StringBuilder sb2 = new StringBuilder();
        sb2.append("      line1\n");
        sb2.append("      line2\n");
        sb2.append("      line3");// <-- last line has no \n
        
        String expected = sb2.toString();
        
        assertEquals(expected,replacement);
    }
    
    @Test
    public void createBlockReplacement_outdent_4_lines_before_0_spaces() {
        /* prepare */
        StringBuilder sb1 = new StringBuilder();
        sb1.append("line1\n");
        sb1.append("line2\n");
        sb1.append("line3\n"); 
        
        String lineBlock = sb1.toString();
        
        int numSpaces=4;
        
        boolean doIndent=false;
        
        /* execute*/
        String replacement = builderToTest.createBlockReplacement(doIndent, numSpaces, lineBlock);
        
        /* test */
        StringBuilder sb2 = new StringBuilder();
        sb2.append("line1\n");
        sb2.append("line2\n");
        sb2.append("line3");// <-- last line has no \n
        
        String expected = sb2.toString();
        
        assertEquals(expected,replacement);
    }
    
    @Test
    public void createBlockReplacement_outdent_4_lines_before_1to4_spaces() {
        /* prepare */
        StringBuilder sb1 = new StringBuilder();
        sb1.append("   line1\n");
        sb1.append("  line2\n");
        sb1.append(" line3\n"); 
        
        String lineBlock = sb1.toString();
        
        int numSpaces=4;
        
        boolean doIndent=false;
        
        /* execute*/
        String replacement = builderToTest.createBlockReplacement(doIndent, numSpaces, lineBlock);
        
        /* test */
        StringBuilder sb2 = new StringBuilder();
        sb2.append("line1\n");
        sb2.append("line2\n");
        sb2.append("line3");// <-- last line has no \n
        
        String expected = sb2.toString();
        
        assertEquals(expected,replacement);
    }

    @Test
    public void createTabReplacement_negative_or_zero_is_empty_string() {
        assertEquals("", builderToTest.createTabReplacement(0));
        assertEquals("", builderToTest.createTabReplacement(-1));
    }

    @Test
    public void createTabReplacement_one_is_one_space() {
        assertEquals(" ", builderToTest.createTabReplacement(1));
    }

    @Test
    public void createTabReplacement_ten_is_ten_space() {
        assertEquals("          ", builderToTest.createTabReplacement(10));
    }

    @Test
    public void outdent_null_string_keeps_empty() {
        assertEquals("", builderToTest.outdent(null, -1));
        assertEquals("", builderToTest.outdent(null, 6));
    }

    @Test
    public void outdent_empty_string_keeps_empty() {
        assertEquals("", builderToTest.outdent("", -1));
        assertEquals("", builderToTest.outdent("", 6));
        assertEquals("", builderToTest.outdent("", 0));
        assertEquals("", builderToTest.outdent("", 1));
    }
    
    @Test
    public void outdent_string_6_spaces_with_n1_keeps_6_spaces() {
        assertEquals("      X", builderToTest.outdent("      X", -1));
    }
    
    @Test
    public void outdent_string_6_spaces_with_0_keeps_6_spaces() {
        assertEquals("      X", builderToTest.outdent("      X", 0));
    }

    @Test
    public void outdent_string_6_spaces_with_4_keeps_2_spaces() {
        assertEquals("  X", builderToTest.outdent("      X", 4));
    }

    @Test
    public void outdent_string_6_spaces_with_6_keeps_0_spaces() {
        assertEquals("X", builderToTest.outdent("      X", 6));
    }
    
    @Test
    public void outdent_string_6_spaces_with_8_keeps_0_spaces() {
        assertEquals("X", builderToTest.outdent("      X", 8));
    }
}
