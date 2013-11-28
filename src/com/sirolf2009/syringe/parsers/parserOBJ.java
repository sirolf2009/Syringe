package com.sirolf2009.syringe.parsers;

import com.sirolf2009.syringe.client.models.AABB;
import com.sirolf2009.syringe.client.models.Model3D;

import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

/**
 * The parserOBJ Class
 * Parses OBJ files and creates {@link Model3D}
 * 
 * @author sirolf2009
 *
 */
public class parserOBJ {

	static ArrayList<float[]> vertexsets = new ArrayList<float[]>();
	static ArrayList<float[]> vertexsetsnorms = new ArrayList<float[]>();
	static ArrayList<float[]> vertexsetstexs = new ArrayList<float[]>();
	static ArrayList<int[]> faces = new ArrayList<int[]>();
	static ArrayList<int[]> facestexs = new ArrayList<int[]>();
	static ArrayList<int[]> facesnorms = new ArrayList<int[]>();
	
	/**
	 * Load a model
	 * 
	 * @param file - The OBJ file
	 * @return The parsed {@link Model3D}
	 * @throws IOException
	 */
	public static Model3D loadModel(File file) throws IOException {
		Model3D model = loadobject(file);
		//opengldrawtolist(model);
		//loadTexture(model, file);
		model.numpolys = model.faces.size();
		model.cleanUp();
		return model;
	}

