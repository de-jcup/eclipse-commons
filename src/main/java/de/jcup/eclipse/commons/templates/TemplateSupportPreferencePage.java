package de.jcup.eclipse.commons.templates;

import org.eclipse.ui.texteditor.templates.TemplatePreferencePage;

public abstract class TemplateSupportPreferencePage extends TemplatePreferencePage{

    public TemplateSupportPreferencePage(TemplateSupportProvider provider){
        if (provider==null) {
            throw new IllegalArgumentException("provider may not be null");
        }
        setPreferenceStore(provider.getPluginContextProvider().getActivator().getPreferenceStore());
        setTemplateStore(provider.getSupport().getTemplateStore());
        setContextTypeRegistry(provider.getSupport().getContextTypeRegistry());
    }
    
    @Override
    protected boolean isShowFormatterSetting() {
        return false;
    }
}
