package com.sirolf2009.syringe.client.renderers;

import org.lwjgl.opengl.GL11;

import com.sirolf2009.syringe.world.entity.Entity;

public class EntityRenderer extends ModelRenderSimple implements IEntityRenderer {

	private Entity entity;

	public EntityRenderer(String modelLocation) {
		super(modelLocation);
	}
	
	@Override
	public void renderEntity() {
		GL11.glPushMatrix();
		GL11.glTranslated(entity.getPosX(), entity.getPosY(), entity.getPosZ());
		renderModel();
		GL11.glPopMatrix();
	}

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

	public Entity getEntity() {
		return entity;
	}

	public void setEntity(Entity entity) {
		this.entity = entity;
	}

}
