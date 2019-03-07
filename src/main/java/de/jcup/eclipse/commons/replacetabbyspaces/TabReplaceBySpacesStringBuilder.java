package de.jcup.eclipse.commons.replacetabbyspaces;

import java.util.ArrayList;

public class TabReplaceBySpacesStringBuilder {

    /**
     * Creates a bloc replacement
     * @param doIndent
     * @param numSpaces
     * @param lineBlock
     * @return replacement string for block. Last line will have no ending new line!
     */
    public String createBlockReplacement(boolean doIndent, int numSpaces, String lineBlock) {
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
    
    public String createTabReplacement(int spaces) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < spaces; i++) {
            sb.append(' ');
        }
        return sb.toString();
    }

    public String outdent(String line, int spaces) {
        if (line==null) {
            return "";
        }
        int numLeadingSpaces = calculateAmountOfLeadingSpaces(line, spaces);

        // most editors, including default Eclipse text editor, will outdent a line even
        // if
        // the number of leading spaces is smaller than the configured TAB length:
        // in such a case they simply remove all leading spaces:
        int index = Math.min(numLeadingSpaces, spaces);
        if (index<0) {
            return line;
        }
        if (index>=line.length()) {
            return "";
        }
        return line.substring(index);
    }

    private int calculateAmountOfLeadingSpaces(String line, int spaces) {
        int numLeadingSpaces = 0;
        for (int i = 0; i < spaces && i < line.length(); i++) {
            if (line.charAt(i) == ' ') {
                numLeadingSpaces++;
            }
            else {
                break;
            }
        }
        return numLeadingSpaces;
    }

    // just for symmetry with outdent():
    private String indent(String line, String toInsert) {
        return toInsert + line;
    }
}
