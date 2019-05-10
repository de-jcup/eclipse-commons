package de.jcup.eclipse.commons.workspacemodel;

import de.jcup.eclipse.commons.PluginContextProvider;

/**
 * Abstract base implementation for todo tasks being configurable inside preferences
 * @author Albert Tregnaghi
 *
 */
public abstract class AbstractConfigurableModelBuilderSupportProvider<M> implements ModelBuilderSupportProvider<M> {

	private ModelBuilderSupport<M> modelBuilderSupport;
    private PluginContextProvider pluginContextProvider;
	
	public AbstractConfigurableModelBuilderSupportProvider(PluginContextProvider pluginContextProvider) {
		this.modelBuilderSupport = new ModelBuilderSupport<M>(this);
		this.pluginContextProvider = pluginContextProvider;
	}
	
	@Override
	public PluginContextProvider getPluginContextProvider() {
	    return pluginContextProvider;
	}
	
	public ModelBuilderSupport<M> getWorkspaceModelSupport() {
		return modelBuilderSupport;
	}
	
	@Override
	public abstract boolean isProjectModelBuilderSupportEnabled();

}
