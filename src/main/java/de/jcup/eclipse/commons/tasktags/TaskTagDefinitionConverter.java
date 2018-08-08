package de.jcup.eclipse.commons.tasktags;

import de.jcup.eclipse.commons.preferences.AbstractPreferenceValueConverter;
import de.jcup.eclipse.commons.tasktags.TaskTagsSupport.TaskTagDefinition;

class TaskTagDefinitionConverter extends AbstractPreferenceValueConverter<TaskTagDefinition>{

	@Override
	protected void write(TaskTagDefinition oneEntry,
			de.jcup.eclipse.commons.preferences.PreferenceDataWriter writer) {
		writer.writeString(oneEntry.getIdentifier());
		writer.writeString(oneEntry.getPriority().name());
	}

	@Override
	protected TaskTagDefinition read(
			de.jcup.eclipse.commons.preferences.PreferenceDataReader reader) {
		TaskTagDefinition definition = new TaskTagDefinition();
		definition.setIdentifier(reader.readString());
		definition.setPriority(reader.readString());
		return definition;
	}
	

}