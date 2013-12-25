package com.sirolf2009.syringe;

import static org.lwjgl.opengl.GL11.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.LWJGLException;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

import com.sirolf2009.syringe.client.renderers.EntityRenderer;
import com.sirolf2009.syringe.client.renderers.EntityRendererAnimated;
import com.sirolf2009.syringe.client.renderers.RenderManager;
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

	Entity entity;

	/** The constructor */
	public Syringe() {
		try {
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
		} catch (IOException | URISyntaxException | LWJGLException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	/** Main init */
	public void init() {
		renderManager = new RenderManager(this);
		camera = new EulerCamera.Builder().setAspectRatio((float) Display.getWidth() / Display.getHeight())
				.setRotation(0.0f, 0.0f, 0.0f).setPosition(2.0F, 2.0f, 0.0f).setFieldOfView(80).build();
		camera.applyOptimalStates();
		camera.applyPerspectiveMatrix();
		world = new World();

		entity = new EntityTest(world, new EntityRendererAnimated("models/zombie/", 60));
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
		GL11.glCallList(world.groundList);
		renderManager.render(1);
		renderManager.drawGUI();
		//renderManager.drawSpecial(1 | 2);
	}

	/** Initializes the frame 
	 * @throws URISyntaxException 
	 * @throws IOException 
	 * @throws LWJGLException */
	private void initDisplay() throws IOException, URISyntaxException, LWJGLException {
		Display.setDisplayMode(new DisplayMode(screenWidth, screenHeight));
		Display.setVSyncEnabled(true);
		Display.setTitle("Syringe");
		String OS = System.getProperty("os.name");
		ByteBuffer[] list = new ByteBuffer[OS.contains("win") ? 2 : 1]; //Windows and Linux support, aka FUCK MAC >:(
		list[0] = convertToByteBuffer(ImageIO.read(new File(getClass().getClassLoader().getResource("icon32.png").toURI())));
		if(OS.contains("win")) {
			list[1] = convertToByteBuffer(ImageIO.read(new File(getClass().getClassLoader().getResource("icon16.png").toURI())));
		}
		Display.setIcon(list);
		Display.create();
	}

	private static ByteBuffer convertToByteBuffer(BufferedImage image) {
		byte[] buffer = new byte[image.getWidth() * image.getHeight() * 4];
		int counter = 0;
		for (int i = 0; i < image.getHeight(); i++) {
			for (int j = 0; j < image.getWidth(); j++) {
				int colorSpace = image.getRGB(j, i);
				buffer[counter + 0] = (byte) ((colorSpace << 8) >> 24);
				buffer[counter + 1] = (byte) ((colorSpace << 16) >> 24);
				buffer[counter + 2] = (byte) ((colorSpace << 24) >> 24);
				buffer[counter + 3] = (byte) (colorSpace >> 24);
				counter += 4;
			}
		}
		return ByteBuffer.wrap(buffer);
	}

	/** Main method */
	public static void main(String[] args) {
		new Syringe();
	}
}