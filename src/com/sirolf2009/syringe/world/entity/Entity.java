package com.sirolf2009.syringe.world.entity;

import org.lwjgl.util.vector.Vector3f;

import com.sirolf2009.syringe.client.models.AABB;
import com.sirolf2009.syringe.client.renderers.IEntityRenderer;
import com.sirolf2009.syringe.world.World;

public abstract class Entity {

	/** The {@link AABB} from this entity */
	private AABB AABB;

	/** The entity's {@link World} */
	private World world;
	/** The X position */
	private float posX;
	/** The Y position */
	private float posY;
	/** The Z position */
	private float posZ;
	/** The X velocity */
	private float velX;
	/** The Y velocity */
	private float velY;
	/** The Z velocity */
	private float velZ;

	/** The entity's renderer */
	private IEntityRenderer renderer;

	/** The constructor
	 * 
	 * @param world - The world to put the entity in
	 * @param renderer - The renderer
	 */
	public Entity(World world, IEntityRenderer renderer) {
		this.world = world;
		setRenderer(renderer);
		AABB = getRenderer().getModel().AABB;
	}
	
	/**
	 * Updated the entity's position and checks for collision
	 * 
	 * @param delta - The amount of time since the last tick
	 */
	public void update(long delta) {
		posX += velX;
		posY += velY;
		posZ += velZ;
		AABB.setLocation(posX, posY, posZ);
		if(AABB.intersects(world.ground)) {
			velX *= .9;
			velY = 0;
			velZ *= .9;
		} else {
			velY -= .005;
		}
		Entity colliding = checkColliding();
		if((colliding = checkColliding()) != null) {
			Vector3f bounce = getDistanceTo(colliding.getAABB());
			velX -= bounce.x/100;
			velY -= bounce.y/100;
			velZ -= bounce.z/100;
		}
		velX *= 0.5;
		velY *= 0.5;
		velZ *= 0.5;
	}

	/**
	 * calculate the distance to another {@link AABB}
	 * 
	 * @param AABB - The other {@link AABB}
	 * @return {@link Vector3f} - A vector containing the distance
	 */
	public Vector3f getDistanceTo(AABB AABB) {
		float xAxis = Math.abs(getAABB().center.x - AABB.center.x); //distance between centers
		float yAxis = Math.abs(getAABB().center.y - AABB.center.y); //distance between centers
		float zAxis = Math.abs(getAABB().center.z - AABB.center.z); //distance between centers

	    float cw = getAABB().width/2 + AABB.width/2; //combined width
	    float ch = getAABB().height/2 + AABB.height/2; //combined height
	    float cd = getAABB().depth/2 + AABB.depth/2; //combined depth

	    //early exit
	    if(xAxis > cw) return null;
	    if(yAxis > ch) return null;
	    if(zAxis > cd) return null;
	    
		float ox = Math.abs(xAxis - cw); //overlap on x
		float oy = Math.abs(yAxis - ch); //overlap on y
		float oz = Math.abs(zAxis - cd); //overlap on z

		//direction
		Vector3f deltaDir = Vector3f.sub(AABB.center, getAABB().center, null); //subtract other.position from this.position
		deltaDir.normalise();

		return new Vector3f(deltaDir.x * ox, deltaDir.y * oy, deltaDir.z * oz);
		
		/*Vector3f distance = new Vector3f();
		distance.x = getAABB().center.x - AABB.center.x;
		distance.y = getAABB().center.y - AABB.center.y;
		distance.z = getAABB().center.z - AABB.center.z;
		distance.normalise();
		return distance;*/
	}

	/** Returns an entity that this entity is colliding with */
	private Entity checkColliding() {
		for(Entity entity : getWorld().entities) {
			if(entity == this) {
				continue;
			}
			if(AABB.intersects(entity.getAABB())) {
				return entity;
			}
		}
		return null;
	}
	
	public void printCoords() {
		System.out.println(getPosX()+", "+getPosY()+", "+getPosZ());
	}

	public World getWorld() {
		return world;
	}

	public void setWorld(World world) {
		this.world = world;
	}

	public float getPosX() {
		return posX;
	}

	public void setPosX(float posX) {
		this.posX = posX;
	}

	public float getPosY() {
		return posY;
	}

	public void setPosY(float posY) {
		this.posY = posY;
	}

	public float getPosZ() {
		return posZ;
	}

	public void setPosZ(float posZ) {
		this.posZ = posZ;
	}

	public float getVelX() {
		return velX;
	}

	public void setVelX(float velX) {
		this.velX = velX;
	}

	public float getVelY() {
		return velY;
	}

	public void setVelY(float velY) {
		this.velY = velY;
	}

	public float getVelZ() {
		return velZ;
	}

	public void setVelZ(float velZ) {
		this.velZ = velZ;
	}

	public IEntityRenderer getRenderer() {
		return renderer;
	}

	public void setRenderer(IEntityRenderer renderer) {
		this.renderer = renderer;
	}

	public AABB getAABB() {
		return AABB;
	}

	public void setAABB(AABB AABB) {
		this.AABB = AABB;
	}
}
