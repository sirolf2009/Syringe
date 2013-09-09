package com.sirolf2009.syringe;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import com.sirolf2009.syringe.client.renderers.ModelRenderSimple;
import com.sirolf2009.syringe.client.renderers.RenderManager;
import com.sirolf2009.syringe.util.BufferTools;
import com.sirolf2009.syringe.util.Camera;
import com.sirolf2009.syringe.util.EulerCamera;

public class Syringe {

    private Camera camera;
    private int modelDisplayList;
    private RenderManager renderManager;

    public Syringe() {
        initDisplay();
        init();
        while (!Display.isCloseRequested()) {
            render();
            checkInput();
            Display.update();
            Display.sync(60);
        }
        cleanUp();
        System.exit(0);
    }

    private void init() {
    	renderManager = new RenderManager();
    	renderManager.registerRenderer(new ModelRenderSimple("models/ak.obj"));
    	camera = new EulerCamera.Builder().setAspectRatio((float) Display.getWidth() / Display.getHeight())
                .setRotation(0.0f, 0.0f, 0.0f).setPosition(2.0F, 0.0f, 0.0f).setFieldOfView(80).build();
        camera.applyOptimalStates();
        camera.applyPerspectiveMatrix();
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
        renderManager.renderAll();
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