package de.jcup.eclipse.commons.ui;

public interface PlainTextToHTMLProvider {

    CSSProvider getCSSProvider();

    String getHTML(String plainText);

}