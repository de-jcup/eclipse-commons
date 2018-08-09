package de.jcup.eclipse.commons.source;

import java.util.ArrayList;
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

	public String buildCode(String indentionStringBefore, List<String> proposalLines, boolean replaceCursorTag) {
		StringBuilder sb = new StringBuilder();
		boolean mustIndent=false;
		for (String proposalLine: proposalLines){
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
			sb.append("\n");
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
