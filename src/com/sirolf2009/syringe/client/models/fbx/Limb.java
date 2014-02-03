package com.sirolf2009.syringe.client.models.fbx;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.sirolf2009.syringe.client.models.Mesh;

public class Limb {

	protected List<Integer> affectedVertices;
	protected List<Double> weights;
	protected Map<String, Take> takes;
	protected String name;
	protected Mesh mesh;
	
	public Limb(String name) {
		this.name = name;
		affectedVertices = new ArrayList<Integer>();
		weights = new ArrayList<Double>();
	}
	
	public void addVertex(int index, double weight) {
		affectedVertices.add(index);
		weights.add(weight);
	}
	
	public void setMesh(Mesh mesh) {
		this.mesh = mesh;
	}
	
	public String getName() {
		return name;
	}
	
	public void generateAnimation() {
		if(mesh == null) {
			return;
		}
		for(Entry<String, Take> entry : takes.entrySet()) {
			System.out.println(entry.getKey());
		}
	}

}
