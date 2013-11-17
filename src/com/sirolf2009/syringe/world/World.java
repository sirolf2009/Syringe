package com.sirolf2009.syringe.world;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.Display;

import com.sirolf2009.syringe.client.models.AABB;
import com.sirolf2009.syringe.client.models.Model3D;
import com.sirolf2009.syringe.parsers.parserOBJ;
import com.sirolf2009.syringe.world.entity.Entity;
import com.sirolf2009.syringe.world.entity.EntityLiving;

public class World {
	
	public AABB ground;
	public int groundList;
	public List<Entity> entities = new ArrayList<Entity>();
	private List<Entity> deadEntities = new ArrayList<Entity>();
	
	public World() {
		setupGround();
		System.out.println(ground.posX1+", "+ground.posY1+", "+ground.posZ1);
		System.out.println(ground.posX2+", "+ground.posY2+", "+ground.posZ2);
	}
	
	public void setupGround() {
		try {
			Model3D groundModel = parserOBJ.loadModel(new File(getClass().getClassLoader().getResource("models/ground.obj").toURI()));
			ground = groundModel.AABB;
			groundList = groundModel.objectlist;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			Display.destroy();
			System.exit(1);
		} catch (IOException e) {
			System.err.println("Something went wrong with loading ground");
			e.printStackTrace();
			Display.destroy();
			System.exit(1);
		} catch (URISyntaxException e) {
			System.err.println("Something went wrong with loading ground");
			e.printStackTrace();
			Display.destroy();
			System.exit(1);
		}
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
