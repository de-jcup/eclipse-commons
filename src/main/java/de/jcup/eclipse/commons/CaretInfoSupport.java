package de.jcup.eclipse.commons;

import org.eclipse.swt.custom.CaretEvent;
import org.eclipse.swt.custom.CaretListener;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.texteditor.ITextEditor;

public class CaretInfoSupport implements CaretInfoProvider{
    private int lastCaretPosition;

    public void install(ITextEditor editor) {
        Control adapter = editor.getAdapter(Control.class);
        if (adapter instanceof StyledText) {
            StyledText text = (StyledText) adapter;
            text.addCaretListener(new CaretAwareSupportCaretListener());
        }

    }
    
    public int getLastCaretPosition() {
        return lastCaretPosition;
    }
    
    private class CaretAwareSupportCaretListener implements CaretListener {


        @Override
        public void caretMoved(CaretEvent event) {
            if (event == null) {
                return;
            }
            lastCaretPosition = event.caretOffset;
        }

    }
    
}
