package de.jcup.eclipse.commons.projectmodelbuilder;

import de.jcup.eclipse.commons.PluginContextProvider;

/**
 * Abstract base implementation for todo tasks being configurable inside preferences
 * @author Albert Tregnaghi
 *
 */
public abstract class AbstractConfigurableProjectModelBuilderSupportProvider<M> implements ProjectModelBuilderSupportProvider<M> {

	private ProjectModelBuilderSupport<M> projectModelBuilderSupport;
    private PluginContextProvider pluginContextProvider;
	
	public AbstractConfigurableProjectModelBuilderSupportProvider(PluginContextProvider pluginContextProvider) {
		this.projectModelBuilderSupport = new ProjectModelBuilderSupport<M>(this);
		this.pluginContextProvider = pluginContextProvider;
	}
	
	@Override
	public PluginContextProvider getPluginContextProvider() {
	    return pluginContextProvider;
	}
	
	public ProjectModelBuilderSupport<M> getProjectModelBuilderSupport() {
		return projectModelBuilderSupport;
	}
	
	@Override
	public abstract boolean isProjectModelBuilderSupportEnabled();

}
