package de.jcup.eclipse.commons.properties;

import de.jcup.eclipse.commons.preferences.AbstractPreferenceValueConverter;

public class PropertiesPreferenceValueConverter extends AbstractPreferenceValueConverter<PropertyDefinition>{

	@Override
	protected void write(PropertyDefinition oneEntry,
			de.jcup.eclipse.commons.preferences.PreferenceDataWriter writer) {
		writer.writeString(oneEntry.getName());
		writer.writeString(oneEntry.getValue());
	}

	@Override
	protected PropertyDefinition read(
			de.jcup.eclipse.commons.preferences.PreferenceDataReader reader) {
		String name = reader.readString();
		String value = reader.readString();
		PropertyDefinition definition = new PropertyDefinition(name,value);
		return definition;
	}
	

}