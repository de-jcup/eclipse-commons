package de.jcup.eclipse.commons.codeassist;

import java.util.List;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.contentassist.BoldStylerProvider;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.ICompletionProposalExtension6;
import org.eclipse.jface.text.contentassist.ICompletionProposalExtension7;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;

import de.jcup.eclipse.commons.PluginContextProvider;
import de.jcup.eclipse.commons.codeassist.ProposalProvider;
import de.jcup.eclipse.commons.codeassist.ProposalProviderSupport;
import de.jcup.eclipse.commons.source.SourceCodeBuilder;
import de.jcup.eclipse.commons.ui.EclipseUtil;

public class SimpleCompletionProposal implements ICompletionProposal, ICompletionProposalExtension6, ICompletionProposalExtension7 {

		private int offset;
		private ProposalProvider proposalProvider;
		private int nextSelection;
		private StyledString styledString;
		private String textBefore;
		private PluginContextProvider provider;
		private ProposalInfoProvider proposalInfoProvider;

		public SimpleCompletionProposal(ProposalProviderSupport completion, PluginContextProvider provider, IDocument document, int offset, ProposalProvider proposalProvider, ProposalInfoProvider proposalInfo) {
			this.offset = offset;
			this.proposalProvider = proposalProvider;
			this.provider=provider;
			this.proposalInfoProvider=proposalInfo;
			
			String source = document.get();
			textBefore = completion.getTextbefore(source, offset);
		}

		
		@Override
		public void apply(IDocument document) {
			List<String> template = proposalProvider.getCodeTemplate();
			int zeroOffset = offset - textBefore.length();

			int offsetBefore=zeroOffset-1;
			String indentionStringBefore = createIndentionString(document, zeroOffset, offsetBefore);
			try {
				
				String codeWithCursorTag= sourceCodeBuilder.buildCode(indentionStringBefore, template,false);
				String codeWithoutCursorTag = sourceCodeBuilder.buildCode(indentionStringBefore, template,true);
				
				document.replace(zeroOffset, textBefore.length(), codeWithoutCursorTag);
				nextSelection = sourceCodeBuilder.calculateNextSelection(zeroOffset,codeWithCursorTag);
				
			} catch (BadLocationException e) {
				EclipseUtil.logError("Not able to replace by proposal:" + proposalProvider +", zero offset:"+zeroOffset+", textBefore:"+textBefore, e,provider);
			}

		}


		protected String createIndentionString(IDocument document, int zeroOffset, int offsetBefore) {
			StringBuilder indentionStringBefore = new StringBuilder();
			while (offsetBefore>0){
				char b;
				try {
					b = document.getChar(offsetBefore);
				} catch (BadLocationException e) {
					EclipseUtil.logError("Not able to create indention string for proposal:" + proposalProvider +", zero offset:"+zeroOffset+", textBefore:"+textBefore, e,provider);
					return "";
				}
				if (b=='\n' || b=='\r'){
					break;
				}
				if (b==' ' || b=='\t'){
					indentionStringBefore.append(b);
					offsetBefore--;
				}else{
					break;
				}
				
			}
			return indentionStringBefore.toString();
		}
		
		private SourceCodeBuilder sourceCodeBuilder = new SourceCodeBuilder();
		

		@Override
		public Point getSelection(IDocument document) {
			Point point = new Point(nextSelection, 0);
			return point;
		}

		@Override
		public String getAdditionalProposalInfo() {
			/* normally no longer used because of other method */
			Object info = proposalInfoProvider.getProposalInfo(null,proposalProvider.getLabel());
			if (info!=null){
				return info.toString();
			}
			return "";
		}
		
		@Override
		public String getDisplayString() {
			return proposalProvider.getLabel();
		}

		@Override
		public Image getImage() {
			if (proposalInfoProvider==null){
				return null;
			}
			return proposalInfoProvider.getImage(proposalProvider.getLabel());
		}

		@Override
		public IContextInformation getContextInformation() {
			return null;//new ContextInformation(word, getAdditionalProposalInfo());
		}

		@Override
		public StyledString getStyledDisplayString(IDocument document, int offset,
				BoldStylerProvider boldStylerProvider) {
			if (styledString != null) {
				return styledString;
			}
			styledString = new StyledString();
			styledString.append(proposalProvider.getLabel());
			try {

				int enteredTextLength = textBefore.length();
				int indexOfTextBefore = proposalProvider.getLabel().toLowerCase().indexOf(textBefore.toLowerCase());

				if (indexOfTextBefore != -1) {
					styledString.setStyle(indexOfTextBefore, enteredTextLength, boldStylerProvider.getBoldStyler());
				}
			} catch (RuntimeException e) {
				EclipseUtil.logError("Not able to set styles for proposal:" + proposalProvider.getLabel(), e,provider);
			}
			return styledString;
		}
		
		@Override
		public StyledString getStyledDisplayString() {
			return null;
		}
		
	}