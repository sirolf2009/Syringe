package com.sirolf2009.syringe.world.entity;

import com.sirolf2009.syringe.Syringe;
import com.sirolf2009.syringe.client.renderers.IEntityRenderer;
import com.sirolf2009.syringe.world.World;

public class EntityPlayer extends EntityLiving {

	public EntityPlayer(World world, IEntityRenderer renderer) {
		super(world, renderer);
	}
	
	@Override
	public void update(long delta) {
		super.update(delta);
		float rotX = (float) (Math.sin((getRotY()) * Math.PI / 180));
		float rotZ = (float) (Math.cos((getRotY()) * Math.PI / 180));
		Syringe.camera.setPosition(getPosX()-rotX, getPosY()+1, getPosZ()-rotZ);
	}
	
	@Override
	public void wander() {
	}
	
	@Override
	public void onAddedToWorld() {
		Syringe.player = this;
	}

}
