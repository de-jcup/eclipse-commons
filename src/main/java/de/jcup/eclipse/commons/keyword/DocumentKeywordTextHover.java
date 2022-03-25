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
package de.jcup.eclipse.commons.keyword;

import org.eclipse.jface.text.DefaultInformationControl;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IInformationControl;
import org.eclipse.jface.text.IInformationControlCreator;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextHover;
import org.eclipse.jface.text.ITextHoverExtension;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.Region;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Shell;

import de.jcup.eclipse.commons.SimpleStringUtils;
import de.jcup.eclipse.commons.WhitespaceWordEndDetector;
import de.jcup.eclipse.commons.WordEndDetector;
import de.jcup.eclipse.commons.ui.ColorUtil;
import de.jcup.eclipse.commons.ui.EclipseUtil;
import de.jcup.eclipse.commons.ui.OpenLinksInExternalBrowserListener;
import de.jcup.eclipse.commons.ui.ReducedBrowserInformationControl;

public class DocumentKeywordTextHover implements ITextHover, ITextHoverExtension {

	private static final WordEndDetector WHITE_SPACE_END_DETECTOR = new WhitespaceWordEndDetector();
	private IInformationControlCreator creator;
	private String bgColor;
	private String fgColor;
	private String commentColorWeb;
	private TooltipTextSupportPreferences preferences;
	private WordEndDetector reducedWordEndDetector;

	public DocumentKeywordTextHover(TooltipTextSupportPreferences preferences, WordEndDetector reducedWordEndDetector) {
		this.preferences=preferences;
		this.reducedWordEndDetector=reducedWordEndDetector;
	}
	
	@Override
	public IInformationControlCreator getHoverControlCreator() {
		if (creator == null) {
			creator = new DocumentKeywordTextHoverControlCreator();
		}
		return creator;
	}

	@Override
	public String getHoverInfo(ITextViewer textViewer, IRegion hoverRegion) {
		
		if (!preferences.areTooltipsForKeyWordsEnabled()){
			return null;
		}
		
		if (bgColor == null || fgColor == null) {

			StyledText textWidget = textViewer.getTextWidget();
			if (textWidget != null) {

				EclipseUtil.getSafeDisplay().syncExec(new Runnable() {

					@Override
					public void run() {
						bgColor = ColorUtil.convertToHexColor(textWidget.getBackground());
						fgColor = ColorUtil.convertToHexColor(textWidget.getForeground());
					}
				});
			}

		}
		if (commentColorWeb == null) {
			commentColorWeb =  preferences.getCommentColorWeb();
		}
		
		IDocument document = textViewer.getDocument();
		if (document == null) {
			return "";
		}
		String text = document.get();
		if (text == null) {
			return "";
		}
		int offset = hoverRegion.getOffset();
		String word = SimpleStringUtils.nextReducedVariableWord(text, offset,reducedWordEndDetector);
		if (word.isEmpty()){
			/* FIXME ALBERT, 2019-04-06: this is asciidoctor editor specific and should 
			 * be removed. Also html building shall be a generic approach - maybe interface
			 * necessary or extension of configuration
			 */
		    
		    /* special case for headlines - reduction detector does remove "=" always */
			String section = SimpleStringUtils.nextWord(text, offset, WHITE_SPACE_END_DETECTOR);
			if (section.startsWith("=")){
				StringBuilder sb = new StringBuilder();
				for (char c: section.toCharArray()){
					if (c=='='){
						sb.append(c);
					}else{
						break;
					}
				}
				word = sb.toString();
			}
		}
		/* FIXME ATR, 27.09.2018: add interface wordreducer and make a generic approach here! was copied from asciidoctor editor! */
		/* reduce words like include::xyz to 'include::' */
		/* a part like :icons: will not be influenced */
		int indexOf = word.indexOf("::");
		if (indexOf!=-1){
			word=word.substring(0,indexOf+2);
		}
		if (word.isEmpty()) {
			return "";
		}

		for (DocumentKeyWord keyword :preferences.getAllKeywords()) {
		    if (preferences.isIgnoreCaseOnKeywordText()) {
		        if (word.equalsIgnoreCase(keyword.getText())) {
                    return buildHoverInfo(keyword);
                }
		    }else {
		        if (word.equals(keyword.getText())) {
		            return buildHoverInfo(keyword);
		        }
		    }
		}

		return "";
	}

	private String buildHoverInfo(DocumentKeyWord keyword) {
		String link = keyword.getLinkToDocumentation();
		String tooltip = keyword.getTooltip();

		if (isEmpty(tooltip) && isEmpty(link)) {
			return "";
		}

		StringBuilder sb = new StringBuilder();
		sb.append("<html>");
		sb.append("<head>");
		sb.append("<style>");
		sb.append(TooltipTextSupport.getTooltipCSS());
		addCSStoBackgroundTheme(sb);
		sb.append("</style>");
		sb.append("</head>");
		sb.append("<body>");
		if (!isEmpty(link)) {
			sb.append("Detailed information available at: <a href='" + link + "' target='_blank'>" + link
					+ "</a><br><br>");
		}
		
		sb.append("<u>Offline description:</u>");
		if (isEmpty(tooltip)) {
			sb.append("<b>Not available</b>");
		} else {
			if (TooltipTextSupport.isHTMLToolTip(tooltip)){
				/* it's already a HTML variant - so just keep as is*/
				sb.append(tooltip);
			}else{
				/* plain text */
				sb.append("<pre class='preWrapEnabled'>");
				sb.append(tooltip);
				sb.append("</pre>");
			}
			
		}
		sb.append("</body>");
		return sb.toString();
	}

	private void addCSStoBackgroundTheme(StringBuilder sb) {
		if (bgColor==null){
			return;
		}
		if (fgColor==null){
			return;
		}
		sb.append("body {");
		sb.append("background-color:").append(bgColor).append(";");
		sb.append("color:").append(fgColor).append(";");
		sb.append("}");
		
	}

	
	
	private boolean isEmpty(String string) {
		if (string == null) {
			return true;
		}
		return string.isEmpty();
	}

	@Override
	public IRegion getHoverRegion(ITextViewer textViewer, int offset) {
		return new Region(offset, 0);
	}

	private class DocumentKeywordTextHoverControlCreator implements IInformationControlCreator {

		@Override
		public IInformationControl createInformationControl(Shell parent) {
			if (ReducedBrowserInformationControl.isAvailableFor(parent)) {
				ReducedBrowserInformationControl control = new ReducedBrowserInformationControl(parent);
				control.setListener(new OpenLinksInExternalBrowserListener());
				return control;
			} else {
				return new DefaultInformationControl(parent, true);
			}
		}
	}

}
