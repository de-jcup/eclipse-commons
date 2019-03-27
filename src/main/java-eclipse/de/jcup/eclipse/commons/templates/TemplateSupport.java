package de.jcup.eclipse.commons.templates;

import java.io.IOException;

import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.text.IInformationControlCreator;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.text.templates.TemplateCompletionProcessor;
import org.eclipse.jface.text.templates.persistence.TemplateStore;
import org.eclipse.ui.editors.text.templates.ContributionContextTypeRegistry;
import org.eclipse.ui.editors.text.templates.ContributionTemplateStore;
import org.osgi.service.prefs.BackingStoreException;

import de.jcup.eclipse.commons.PluginContextProvider;
import de.jcup.eclipse.commons.ui.EclipseUtil;

public class TemplateSupport {

    
    private PluginContextProvider provider;
    private ContributionTemplateStore fStore;
    private ContributionContextTypeRegistry fRegistry;
    private TemplateSupportConfig config;
    private TemplateCompletionProcessor processor;
    
    TemplateSupport(TemplateSupportProvider supportProvider) {
        if (supportProvider==null) {
            throw new IllegalStateException("supportProvider may never be null!");
        }
        this.provider=supportProvider.getPluginContextProvider();
        if (provider==null) {
            throw new IllegalStateException("provider may never be null!");
        }
        this.config=supportProvider.getConfig();
        if (config==null) {
            throw new IllegalStateException("config may never be null!");
        }
        this.processor=new SimpleTemplateCompletionProcessor(this);
    }
    
    public TemplateCompletionProcessor getProcessor() {
        return processor;
    }
    public TemplateSupportConfig getConfig() {
        return config;
    }
    
    public TemplateStore getTemplateStore() {
        if (fStore == null) {
            fStore = new ContributionTemplateStore(getContextTypeRegistry(), provider.getActivator().getPreferenceStore(), config.getTemplatesKey());
            try {
                fStore.load();
            } catch (IOException e) {
                EclipseUtil.logError("Was not able to load template store", e, provider);
            }
        }
        return fStore;
    }

    public ContributionContextTypeRegistry getContextTypeRegistry() {
        if (fRegistry == null) {
            // create an configure the contexts available in the template editor
            fRegistry = new ContributionContextTypeRegistry();
            for (String id : config.getContextTypes()) {
                fRegistry.addContextType(id);
            }
        }
        return fRegistry;
    }

    public void savePluginPreferences() {
        try {
            InstanceScope.INSTANCE.getNode(provider.getPluginID()).flush();
        } catch (BackingStoreException e) {
            EclipseUtil.logError("Was not able to save plugin preferences", e, provider);
        }
    }

    public PluginContextProvider getProvider() {
        return provider;
    }

    public void install(ContentAssistant assistant, IInformationControlCreator informationControlCreator) {
        for (String id: config.getContextTypes()) {
            assistant.setContentAssistProcessor(processor, id);
        }
        /* TODO ALBERT, 27.03.2019 - improve - not only CONTEXT_INFO_ABOVE */
        /* TODO ALBERT, 27.03.2019 - hmm.. is it really correct to use this support for all these editor instances?*/
        assistant.setContextInformationPopupOrientation(IContentAssistant.CONTEXT_INFO_ABOVE);
        assistant.setInformationControlCreator(informationControlCreator);
        
    }
}
