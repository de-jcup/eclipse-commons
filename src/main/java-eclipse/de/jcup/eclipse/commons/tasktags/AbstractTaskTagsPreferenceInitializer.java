package de.jcup.eclipse.commons.tasktags;

import java.util.List;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.ui.preferences.ScopedPreferenceStore;

import de.jcup.eclipse.commons.tasktags.TaskTagsSupport.TaskTagDefinition;

/**
 * Children must only provide the plugin id...
 * @author Albert Tregnaghi
 *
 */
public abstract class AbstractTaskTagsPreferenceInitializer extends AbstractPreferenceInitializer {

	private ScopedPreferenceStore store;

	public AbstractTaskTagsPreferenceInitializer(String pluginId){
		store = new ScopedPreferenceStore(InstanceScope.INSTANCE, pluginId);
	}
	
	
	@Override
	public void initializeDefaultPreferences() {
		TaskTagDefinitionConverter converter = new TaskTagDefinitionConverter();
		
		List<TaskTagDefinition> list = TaskTagDefinitionDefaults.get();
		
		String defaultData = converter.convertListTostring(list);
		
		store.setDefault(AbstractTaskTagsPreferencePage.PREFERENCE_KEY_TODOS_ENABLED,perDefaultTodosEnabled());
		store.setDefault(AbstractTaskTagsPreferencePage.PREFERENCE_KEY_TODOS_DEFINITIONS,defaultData);
	}

	protected boolean perDefaultTodosEnabled() {
		return true;
	}

}
