package com.sirolf2009.syringe.client.models;

public class AABB {

	float posX1;
	float posY1;
	float posZ1;
	float posX2;
	float posY2;
	float posZ2;
	
	public AABB(float posX1, float posY1, float posZ1, float posX2, float posY2, float posZ2) {
		this.posX1 = posX1;
		this.posY1 = posY1;
		this.posZ1 = posZ1;
		this.posX2 = posX2;
		this.posY2 = posY2;
		this.posZ2 = posZ2;
	}
	
	public boolean intersects(AABB other) {
		return (posX2 > other.posX1 &&
				posX1 < other.posX2 &&
				posY2 > other.posY1 &&
				posY1 < other.posY2 &&
				posZ2 > other.posZ1 &&
				posZ1 < other.posZ2);
	}
	
	public void applyTranslation(float tranX, float tranY, float tranZ) {
		this.posX1 += tranX;
		this.posY1 += tranY;
		this.posZ1 += tranZ;
		this.posX2 += tranX;
		this.posY2 += tranY;
		this.posZ2 += tranZ;
	}
	
	public static AABB createAABBFromModel(Model3D model) {
		return new AABB(model.posX1, model.posY1, model.posZ1, model.posX2, model.posY2, model.posZ2);
	}

}
