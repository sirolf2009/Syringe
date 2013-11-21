package com.sirolf2009.syringe.client.renderers;

import com.sirolf2009.syringe.client.models.Model3D;

/**
 * The IModelRenderer Interface
 * Used to render models
 * 
 * @author sirolf2009
 *
 */
public interface IModelRenderer {

	/** Loads the model */
	public void loadModel();
	/** Renders the model */
	public void renderModel();
	/** Disposes the model */
	public void disposeModel();
	/** 
	 * Get the model
	 * 
	 * @return The model
	 */
	public Model3D getModel();
	
}
