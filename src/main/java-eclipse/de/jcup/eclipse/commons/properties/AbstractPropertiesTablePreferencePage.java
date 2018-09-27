package de.jcup.eclipse.commons.properties;


import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import de.jcup.eclipse.commons.tasktags.TaskTagsSupport.TaskTagDefinition;
import de.jcup.eclipse.commons.ui.SWTFactory;
import static de.jcup.eclipse.commons.ui.SWTFactory.*;
/**
 * Base class for preference pages for TODOs
 * 
 * @author albert
 * @version 0.1.0.alpha
 *
 */
public class AbstractPropertiesTablePreferencePage extends PreferencePage implements IWorkbenchPreferencePage {

	private PropertyProvider provider;
	private Button btnPropertiesEnabled;
	private boolean propertiesEnabled;
	
	public AbstractPropertiesTablePreferencePage(PropertyProvider provider, String title, String description) {
		setPreferenceStore(provider.getPreferenceStore());
		setDescription(description);
		setTitle(title);
		this.provider=provider;
		this.propertiesWorkingCopy=new ArrayList<>(provider.getProperties());
		this.propertiesEnabled=provider.getPreferenceStore().getBoolean(provider.getPreferencePropertyEnabledKey());
	}

	@Override
	protected void performDefaults() {
		this.propertiesWorkingCopy=new ArrayList<>();
		updateTable();
	}

	@Override
	public boolean performOk() {
		
		boolean sameContentAsBefore = checkSameDefinitionsAsBefore();
		if (!sameContentAsBefore){
			String convertListTostring = provider.getConverter().convertListTostring(propertiesWorkingCopy);
			
			getPreferenceStore().setValue(getPropertyDataKey(), convertListTostring);
			getPreferenceStore().setValue(getPropertyEnabledKey(), propertiesEnabled);

		}
		return super.performOk();
	}

	private String getPropertyDataKey() {
		return provider.getPreferencePropertyDataKey();
	}
	
	private String getPropertyEnabledKey() {
		return  provider.getPreferencePropertyEnabledKey();
	}

	private boolean checkSameDefinitionsAsBefore() {
		/* we use a set to check for changes - list does inspect ordering too! */
		HashSet<PropertyDefinition> freshCopy = new HashSet<>(provider.getProperties());
		HashSet<PropertyDefinition> copiedDefinitionsAsSet = new HashSet<>(propertiesWorkingCopy);
		
		return copiedDefinitionsAsSet.equals(freshCopy);
	}

	@Override
	protected void performApply() {
		super.performApply();
	}

	@Override
	public void dispose() {
		super.dispose();
	}
	
	@Override
	public void init(IWorkbench workbench) {
	}

	@Override
	protected Control createContents(Composite parent) {
		Composite mainComposite = SWTFactory.createComposite(parent, 2, 2,GridData.FILL_BOTH);
		createCheckBox(mainComposite);
		
		createTable(mainComposite);
		createTableButtons(mainComposite);

		return mainComposite;
	}

