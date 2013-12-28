package com.sirolf2009.syringe.client.renderers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.Display;

import com.sirolf2009.syringe.client.models.Model;
import com.sirolf2009.syringe.parsers.ParserOBJ;

public class ModelRenderAnimated implements IModelRenderer {

	public List<Model> animation;
	public int currentFrame;
	public String animationLocation = "";
	public int frames;

	public ModelRenderAnimated(String animationLocation, int frames) {
		this.animationLocation = animationLocation;
		this.frames = frames;
		animation = new ArrayList<Model>();
		loadModel();
	}

	@Override
	public void loadModel() {
		try {
			System.out.println("loading anim");
			File folder = new File(getClass().getClassLoader().getResource(animationLocation).toURI());
			for(File frame : folder.listFiles()) {
				if(frame.getName().endsWith(".obj")) {
					animation.add(parseFrame(animationLocation+frame.getName()));
				}
			}
			System.out.println("done loading anim");
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}

	private Model parseFrame(String frame) {
		try {
			return ParserOBJ.loadModel(new File(getClass().getClassLoader().getResource(frame).toURI()));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			Display.destroy();
			System.exit(1);
		} catch (IOException e) {
			System.err.println("Something went wrong with loading "+frame);
			e.printStackTrace();
			Display.destroy();
			System.exit(1);
		} catch (URISyntaxException e) {
			System.err.println("Something went wrong with loading "+frame);
			e.printStackTrace();
			Display.destroy();
			System.exit(1);
		}
		return null;
	}

	@Override
	public void renderModel() {
		animation.get(currentFrame).openGLDrawTextured();
		currentFrame++;
		if(currentFrame > frames) {
			currentFrame = 0;
		}
	}

	@Override
	public void disposeModel() {
	}

	@Override
	public Model getModel() {
		return animation.get(currentFrame);
	}

}
