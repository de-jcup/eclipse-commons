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
    boolean isModelBuilderSupportEnabled();

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
     * Gives back the maximum amount of lines which will be checked, or <code>-1</code> when all lines shall be read.
     * Per default -1 is returned, means all lines are read and given to {@link #isLineCheckforModelNessary(String, int, String[])}.
     * <br><br>
     * If you do not need all lines but only for example the first one override the method and return your wanted maximum value - in the example
     * mentioned before we would give back just a 1..
     * @return amount of lines to read or <code>-1</code> when all lines shall be read
     */
    public default int getAmountOfLinesToCheck() {
        return -1;
    }

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