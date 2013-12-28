package com.sirolf2009.syringe.client.renderers;

import com.sirolf2009.syringe.world.entity.Entity;

/**
 * The IEntityRenderer Interface
 * Used to create entity renderers
 * 
 * @author sirolf2009
 *
 */
public interface IEntityRenderer extends IModelRenderer {
	
	/** Render the entity */
	public void renderEntity();
	/** Checks if the {@link Model} intersects with another {@link Model} */
	public boolean intersects(IModelRenderer other);
	
	/** Set the entity to be rendered */
	public void setEntity(Entity entity);
	/** Get the entity to be rendered */
	public Entity getEntity();

}
