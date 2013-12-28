package com.sirolf2009.syringe.client.models;

import org.lwjgl.util.vector.Vector3f;

/**
 * The AABB class
 * Used for collision detection
 * 
 * @author sirolf2009
 *
 */
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
	
	/** 
	 * The constructor 
	 * 
	 * @param The bottom-left 
	 */
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
	
	/**
     * Check if this AABB intersects with another
     * 
     * @return true if intersecting, false if not
     */
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
	
	/**
     * Check if this AABB intersects with another
     * 
     * @return true if intersecting, false if not
     */
	public boolean intersectsPrecise(AABB other) {
		if(Math.abs(center.x - other.center.x) < width/2 + other.width/2) {
			if(Math.abs(center.y - other.center.y) < height/2 + other.height/2) {
				if(Math.abs(center.z - other.center.z) < depth/2 + other.depth/2) {
					return true;
				}
	         }
		}
		return false;
	}
	
	public static Vector3f getDistanceBetween(AABB AABB1, AABB AABB2) {
		float xAxis = Math.abs(AABB1.center.x - AABB2.center.x); //distance between centers
		float yAxis = Math.abs(AABB1.center.y - AABB2.center.y); //distance between centers
		float zAxis = Math.abs(AABB1.center.z - AABB2.center.z); //distance between centers

		float cw = AABB1.width/2 + AABB2.width/2; //combined width
		float ch = AABB1.height/2 + AABB2.height/2; //combined height
		float cd = AABB1.depth/2 + AABB2.depth/2; //combined depth

		//early exit
		if(xAxis > cw) return null;
		if(yAxis > ch) return null;
		if(zAxis > cd) return null;

		float ox = Math.abs(xAxis - cw); //overlap on x
		float oy = Math.abs(yAxis - ch); //overlap on y
		float oz = Math.abs(zAxis - cd); //overlap on z

		//direction
		Vector3f deltaDir = Vector3f.sub(AABB2.center, AABB1.center, null); //subtract other.position from this.position

		return new Vector3f(deltaDir.x * ox, deltaDir.y * oy, deltaDir.z * oz);
	}
	
	/**
	 * Apply translation to the AABB
	 * 
	 * @param tranX - delta x
	 * @param tranY - delta y
	 * @param tranZ - delta z
	 */
	public void applyTranslation(float tranX, float tranY, float tranZ) {
		this.posX1 += tranX;
		this.posY1 += tranY;
		this.posZ1 += tranZ;
		this.posX2 += tranX;
		this.posY2 += tranY;
		this.posZ2 += tranZ;
	}
	
	/**
	 * Set the location of the AABB
	 * 
	 * @param posX - position x
	 * @param posY - position y
	 * @param posZ - position z
	 */
	public void setLocation(float posX, float posY, float posZ) {
		this.posX1 = posX;
		this.posY1 = posY;
		this.posZ1 = posZ;
		this.posX2 = posX + width;
		this.posY2 = posY + height;
		this.posZ2 = posZ + depth;
		center = new Vector3f(posX1 + width/2, posY1 + height/2, posZ1 + depth/2);
	}
	
	@Override
	public String toString() {
		return posX1+", "+posY1+", "+posZ1+", "+width+", "+height+", "+depth;
	}
	
	/**
	 * Create an AABB from a {@link Model}
	 * 
	 * @return the generated AABB
	 */
	public static AABB createAABBFromModel(Model model) {
		return new AABB(model.posX1, model.posY1, model.posZ1, model.posX2, model.posY2, model.posZ2);
	}

}
