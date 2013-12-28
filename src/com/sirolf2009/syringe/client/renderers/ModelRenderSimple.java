package com.sirolf2009.syringe.client.renderers;

import static org.lwjgl.opengl.GL11.glDeleteLists;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;

import org.lwjgl.opengl.Display;

import com.sirolf2009.syringe.client.models.Model;
import com.sirolf2009.syringe.parsers.ParserOBJ;

/**
 * The ModelRenderSimple Class
 * Renders a model
 * 
 * @author sirolf2009
 *
 */
public class ModelRenderSimple implements IModelRenderer {

	/** The OpenGL display list */
	public int modelDisplayList = 0;
	/** The {@link Model} */
	public Model model = null;
	/** The location of the model */
	public String modelLocation = "";

	/**
	 * The constructor
	 * 
	 * @param modelLocation - The location of the model
	 */
	public ModelRenderSimple(String modelLocation) {
		this.modelLocation = modelLocation;
		loadModel();
	}

	@Override
	public void loadModel() {
		try {
			model = ParserOBJ.loadModel(new File(getClass().getClassLoader().getResource(modelLocation).toURI()));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			Display.destroy();
			System.exit(1);
		} catch (IOException e) {
			System.err.println("Something went wrong with loading "+modelLocation);
			e.printStackTrace();
			Display.destroy();
			System.exit(1);
		} catch (URISyntaxException e) {
			System.err.println("Something went wrong with loading "+modelLocation);
			e.printStackTrace();
			Display.destroy();
			System.exit(1);
		}
	}

	@Override
	public void renderModel() {
		model.openGLDrawTextured();
	}

	@Override
	public void disposeModel() {
		glDeleteLists(modelDisplayList, 1);
	}

	@Override
	public Model getModel() {
		return model;
	}
	
	public boolean intersects(ModelRenderSimple other) {
		if (getModel().bottompoint < other.getModel().bottompoint || 
				getModel().toppoint < other.getModel().toppoint || 
				getModel().leftpoint > other.getModel().leftpoint || 
				getModel().rightpoint > other.getModel().rightpoint ||
				getModel().nearpoint > other.getModel().nearpoint || 
				getModel().farpoint > other.getModel().farpoint) {
			return false;
		}
		return true;
	}

}
