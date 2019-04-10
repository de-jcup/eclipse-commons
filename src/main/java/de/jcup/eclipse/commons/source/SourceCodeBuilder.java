package de.jcup.eclipse.commons.source;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

public class SourceCodeBuilder {

	public static final String CURSOR_TAG = "####___cursor____####";
	private static final Pattern CURSOR_TAG_PATTERN= Pattern.compile(CURSOR_TAG);

	public List<String> buildClosureTemplate(String before){
		List<String> list = new ArrayList<>();
		list.add(before+" {");
		
		StringBuilder sb = new StringBuilder();
		for (int i=0;i<4;i++){
			sb.append(" ");
		}
		sb.append(CURSOR_TAG);
		list.add(sb.toString());
		list.add("}");
		return list;
	}

	public int calculateNextSelection(int zeroOffset,String code) {
		int index = code.indexOf(CURSOR_TAG);
		if (index==-1){
			return zeroOffset + code.length();
		}else{
			return zeroOffset + index;
		}
	}

	public String buildCode(String indentionStringBefore, List<String> proposalLines, boolean replaceCursorTag, boolean addNewLineAtEnd) {
	    if (proposalLines==null || proposalLines.isEmpty()) {
	        return "";
	    }
	    
	    StringBuilder sb = new StringBuilder();
		boolean mustIndent=false;
		for (Iterator<String> it = proposalLines.iterator();it.hasNext();){
		    String proposalLine = it.next();
			if (mustIndent){
				sb.append(indentionStringBefore);
			}
			String line = null;
			if (replaceCursorTag){
				line = CURSOR_TAG_PATTERN.matcher(proposalLine).replaceFirst("");
			}else{
				line = proposalLine;
			}
			sb.append(line);
			if (it.hasNext() || addNewLineAtEnd) {
			    sb.append("\n");
			}
			mustIndent=true;
		}
		return sb.toString();
	}

	public List<String> buildSingleLineTemplate(String content) {
		List<String> list = new ArrayList<>();
		list.add(content);
		return list;
	}
}
