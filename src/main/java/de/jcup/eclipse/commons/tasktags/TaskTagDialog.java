/*
 * Copyright 2018 Albert Tregnaghi
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
package de.jcup.eclipse.commons.tasktags;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import de.jcup.eclipse.commons.tasktags.TaskTagsSupport.TaskTagDefinition;
import de.jcup.eclipse.commons.tasktags.TaskTagsSupport.TaskTagPriority;

class TaskTagDialog extends TitleAreaDialog {

	private Text txtIdentifier;

	private Button btnPrioHigh;
	private Button btnPrioNormal;
	private Button btnPrioLow;

	private TaskTagDefinition definition;

	public TaskTagDialog(Shell parentShell, TaskTagDefinition definition) {
		super(parentShell);
		if (definition==null){
			definition = new TaskTagDefinition("XXX", TaskTagPriority.NORMAL);
		}
		this.definition=definition;
	}

	@Override
	public void create() {
		super.create();
		setTitle("Define Todo Task");
		setMessage("Create or change your task definition", IMessageProvider.INFORMATION);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		GridLayout layout = new GridLayout(2, false);
		container.setLayout(layout);

		createPrioritySelection(container);
		createIdentifierTextfields(container);

		return area;
	}

	private void createPrioritySelection(Composite container) {
		GridData data = new GridData();
		data.grabExcessHorizontalSpace = true;
		data.horizontalAlignment = GridData.FILL;
		data.horizontalSpan = 2;

		Group prioGroup = new Group(container, SWT.NONE);
		prioGroup.setText("Priority:");
		prioGroup.setLayout(new RowLayout(SWT.VERTICAL));
		prioGroup.setLayoutData(data);

		btnPrioHigh = new Button(prioGroup, SWT.RADIO);
		btnPrioHigh.setText(TaskTagPriority.HIGH.labelText());
		btnPrioHigh.setSelection(definition.isHighPriority());
		btnPrioHigh.setToolTipText("High priority");

		btnPrioNormal = new Button(prioGroup, SWT.RADIO);
		btnPrioNormal.setText(TaskTagPriority.NORMAL.labelText());
		btnPrioNormal.setToolTipText("Normal priority");
		btnPrioNormal.setSelection(definition.isNormalPriority());

		btnPrioLow = new Button(prioGroup, SWT.RADIO);
		btnPrioLow.setText(TaskTagPriority.LOW.labelText());
		btnPrioLow.setToolTipText("Low priority");
		btnPrioLow.setSelection(definition.isLowPriority());

	}

	private void createIdentifierTextfields(Composite container) {
		Label lblIdentifier = new Label(container, SWT.NONE);
		lblIdentifier.setText("Identifier");

		GridData data = new GridData();
		data.grabExcessHorizontalSpace = true;
		data.horizontalAlignment = GridData.FILL;

		txtIdentifier = new Text(container, SWT.BORDER);
		txtIdentifier.setLayoutData(data);
		txtIdentifier.setText(definition.getIdentifier());
	}

	@Override
	protected boolean isResizable() {
		return true;
	}

	private void saveInput() {
		String identifier = txtIdentifier.getText();
		definition.setIdentifier(identifier);
		
		if (btnPrioHigh.getSelection()) {
			definition.setPriority(TaskTagPriority.HIGH);
		} else if (btnPrioNormal.getSelection()) {
			definition.setPriority(TaskTagPriority.NORMAL);
		} else if (btnPrioLow.getSelection()) {
			definition.setPriority(TaskTagPriority.LOW);
		}
	}

	@Override
	protected void okPressed() {
		saveInput();
		super.okPressed();
	}

	public TaskTagDefinition getDefinition() {
		return definition;
	}
}