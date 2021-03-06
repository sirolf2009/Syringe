package com.sirolf2009.syringe.client.renderers;

import org.lwjgl.opengl.GL11;

import com.sirolf2009.syringe.world.entity.Entity;

/**
 * The EntityRendererAnimated class
 * Renders an entity
 * 
 * @author sirolf2009
 *
 */
public class EntityRendererAnimated extends ModelRenderAnimated implements IEntityRenderer {

	/** The entity to render */
	private Entity entity;

	/** 
	 * The constructor 
	 * 
	 * @param The location of the .OBJ file
	 */
	public EntityRendererAnimated(String modelLocation, int frames) {
		super(modelLocation, frames);
	}
	
	/** Render the entity */
	@Override
	public void renderEntity() {
		GL11.glPushMatrix();
		GL11.glTranslated(entity.getPosX(), entity.getPosY(), entity.getPosZ());
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
			EntityRendererAnimated other2 = (EntityRendererAnimated) other;
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
