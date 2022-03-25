package de.jcup.eclipse.commons.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Control;

/**
 * Gives a simple possibility to add enabled state cascade from master component
 * to slaves. Interesting e.g. for preference pages where a check box disabling
 * disables also all sub preferences...
 * 
 * @author albert
 *
 */
public class UIMasterSlaveSupport {

    private static final int INDENT = 20;
    private ArrayList<SlaveSelectionListener> masterSlaveListeners = new ArrayList<>();

    private static void indent(Control control) {
        ((GridData) control.getLayoutData()).horizontalIndent += INDENT;
    }


    public void createDependency(Button master, Control slave) {
        Assert.isNotNull(slave);
        indent(slave);
        MasterButtonSlaveSelectionListener listener = new MasterButtonSlaveSelectionListener(master, slave);
        master.addSelectionListener(listener);
        this.masterSlaveListeners.add(listener);
    }

    public void createDependency(Combo master, Control slave, List<String> enabledVariants) {
        Assert.isNotNull(slave);
        indent(slave);
        MasterComboSlaveSelectionListener listener = new MasterComboSlaveSelectionListener(master, slave, enabledVariants);
        master.addSelectionListener(listener);
        this.masterSlaveListeners.add(listener);
    }

    public void updateSlaveComponents() {
        for (SlaveSelectionListener listener : masterSlaveListeners) {
            listener.updateSlaveComponent();
        }
    }

    private abstract class SlaveSelectionListener implements SelectionListener {
        @Override
        public void widgetDefaultSelected(SelectionEvent e) {

        }

        @Override
        public void widgetSelected(SelectionEvent e) {
            updateSlaveComponent();
        }

        protected abstract void updateSlaveComponent();
    }

    private class MasterButtonSlaveSelectionListener extends SlaveSelectionListener {
        private Button master;
        private Control slave;

        public MasterButtonSlaveSelectionListener(Button master, Control slave) {
            this.master = master;
            this.slave = slave;
        }

        protected void updateSlaveComponent() {
            boolean state = master.getSelection();
            slave.setEnabled(state);
        }
    }

    private class MasterComboSlaveSelectionListener extends SlaveSelectionListener {
        private Combo master;
        private Control slave;
        private List<String> enabledVariants;

        public MasterComboSlaveSelectionListener(Combo master, Control slave, List<String> enabledVariants) {
            this.master = master;
            this.slave = slave;
            if (enabledVariants == null) {
                this.enabledVariants = Collections.emptyList();
            } else {
                this.enabledVariants = new ArrayList<String>(enabledVariants);
            }
        }

        protected void updateSlaveComponent() {
            int index = master.getSelectionIndex();
            boolean enabled = false;
            if (index != -1) {
                String[] items = master.getItems();
                if (items != null && items.length > index) {
                    String selected = items[index];
                    enabled = enabledVariants.contains(selected);
                }
            }
            slave.setEnabled(enabled);
        }
    }
}
