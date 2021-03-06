/*
 * Copyright 2017 Albert Tregnaghi
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
package de.jcup.eclipse.commons;

public class SimpleStringUtils {
    private static final String EMPTY = "";

    public static boolean equals(String text1, String text2) {
        if (text1 == null) {
            if (text2 == null) {
                return true;
            }
            return false;
        }
        if (text2 == null) {
            return false;
        }
        return text2.equals(text1);
    }

    public static String trimRight(String line) {
        if (line == null) {
            return null;
        }
        if (line.length() == 0) {
            return "";
        }

        char[] chars = line.toCharArray();
        /* scan for char from right which is not a whitespace */
        int i = -1;
        for (int j = chars.length - 1; j >= 0; j--) {
            if (!Character.isWhitespace(chars[j])) {
                i = j;
                break;
            }
        }
        if (i == -1) {
            return "";
        }
        return line.substring(0, i+1);
    }

    public static String shortString(String string, int max) {
        if (max == 0) {
            return EMPTY;
        }
        if (string == null) {
            return EMPTY;
        }
        if (string.length() <= max) {
            return string;
        }
        /* length > max */
        if (max == 1) {
            return ".";
        }
        if (max == 2) {
            return "..";
        }
        if (max == 3) {
            return "...";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(string.substring(0, max - 3));
        sb.append("...");
        return sb.toString();
    }

    /**
     * Returns next reduced variable from given offset. Reduced means a variable
     * array like $ASCIIDOCTOR_VERSION[0] will be reduced to $ASCIIDOCTOR_VERSION!
     * 
     * @param string
     * @param offset
     * @param reducedVariableWordDetector
     * @return word, or empty string, never <code>null</code>
     */
    public static String nextReducedVariableWord(String string, int offset, WordEndDetector reducedVariableWordDetector) {
        return nextWord(string, offset, reducedVariableWordDetector);
    }

    /**
     * Returns next word until word end detected from given offset
     * 
     * @param string
     * @param offset
     * @param wordEndDetector - if null {@link WhitespaceWordEndDetector} will be
     *                        used automatically
     * @return word, or empty string, never <code>null</code>
     */
    public static String nextWord(String string, int offset, WordEndDetector wordEndDetector) {
        if (string == null) {
            return EMPTY;
        }
        if (offset < 0) {
            return EMPTY;
        }
        if (offset >= string.length()) {
            return EMPTY;
        }
        char c2 = string.charAt(offset);
        if (Character.isWhitespace(c2)) {
            return EMPTY;
        }
        if (wordEndDetector == null) {
            /* back to fall back impl */
            wordEndDetector = new WhitespaceWordEndDetector();
        }
        /* go to word start (offset == 0 or whitespace) */
        int start = offset;
        for (; start > 0; start--) {
            char c = string.charAt(start);
            if (wordEndDetector.isWordEnd(c)) {
                start += 1;
                break;
            }
        }
        /* start defined so scan for word */
        StringBuilder sb = new StringBuilder();
        for (int i = start; i < string.length(); i++) {
            char c = string.charAt(i);
            boolean isNotStartOfDocument = (i != 0);
            if (wordEndDetector.isWordEnd(c)) {
                if (isNotStartOfDocument) {
                    break;
                } else {
                    continue; // but no append... so this will reduce a "[TIP]"
                              // to an "TIP]";
                }
            }
            sb.append(c);
        }
        return sb.toString();
    }

    /**
     * @param string
     * @return <code>true</code> when given string is null or contains only
     *         whitespaces or an empty string
     */
    public static boolean isEmpty(String string) {
        if (string == null) {
            return true;
        }
        return string.trim().isEmpty();
    }
}
