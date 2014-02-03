package com.sirolf2009.syringe.client.renderers;

import static org.lwjgl.opengl.GL11.GL_COLOR_MATERIAL;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_DIFFUSE;
import static org.lwjgl.opengl.GL11.GL_FRONT;
import static org.lwjgl.opengl.GL11.GL_LIGHTING;
import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glColorMaterial;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glTranslated;
import static org.lwjgl.opengl.GL11.glVertex2f;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import com.sirolf2009.syringe.Syringe;
import com.sirolf2009.syringe.parsers.ParserOBJ;
import com.sirolf2009.syringe.world.entity.Entity;
import com.sirolf2009.syringe.world.entity.EntityPlayer;

/**
 * The RenderManager Class
 * Stores the renderers
 * 
 * @author sirolf2009
 *
 */
public class RenderManager {

	/** The {@link Syringe} reference */
	private Syringe syringe;

	/** A list containing all the registered {@link IModelRenderer} */
	private List<IModelRenderer> modelRenderers = new ArrayList<>();

	/** A map containing all the registered {@link IEntityRenderer} */
	private Map<Entity, IEntityRenderer> entityRenderers = new HashMap<Entity, IEntityRenderer>();

	TextRenderer text;
	
	private Texture skybox;
	/**
	 * The constructor
	 * 
	 * @param syringe - The main syringe thread
	 */
	public RenderManager(Syringe syringe) {
		this.syringe = syringe;
		text = new TextRenderer();
		try {
			skybox = TextureLoader.getTexture("jpg", new FileInputStream(new File(ParserOBJ.class.getClassLoader().getResource("img/skybox.jpg").toURI())));
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
		}
	}

	/** draws the HUD */
	public void drawGUI() {
		setup2D();
		glPushMatrix();
		GL11.glColor3f(1f,1f,1f);
		glTranslated(Display.getWidth()/2, Display.getHeight()/2, 0);
		glBegin(GL_LINES);
		glVertex2f(0.0f, 10f);
		glVertex2f(0.0f, -10f);
		glVertex2f(-10, 0.0f);
		glVertex2f(10, 0.0f);
		glEnd();
		glPopMatrix();
		glPushMatrix();
		glTranslated(0, Display.getHeight(), 0); //this is needed because reasons
		text.renderText(0, 0, "X: "+Syringe.camera.x());
		text.renderText(0, 16, "Y: "+Syringe.camera.y());
		text.renderText(0, 32, "Z: "+Syringe.camera.z());
		text.renderText(0, 48, "FPS: "+syringe.FPS);
		glPopMatrix();
		setup3D();
	}
	
