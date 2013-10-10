package com.sirolf2009.syringe.world;

import java.util.ArrayList;
import java.util.List;

import com.sirolf2009.syringe.world.entity.Entity;
import com.sirolf2009.syringe.world.entity.EntityLiving;

public class World {
	
	public List<Entity> entities = new ArrayList<Entity>();
	private List<Entity> deadEntities = new ArrayList<Entity>();
	
	public World() {
		
	}
	
	public synchronized void update(long delta) {
		for(Entity entity : entities) {
			entity.getRenderer().setEntity(entity);
			entity.update(delta);
			if(entity instanceof EntityLiving) {
				if(((EntityLiving) entity).getHealth() < 0) {
					deadEntities.add(entity);
				}
			}
		}
		for(Entity entity : deadEntities) {
			entities.remove(entity);
		}
		deadEntities.clear();
	}
	
	public synchronized void removeEntity(Entity entity) {
		deadEntities.add(entity);
	}
	
	public synchronized void addEntity(Entity entity) {
		entities.add(entity);
	}
	
}
