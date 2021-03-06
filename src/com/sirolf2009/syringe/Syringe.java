package com.sirolf2009.syringe;

import static org.lwjgl.opengl.GL11.GL_BACK;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_COLOR_MATERIAL;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_DIFFUSE;
import static org.lwjgl.opengl.GL11.GL_FILL;
import static org.lwjgl.opengl.GL11.GL_FRONT;
import static org.lwjgl.opengl.GL11.GL_FRONT_AND_BACK;
import static org.lwjgl.opengl.GL11.GL_LIGHTING;
import static org.lwjgl.opengl.GL11.GL_LIGHT_MODEL_AMBIENT;
import static org.lwjgl.opengl.GL11.GL_SMOOTH;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glColorMaterial;
import static org.lwjgl.opengl.GL11.glCullFace;
import static org.lwjgl.opengl.GL11.glDeleteLists;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glLightModel;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glPolygonMode;
import static org.lwjgl.opengl.GL11.glShadeModel;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

import com.sirolf2009.syringe.client.models.IModelAnimated;
import com.sirolf2009.syringe.client.models.Model;
import com.sirolf2009.syringe.client.models.ModelFBX;
import com.sirolf2009.syringe.client.models.ModelStore;
import com.sirolf2009.syringe.client.renderers.EntityRenderer;
import com.sirolf2009.syringe.client.renderers.RenderManager;
import com.sirolf2009.syringe.parsers.ParserFBX;
import com.sirolf2009.syringe.util.BufferTools;
import com.sirolf2009.syringe.util.Camera;
import com.sirolf2009.syringe.util.EulerCamera;
import com.sirolf2009.syringe.world.World;
import com.sirolf2009.syringe.world.entity.Entity;
import com.sirolf2009.syringe.world.entity.EntityPlayer;
import com.sirolf2009.syringe.world.entity.EntityTest;

/**
 * The main Syringe class
 * 
 * @author sirolf2009
 *
 */
public class Syringe {

	/** The camera used to move the world */
	public static Camera camera;
	public static EntityPlayer player;

	@Deprecated
	/** Not sure what this was used for :3 */
	private int modelDisplayList;

	/** The rendermanager */
	public static RenderManager renderManager;
	public static ModelStore modelStore;

	/** The world */
	public World world;

	/** The screen's width */
	public final int screenWidth = 800;
	/** The screen's height */
	public final int screenHeight = 600;

	public int FPS;
	private int countingFPS;
	private long lastFPS;
	
	Model modelFBX, modelOBJ;

	/** The constructor */
	public Syringe() {
		try {
			long oldTime = System.currentTimeMillis();
			initDisplay();
			init();
			while (!Display.isCloseRequested()) {
				if ((Sys.getTime() * 1000) / Sys.getTimerResolution() - lastFPS > 1000) {
					FPS = countingFPS;
					countingFPS = 0;
					lastFPS += 1000;
				}
				countingFPS++;
				long deltaTime = System.currentTimeMillis() - oldTime;
				world.update(deltaTime);
				oldTime = System.currentTimeMillis();
				checkInput();
				render(deltaTime);
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
		modelStore = new ModelStore();
		camera = new EulerCamera.Builder().setAspectRatio((float) Display.getWidth() / Display.getHeight())
				.setRotation(0.0f, 0.0f, 0.0f).setPosition(0.0F, 1.0f, 2.0f).setFieldOfView(80).build();
		camera.applyOptimalStates();
		camera.applyPerspectiveMatrix();
		world = new World();
		
		lastFPS = (Sys.getTime() * 1000) / Sys.getTimerResolution();

		//Entity entity = new EntityTest(world, new EntityRendererAnimated("models/zombie/", 60));
		//entity.setPosX(0.1F);
		//entity.setPosY(2.1F);
		//entity.setPosZ(0.1F);
		//world.addEntity(entity);
		for(int i = 0; i < 10; i++) {
			Entity entity2 = new EntityTest(world, new EntityRenderer("models/Human.obj"));
			world.addEntity(entity2);
			entity2.setPosY(.1F);
			entity2.setPosX(i);
		}
		EntityPlayer player = new EntityPlayer(world, new EntityRenderer("models/Human.obj"));
		player.setPosY(1);
		player.setPosX(10);
		world.addEntity(player);

		modelFBX = new ParserFBX().parse("models/human.fbx");

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
	private void render(long deltaTime) {		
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glEnable(GL_TEXTURE_2D);
		glLoadIdentity();
		glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
		camera.applyTranslations();
		for(IModelAnimated model : modelStore.modelsAnimated.values()) {
			model.updateCurrentKeyFrame(deltaTime);
		}
		world.groundModel.render();
		GL11.glPushMatrix();
		GL11.glTranslated(0, 2, 0);
		modelFBX.render();
		GL11.glRotated(180, 0, 0, 1);
		GL11.glPopMatrix();
		renderManager.render(1);
		renderManager.drawGUI();
		renderManager.drawSkybox();
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
		Display.setResizable(true);
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