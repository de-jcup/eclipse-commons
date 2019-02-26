package testcase.de.jcup.eclipse.commons;

import java.util.Map;

import org.eclipse.core.resources.IResource;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.part.ViewPart;

public class TestcaseView extends ViewPart {

	private IContentProvider contentProvider;
	private TreeViewer viewer;

	@Override
	public void createPartControl(Composite parent) {
		contentProvider = new InternalTreeContentProvider();

//		parent.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_DARK_GREEN));
		viewer = new TreeViewer(parent);
		viewer.setContentProvider(contentProvider);
		viewer.setInput(TestcaseActivator.getDefault().getProjectModel());
		viewer.setLabelProvider(new TestcaseLabelProvider());

		GridLayoutFactory.fillDefaults().generateLayout(parent);

		IAction action = new RefreshAction();
		
		IActionBars actionBars = getViewSite().getActionBars();
		IMenuManager dropDownMenu = actionBars.getMenuManager();
		IToolBarManager toolBar = actionBars.getToolBarManager();
		dropDownMenu.add(action);
		toolBar.add(action);
	}
	

	@Override
	public void setFocus() {

	}

	private class RefreshAction extends Action {

		public RefreshAction() {
			setText("Refresh");
		}

		@Override
		public void run() {
			viewer.refresh();
		}
	}

	private class TestcaseLabelProvider extends LabelProvider {

		@Override
		public String getText(Object element) {
			if (element instanceof Map) {
				return "Model, size:"+((Map<?, ?>)element).size();
			}
			if (element instanceof StringBuilder) {
				return element.toString();
			}
			if (element instanceof IResource) {
				return ((IResource) element).getName();
			}
			if (element instanceof TestCaseProjectModel) {
				return "Model";
			}
			return super.getText(element);
		}
	}

	private class InternalTreeContentProvider implements ITreeContentProvider {

		@Override
		public Object[] getElements(Object inputElement) {
			if (inputElement instanceof TestCaseProjectModel) {
				TestCaseProjectModel model = (TestCaseProjectModel) inputElement;
				return new Object[] {model.getMap()};
			}
			return new Object[] {};
		}

		@Override
		public Object[] getChildren(Object parentElement) {
			if (parentElement instanceof Map) {
				return ((Map<?,?>)parentElement).keySet().toArray();
			}
			if (parentElement instanceof IResource) {
				IResource key = (IResource) parentElement;
				TestCaseProjectModel model = TestcaseActivator.getDefault().getProjectModel();
				String data = model.getMap().get(key).toString();
				return new Object[] { data };
			}
			return new Object[] {};
		}

		@Override
		public Object getParent(Object element) {
			return null;
		}

		@Override
		public boolean hasChildren(Object element) {
			if (element instanceof Map) {
				return true;
			}
			if (element instanceof IResource) {
				return true;
			}
			return false;
		}

	}
}
