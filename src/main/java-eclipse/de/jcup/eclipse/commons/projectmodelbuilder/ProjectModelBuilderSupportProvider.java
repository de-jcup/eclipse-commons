package de.jcup.eclipse.commons.projectmodelbuilder;

import org.eclipse.core.resources.IFile;

public interface ProjectModelBuilderSupportProvider<M> {

	public void logError(String error, Throwable t);

	/**
	 * Check if this file is handled by provider.
	 * @param file - never <code>null</code>
	 * @return
	 */
	public boolean isFileHandled(IFile file);

	boolean isProjectModelBuilderSupportEnabled();

	String getPluginId();

	public String getModelName();

	public boolean isLineCheckforModelNessary(String line, int lineNumber, String[] lines);
	
	public ProjectModelBuilder<M> createBuilder();
}