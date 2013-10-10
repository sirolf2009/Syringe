package com.sirolf2009.syringe.client.renderers;

import com.sirolf2009.syringe.world.entity.Entity;

public interface IEntityRenderer extends IModelRenderer {
		
	public void renderEntity();
	public boolean intersects(IModelRenderer other);
	
	public void setEntity(Entity entity);
	public Entity getEntity();

}
