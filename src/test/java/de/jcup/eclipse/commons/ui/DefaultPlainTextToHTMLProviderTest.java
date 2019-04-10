package de.jcup.eclipse.commons.ui;

import static org.junit.Assert.*;

import org.junit.Test;

public class DefaultPlainTextToHTMLProviderTest {

    @Test
    public void test_default_output_as_expected() {
        /* prepare*/
        DefaultPlainTextToHTMLProvider provider = new DefaultPlainTextToHTMLProvider();
        
        /* execute */
        String html = provider.getHTML("line1\nline2");
        
        /* test */
        assertEquals("<html><meta charset=\"UTF-8\"><style type=\"text/css\">.preWrapEnabled {\n" + 
                "        white-space: pre-wrap;\n" + 
                "        white-space: -moz-pre-wrap;\n" + 
                "        white-space: -pre-wrap;\n" + 
                "        white-space: -o-pre-wrap;\n" + 
                "        word-wrap: break-word;\n" + 
                "}\n" + 
                "</style><body><pre class=\"preWrapEnabled\">line1\nline2</pre></body></html>",html);
    }

}