	private void createCheckBox(Composite mainComposite) {
		btnPropertiesEnabled = new Button(mainComposite, SWT.CHECK);
		btnPropertiesEnabled.setText("Properties enabled");
		btnPropertiesEnabled.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				updateTable();
				propertiesEnabled=btnPropertiesEnabled.getSelection();
			}
		});
		GridData layoutData = new GridData();
		layoutData.verticalSpan=2;
		btnPropertiesEnabled.setLayoutData(layoutData);
		
	}

	protected TableViewer propertiesTable;
	protected String[] propertyTableColumnHeaders = { "Identifier", "Priority", };
	protected static final String P_VARIABLE = "variable";
	protected static final String P_VALUE = "value";
	protected Button propertyAddButton;
	protected Button propertyEditButton;
	protected Button propertyRemoveButton;
	private List<PropertyDefinition> propertiesWorkingCopy;
	private static final Object[] NO_OBJECTS = new Object[]{};

	/**
	 * Content provider for the environment table
	 */
	protected class TaskTagDefinitionsContentProvider implements IStructuredContentProvider {

		public Object[] getElements(Object inputElement) {
			if (inputElement instanceof List){
				Object[] result = ((List<?>)inputElement).toArray();
				return result;
			}
			return NO_OBJECTS;
		}

		public void dispose() {
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			if (newInput == null) {
				return;
			}
			if (viewer instanceof TableViewer) {
				TableViewer tableViewer = (TableViewer) viewer;
				if (tableViewer.getTable().isDisposed()) {
					return;
				}
				tableViewer.refresh();
			}
		}
	}

	/**
	 * Label provider for the table
	 */
	public class TaskTagDefinitionLabelProvider extends LabelProvider implements ITableLabelProvider {
		public String getColumnText(Object element, int columnIndex) {
			String result = null;
			if (element != null) {
				TaskTagDefinition var = (TaskTagDefinition) element;
				switch (columnIndex) {
				case 0: // variable
					result = var.getIdentifier();
					break;
				case 1: // value
					result = var.getPriority().name();
					break;
				}
			}
			return result;
		}

		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}
	}

	public void createControl(Composite parent) {
		super.createControl(parent);
	}

	/**
	 * Creates and configures the table that displayed the key/value pairs that
	 * comprise the environment.
	 * 
	 * @param parent
	 *            the composite in which the table should be created
	 */
	protected void createTable(Composite parent) {
		Font font = parent.getFont();
		SWTFactory.createLabel(parent, "Definitions:", 2);
		// Create table composite
		Composite tableComposite = SWTFactory.createComposite(parent, font, 1, 1, GridData.FILL_BOTH, 0,
				0);
		// Create table
		propertiesTable = new TableViewer(tableComposite,
				SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.MULTI | SWT.FULL_SELECTION);
		Table table = propertiesTable.getTable();
		table.setLayout(new GridLayout());
		table.setLayoutData(new GridData(GridData.FILL_BOTH));
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		table.setFont(font);
		propertiesTable.setContentProvider(new TaskTagDefinitionsContentProvider());
		propertiesTable.setLabelProvider(new TaskTagDefinitionLabelProvider());
		propertiesTable.setColumnProperties(new String[] { P_VARIABLE, P_VALUE });
		propertiesTable.setComparator(new ViewerComparator() {
			public int compare(Viewer iviewer, Object e1, Object e2) {
				if (e1 == null) {
					return -1;
				} else if (e2 == null) {
					return 1;
				} else {
					return ((TaskTagDefinition) e1).getIdentifier()
							.compareToIgnoreCase(((TaskTagDefinition) e2).getIdentifier());
				}
			}
		});
		propertiesTable.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				handleTableSelectionChanged(event);
			}
		});
		propertiesTable.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				if (!propertiesTable.getSelection().isEmpty()) {
					handlePropertiesEditButtonSelected();
				}
			}
		});
		// Create columns
		final TableColumn tc1 = new TableColumn(table, SWT.NONE, 0);
		tc1.setText(propertyTableColumnHeaders[0]);
		final TableColumn tc2 = new TableColumn(table, SWT.NONE, 1);
		tc2.setText(propertyTableColumnHeaders[1]);
		final Table tref = table;
		final Composite comp = tableComposite;
		tableComposite.addControlListener(new ControlAdapter() {
			public void controlResized(ControlEvent e) {
				Rectangle area = comp.getClientArea();
				Point size = tref.computeSize(SWT.DEFAULT, SWT.DEFAULT);
				ScrollBar vBar = tref.getVerticalBar();
				int width = area.width - tref.computeTrim(0, 0, 0, 0).width - 2;
				if (size.y > area.height + tref.getHeaderHeight()) {
					Point vBarSize = vBar.getSize();
					width -= vBarSize.x;
				}
				Point oldSize = tref.getSize();
				if (oldSize.x > area.width) {
					tc1.setWidth(width / 2 - 1);
					tc2.setWidth(width - tc1.getWidth());
					tref.setSize(area.width, area.height);
				} else {
					tref.setSize(area.width, area.height);
					tc1.setWidth(width / 2 - 1);
					tc2.setWidth(width - tc1.getWidth());
				}
			}
		});
		propertiesTable.setInput(propertiesWorkingCopy);
		propertiesTable.refresh();
	}

	/**
	 * Responds to a selection changed event in the table
	 * 
	 * @param event
	 *            the selection change event
	 */
	protected void handleTableSelectionChanged(SelectionChangedEvent event) {
		int size = ((IStructuredSelection) event.getSelection()).size();
		propertyEditButton.setEnabled(size == 1);
		propertyRemoveButton.setEnabled(size > 0);
	}

	/**
	 * Creates the add/edit/remove buttons for the table
	 * 
	 * @param parent
	 *            the composite in which the buttons should be created
	 */
	protected void createTableButtons(Composite parent) {
		// Create button composite
		Composite buttonComposite = SWTFactory.createComposite(parent, parent.getFont(), 1, 1,
				GridData.VERTICAL_ALIGN_BEGINNING | GridData.HORIZONTAL_ALIGN_END, 0, 0);

		// Create buttons
		propertyAddButton = createPushButton(buttonComposite, "N&ew...", null);
		propertyAddButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				handlePropertiesAddButtonSelected();
			}
		});
		propertyEditButton = createPushButton(buttonComposite, "E&dit...", null);
		propertyEditButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				handlePropertiesEditButtonSelected();
			}
		});
		propertyEditButton.setEnabled(false);
		propertyRemoveButton = createPushButton(buttonComposite, "Rem&ove", null);
		propertyRemoveButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				handlePropertiesRemoveButtonSelected();
			}
		});
		propertyRemoveButton.setEnabled(false);
	}

	/**
	 * Adds a new variable to the table.
	 */
	protected void handlePropertiesAddButtonSelected() {
		PropertyDialog dialog = new PropertyDialog(getShell(), null);
		if (dialog.open() != Window.OK) {
			return;
		}
		PropertyDefinition propertyDefinition = dialog.getResult();
		String name = propertyDefinition.getName();

		if (name != null && name.length() > 0 ) {
			addVariable(propertyDefinition);
		}
	}

	/**
	 * Attempts to add the given variable. Returns whether the variable was
	 * added or not (as when the user answers not to overwrite an existing
	 * variable).
	 * 
	 * @param definition
	 *            the variable to add
	 * @return <code>true</code> when variable was added
	 */
	protected boolean addVariable(PropertyDefinition definition) {
		String identifier = definition.getName();
		TableItem[] items = propertiesTable.getTable().getItems();
		for (int i = 0; i < items.length; i++) {
			TaskTagDefinition existingVariable = (TaskTagDefinition) items[i].getData();
			if (existingVariable.getIdentifier().equals(identifier)) {
				boolean overWrite = MessageDialog.openQuestion(getShell(), "Overwrite property?", MessageFormat
						.format("A property named {0} already exists. Overwrite?", new Object[] { identifier })); //
				if (!overWrite) {
					return false;
				}
				propertiesWorkingCopy.remove(existingVariable);
				break;
			}
		}
		propertiesWorkingCopy.add(definition);
		updateTable();
		return true;
	}

	private void updateTable() {
		this.propertiesTable.getContentProvider().inputChanged(propertiesTable, null, propertiesWorkingCopy);
		
		boolean todosEnabled = btnPropertiesEnabled.getSelection();
		
		propertiesTable.getControl().setEnabled(todosEnabled);
		propertyAddButton.setEnabled(todosEnabled);
		if (!todosEnabled){
			propertiesTable.setSelection(null);
		}
		
	}

	private void handlePropertiesEditButtonSelected() {
		IStructuredSelection sel = (IStructuredSelection) propertiesTable.getSelection();
		PropertyDefinition var = (PropertyDefinition) sel.getFirstElement();
		if (var == null) {
			return;
		}
		
		PropertyDialog dialog = new PropertyDialog(getShell(), var);
		if (dialog.open() != Window.OK) {
			return;
		}
		updateTable();
	}

	private void handlePropertiesRemoveButtonSelected() {
		IStructuredSelection sel = (IStructuredSelection) propertiesTable.getSelection();
		for (@SuppressWarnings("unchecked")
		Iterator<TaskTagDefinition> i = sel.iterator(); i.hasNext();) {
			TaskTagDefinition var = i.next();
			propertiesWorkingCopy.remove(var);
		}
		updateTable();
	}

}