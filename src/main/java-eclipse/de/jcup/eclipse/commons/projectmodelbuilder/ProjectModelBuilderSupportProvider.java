package de.jcup.eclipse.commons.projectmodelbuilder;

import org.eclipse.core.resources.IFile;

import de.jcup.eclipse.commons.PluginContextProvider;

public interface ProjectModelBuilderSupportProvider<M> {

	/**
	 * Check if this file is handled by provider.
	 * @param file - never <code>null</code>
	 * @return
	 */
	public boolean isFileHandled(IFile file);

	boolean isProjectModelBuilderSupportEnabled();

	public String getModelName();

	public boolean isLineCheckforModelNessary(String line, int lineNumber, String[] lines);
	
	public ProjectModelBuilder<M> createBuilder();
	
	public PluginContextProvider getPluginContextProvider();
}