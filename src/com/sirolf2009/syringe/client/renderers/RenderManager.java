package com.sirolf2009.syringe.client.renderers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import static org.lwjgl.opengl.GL11.*;

import com.sirolf2009.syringe.Syringe;
import com.sirolf2009.syringe.world.entity.Entity;

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

	/**
	 * The constructor
	 * 
	 * @param syringe - The main syringe thread
	 */
	public RenderManager(Syringe syringe) {
		this.syringe = syringe;
	}

	/** draws the HUD */
	public void drawGUI() {
		setup2D();
		glTranslated(Display.getWidth()/2, Display.getHeight()/2, 0);
		glBegin(GL_LINES);
		glVertex2f(0.0f, 100f);
		glVertex2f(0.0f, -100f);
		glVertex2f(-100, 0.0f);
		glVertex2f(100, 0.0f);
		glEnd();
		SimpleText.drawString("X: "+syringe.camera.x(), -syringe.screenWidth/2, syringe.screenHeight/2-10);
		SimpleText.drawString("Y: "+syringe.camera.y(), -syringe.screenWidth/2, syringe.screenHeight/2-20);
		SimpleText.drawString("Z: "+syringe.camera.z(), -syringe.screenWidth/2, syringe.screenHeight/2-30);
		SimpleText.drawString("Pitch: "+syringe.camera.pitch(), -syringe.screenWidth/2+100, syringe.screenHeight/2-10);
		SimpleText.drawString("Roll: "+syringe.camera.roll(), -syringe.screenWidth/2+100, syringe.screenHeight/2-20);
		SimpleText.drawString("Yaw: "+syringe.camera.yaw(), -syringe.screenWidth/2+100, syringe.screenHeight/2-30);
		setup3D();
	}

	@Deprecated
	public void drawSpecial(int flag) {
		if((flag & 1) == 1) {
			switchTo2D();
			glScalef(100, 100, 100);
			glRotated(90, 0, 1, 0);
			glTranslated(Display.getWidth()/2, Display.getHeight()-10, 0);
			switchTo3D();
		}
		if((flag & 2) == 2) {
			switchTo2D();
			glTranslated(Display.getWidth()/2, Display.getHeight()/2, 0);
			glColor4f(0.2f,1.0f,0f,0.7f);
			glBegin(GL_LINES);
			glVertex2f(0.0f, 100f);
			glVertex2f(0.0f, -100f);
			glVertex2f(-100, 0.0f);
			glVertex2f(100, 0.0f);
			glEnd();
			switchTo3D();
		}
	}

	/** Sets OpenGL up to render 2D */
	public void setup2D() {
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glPushMatrix();
		GL11.glLoadIdentity();
		GL11.glOrtho(0, syringe.screenWidth, 0, syringe.screenHeight, -1, 1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		glBindTexture(GL_TEXTURE_2D, 0);
		GL11.glPushMatrix();
		GL11.glLoadIdentity();
	}

	/** Sets OpenGL up to render 3D */
	public void setup3D() {
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glPopMatrix();
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glPopMatrix();
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_LIGHTING);
		glEnable(GL_COLOR_MATERIAL);
        glColorMaterial(GL_FRONT, GL_DIFFUSE);
		glEnable(GL_TEXTURE_2D);
		
	}

	@Deprecated
	public void switchTo2D() {
		glMatrixMode(GL11.GL_PROJECTION);
		glPushMatrix();
		glLoadIdentity();
		glOrtho(0, Display.getWidth(), 0, Display.getHeight(), -1, 1);
		glMatrixMode(GL11.GL_MODELVIEW);
		glPushMatrix();
		glLoadIdentity();
		glDisable(GL11.GL_DEPTH_TEST);
		glDisable(GL11.GL_LIGHTING);
	}

	@Deprecated
	public void switchTo3D() {
		glEnable(GL_LIGHTING);
		glEnable(GL_DEPTH_TEST);
		glEnable(GL_TEXTURE_2D);
		glMatrixMode(GL_PROJECTION);
		glPopMatrix();
		glMatrixMode(GL_MODELVIEW);
		glPopMatrix();
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
