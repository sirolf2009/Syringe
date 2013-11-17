package com.sirolf2009.syringe.client.models;

import org.lwjgl.util.vector.Vector3f;

public class AABB {

	public float posX1;
	public float posY1;
	public float posZ1;
	public float posX2;
	public float posY2;
	public float posZ2;
	public float width;
	public float height;
	public float depth;
	public Vector3f center;
	
	public AABB(float posX1, float posY1, float posZ1, float posX2, float posY2, float posZ2) {
		this.posX1 = posX1;
		this.posY1 = posY1;
		this.posZ1 = posZ1;
		this.posX2 = posX2;
		this.posY2 = posY2;
		this.posZ2 = posZ2;
		width = posX2 - posX1;
		height = posY2 - posY1;
		depth = posZ2 - posZ1;
		center = new Vector3f(posX1 + width/2, posY1 + height/2, posZ1 + depth/2);
	}
	
	public boolean intersects(AABB other) {
		if(Math.abs(center.x - other.center.x) < width/2 + other.width/2) {
			if(Math.abs(center.y - other.center.y) < height/2 + other.height/2) {
				if(Math.abs(center.z - other.center.z) < depth/2 + other.depth/2) {
					return true;
				}
	         }
		}
		return false;
	}
	
	public void applyTranslation(float tranX, float tranY, float tranZ) {
		this.posX1 += tranX;
		this.posY1 += tranY;
		this.posZ1 += tranZ;
		this.posX2 += tranX;
		this.posY2 += tranY;
		this.posZ2 += tranZ;
	}
	
	public void setLocation(float posX, float posY, float posZ) {
		this.posX1 = posX;
		this.posY1 = posY;
		this.posZ1 = posZ;
		this.posX2 = posX + width;
		this.posY2 = posY + height;
		this.posZ2 = posZ + depth;
		center = new Vector3f(posX1 + width/2, posY1 + height/2, posZ1 + depth/2);
	}
	
	public static AABB createAABBFromModel(Model3D model) {
		return new AABB(model.posX1, model.posY1, model.posZ1, model.posX2, model.posY2, model.posZ2);
	}

}
