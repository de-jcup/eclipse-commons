# Simple variant
TaskTagsSupport class is necessary for all support actions with todo tasks
- is complete standalone.
- but you must define a marker which extends problem marker in your plugin.xml

Just integrate into plugin activator class - see TestcaseActivator.

If you have fixed Todo identifiers and you do NOT need any preferences etc. you can 
simply set a fixed list of tododefinitions inside an own TaskTagsupportProvider

# Handling todo tasks being configurable by preferences
Use the complete package
- create a support provider by extending `AbstractConfigurableTaskTagsSupportProvider`
- create a preference page and extend there `AbstractTaskTagsPreferencePage`
- create a preference initializer and extend there `AbstractTaskTagsPreferenceInitializer`
  + define usage inside plugin.xml