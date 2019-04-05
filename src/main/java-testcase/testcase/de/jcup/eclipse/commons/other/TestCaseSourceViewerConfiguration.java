package testcase.de.jcup.eclipse.commons.other;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextHover;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.ITokenScanner;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

import de.jcup.eclipse.commons.codeassist.MultipleContentAssistProcessor;
import de.jcup.eclipse.commons.presentation.PresentationSupport;
import de.jcup.eclipse.commons.templates.TemplateSupport;
import testcase.de.jcup.eclipse.commons.TestCaseColorManager;
import testcase.de.jcup.eclipse.commons.TestcaseActivator;
import testcase.de.jcup.eclipse.commons.codeassist.TestcaseContentAssistProcessor;
import testcase.de.jcup.eclipse.commons.keywords.TestCaseDocumentKeywordTextHover;

public class TestCaseSourceViewerConfiguration extends SourceViewerConfiguration {
    private TextAttribute defaultTextAttribute;
    private ContentAssistant contentAssistant;
    private ITokenScanner textScanner;

    public TestCaseSourceViewerConfiguration() {
        this.defaultTextAttribute = new TextAttribute(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));

        TemplateSupport templatesupport = TestcaseActivator.getDefault().getTemplateSupportProvider().getSupport();
        contentAssistant = new ContentAssistant();

        TestcaseContentAssistProcessor contentAssistProcessor = new TestcaseContentAssistProcessor();
        IContentAssistProcessor templateProcessor = templatesupport.getProcessor();

        MultipleContentAssistProcessor multiProcessor = new MultipleContentAssistProcessor(templateProcessor, contentAssistProcessor);
        contentAssistant.setContentAssistProcessor(multiProcessor, IDocument.DEFAULT_CONTENT_TYPE);

    }

    @Override
    public IContentAssistant getContentAssistant(ISourceViewer sourceViewer) {
        contentAssistant.setContextInformationPopupOrientation(IContentAssistant.CONTEXT_INFO_ABOVE);
        contentAssistant.setInformationControlCreator(getInformationControlCreator(sourceViewer));
        return contentAssistant;
    }

    @Override
    public ITextHover getTextHover(ISourceViewer sourceViewer, String contentType) {
        return new TestCaseDocumentKeywordTextHover();
    }

    private RGB COLOR_RED = new RGB(255, 0, 0);
    private RGB COLOR_GREEN = new RGB(0, 255, 0);

    @Override
    public IPresentationReconciler getPresentationReconciler(ISourceViewer sourceViewer) {
        PresentationReconciler reconciler = new PresentationReconciler();

        addDefaultPresentation(reconciler);

        addPresentation(reconciler, TestcaseDocumentIdentifiers.MAKE_ME_BOLD.getId(), COLOR_GREEN, SWT.BOLD);
        addPresentation(reconciler, TestcaseDocumentIdentifiers.MAKE_ME_ITALIC.getId(), COLOR_RED, SWT.ITALIC);

        return reconciler;
    }

    @Override
    public String[] getConfiguredContentTypes(ISourceViewer sourceViewer) {
        return new String[] { TestcaseDocumentIdentifiers.MAKE_ME_BOLD.getId(), TestcaseDocumentIdentifiers.MAKE_ME_ITALIC.getId(), IDocument.DEFAULT_CONTENT_TYPE };
    }

    private void addDefaultPresentation(PresentationReconciler reconciler) {
        DefaultDamagerRepairer dr = new DefaultDamagerRepairer(getDefaultTextScanner());
        reconciler.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
        reconciler.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);
    }

    private ITokenScanner getDefaultTextScanner() {
        if (textScanner == null) {
            textScanner = new TestcaseDefaultTextScanner();
        }
        return textScanner;
    }

    private void addPresentation(PresentationReconciler reconciler, String id, RGB rgb, int style) {
        TestCaseColorManager colorManager = getColorManager();
        TextAttribute textAttribute = new TextAttribute(colorManager.getColor(rgb), defaultTextAttribute.getBackground(), style);
        PresentationSupport presentation = new PresentationSupport(textAttribute);
        reconciler.setDamager(presentation, id);
        reconciler.setRepairer(presentation, id);
    }

    protected TestCaseColorManager getColorManager() {
        return TestcaseActivator.getDefault().getTestCaseColorManager();
    }

    

}
