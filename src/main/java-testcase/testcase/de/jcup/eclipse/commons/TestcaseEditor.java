package testcase.de.jcup.eclipse.commons;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.editors.text.TextEditor;

public class TestcaseEditor extends TextEditor{
	public TestcaseEditor() {
		setSourceViewerConfiguration(new TestCaseSourceViewerConfiguration());
	}
	@Override
	public void createPartControl(Composite parent) {
		parent.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_DARK_GREEN));
		super.createPartControl(parent);
	}
}
