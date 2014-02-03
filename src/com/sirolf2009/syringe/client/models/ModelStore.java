package com.sirolf2009.syringe.client.models;

import java.util.HashMap;
import java.util.Map;

public class ModelStore {

	public Map<String, Model> models;
	public Map<String, IModelAnimated> modelsAnimated;
	
	public ModelStore() {
		models = new HashMap<String, Model>();
		modelsAnimated = new HashMap<String, IModelAnimated>();
	}
	
	

}