	public void drawSkybox() {
		EntityPlayer player = Syringe.player;
		int offset = 0;
		skybox.bind();
	    GL11.glBegin(GL11.GL_QUADS);
	        GL11.glTexCoord2f(1f, 0.0f);
	        GL11.glVertex3f(-player.getPosX() - offset, -player.getPosY() - offset, -player.getPosZ() - offset);
	        GL11.glTexCoord2f(1f, 1f);
	        GL11.glVertex3f(-player.getPosX() - offset, -player.getPosY() + offset, -player.getPosZ() - offset);
	        GL11.glTexCoord2f(0.0f, 1f);
	        GL11.glVertex3f(-player.getPosX() + offset, -player.getPosY() + offset, -player.getPosZ() - offset);
	        GL11.glTexCoord2f(0.0f, 0.0f);
	        GL11.glVertex3f(-player.getPosX() + offset, -player.getPosY() - offset, -player.getPosZ() - offset);
	    GL11.glEnd();

	    // Back Face
	    GL11.glBegin(GL11.GL_QUADS);
	        GL11.glTexCoord2f(0.0f, 0.0f);
	        GL11.glVertex3f(-player.getPosX() - offset, -player.getPosY() - offset, -player.getPosZ() + offset);
	        GL11.glTexCoord2f(1f, 0.0f);
	        GL11.glVertex3f(-player.getPosX() + offset, -player.getPosY() - offset, -player.getPosZ() + offset);
	        GL11.glTexCoord2f(1f, 1f);
	        GL11.glVertex3f(-player.getPosX() + offset, -player.getPosY() + offset, -player.getPosZ() + offset);
	        GL11.glTexCoord2f(0.0f, 1f);
	        GL11.glVertex3f(-player.getPosX() - offset, -player.getPosY() + offset, -player.getPosZ() + offset);
	    GL11.glEnd();

	    // Top Face
	    GL11.glBegin(GL11.GL_QUADS);
	        GL11.glTexCoord2f(1f, 1f);
	        GL11.glVertex3f(-player.getPosX() - offset, -player.getPosY() - offset, -player.getPosZ() - offset);
	        GL11.glTexCoord2f(0.0f, 1f);
	        GL11.glVertex3f(-player.getPosX() + offset, -player.getPosY() - offset, -player.getPosZ() - offset);
	        GL11.glTexCoord2f(0.0f, 0.0f);
	        GL11.glVertex3f(-player.getPosX() + offset, -player.getPosY() - offset, -player.getPosZ() + offset);
	        GL11.glTexCoord2f(1f, 0.0f);
	        GL11.glVertex3f(-player.getPosX() - offset, -player.getPosY() - offset, -player.getPosZ() + offset);
	    GL11.glEnd();

	    // Bottom Face
	    GL11.glBegin(GL11.GL_QUADS);
	        GL11.glTexCoord2f(1f, 0f);
	        GL11.glVertex3f(-player.getPosX() - offset, -player.getPosY() + offset, -player.getPosZ() - offset);
	        GL11.glTexCoord2f(1f, 1f);
	        GL11.glVertex3f(-player.getPosX() - offset, -player.getPosY() + offset, -player.getPosZ() + offset);
	        GL11.glTexCoord2f(0f, 1f);
	        GL11.glVertex3f(-player.getPosX() + offset, -player.getPosY() + offset, -player.getPosZ() + offset);
	        GL11.glTexCoord2f(0f, 0f);
	        GL11.glVertex3f(-player.getPosX() + offset, -player.getPosY() + offset, -player.getPosZ() - offset);
	    GL11.glEnd();


	    // Right face
	    GL11.glBegin(GL11.GL_QUADS);
	        GL11.glTexCoord2f(0.0f, 0.0f);
	        GL11.glVertex3f(-player.getPosX() - offset, -player.getPosY() - offset, -player.getPosZ() - offset);
	        GL11.glTexCoord2f(1f, 0.0f);
	        GL11.glVertex3f(-player.getPosX() - offset, -player.getPosY() - offset, -player.getPosZ() + offset);
	        GL11.glTexCoord2f(1f, 1f);
	        GL11.glVertex3f(-player.getPosX() - offset, -player.getPosY() + offset, -player.getPosZ() + offset);
	        GL11.glTexCoord2f(0.0f, 1f);
	        GL11.glVertex3f(-player.getPosX() - offset, -player.getPosY() + offset, -player.getPosZ() - offset);
	    GL11.glEnd();

	    // Left Face
	    GL11.glBegin(GL11.GL_QUADS);
	        GL11.glTexCoord2f(1f, 0.0f);
	        GL11.glVertex3f(-player.getPosX() + offset, -player.getPosY() - offset, -player.getPosZ() - offset);
	        GL11.glTexCoord2f(1f, 1f);
	        GL11.glVertex3f(-player.getPosX() + offset, -player.getPosY() + offset, -player.getPosZ() - offset);
	        GL11.glTexCoord2f(0.0f, 1f);
	        GL11.glVertex3f(-player.getPosX() + offset, -player.getPosY() + offset, -player.getPosZ() + offset);
	        GL11.glTexCoord2f(0.0f, 0.0f);
	        GL11.glVertex3f(-player.getPosX() + offset, -player.getPosY() - offset, -player.getPosZ() + offset);
	    GL11.glEnd();
	}

	/** Sets OpenGL up to render 2D */
	public void setup2D() {
		glDisable(GL_LIGHTING);
		glDisable(GL_DEPTH_TEST);
		glMatrixMode(GL_PROJECTION);
		glPushMatrix();
		glLoadIdentity();
		glOrtho(0, syringe.screenWidth, 0, syringe.screenHeight, -1, 1);
		glMatrixMode(GL_MODELVIEW);
		glEnable(GL_TEXTURE_2D);
		glBindTexture(GL_TEXTURE_2D, 0);
		glPushMatrix();
		glLoadIdentity();
	}

	/** Sets OpenGL up to render 3D */
	public void setup3D() {
		glMatrixMode(GL_PROJECTION);
		glPopMatrix();
		glMatrixMode(GL_MODELVIEW);
		glPopMatrix();
		glEnable(GL_DEPTH_TEST);
		glEnable(GL_LIGHTING);
		glEnable(GL_COLOR_MATERIAL);
        glColorMaterial(GL_FRONT, GL_DIFFUSE);
		glEnable(GL_TEXTURE_2D);
	}

	/**
	 * Register a renderer
	 * @param renderer
	 */
	public void registerRenderer(Object renderer) {
		if(renderer instanceof IModelRenderer) {
			modelRenderers.add((IModelRenderer) renderer);
		}
	}

	/**
	 * Render using bit flags
	 * 1 - Entities
	 * 
	 * @param flag - The bit flags
	 */
	public void render(int flag) {
		if((flag & 1) == 1) {
			for(Entity entity : syringe.world.entities) {
				entity.getRenderer().setEntity(entity);
				entity.getRenderer().renderEntity();
			}
		}
	}
	
	/** Render all the models */
	public void renderAll() {
		for(IModelRenderer renderer : modelRenderers) {
			renderer.renderModel();
		}
	}

	/** Dispose every model */
	public void cleanUp() {
		for(IModelRenderer renderer : modelRenderers) {
			renderer.disposeModel();
		}
	}

	/** 
	 * Register a {@link IModelRenderer} 
	 * 
	 * @param renderer - The {@link IModelRenderer} to be registered
	 */
	public void registerRenderer(IModelRenderer renderer) {
		modelRenderers.add(renderer);
	}

	/** 
	 * Register a {@link IEntityRenderer} 
	 * 
	 * @param renderer - The {@link IEntityRenderer} to be registered
	 */
	public void registerEntityRenderer(Entity entity, IEntityRenderer renderer) {
		entityRenderers.put(entity, renderer);
		modelRenderers.add(renderer);
	}
}
