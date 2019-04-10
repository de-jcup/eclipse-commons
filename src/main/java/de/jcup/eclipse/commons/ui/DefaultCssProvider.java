package de.jcup.eclipse.commons.ui;

public class DefaultCssProvider implements CSSProvider {

    protected String backgroundColor;
    protected String foregroundColor;
    
    @Override
    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }
    
    @Override
    public void setForegroundColor(String foregroundColor) {
        this.foregroundColor = foregroundColor;
    }
    
    @Override
    public String getCSS() {
        /* use this inside pre elements to show them up in code style but with wrapping-
         * this is automatically used for text only tooltip files 
         */
        StringBuilder sb = new StringBuilder();
        createCSS(sb);
        return sb.toString();
    }

    protected void createCSS(StringBuilder sb) {
        sb.append(".preWrapEnabled {\n");
        sb.append("        white-space: pre-wrap;\n");       /* Since CSS 2.1 */
        sb.append("        white-space: -moz-pre-wrap;\n");  /* Mozilla, since 1999 */
        sb.append("        white-space: -pre-wrap;\n");      /* Opera 4-6 */
        sb.append("        white-space: -o-pre-wrap;\n");    /* Opera 7 */
        sb.append("        word-wrap: break-word;\n");       /* Internet Explorer 5.5+ */
        sb.append("}\n");
        addCSStoBackgroundTheme(sb);
    }

    protected void addCSStoBackgroundTheme(StringBuilder sb) {
        if (backgroundColor==null){
            return;
        }
        if (foregroundColor==null){
            return;
        }
        sb.append("body {");
        sb.append("background-color:").append(backgroundColor).append(";");
        sb.append("color:").append(foregroundColor).append(";");
        sb.append("}");
        
    }

}
