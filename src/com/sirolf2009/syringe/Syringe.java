package com.sirolf2009.syringe;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

import com.sirolf2009.syringe.client.renderers.EntityRenderer;
import com.sirolf2009.syringe.client.renderers.RenderManager;
import com.sirolf2009.syringe.util.BufferTools;
import com.sirolf2009.syringe.util.Camera;
import com.sirolf2009.syringe.util.EulerCamera;
import com.sirolf2009.syringe.world.World;
import com.sirolf2009.syringe.world.entity.Entity;
import com.sirolf2009.syringe.world.entity.EntityTest;

public class Syringe {

    public Camera camera;
    private int modelDisplayList;
    private RenderManager renderManager;
    public World world;
    
    public final int screenWidth = 800;
    public final int screenHeight = 600;
    
    Entity entity;

    public Syringe() {
    	long oldTime = System.currentTimeMillis();
        initDisplay();
        init();
        while (!Display.isCloseRequested()) {
        	long newTime = System.currentTimeMillis();
        	world.update(newTime - oldTime);
        	oldTime = newTime;
            checkInput();
            render();
            Display.update();
            Display.sync(60);
        }
        cleanUp();
        System.exit(0);
    }

    private void init() {
    	renderManager = new RenderManager(this);
    	camera = new EulerCamera.Builder().setAspectRatio((float) Display.getWidth() / Display.getHeight())
                .setRotation(0.0f, 0.0f, 0.0f).setPosition(2.0F, 2.0f, 0.0f).setFieldOfView(80).build();
        camera.applyOptimalStates();
        camera.applyPerspectiveMatrix();
        world = new World();
        
        entity = new EntityTest(world, new EntityRenderer("models/ak.obj"));
        entity.setPosX(0.1F);
        entity.setPosY(2.1F);
        entity.setPosZ(0.1F);
        world.addEntity(entity);
        Entity entity2 = new EntityTest(world, new EntityRenderer("models/ak.obj"));
        world.addEntity(entity2);
        entity2.setPosY(2);
        
        glShadeModel(GL_SMOOTH );
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_LIGHTING);
        glLightModel(GL_LIGHT_MODEL_AMBIENT, BufferTools.asFlippedFloatBuffer(new float[]{0.5f, 0.5f, 0.5f, 1f}));
        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);
        glEnable(GL_COLOR_MATERIAL);
        glColorMaterial(GL_FRONT, GL_DIFFUSE);
    }

    private void checkInput() {
        camera.processMouse(1, 80, -80);
        camera.processKeyboard(16, 2, 2, 2);
        if (Mouse.isButtonDown(0)) {
            Mouse.setGrabbed(true);
        } else if (Mouse.isButtonDown(1)) {
            Mouse.setGrabbed(false);
        }
    }

    private void cleanUp() {
        glDeleteLists(modelDisplayList, 1);
        Display.destroy();
    }

    private void render() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glEnable(GL_TEXTURE_2D);
        glLoadIdentity();
        camera.applyTranslations();
        glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
        GL11.glScalef(10, 10, 10);
        GL11.glCallList(world.groundList);
        renderManager.render(1);
        renderManager.drawGUI();
        //renderManager.drawSpecial(1 | 2);
    }

    private void initDisplay() {
        try {
            Display.setDisplayMode(new DisplayMode(screenWidth, screenHeight));
            Display.setVSyncEnabled(true);
            Display.setTitle("Syringe");
            Display.create();
        } catch (LWJGLException e) {
            System.err.println("The display wasn't initialized correctly. :(");
            Display.destroy();
            System.exit(1);
        }
    }
    
    public static void main(String[] args) {
        new Syringe();
    }
}