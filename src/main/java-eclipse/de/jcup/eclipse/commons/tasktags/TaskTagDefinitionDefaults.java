package de.jcup.eclipse.commons.tasktags;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.jcup.eclipse.commons.tasktags.TaskTagsSupport.TaskTagDefinition;
import de.jcup.eclipse.commons.tasktags.TaskTagsSupport.TaskTagPriority;

public class TaskTagDefinitionDefaults {
	private static List<TaskTagDefinition> defaults = new ArrayList<>();
	static{
		defaults.add(new TaskTagDefinition("FIXME", TaskTagPriority.HIGH));
		defaults.add(new TaskTagDefinition("TODO", TaskTagPriority.NORMAL));
	}
	/**
	 * @return unmodifiable list of default definitions
	 */
	public static List<TaskTagDefinition> get() {
		return Collections.unmodifiableList(defaults);
	}

}
