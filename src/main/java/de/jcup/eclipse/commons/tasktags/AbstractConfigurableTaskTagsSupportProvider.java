package de.jcup.eclipse.commons.tasktags;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import de.jcup.eclipse.commons.PluginContextProvider;
import de.jcup.eclipse.commons.tasktags.TaskTagsSupport.TaskTagDefinition;
import de.jcup.eclipse.commons.tasktags.TaskTagsSupport.TaskTagSupportProvider;

/**
 * Abstract base implementation for todo tasks being configurable inside preferences
 * @author Albert Tregnaghi
 *
 */
public abstract class AbstractConfigurableTaskTagsSupportProvider implements TaskTagSupportProvider {

	private List<TaskTagDefinition> cachedTaskTagDefinitions;
	private TaskTagDefinitionConverter converter;
	private AbstractUIPlugin plugin;
	private String pluginId;
	private TaskTagsSupport todoTaskSupport;
	
	public AbstractConfigurableTaskTagsSupportProvider(PluginContextProvider provider) {
		this.plugin=provider.getActivator();
		this.pluginId=provider.getPluginID();

		converter = new TaskTagDefinitionConverter();
		todoTaskSupport = new TaskTagsSupport(this);
	}
	
	public TaskTagsSupport getTodoTaskSupport() {
		return todoTaskSupport;
	}
	
	@Override
	public void logError(String error, Throwable t) {
		plugin.getLog().log(new Status(IStatus.ERROR, pluginId, error, t));
	}
	
	TaskTagDefinitionConverter getConverter() {
		return converter;
	}

	@Override
	public List<TaskTagDefinition> getTaskTagDefinitions() {
		if (cachedTaskTagDefinitions==null){
			String string = getPreferenceStore().getString(AbstractTaskTagsPreferencePage.PREFERENCE_KEY_TODOS_DEFINITIONS);
			cachedTaskTagDefinitions = converter.convertStringToList(string);
		}
		return cachedTaskTagDefinitions;
	}

	protected void resetTaskTagDefinitions() {
		cachedTaskTagDefinitions=null;
	}

	@Override
	public boolean isTodoTaskSupportEnabled() {
		return getPreferenceStore().getBoolean(AbstractTaskTagsPreferencePage.PREFERENCE_KEY_TODOS_ENABLED);
	}

	@Override
	public String getTodoTaskPluginId() {
		return pluginId;
	}

	public IPreferenceStore getPreferenceStore() {
		return plugin.getPreferenceStore();
	}

}
