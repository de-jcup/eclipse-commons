package de.jcup.eclipse.commons;

import org.eclipse.ui.plugin.AbstractUIPlugin;

import de.jcup.eclipse.commons.templates.TemplateSupport;

public interface PluginContextProvider {

	public AbstractUIPlugin getActivator();
	
	public String getPluginID();
	
}
