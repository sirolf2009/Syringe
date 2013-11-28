package com.sirolf2009.syringe.client.models;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import com.sirolf2009.syringe.parsers.parserOBJ;

/**
 * The Model3D class
 * Used to store lists containing vertices
 * 
 * @author sirolf2009
 *
 */
public class Model3D {

	/** Vertex coords */
	public ArrayList<float[]> vertexsets = new ArrayList<float[]>();
	/** Vertex normal coords */
	public ArrayList<float[]> vertexsetsnorms = new ArrayList<float[]>();
	/** Vertex texture coords */
	public ArrayList<float[]> vertexsetstexs = new ArrayList<float[]>(); // Vertex Coordinates Textures
	/** Faces */
	public ArrayList<int[]> faces = new ArrayList<int[]>(); // Array of Faces (vertex sets)
	/** Face textures */
	public ArrayList<int[]> facestexs = new ArrayList<int[]>(); // Array of of Faces textures
	/** Face normals */
	public ArrayList<int[]> facesnorms = new ArrayList<int[]>(); // Array of Faces normals

	/** The OpenGL object list */
	public int objectlist;
	/** The amount of polygons */
	public int numpolys = 0;

	////Statisitcs for drawing ////
	/** The highest point of the model */
	public float toppoint = 0;
	/** The lowest point of the model */
	public float bottompoint = 0;
	/** The most left point of the model */
	public float leftpoint = 0;
	/** The most right point of the model */
	public float rightpoint = 0;
	/** The furthest point of the model */
	public float farpoint = 0;
	/** The nearest point of the model */
	public float nearpoint = 0;

	////Statisitcs for collision ////
	/** The bottom-left X coord of the model */
	public float posX1 = 0;
	/** The bottom-left Y coord of the model */
	public float posY1 = 0;
	/** The bottom-left Z coord of the model */
	public float posZ1 = 0;
	/** The top-right X coord of the model */
	public float posX2 = 0;
	/** The top-right Y coord of the model */
	public float posY2 = 0;
	/** The top-right Z coord of the model */
	public float posZ2 = 0;

	/** true if the model uses mtllib */
	public boolean isTextured;
	/** The Slick-2D texture */
	public Texture texture;

	public Map<String, Texture> textures = new HashMap<String, Texture>();
	public Map<String, Integer> lists = new HashMap<String, Integer>();

	/** The {@link AABB} of the model */
	public AABB AABB;

	/** Destroy all lists */
	public void cleanUp() {
		vertexsets.clear();
		vertexsetsnorms.clear();
		vertexsetstexs.clear();
		faces.clear();
		facestexs.clear();
		facesnorms.clear();
	}

	/** The width of the model */
	public float getXWidth() {
		float returnval = 0;
		returnval = rightpoint - leftpoint;
		return returnval;
	}

	/** The height of the model */
	public float getYHeight() {
		float returnval = 0;
		returnval = toppoint - bottompoint;
		return returnval;
	}

	/** The depth of the model */
	public float getZDepth() {
		float returnval = 0;
		returnval = nearpoint - farpoint;
		return returnval;
	}

	/** 
	 * The amount of polygons 
	 * 
	 * @return The amount of polygons
	 */
	public int numpolygons() {
		return numpolys;
	}

	/** Bind the texture and draw the model
	 *  Deprecated: Refer to {@link #openGLDrawTextured()} */
	@Deprecated
	public void openGLDraw() {
		texture.bind();
		GL11.glCallList(objectlist);
	}

	/** Bind the texture and draw the model */
	public void openGLDrawTextured() {
		for(String material : lists.keySet()) {
			if(textures.get(material) != null) {
				textures.get(material).bind();
			} else {
				System.err.println("Could not find texture for material: "+material);
				try {
					textures.put(material, TextureLoader.getTexture("png", new FileInputStream(new File(parserOBJ.class.getClassLoader().getResource("img/MissingTexture.png").toURI()))));
				} catch (IOException | URISyntaxException e) {
					e.printStackTrace();
				}
			}
			if(lists.get(material) != null) {
				GL11.glCallList(lists.get(material));
			} else {
				System.err.println("Could not find object list for material: "+material);
			}
		}
	}

}