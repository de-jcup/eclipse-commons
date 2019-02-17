package de.jcup.eclipse.commons.projectmodelbuilder;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import de.jcup.eclipse.commons.PluginContextProvider;
import de.jcup.eclipse.commons.projectmodelbuilder.ProjectModelBuilderSupport.ProjectModelBuilderSupportProvider;

/**
 * Abstract base implementation for todo tasks being configurable inside preferences
 * @author Albert Tregnaghi
 *
 */
public abstract class AbstractConfigurableProjectModelBuilderSupportProvider implements ProjectModelBuilderSupportProvider {

	private AbstractUIPlugin plugin;
	private String pluginId;
	private ProjectModelBuilderSupport todoTaskSupport;
	
	public AbstractConfigurableProjectModelBuilderSupportProvider(PluginContextProvider provider) {
		this.plugin=provider.getActivator();
		this.pluginId=provider.getPluginID();

		todoTaskSupport = new ProjectModelBuilderSupport(this);
	}
	
	public ProjectModelBuilderSupport getTodoTaskSupport() {
		return todoTaskSupport;
	}
	
	@Override
	public void logError(String error, Throwable t) {
		plugin.getLog().log(new Status(IStatus.ERROR, pluginId, error, t));
	}
	
	@Override
	public abstract boolean isProjectModelBuilderSupportEnabled();
	

	@Override
	public String getPluginId() {
		return pluginId;
	}

	public IPreferenceStore getPreferenceStore() {
		return plugin.getPreferenceStore();
	}

}
