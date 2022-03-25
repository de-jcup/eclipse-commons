package de.jcup.eclipse.commons.workspacemodel;

public interface ModelBuilder<M> {

    /**
     * Creates a new project model
     * @return new project model
     */
	public M create();
	
	/**
	 * Updates existing model by an update action
	 * @param model
	 * @param action
	 */
	public void update(M model, ModelUpdateAction action);

}
