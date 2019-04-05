package testcase.de.jcup.eclipse.commons.other;

public enum TestcaseDocumentIdentifiers implements TestcaseDocumentIdentifer{

    MAKE_ME_BOLD,
    
    MAKE_ME_ITALIC;
    
    @Override
    public String getId() {
        return "___"+name()+"___";
    }
}
