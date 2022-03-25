package de.jcup.eclipse.commons.replacetabbyspaces;

import de.jcup.eclipse.commons.PluginContextProvider;

public interface ReplaceTabBySpacesProvider {
    
    public boolean isReplaceTabBySpacesEnabled();
    
    public int getAmountOfSpacesToReplaceTab();

    public PluginContextProvider getPluginContextProvider();

}
