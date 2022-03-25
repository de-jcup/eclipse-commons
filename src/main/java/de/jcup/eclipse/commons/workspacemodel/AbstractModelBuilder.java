package de.jcup.eclipse.commons.workspacemodel;

public abstract class AbstractModelBuilder<M> implements ModelBuilder<M>{

	@Override
	public final void update(M model, ModelUpdateAction action) {
		if (model==null) {
			throw new IllegalArgumentException("model is null!");
		}
		if (action==null) {
			throw new IllegalArgumentException("action is null!");
		}
		this.updateImpl(model,action);
	}

	/**
	 * Does update. Parameters are already checked and never <code>null</code>
	 * @param model never <code>null</code>
	 * @param action never <code>null</code>
	 */
	protected abstract void updateImpl(M model, ModelUpdateAction action);

}
