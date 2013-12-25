package com.sirolf2009.syringe;

import static org.lwjgl.opengl.GL11.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.TextureLoader;

import com.sirolf2009.syringe.client.renderers.EntityRenderer;
import com.sirolf2009.syringe.client.renderers.RenderManager;
import com.sirolf2009.syringe.parsers.ParserOBJ;
import com.sirolf2009.syringe.util.BufferTools;
import com.sirolf2009.syringe.util.Camera;
import com.sirolf2009.syringe.util.EulerCamera;
import com.sirolf2009.syringe.world.World;
import com.sirolf2009.syringe.world.entity.Entity;
import com.sirolf2009.syringe.world.entity.EntityTest;

/**
 * The main Syringe class
 * 
 * @author sirolf2009
 *
 */
public class Syringe {

	/** The camera used to move the world */
    public Camera camera;
    
    @Deprecated
    /** Not sure what this was used for :3 */
    private int modelDisplayList;
    
    /** The rendermanager */
    private RenderManager renderManager;
    
    /** The world */
    public World world;
    
    /** The screen's width */
    public final int screenWidth = 800;
    /** The screen's height */
    public final int screenHeight = 600;

    /** The constructor */
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

    /** Main init */
    public void init() {
    	renderManager = new RenderManager(this);
    	camera = new EulerCamera.Builder().setAspectRatio((float) Display.getWidth() / Display.getHeight())
                .setRotation(0.0f, 0.0f, 0.0f).setPosition(2.0F, 2.0f, 0.0f).setFieldOfView(80).build();
        camera.applyOptimalStates();
        camera.applyPerspectiveMatrix();
        world = new World();
        
        EntityTest test = new EntityTest(world, new EntityRenderer("models/Human.obj"));
        test.setPosY(1);
        world.addEntity(test);
        
        glShadeModel(GL_SMOOTH );
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_LIGHTING);
        glLightModel(GL_LIGHT_MODEL_AMBIENT, BufferTools.asFlippedFloatBuffer(new float[]{0.5f, 0.5f, 0.5f, 1f}));
        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);
        glEnable(GL_COLOR_MATERIAL);
        glColorMaterial(GL_FRONT, GL_DIFFUSE);
    }

    /** Processes camera movement */
    private void checkInput() {
        camera.processMouse(1, 80, -80);
        camera.processKeyboard(16, 2, 8, 2);
        if (Mouse.isButtonDown(0)) {
            Mouse.setGrabbed(true);
        } else if (Mouse.isButtonDown(1)) {
            Mouse.setGrabbed(false);
        }
    }

    /** clean up */
    private void cleanUp() {
        glDeleteLists(modelDisplayList, 1);
        Display.destroy();
    }

    /** Clears the screen and renders the registered models */
    private void render() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glEnable(GL_TEXTURE_2D);
        glLoadIdentity();
        camera.applyTranslations();
        glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
        try {
			TextureLoader.getTexture("png", new FileInputStream(new File(ParserOBJ.class.getClassLoader().getResource("img/MissingTexture.png").toURI()))).bind();
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
		}
        world.groundModel.openGLDrawTextured();
        renderManager.render(1);
        //renderManager.drawGUI();
        //renderManager.drawSpecial(1 | 2);
    }

    /** Initializes the frame */
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
    
    /** Main method */
    public static void main(String[] args) {
        new Syringe();
    }
}