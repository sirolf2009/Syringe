package com.sirolf2009.syringe.world.entity;

import com.sirolf2009.syringe.client.renderers.IEntityRenderer;
import com.sirolf2009.syringe.world.World;

public abstract class EntityLiving extends Entity {

	private int maxHealth;
	private int health;
	
	public EntityLiving(World world, IEntityRenderer renderer) {
		super(world, renderer);
	}
	
	public void update(long delta) {
		super.update(delta);
		if(health <= 0) {
			getWorld().removeEntity(this);
		}
	}

	public int getMaxHealth() {
		return maxHealth;
	}

	public void setMaxHealth(int maxHealth) {
		this.maxHealth = maxHealth;
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

}
