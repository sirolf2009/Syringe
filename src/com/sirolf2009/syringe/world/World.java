package com.sirolf2009.syringe.world;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.Display;

import com.sirolf2009.syringe.client.models.AABB;
import com.sirolf2009.syringe.client.models.Model;
import com.sirolf2009.syringe.parsers.ParserOBJ;
import com.sirolf2009.syringe.world.entity.Entity;
import com.sirolf2009.syringe.world.entity.EntityLiving;

/**
 * The World Class
 * Contains all entities and handles entity updating
 * 
 * @author sirolf2009
 *
 */
public class World {
	
	/** The {@link AABB} from the ground */
	public AABB ground;
	public ArrayList<float[]> vertexsets;
	/** The OpenGL object list from the ground */
	public Model groundModel;
	/** All entities currently in the world */
	public List<Entity> entities = new ArrayList<Entity>();
	/** Entities that have died, will be removed after a tick */
	private List<Entity> deadEntities = new ArrayList<Entity>();
	
	/** The constructor */
	public World() {
		setupGround();
	}
	
	/** Creates a ground from models/ground.obj */
	public void setupGround() {
		try {
			groundModel = ParserOBJ.loadModel(new File(getClass().getClassLoader().getResource("models/ground.obj").toURI()));
			ground = groundModel.AABB;
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
	
	/** Updates all entities, then removes all dead entities */
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
	
	/** 
	 * Remove an entity from this world
	 * 
	 * @param entity - The entity to be removed
	 */
	public synchronized void removeEntity(Entity entity) {
		deadEntities.add(entity);
	}
	
	/** 
	 * Adds an entity to this world
	 * 
	 * @param entity - The entity to be added
	 */
	public synchronized void addEntity(Entity entity) {
		entities.add(entity);
	}
	
}
