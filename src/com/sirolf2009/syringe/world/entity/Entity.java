package com.sirolf2009.syringe.world.entity;

import org.lwjgl.util.vector.Vector3f;

import com.sirolf2009.syringe.client.models.AABB;
import com.sirolf2009.syringe.client.renderers.EntityRenderer;
import com.sirolf2009.syringe.client.renderers.IEntityRenderer;
import com.sirolf2009.syringe.world.World;

public abstract class Entity {
	
	private AABB AABB;
	
	private World world;
	private float posX;
	private float posY;
	private float posZ;
	private float velX;
	private float velY;
	private float velZ;
	
	private IEntityRenderer renderer;

	public Entity(World world, IEntityRenderer renderer) {
		this.world = world;
		setRenderer(renderer);
		AABB = getRenderer().getModel().AABB;
	}

	public void update(long delta) {
		posX += velX;
		posY += velY;
		posZ += velZ;
		AABB.applyTranslation(velX, velY, velZ);
		Entity colliding = checkColliding();
		System.out.println(colliding);
		/*if((colliding = checkColliding()) != null) {
			Vector3f bounce = getDistanceTo(colliding);
			posX += bounce.x;
			posY += bounce.y;
			posZ += bounce.z;
		}*/
	}
	
	public Vector3f getDistanceTo(Entity entity) {
		float dX = getRenderer().getModel().leftpoint - entity.getRenderer().getModel().rightpoint;
		float dY = getRenderer().getModel().bottompoint - entity.getRenderer().getModel().toppoint;
		float dZ = getRenderer().getModel().nearpoint - entity.getRenderer().getModel().farpoint;
		if(dX == 0 || dY == 0 || dZ == 0) {
			new Exception("Entities are not colliding");
		}
		return new Vector3f(dX, dY, dZ);
	}

	private Entity checkColliding() {
		for(Entity entity : getWorld().entities) {
			if(entity.getRenderer().getModel().AABB.intersects(getRenderer().getModel().AABB)) {
				if(AABB.intersects(entity.getAABB())) {
					return entity;
				}
			}
		}
		return null;
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