	/**
	 * Parse an OBJ file
	 * 
	 * @param file - The OBJ file
	 * @return The parsed {@link Model3D}
	 */
	private static Model3D loadobject(File file) {

		Model3D model = new Model3D();
		int linecounter = 0;
		boolean textured = false;
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String newline = null;
			int line = 0;
			boolean firstpass = true;
			String currentMaterial = "default";
			Map<String, Integer> lists = new HashMap<String, Integer>();

			while (((newline = reader.readLine()) != null)) {
				linecounter++;
				newline = newline.trim();
				if (newline.length() > 0) {
					if(newline.startsWith("mtllib")) { //TODO load mtl lib
						textured = true;
					}
					if (newline.charAt(0) == 'v' && newline.charAt(1) == ' ') {
						float[] coords = new float[4];
						String[] coordstext = new String[4];
						coordstext = newline.split("\\s+");
						for (int i = 1;i < coordstext.length;i++) {
							coords[i-1] = Float.valueOf(coordstext[i]).floatValue();
						}
						//// check for farpoints ////
						if (firstpass) {
							model.rightpoint = coords[0];
							model.leftpoint = coords[0];
							model.toppoint = coords[1];
							model.bottompoint = coords[1];
							model.nearpoint = coords[2];
							model.farpoint = coords[2];
							model.posX1 = coords[0];
							model.posY1 = coords[1];
							model.posZ1 = coords[2];
							model.posX2 = coords[0];
							model.posY2 = coords[1];
							model.posZ2 = coords[2];
							firstpass = false;
						}
						if (coords[0] > model.rightpoint) {
							model.rightpoint = coords[0];
						}
						if (coords[0] < model.leftpoint) {
							model.leftpoint = coords[0];
						}
						if (coords[1] > model.toppoint) {
							model.toppoint = coords[1];
						}
						if (coords[1] < model.bottompoint) {
							model.bottompoint = coords[1];
						}
						if (coords[2] > model.nearpoint) {
							model.nearpoint = coords[2];
						}
						if (coords[2] < model.farpoint) {
							model.farpoint = coords[2];
						}
						if(coords[0] < model.posX1) {
							model.posX1 = coords[0];
						}
						if(coords[1] < model.posY1) {
							model.posY1 = coords[1];
						}
						if(coords[2] < model.posZ1) {
							model.posZ1 = coords[2];
						}
						if(coords[0] > model.posX2) {
							model.posX2 = coords[0];
						}
						if(coords[1] > model.posY2) {
							model.posY2 = coords[1];
						}
						if(coords[2] > model.posZ2) {
							model.posZ2 = coords[2];
						}
						vertexsets.add(coords);
					}
					if (newline.charAt(0) == 'v' && newline.charAt(1) == 't') {
						float[] coords = new float[4];
						String[] coordstext = new String[4];
						coordstext = newline.split("\\s+");
						for (int i = 1;i < coordstext.length;i++) {
							coords[i-1] = Float.valueOf(coordstext[i]).floatValue();
						}
						vertexsetstexs.add(coords);
					}
					if (newline.charAt(0) == 'v' && newline.charAt(1) == 'n') {
						float[] coords = new float[4];
						String[] coordstext = new String[4];
						coordstext = newline.split("\\s+");
						for (int i = 1;i < coordstext.length;i++) {
							coords[i-1] = Float.valueOf(coordstext[i]).floatValue();
						}
						vertexsetsnorms.add(coords);
					}
					if (newline.charAt(0) == 'f' && newline.charAt(1) == ' ') {
						String[] coordstext = newline.split("\\s+");
						int[] v = new int[coordstext.length - 1];
						int[] vt = new int[coordstext.length - 1];
						int[] vn = new int[coordstext.length - 1];

						for (int i = 1;i < coordstext.length;i++) {
							String fixstring = coordstext[i].replaceAll("//","/0/");
							String[] tempstring = fixstring.split("/");
							v[i-1] = Integer.valueOf(tempstring[0]).intValue();
							if (tempstring.length > 1) {
								vt[i-1] = Integer.valueOf(tempstring[1]).intValue();
							} else {
								vt[i-1] = 0;
							}
							if (tempstring.length > 2) {
								vn[i-1] = Integer.valueOf(tempstring[2]).intValue();
							} else {
								vn[i-1] = 0;
							}
						}
						faces.add(v);
						facestexs.add(vt);
						facesnorms.add(vn);
					}
					if(newline.startsWith("usemtl")) {
						String[] pars = newline.split("\\s+");
						if(pars[1] != currentMaterial) {
							lists.put(currentMaterial, openGLDrawToList(model));
							currentMaterial = pars[1];
							System.out.println("Adding mtl "+currentMaterial+" for "+file);
							lists.put(currentMaterial, -1);
						}
					}
				}
				line++;
			}
			lists.put(currentMaterial, openGLDrawToList(model));
			model.cleanUp();
			model.AABB = AABB.createAABBFromModel(model);
			reader.close();
			model.lists = lists;
			if(textured) {
				parseMTL(file, model);
			}
			return model;
		} catch (IOException e) {
			System.out.println("Failed to read file: " + file.toString());
			System.exit(1);			
		} catch (NumberFormatException e) {
			System.out.println("Malformed OBJ (on line " + linecounter + "): " + file.toString() + "\r \r" + e.getMessage());
			System.exit(1);
		}
		return null;
	}

	/** 
	 * Parse an MTL file
	 * 
	 * @param file - The OBJ file
	 * @param model - The model the MTL will be applied to
	 */
	private static void parseMTL(File file, Model3D model) {
		File mtllib = new File(file.getPath().replace(".obj", ".mtl"));
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(mtllib));
			String newline;
			Map<String, Texture> textures = new HashMap<String, Texture>();
			String currentTexture = "default";
			System.out.println(parserOBJ.class.getClassLoader().getResource("img/MissingTexture.png"));
			textures.put(currentTexture, TextureLoader.getTexture("png", new FileInputStream(new File(parserOBJ.class.getClassLoader().getResource("img/MissingTexture.png").toURI()))));
			while (((newline = reader.readLine()) != null)) {
				if(newline.startsWith("newmtl")) {
					currentTexture = newline.split("\\s+")[1];
					textures.put(currentTexture, TextureLoader.getTexture("png", new FileInputStream(new File(parserOBJ.class.getClassLoader().getResource("img/MissingTexture.png").toURI()))));
					System.out.println("Creating mtl "+currentTexture);
				}
				if(newline.startsWith("Ka")) {
					float[] rgbArray = new float[3];
					String[] rgbText = new String[3];
					rgbText = newline.split("\\s+");
					for (int i = 1;i < rgbText.length;i++) {
						rgbArray[i-1] = Float.valueOf(rgbText[i]).floatValue();
					}
					int rgb = (int) rgbArray[0];
					rgb = rgb << 8;
					rgb |= (int) rgbArray[1];
					rgb = rgb << 8;
					rgb |= (int) rgbArray[2];
					BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
					image.setRGB(0, 0, rgb);
					ByteArrayOutputStream output = new ByteArrayOutputStream();
					ImageIO.write(image, "png", output);
					//textures.put(currentTexture, TextureLoader.getTexture("PNG", new ByteArrayInputStream(output.toByteArray())));
				}
				if(newline.startsWith("Kd")) {

				}
				if(newline.startsWith("Ks")) {

				}
				if(newline.startsWith("map_Kd")) {
					String texture = newline.split("\\s+")[1];
					System.out.println("loading texture "+file.getParent()+"/"+texture);
					textures.put(currentTexture, TextureLoader.getTexture("PNG", new FileInputStream(file.getParent()+"/"+texture)));
				}
			}
			reader.close();
			model.textures = textures;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}

	@Deprecated
	private static void opengldrawtolist(Model3D model) {
		model.objectlist = GL11.glGenLists(1);
		GL11.glNewList(model.objectlist,GL11.GL_COMPILE);
		for (int i=0;i<model.faces.size();i++) {
			int[] tempfaces = (int[])(model.faces.get(i));
			int[] tempfacesnorms = (int[])(model.facesnorms.get(i));
			int[] tempfacestexs = (int[])(model.facestexs.get(i));

			int polytype;
			if (tempfaces.length == 3) {
				polytype = GL11.GL_TRIANGLES;
			} else if (tempfaces.length == 4) {
				polytype = GL11.GL_QUADS;
			} else {
				polytype = GL11.GL_POLYGON;
			}
			GL11.glBegin(polytype);

			for (int w=0;w<tempfaces.length;w++) {
				if (tempfacesnorms[w] != 0) {
					float normtempx = ((float[])model.vertexsetsnorms.get(tempfacesnorms[w] - 1))[0];
					float normtempy = ((float[])model.vertexsetsnorms.get(tempfacesnorms[w] - 1))[1];
					float normtempz = ((float[])model.vertexsetsnorms.get(tempfacesnorms[w] - 1))[2];
					GL11.glNormal3f(normtempx, normtempy, normtempz);
				}

				if (tempfacestexs[w] != 0) {
					float textempx = ((float[])model.vertexsetstexs.get(tempfacestexs[w] - 1))[0];
					float textempy = ((float[])model.vertexsetstexs.get(tempfacestexs[w] - 1))[1];
					float textempz = ((float[])model.vertexsetstexs.get(tempfacestexs[w] - 1))[2];
					GL11.glTexCoord3f(textempx,1f-textempy,textempz);
				}

				float tempx = ((float[])model.vertexsets.get(tempfaces[w] - 1))[0];
				float tempy = ((float[])model.vertexsets.get(tempfaces[w] - 1))[1];
				float tempz = ((float[])model.vertexsets.get(tempfaces[w] - 1))[2];
				GL11.glVertex3f(tempx,tempy,tempz);
			}

			GL11.glEnd();
		}
		GL11.glEndList();
	}

	private static int openGLDrawToList(Model3D model) {
		int objectlist = GL11.glGenLists(1);
		GL11.glNewList(objectlist, GL11.GL_COMPILE);
		for (int i=0;i<faces.size();i++) {
			int[] tempfaces = (int[])(faces.get(i));
			int[] tempfacesnorms = (int[])(facesnorms.get(i));
			int[] tempfacestexs = (int[])(facestexs.get(i));

			int polytype;
			if (tempfaces.length == 3) {
				polytype = GL11.GL_TRIANGLES;
			} else if (tempfaces.length == 4) {
				polytype = GL11.GL_QUADS;
			} else {
				polytype = GL11.GL_POLYGON;
			}
			GL11.glBegin(polytype);

			for (int w=0;w<tempfaces.length;w++) {
				if (tempfacesnorms[w] != 0) {
					float normtempx = ((float[])vertexsetsnorms.get(tempfacesnorms[w] - 1))[0];
					float normtempy = ((float[])vertexsetsnorms.get(tempfacesnorms[w] - 1))[1];
					float normtempz = ((float[])vertexsetsnorms.get(tempfacesnorms[w] - 1))[2];
					GL11.glNormal3f(normtempx, normtempy, normtempz);
				}

				if (tempfacestexs[w] != 0) {
					float textempx = ((float[])vertexsetstexs.get(tempfacestexs[w] - 1))[0];
					float textempy = ((float[])vertexsetstexs.get(tempfacestexs[w] - 1))[1];
					float textempz = ((float[])vertexsetstexs.get(tempfacestexs[w] - 1))[2];
					GL11.glTexCoord3f(textempx,1f-textempy,textempz);
				}

				if(vertexsets.size() > 0) {
					float tempx = ((float[])vertexsets.get(tempfaces[w] - 1))[0];
					float tempy = ((float[])vertexsets.get(tempfaces[w] - 1))[1];
					float tempz = ((float[])vertexsets.get(tempfaces[w] - 1))[2];
					GL11.glVertex3f(tempx,tempy,tempz);
				}
			}

			GL11.glEnd();
		}
		GL11.glEndList();
		return objectlist;
	}

	//TODO MTL support
	/**
	 * Loads a PNG texture from an OBJ file
	 * 
	 * @param model - The {@link Model3D}
	 * @param file - The OBJ file
	 */
	private static void loadTexture(Model3D model, File file) {
		try {
			model.texture = TextureLoader.getTexture("png", new FileInputStream(new File(file.getPath().replace(".obj", ".png"))));
		} catch (FileNotFoundException e) {
			System.err.println("Could not find texture "+file+" for model "+model+".");
			e.printStackTrace();
			System.exit(1);
		} catch (IOException e) {
			System.err.println("Could not open texture "+file+" for model "+model+".");
			e.printStackTrace();
			System.exit(1);
		}
	}
}