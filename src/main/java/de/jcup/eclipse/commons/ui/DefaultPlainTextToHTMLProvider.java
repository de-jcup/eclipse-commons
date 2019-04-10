package de.jcup.eclipse.commons.ui;

public class DefaultPlainTextToHTMLProvider implements PlainTextToHTMLProvider {

    private CSSProvider cssProvider;

    public DefaultPlainTextToHTMLProvider() {
        this(null);
    }
    public DefaultPlainTextToHTMLProvider(CSSProvider defaultCssProvider) {
        if (defaultCssProvider == null) {
            defaultCssProvider = new DefaultCssProvider();
        }
        this.cssProvider = defaultCssProvider;
    }


    @Override
    public CSSProvider getCSSProvider() {
        return cssProvider;
    }

    @Override
    public String getHTML(String information) {
        StringBuilder htmlSb = new StringBuilder();
        htmlSb.append("<html>");
        htmlSb.append("<meta charset=\"UTF-8\">");
        htmlSb.append("<style type=\"text/css\">");
        htmlSb.append(cssProvider.getCSS());
        htmlSb.append("</style>");
        htmlSb.append("<body><pre class=\"preWrapEnabled\">");
        htmlSb.append(information);
        htmlSb.append("</pre></body></html>");
        return htmlSb.toString();
    }
}
