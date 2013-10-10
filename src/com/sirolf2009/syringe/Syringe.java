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

    private Camera camera;
    private int modelDisplayList;
    private RenderManager renderManager;
    public World world;
    
    Entity entity;

    public Syringe() {
    	long oldTime = System.currentTimeMillis();
        initDisplay();
        init();
        while (!Display.isCloseRequested()) {
        	long newTime = System.currentTimeMillis();
        	entity.setPosX(2F);
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
                .setRotation(0.0f, 0.0f, 0.0f).setPosition(2.0F, 0.0f, 0.0f).setFieldOfView(80).build();
        camera.applyOptimalStates();
        camera.applyPerspectiveMatrix();
        world = new World();
        
        entity = new EntityTest(world, new EntityRenderer("models/ak.obj"));
        entity.setPosX(2);
        EntityRenderer renderer = new EntityRenderer("models/ak.obj");
        EntityTest.setRenderer(renderer);
        renderManager.registerEntityRenderer(entity, renderer);
        world.addEntity(entity);
        Entity entity2 = new EntityTest(world);
        world.addEntity(entity2);
        
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
    	entity.setPosX(1.0F);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glEnable(GL_TEXTURE_2D);
        glLoadIdentity();
        camera.applyTranslations();
        glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
        GL11.glScalef(10, 10, 10);
        renderManager.render(1);
        //renderManager.drawSpecial(1 | 2);
    }

    private void initDisplay() {
        try {
            Display.setDisplayMode(new DisplayMode(800, 600));
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