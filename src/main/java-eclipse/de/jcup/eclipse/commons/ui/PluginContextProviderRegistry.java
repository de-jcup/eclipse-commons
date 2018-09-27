package de.jcup.eclipse.commons.ui;

import de.jcup.eclipse.commons.PluginContextProvider;

public class PluginContextProviderRegistry {

	private static PluginContextProvider provider = null;

	public static void register(PluginContextProvider provider){
		PluginContextProviderRegistry.provider=provider;
	}
	
	/**
	 * @return registered provider or <code>null</code>
	 */
	public static PluginContextProvider getProvider() {
		return provider;
	}
	
}
