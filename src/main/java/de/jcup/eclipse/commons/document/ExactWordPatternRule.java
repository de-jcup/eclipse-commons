package de.jcup.eclipse.commons.document;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.IWordDetector;
import org.eclipse.jface.text.rules.WordPatternRule;

public class ExactWordPatternRule extends WordPatternRule{

    private MetaCharacterDetector metaCharacterDetector;
	private String toStringValue;
	StringBuilder traceSb;
	boolean trace = false;
	private boolean acceptStartBrackets;
	
	public void setAcceptStartBrackets(boolean acceptStartBrackets) {
		this.acceptStartBrackets = acceptStartBrackets;
	}
	
	public ExactWordPatternRule(IWordDetector detector, String exactWord, IToken token) {
		this(detector,exactWord,token,true);
	}
	
	public ExactWordPatternRule(IWordDetector detector, String exactWord, IToken token, boolean breaksOnEOF) {
		super(detector, exactWord, null, token);
		toStringValue=getClass().getSimpleName()+":"+exactWord;
		this.fBreaksOnEOF=breaksOnEOF;
	}
	
	protected boolean sequenceDetected(ICharacterScanner scanner, char[] sequence, boolean eofAllowed) {
		/* sequence is not the word found by word detector but the start sequence!!!!! (in this case always the exact word)*/
		
		// -------------------------------------------------
		// example: exactWord='test' 
		// 
		// subjects: atest,test,testa
		//                   ^----------------only result!
		Counter counter = new Counter();
		if (trace){
			// trace contains NOT first character, this is done at PatternRule
			traceSb = new StringBuilder();
		}
		int column=scanner.getColumn();
		boolean wordHasPrefix;
		if (column==1){
			wordHasPrefix=false;
		}else{
			scannerUnread(scanner, counter);
			scannerUnread(scanner, counter);
			char charBefore =(char)scannerRead(scanner, counter);
			scannerRead(scanner, counter);
			if (charBefore=='(' && acceptStartBrackets) {
				/* we accept this */
				wordHasPrefix=false;
			}else {
				wordHasPrefix = isPrefixCharacter(charBefore);
			}
		}
		if (wordHasPrefix){
			scannerRead(scanner, counter);
			return counter.cleanupAndReturn(scanner,false);
		}
		// scan fall characters in sequence
		for (int i= 1; i < sequence.length; i++) {
			int c= scannerRead(scanner, counter);
			if (c == ICharacterScanner.EOF){
				if (eofAllowed) {
					return counter.cleanupAndReturn(scanner,true);
				}else{
					return counter.cleanupAndReturn(scanner,false);
				}
			} else if (c != sequence[i]) {
				scannerUnread(scanner, counter);
				for (int j= i-1; j > 0; j--){
					scannerUnread(scanner, counter);
				}
				return counter.cleanupAndReturn(scanner,false);
			}
		}
		int read = scannerRead(scanner, counter);
		char charAfter = (char)read;
		scannerUnread(scanner, counter);
		
		/* when not a whitespace and not end reached - do cleanup*/
		if (! isMetaCharacter(charAfter) && ICharacterScanner.EOF!=read){
			/* the word is more than the exact one - e.g. instead of 'test' 'testx' ... so not correct*/
			return counter.cleanupAndReturn(scanner,false);
		}
		return counter.cleanupAndReturn(scanner,true);
	}

	private boolean isPrefixCharacter(char charBefore) {
		boolean isPrefix = ! isMetaCharacter(charBefore);
		return isPrefix;
	}

	
	public void setMetaCharacterDetector(MetaCharacterDetector metaCharacterDetector) {
        this.metaCharacterDetector = metaCharacterDetector;
    }
	
	private boolean isMetaCharacter(char charBefore) {
	    if (metaCharacterDetector==null) {
	        return false;
	    }
	    return metaCharacterDetector.isMetaCharacter(charBefore);
    }

    private int scannerRead(ICharacterScanner scanner, Counter counter) {
		int c = scanner.read();
		if (c==ICharacterScanner.EOF){
			return c;
		}
		counter.count++;
		if (trace){
			traceSb.append((char)c);
		}
		return c;
		
	}

	private void scannerUnread(ICharacterScanner scanner, Counter counter) {
		scanner.unread();
		counter.count--;
		if (trace){
			int length = traceSb.length();
			if (length<1){
				traceSb.append("[(-1)]");
			}else{
				length=length-1;
				traceSb.setLength(length);
			}
		}
	}

	@Override
	public String toString() {
		return toStringValue;
	}
	
}
