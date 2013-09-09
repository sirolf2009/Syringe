package com.sirolf2009.syringe.client.renderers;

import java.util.ArrayList;
import java.util.List;

public class RenderManager {

	public List<IModelRenderer> modelRenderers = new ArrayList<>();

	public void registerRenderer(Object renderer) {
		if(renderer instanceof IModelRenderer) {
			modelRenderers.add((IModelRenderer) renderer);
		}
	}

	public void renderAll() {
		for(IModelRenderer renderer : modelRenderers) {
			renderer.renderModel();
		}
	}

	public void cleanUp() {
		for(IModelRenderer renderer : modelRenderers) {
			renderer.disposeModel();
		}
	}
}
