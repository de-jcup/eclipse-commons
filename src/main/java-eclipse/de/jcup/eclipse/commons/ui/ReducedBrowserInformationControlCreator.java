package de.jcup.eclipse.commons.ui;

import org.eclipse.jface.text.IInformationControl;
import org.eclipse.jface.text.IInformationControlCreator;
import org.eclipse.swt.widgets.Shell;

public class ReducedBrowserInformationControlCreator implements IInformationControlCreator {

	@Override
	public IInformationControl createInformationControl(Shell parent) {
		if (! ReducedBrowserInformationControl.isAvailableFor(parent)){
			return null;
		}
		return new ReducedBrowserInformationControl(parent);
	}

}