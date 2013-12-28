package com.sirolf2009.syringe.world.entity;

import java.io.ObjectInputStream.GetField;
import java.util.Random;

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
	/** The X rotation */
	private float rotX;
	/** The Y rotation */
	private float rotY;
	/** The Z rotation */
	private float rotZ;
	/** The X velocity */
	private float velRotX;
	/** The Y velocity */
	private float velRotY;
	/** The Z velocity */
	private float velRotZ;

	private float speed;
	private Vector3f destination;

	public Random rand;

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
		speed = .02F;
		rand = new Random();
		//rotateTowards(-1, 0, -2);
		//moveForward();
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
		getRenderer().getModel().AABB.setLocation(posX, posY, posZ);
		Vector3f bounce;
		if((bounce = world.groundModel.getIntersectionDistance(AABB)) != null) {
			if(bounce.y < 0)
				bounce.y *= -1;
			velX *= .99999;
			posY += bounce.y;
			velZ *= .99999;
		} else {
			//System.out.println("no collide "+posY+" "+velY);
			velY = -0.001F;
		}
		Entity colliding = checkColliding();
		if((colliding = checkColliding()) != null) {
			bounce = getDistanceTo(colliding.getAABB());
			bounce.scale(getRenderer().getModel().weight/colliding.getRenderer().getModel().weight);
			velX -= bounce.x;
			velY -= bounce.y;
			velZ -= bounce.z;
		}
		wander();
		//printCoords();
	}

	/**
	 * calculate the distance to another {@link AABB}
	 * 
	 * @param AABB - The other {@link AABB}
	 * @return {@link Vector3f} - A vector containing the distance
	 */
	public Vector3f getDistanceTo(AABB AABB) {
		return com.sirolf2009.syringe.client.models.AABB.getDistanceBetween(getAABB(), AABB);
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

	public void moveForward() {
		setVelX((float) (speed * Math.cos(rotY)));
		setVelZ((float) (speed * Math.sin(rotY)));
	}

	public void rotateTowards(float x, float y, float z) {
		float xDistance = x - getPosX();
		float zDistance = z - getPosZ();
		float angle = (float) Math.toDegrees(Math.atan2(zDistance, xDistance));
		setRotY(angle);
	}

	public void wander() {
		if(destination == null) {
			//destination = new Vector3f(rand.nextInt(20)-10, 0, rand.nextInt(20)-10);
			destination = new Vector3f(-20, 0, -20);
		}
		if(Math.round(posX) == Math.round(destination.x) && Math.round(posZ) == Math.round(destination.z)) {
			destination = null;
			setVelX(0);
			setVelZ(0);
		} else {
			rotateTowards(destination.x, destination.y, destination.z);
			moveForward();
		}
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

	public float getRotX() {
		return rotX;
	}

	public void setRotX(float rotX) {
		this.rotX = rotX;
	}

	public float getRotY() {
		return rotY;
	}

	public void setRotY(float rotY) {
		this.rotY = rotY;
	}

	public float getRotZ() {
		return rotZ;
	}

	public void setRotZ(float rotZ) {
		this.rotZ = rotZ;
	}

	public float getVelRotX() {
		return velRotX;
	}

	public void setVelRotX(float velRotX) {
		this.velRotX = velRotX;
	}

	public float getVelRotY() {
		return velRotY;
	}

	public void setVelRotY(float velRotY) {
		this.velRotY = velRotY;
	}

	public float getVelRotZ() {
		return velRotZ;
	}

	public void setVelRotZ(float velRotZ) {
		this.velRotZ = velRotZ;
	}
}
