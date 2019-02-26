package de.jcup.eclipse.commons.projectmodelbuilder;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import de.jcup.eclipse.commons.PluginContextProvider;

/**
 * Abstract base implementation for todo tasks being configurable inside preferences
 * @author Albert Tregnaghi
 *
 */
public abstract class AbstractConfigurableProjectModelBuilderSupportProvider<M> implements ProjectModelBuilderSupportProvider<M> {

	private AbstractUIPlugin plugin;
	private String pluginId;
	private ProjectModelBuilderSupport<M> projectModelBuilderSupport;
	
	public AbstractConfigurableProjectModelBuilderSupportProvider(PluginContextProvider provider) {
		this.plugin=provider.getActivator();
		this.pluginId=provider.getPluginID();

		projectModelBuilderSupport = new ProjectModelBuilderSupport<M>(this);
	}
	
	public ProjectModelBuilderSupport<M> getProjectModelBuilderSupport() {
		return projectModelBuilderSupport;
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
