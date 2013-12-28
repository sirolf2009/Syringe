package com.sirolf2009.syringe.client.renderers;

import org.lwjgl.opengl.GL11;

import com.sirolf2009.syringe.world.entity.Entity;

/**
 * The EntityRenderer class
 * Renders an entity
 * 
 * @author sirolf2009
 *
 */
public class EntityRenderer extends ModelRenderSimple implements IEntityRenderer {

	/** The entity to render */
	private Entity entity;

	/** 
	 * The constructor 
	 * 
	 * @param The location of the .OBJ file
	 */
	public EntityRenderer(String modelLocation) {
		super(modelLocation);
	}
	
	/** Render the entity */
	@Override
	public void renderEntity() {
		GL11.glPushMatrix();
		GL11.glTranslatef(entity.getPosX(), entity.getPosY(), entity.getPosZ());
		GL11.glRotatef(entity.getRotX(), 1, 0, 0);
		GL11.glRotatef(entity.getRotY(), 0, 1, 0);
		GL11.glRotatef(entity.getRotZ(), 0, 0, 1);
		renderModel();
		GL11.glPopMatrix();
	}

	/**
	 * Checks if the {@link Model} intersects with another {@link Model}
	 * 
	 * @return true if they collide, false if not
	 */
	@Override
	public boolean intersects(IModelRenderer other) {
		if(other instanceof IEntityRenderer) {
			EntityRenderer other2 = (EntityRenderer) other;
			if (getModel().bottompoint + entity.getPosY() < other2.getModel().bottompoint + other2.entity.getPosY() || 
					getModel().toppoint + entity.getPosY() < other2.getModel().toppoint + other2.entity.getPosY() || 
					getModel().leftpoint + entity.getPosX() > other2.getModel().leftpoint + other2.entity.getPosX() || 
					getModel().rightpoint + entity.getPosX() > other2.getModel().rightpoint + other2.entity.getPosX() ||
					getModel().nearpoint + entity.getPosZ() > other2.getModel().nearpoint + other2.entity.getPosZ() || 
					getModel().farpoint + entity.getPosZ()  > other2.getModel().farpoint + other2.entity.getPosZ()) {
				return false;
			}
		} else {
			if (getModel().bottompoint + entity.getPosY() < other.getModel().bottompoint || 
					getModel().toppoint + entity.getPosY() < other.getModel().toppoint || 
					getModel().leftpoint + entity.getPosX() > other.getModel().leftpoint || 
					getModel().rightpoint + entity.getPosX() > other.getModel().rightpoint ||
					getModel().nearpoint + entity.getPosZ() > other.getModel().nearpoint || 
					getModel().farpoint + entity.getPosZ() > other.getModel().farpoint) {
				return false;
			}
		}
		return true;
	}

	/** Get the entity to be rendered */
	public Entity getEntity() {
		return entity;
	}

	/** Set the entity to be rendered */
	public void setEntity(Entity entity) {
		this.entity = entity;
	}

}
