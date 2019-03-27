package de.jcup.eclipse.commons.templates;

import de.jcup.eclipse.commons.PluginContextProvider;

public class TemplateSupportProvider {

    private TemplateSupport support;
    private TemplateSupportConfig config;
    private PluginContextProvider getPluginContextProvider;

    public TemplateSupportProvider(TemplateSupportConfig config, PluginContextProvider pluginContextProvider) {
        if (config==null) {
            throw new IllegalStateException("config may never be null!");
        }
        if (pluginContextProvider==null) {
            throw new IllegalStateException("pluginContextProvider may never be null!");
        }
        this.config=config;
        this.getPluginContextProvider=pluginContextProvider;
        this.support=new TemplateSupport(this);
    }

    public TemplateSupport getSupport() {
        return support;
    }
    public TemplateSupportConfig getConfig() {
        return config;
    }
    public PluginContextProvider getPluginContextProvider() {
        return getPluginContextProvider;
    }

    
}
