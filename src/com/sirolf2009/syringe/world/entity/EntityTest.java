package com.sirolf2009.syringe.world.entity;

import com.sirolf2009.syringe.client.renderers.IEntityRenderer;
import com.sirolf2009.syringe.world.World;

public class EntityTest extends EntityLiving {

	public EntityTest(World world, IEntityRenderer renderer) {
		super(world, renderer);
		setHealth(100);
	}

}
