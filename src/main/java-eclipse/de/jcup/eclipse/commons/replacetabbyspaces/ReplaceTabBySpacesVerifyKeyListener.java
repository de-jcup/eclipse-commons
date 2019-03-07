/*
 * Copyright 2018 Albert Tregnaghi
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *		http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions
 * and limitations under the License.
 *
 */
package de.jcup.eclipse.commons.replacetabbyspaces;

import java.util.ArrayList;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.custom.VerifyKeyListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditor;

import de.jcup.eclipse.commons.CaretInfoProvider;
import de.jcup.eclipse.commons.ui.EclipseUtil;

class ReplaceTabBySpacesVerifyKeyListener implements VerifyKeyListener {

    private final ITextEditor editor;
    private ReplaceTabBySpacesProvider replaceTabBySpaceProvider;
    private CaretInfoProvider caretInfoProvider;

    ReplaceTabBySpacesVerifyKeyListener(ITextEditor editor, ReplaceTabBySpacesProvider replaceTabBySpaceProvider, CaretInfoProvider caretInfoProvider) {
        if (editor == null) {
            throw new IllegalArgumentException("editor may not be null");
        }
        this.editor = editor;
        this.replaceTabBySpaceProvider = replaceTabBySpaceProvider;
        this.caretInfoProvider = caretInfoProvider;
    }

    public void verifyKey(VerifyEvent event) {
        if (event.character != '\t') {
            return;
        }
        if (!replaceTabBySpaceProvider.isReplaceTabBySpacesEnabled()) {
            return;
        }

        event.doit = false;

        EclipseUtil.safeAsyncExec(()->{

                ISelection selection = editor.getSelectionProvider().getSelection();
                if (!(selection instanceof ITextSelection)) {
                    return;
                }
                
                ITextSelection ts = (ITextSelection) selection;
                IDocumentProvider dp = editor.getDocumentProvider();
                IDocument doc = dp.getDocument(editor.getEditorInput());
                int offset = ts.getOffset();
                if (offset == -1) {
                    offset = caretInfoProvider.getLastCaretPosition();
                }

                boolean isMultiline = ts.getStartLine() != -1 && ts.getEndLine() > ts.getStartLine();
                boolean doIndent = event.stateMask == 0;
                boolean doOutdent = (event.stateMask & SWT.SHIFT) == SWT.SHIFT;

                if (!doIndent && !doOutdent) {
                    return;
                }

                handleIndentOutdent(ts, doc, offset, isMultiline, doIndent);
            }

           
        );

    }

    private void handleIndentOutdent(ITextSelection ts, IDocument doc, int offset, boolean isMultiline, boolean doIndent) {
        try {
            int numSpaces = replaceTabBySpaceProvider.getAmountOfSpacesToReplaceTab();
            if (numSpaces < 1) {
                return;
            }

            if (isMultiline) {
                handleMultiLineSelection(ts, doc, doIndent, numSpaces);
            } else {
                handleSingleLineSelection(ts, doc, offset, doIndent, numSpaces);
            }

        } catch (BadLocationException e) {
            EclipseUtil.logError("Cannot insert tab replacement at " + offset, e, replaceTabBySpaceProvider.getPluginContextProvider());
        }
    }

    private void handleSingleLineSelection(ITextSelection ts, IDocument doc, int offset, boolean doIndent, int numSpaces) throws BadLocationException {
        int newCaretPosition;

        if (doIndent) {
            String tabReplacement = createTabReplacement(numSpaces);
            // replace the selected text with our TAB-equivalent string:
            doc.replace(offset, ts.getLength(), tabReplacement);
            newCaretPosition = offset + numSpaces;
        } else {
            // special behavior: for single-line outdent the logic is similar to the
            // multiline outdent except for the fact that the replaced block shall not be
            // selected entirely, instead only the caret will be adjusted:

            int offsetBlockStart = doc.getLineOffset(ts.getStartLine());
            int offsetBlockEnd = doc.getLineOffset(ts.getEndLine()) + doc.getLineLength(ts.getEndLine());
            int lengthBlock = offsetBlockEnd - offsetBlockStart;
            if (lengthBlock <= 0) {
                return; // should never happen - just in case
            }

            String line = doc.get(offsetBlockStart, lengthBlock);
            String replacement = outdent(line, numSpaces);

            doc.replace(offsetBlockStart, lengthBlock, replacement);

            if (offset > offsetBlockStart + numSpaces) {
                // there is enough space to move left the caret on the current line:
                newCaretPosition = offset - numSpaces;
            } else {
                // don't place the caret on the line before the current one:
                newCaretPosition = offsetBlockStart;
            }
        }

        Control control = editor.getAdapter(Control.class);
        if (control instanceof StyledText) {
            StyledText t = (StyledText) control;
            t.setCaretOffset(newCaretPosition);
        }
    }

    private void handleMultiLineSelection(ITextSelection ts, IDocument doc, boolean doIndent, int numSpaces) throws BadLocationException {
        // in case there is a multiline selection, we mimic the Eclipse behavior
        // (which is quite standard across editors) and thus we:
        // - discard the actual selected text and consider instead only the
        // start/end line of the selection
        // - then we indent/outdent the whole block of lines

        int offsetBlockStart = doc.getLineOffset(ts.getStartLine());
        int offsetBlockEnd = doc.getLineOffset(ts.getEndLine()) + doc.getLineLength(ts.getEndLine());
        int lengthBlock = offsetBlockEnd - offsetBlockStart;
        if (lengthBlock <= 0) {
            return; // should never happen - just in case
        }
        String lineBlock = doc.get(offsetBlockStart, lengthBlock);

        String strReplacement = createReplacementString(doIndent, numSpaces, lineBlock);
        // why next line with lenthBlock-1 ?
        // lineBlock is WITH lineEnding ending.
        // String.join is without last \n, to keep the last line ending we use
        // lengthBlock-1...
        doc.replace(offsetBlockStart, lengthBlock - 1, strReplacement);

        // select the whole block we just indented/outdented:
        editor.selectAndReveal(offsetBlockStart, strReplacement.length());
    }

    private String createReplacementString(boolean doIndent, int numSpaces, String lineBlock) {
        String tabReplacement = createTabReplacement(numSpaces);
        // split each line and insert the additional indent in each line:
        String lines[] = lineBlock.split("\\r?\\n");
        ArrayList<String> replacement = new ArrayList<String>();
        for (String line : lines) {
            String newLine = null;
            if (doIndent) {
                newLine = indent(line, tabReplacement);
            } else {
                newLine = outdent(line, numSpaces);
            }
            replacement.add(newLine);
        }

        String strReplacement = String.join("\n", replacement);
        return strReplacement;
    }
    
    private String createTabReplacement(int spaces) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < spaces; i++) {
            sb.append(' ');
        }
        return sb.toString();
    }

    private String outdent(String line, int spaces) {
        int numLeadingSpaces = 0;
        for (int i = 0; i < spaces && i < line.length(); i++) {
            if (line.charAt(i) == ' ')
                numLeadingSpaces++;
            else
                break;
        }

        // most editors, including default Eclipse text editor, will outdent a line even
        // if
        // the number of leading spaces is smaller than the configured TAB length:
        // in such a case they simply remove all leading spaces:
        return line.substring(Math.min(numLeadingSpaces, spaces));
    }

    // just for symmetry with outdent():
    private String indent(String line, String toInsert) {
        return toInsert + line;
    }
}