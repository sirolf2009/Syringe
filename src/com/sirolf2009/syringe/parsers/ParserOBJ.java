package com.sirolf2009.syringe.parsers;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import javax.imageio.ImageIO;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import com.sirolf2009.syringe.client.models.AABB;
import com.sirolf2009.syringe.client.models.Model;

/**
 * The parserOBJ Class
 * Parses OBJ files and creates {@link Model}
 * 
 * @author sirolf2009
 *
 */
public class ParserOBJ {

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
	 * @return The parsed {@link Model}
	 * @throws IOException
	 */
	public static Model loadModel(File file) throws IOException {
		Model model = loadobject(file);
		model.numpolys = faces.size();
		vertexsets.clear();
		vertexsetsnorms.clear();
		vertexsetstexs.clear();
		faces.clear();
		facestexs.clear();
		facesnorms.clear();
		return model;
	}

	/**
	 * Parse an OBJ file
	 * 
	 * @param file - The OBJ file
	 * @return The parsed {@link Model}
	 */
	@SuppressWarnings("unchecked")
	private static Model loadobject(File file) {

		Model model = new Model();
		int linecounter = 0;
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String newline = null;
			boolean firstpass = true;
			String currentMaterial = "default";
			Map<String, Integer> lists = new HashMap<String, Integer>();

			while (((newline = reader.readLine()) != null)) {
				linecounter++;
				newline = newline.trim();
				if (newline.length() > 0) {
					if(newline.startsWith("mtllib")) {
						String path = (file.getParentFile().getPath().replace(System.getProperty("user.dir")+"\\bin\\", ""))+"\\"+newline.split(" ")[1];
						parseMTL(new File(ParserOBJ.class.getClassLoader().getResource(path).toURI()), model);
					}
					if (newline.charAt(0) == 'v' && newline.charAt(1) == ' ') {
						float coords[] = new float[4];
						newline = newline.substring(2, newline.length());
						StringTokenizer st = new StringTokenizer(newline, " ");
						for(int i = 0; st.hasMoreTokens(); i++) {
							String token = st.nextToken();
							if(!token.contains("#")) {
								coords[i] = Float.parseFloat(token);
							}
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
						newline = newline.substring(2, newline.length());
						StringTokenizer st = new StringTokenizer(newline, " ");
						int count = st.countTokens();
						int v[] = new int[count];
						int vt[] = new int[count];
						int vn[] = new int[count];
						for(int i = 0; i < count; i++){
							char chars[] = st.nextToken().toCharArray();
							StringBuffer sb = new StringBuffer();
							char lc = 'x';
							for(int k = 0; k < chars.length; k++){
								if(chars[k] == '/' && lc == '/')
									sb.append('0');
								lc = chars[k];
								sb.append(lc);
							}
							StringTokenizer st2 = new StringTokenizer
									(sb.toString(), "/");
							int num = st2.countTokens();
							v[i] = Integer.parseInt(st2.nextToken());
							if(num > 1)
								vt[i] = Integer.parseInt(st2.nextToken());
							else
								vt[i] = 0;
							if(num > 2)
								vn[i] = Integer.parseInt(st2.nextToken());
							else
								vn[i] = 0;
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
							lists.put(currentMaterial, -1);
						}
					}
				}
			}
			lists.put(currentMaterial, openGLDrawToList(model));
			model.AABB = AABB.createAABBFromModel(model);
			reader.close();
			model.lists = lists;
			model.vertexsets = (ArrayList<float[]>) vertexsets.clone();
			model.vertexsetsnorms = (ArrayList<float[]>) vertexsetsnorms.clone();
			model.vertexsetstexs = (ArrayList<float[]>) vertexsetsnorms.clone();
			model.faces = (ArrayList<int[]>) faces.clone();
			model.facesnorms = (ArrayList<int[]>) facesnorms.clone();
			model.facestexs = (ArrayList<int[]>) facestexs.clone();
			return model;
		} catch (IOException e) {
			System.err.println("Failed to read file: " + file.toString());
			e.printStackTrace();
			System.exit(1);			
		} catch (NumberFormatException e) {
			System.err.println("Malformed OBJ (on line " + linecounter + "): " + file.toString() + "\r \r" + e.getMessage());
			e.printStackTrace();
			System.exit(1);
		} catch (URISyntaxException e) {
			System.err.println("Could not find MTL lib from: " + file.toString());
			e.printStackTrace();
		}
		return null;
	}

	/** 
	 * Parse an MTL file
	 * 
	 * @param file - The OBJ file
	 * @param model - The model the MTL will be applied to
	 */
	private static void parseMTL(File file, Model model) {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			String newline;
			Map<String, Texture> textures = new HashMap<String, Texture>();
			String currentTexture = "default";
			textures.put(currentTexture, TextureLoader.getTexture("png", new FileInputStream(new File(ParserOBJ.class.getClassLoader().getResource("img/MissingTexture.png").toURI()))));
			while (((newline = reader.readLine()) != null)) {
				if(newline.startsWith("newmtl")) {
					currentTexture = newline.split("\\s+")[1];
					textures.put(currentTexture, TextureLoader.getTexture("png", new FileInputStream(new File(ParserOBJ.class.getClassLoader().getResource("img/MissingTexture.png").toURI()))));
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

	private static int openGLDrawToList(Model model) {
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

				float tempx = ((float[])vertexsets.get(tempfaces[w] - 1))[0];
				float tempy = ((float[])vertexsets.get(tempfaces[w] - 1))[1];
				float tempz = ((float[])vertexsets.get(tempfaces[w] - 1))[2];
				GL11.glVertex3f(tempx,tempy,tempz);
			}

			GL11.glEnd();
		}
		GL11.glEndList();
		return objectlist;
	}
}