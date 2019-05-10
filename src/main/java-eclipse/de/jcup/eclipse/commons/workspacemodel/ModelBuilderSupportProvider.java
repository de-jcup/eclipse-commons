package de.jcup.eclipse.commons.workspacemodel;

import org.eclipse.core.resources.IFile;

import de.jcup.eclipse.commons.PluginContextProvider;

/**
 * Implementations of this interface will provide information for framework and
 * project model implementations
 * 
 * @author albert
 *
 * @param <M> represents model
 */
public interface ModelBuilderSupportProvider<M> {

    /**
     * Check if this file is handled by provider.
     * 
     * @param file - never <code>null</code>
     * @return
     */
    public boolean isFileHandled(IFile file);

    /**
     * This represents a kill switch
     * 
     * @return <code>true</code> when support is enabled
     */
    boolean isProjectModelBuilderSupportEnabled();

    /**
     * Model build is done as a job. To provide information about progress the job
     * has a name which represented by this return value
     * 
     * @return name of the model for job ui
     */
    public String getModelName();

    /**
     * Checks if this line requires a line check action. Means: Inside this method
     * we only check if an action shall be created for the project model builder!
     * There is no need for creating the model at this position!!! It's only a check
     * for the actions which will automatically be created by commons framework.
     * 
     * @param line - will start at 1, never lower!
     * @param lineNumber
     * @param lines
     * @return <code>true</code> when a action is necessary, otherwise
     *         <code>false</code>
     */
    public boolean isLineCheckforModelNessary(String line, int lineNumber, String[] lines);

    /**
     * Creates the project model builder
     * @return builder
     */
    public ModelBuilder<M> createBuilder();

    /**
     * Returns context provider to given information about plugin etc.
     * @return context provider
     */
    public PluginContextProvider getPluginContextProvider();
}