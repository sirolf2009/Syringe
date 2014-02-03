package com.sirolf2009.syringe.client.models;

import java.util.HashMap;
import java.util.Map;

import org.newdawn.slick.opengl.Texture;

public class ModelOBJ extends Model {
	
	public Map<String, Texture> textures = new HashMap<String, Texture>();
	public Map<String, Integer> lists = new HashMap<String, Integer>();

	public ModelOBJ() {
	}
	
	public void createMeshes() {
		for(String mtl : textures.keySet()) {
			meshes.add(new Mesh(mtl, lists.get(mtl), textures.get(mtl)));
		}
	}

}
