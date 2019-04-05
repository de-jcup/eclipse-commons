package testcase.de.jcup.eclipse.commons;

import java.io.File;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.IAction;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditorActionConstants;

import de.jcup.eclipse.commons.EclipseResourceHelper;
import de.jcup.eclipse.commons.PluginContextProvider;
import de.jcup.eclipse.commons.replacetabbyspaces.ReplaceTabBySpacesProvider;
import de.jcup.eclipse.commons.replacetabbyspaces.ReplaceTabBySpacesSupport;
import de.jcup.eclipse.commons.ui.EclipseUtil;
import testcase.de.jcup.eclipse.commons.other.TestCaseSourceViewerConfiguration;
import testcase.de.jcup.eclipse.commons.other.TestcaseFileDocumentProvider;

public class TestcaseEditor extends TextEditor {

    private ReplaceTabBySpacesSupport replaceTabBySpaceSupport = new ReplaceTabBySpacesSupport();

    public TestcaseEditor() {
        setSourceViewerConfiguration(new TestCaseSourceViewerConfiguration());
    }

    @Override
    public void createPartControl(Composite parent) {
        parent.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_DARK_GREEN));
        super.createPartControl(parent);

        replaceTabBySpaceSupport.install(this, new ReplaceTabBySpacesProvider() {

            @Override
            public boolean isReplaceTabBySpacesEnabled() {
                return true;
            }

            @Override
            public PluginContextProvider getPluginContextProvider() {
                return TestcaseActivator.getDefault();
            }

            @Override
            public int getAmountOfSpacesToReplaceTab() {
                return 6;
            }
        });

    }

    @Override
    protected void doSetInput(IEditorInput input) throws CoreException {
        setDocumentProvider(createDocumentProvider(input));
        super.doSetInput(input);
        try {
            File file = EclipseResourceHelper.DEFAULT.getFileOfEditor(this);
            String message = "[Test resource helper] - Input changed, file is:" + file;
            System.out.println(">>> " + message);
            this.getStatusLineManager().setMessage(message);

        } catch (CoreException e) {
            EclipseUtil.logError("Was not able to get file of editor", e, TestcaseActivator.getDefault());
        }
    }

    protected IDocumentProvider createDocumentProvider(IEditorInput input) {
        return new TestcaseFileDocumentProvider();
    }

    @Override
    protected void createActions() {
        super.createActions();

        IAction action = getAction(ITextEditorActionConstants.CONTENT_ASSIST);
        if (action != null) {
            action.setText("Testcase template proposal label");
            action.setToolTipText("Testcase template proposal tooltip");
            action.setDescription("Testcase template proposal description");
        }
    }

    @Override
    protected void handleEditorInputChanged() {
        super.handleEditorInputChanged();
    }

}
