package com.sirolf2009.syringe.client.renderers;

import com.sirolf2009.syringe.client.models.Model3D;

public interface IModelRenderer {

	public void loadModel();
	public void renderModel();
	public void disposeModel();
	public Model3D getModel();
	
}
