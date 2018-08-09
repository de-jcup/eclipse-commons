package de.jcup.eclipse.commons;

import org.eclipse.ui.plugin.AbstractUIPlugin;

public interface PluginContextProvider {

	public AbstractUIPlugin getActivator();
	
	public String getPluginID();
}
