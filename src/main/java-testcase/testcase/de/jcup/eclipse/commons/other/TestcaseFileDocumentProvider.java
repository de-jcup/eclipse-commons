package testcase.de.jcup.eclipse.commons.other;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.ui.editors.text.FileDocumentProvider;

/**
 * Document provider for files inside workspace
 * @author albert
 *
 */
public class TestcaseFileDocumentProvider extends FileDocumentProvider {
	
	@Override
	protected IDocument createDocument(Object element) throws CoreException {
		IDocument document = super.createDocument(element);
		if (document != null) {
			/* installation necessary */
			IDocumentPartitioner partitioner = TestcasePartionerFactory.create();

			partitioner.connect(document);
			document.setDocumentPartitioner(partitioner);
		}
		return document;
	}
	
}