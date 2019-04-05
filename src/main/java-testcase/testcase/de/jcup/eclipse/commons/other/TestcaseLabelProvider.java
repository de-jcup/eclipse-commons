package testcase.de.jcup.eclipse.commons.other;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import testcase.de.jcup.eclipse.commons.keywords.TestCaseKeywords;

public class TestcaseLabelProvider extends LabelProvider{

    @Override
    public Image getImage(Object element) {
        if (element instanceof String) {
           for (TestCaseKeywords k: TestCaseKeywords.values()) {
               if (k.getText().equalsIgnoreCase((String)element)) {
                   return PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_ETOOL_HOME_NAV);
               }
           }
           return PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_ETOOL_PRINT_EDIT);
        }
        return null;
    }
}
