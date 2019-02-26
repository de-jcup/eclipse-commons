package de.jcup.eclipse.commons.projectmodelbuilder;

public abstract class AbstractProjectModelBuilder<M> implements ProjectModelBuilder<M>{

	@Override
	public final void update(M model, ModelUpdateAction action) {
		if (model==null) {
			throw new IllegalArgumentException("model is null!");
		}
		if (action==null) {
			throw new IllegalArgumentException("action is null!");
		}
		this.doUpdate(model,action);
	}

	/**
	 * Does update. Parameters are already checked and never <code>null</code>
	 * @param model never <code>null</code>
	 * @param action never <code>null</code>
	 */
	protected abstract void doUpdate(M model, ModelUpdateAction action);

}
