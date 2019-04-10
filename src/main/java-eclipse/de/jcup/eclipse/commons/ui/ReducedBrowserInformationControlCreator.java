package de.jcup.eclipse.commons.ui;

import org.eclipse.jface.text.IInformationControl;
import org.eclipse.jface.text.IInformationControlCreator;
import org.eclipse.swt.widgets.Shell;

public class ReducedBrowserInformationControlCreator implements IInformationControlCreator {

    /**
     * Fallback html provider is used there where no html is set to information control but normal text
     */
    private PlainTextToHTMLProvider fallbackHtmlProvider;

    public ReducedBrowserInformationControlCreator() {
        this(new DefaultPlainTextToHTMLProvider(new DefaultCssProvider()));
    }
    
	public ReducedBrowserInformationControlCreator(PlainTextToHTMLProvider fallbackHtmlProvider) {
        this.fallbackHtmlProvider = fallbackHtmlProvider;
    }

    @Override
	public IInformationControl createInformationControl(Shell parent) {
		if (! ReducedBrowserInformationControl.isAvailableFor(parent)){
			return null;
		}
		return new ReducedBrowserInformationControl(parent,fallbackHtmlProvider);
	}
    
    public PlainTextToHTMLProvider getFallbackHtmlProvider() {
        return fallbackHtmlProvider;
    }

}