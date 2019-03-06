package de.jcup.eclipse.commons.replacetabbyspaces;

import org.eclipse.jface.text.ITextViewer;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.ui.texteditor.ITextEditor;

import de.jcup.eclipse.commons.CaretInfoProvider;
import de.jcup.eclipse.commons.CaretInfoSupport;

public class ReplaceTabBySpacesSupport {

    private CaretInfoSupport caretInfoSupport;
    private ReplaceTabBySpacesVerifyKeyListener listener;

    /**
     * Installs support on given editor, will also install own caret info provider
     * @param editor
     * @param replaceTabBySpaceProvider
     */
    public void install(ITextEditor editor, ReplaceTabBySpacesProvider replaceTabBySpaceProvider) {
        if (editor==null) {
            throw new IllegalArgumentException("editor may not be null!");
        }
        caretInfoSupport = new CaretInfoSupport();
        caretInfoSupport.install(editor);

        install(editor, replaceTabBySpaceProvider,caretInfoSupport);

    }

    /**
     * Installs support on given editor, will use given caret info provider
     * @param editor
     * @param replaceTabBySpaceProvider
     * @param caretInfoProvider
     */
    public void install(ITextEditor editor, ReplaceTabBySpacesProvider replaceTabBySpaceProvider, CaretInfoProvider caretInfoProvider) {
        if (editor==null) {
            throw new IllegalArgumentException("editor may not be null!");
        }
        if (replaceTabBySpaceProvider==null) {
            throw new IllegalArgumentException("replaceTabBySpaceProvider may not be null!");
        }
        if (caretInfoProvider==null) {
            throw new IllegalArgumentException("caretInfoProvider may not be null!");
        }
        ITextViewer textViewer = editor.getAdapter(ITextViewer.class);
        if (textViewer==null) {
            throw new IllegalArgumentException("textViewer may not be null!");
        }
        StyledText widget = textViewer.getTextWidget();
        if (widget==null) {
            throw new IllegalArgumentException("textviewer widget may not be null!");
        }
        listener = new ReplaceTabBySpacesVerifyKeyListener(editor, replaceTabBySpaceProvider, caretInfoProvider);
        widget.addVerifyKeyListener(listener);
    }

}
