package de.jcup.eclipse.commons.properties;

import java.util.List;

import org.eclipse.jface.preference.IPreferenceStore;

public interface PropertyProvider {

	IPreferenceStore getPreferenceStore();

	List<PropertyDefinition> getProperties();

	PropertiesPreferenceValueConverter getConverter();

	String getPreferencePropertyDataKey();

	String getPreferencePropertyEnabledKey();

}
