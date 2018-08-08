package testcase.de.jcup.eclipse.commons;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.part.ViewPart;

public class TestcaseView extends ViewPart{

	@Override
	public void createPartControl(Composite parent) {
		parent.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_DARK_GREEN));
	}

	@Override
	public void setFocus() {
		
	}

	
}
