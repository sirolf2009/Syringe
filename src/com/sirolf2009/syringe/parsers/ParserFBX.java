package com.sirolf2009.syringe.parsers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import com.sirolf2009.syringe.Syringe;
import com.sirolf2009.syringe.client.models.Mesh;
import com.sirolf2009.syringe.client.models.ModelFBX;
import com.sirolf2009.syringe.client.models.fbx.Channel;
import com.sirolf2009.syringe.client.models.fbx.Limb;
import com.sirolf2009.syringe.client.models.fbx.Take;
import com.sirolf2009.syringe.util.Node;

public class ParserFBX implements IParser {

	private BufferedReader reader;
	private Node root;

	public ModelFBX parse(String fileName) {
		if(Syringe.modelStore.models.get(fileName) != null) {
			return (ModelFBX) Syringe.modelStore.models.get(fileName);
		}
		try {
			root = new Node("root");
			File file = new File(getClass().getClassLoader().getResource(fileName).toURI());
			reader = new BufferedReader(new FileReader(file));
			String line;
			while((line = reader.readLine()) != null) {
				line = formatLine(line);
				if(line.startsWith(";")) {
					continue;
				} else if(line.contains("{")) {
					root.addNode(parseNode(line, root));
				} else if(line.contains(":")) {
					String[] property = parseProperty(line);
					root.addProperty(property[0], property[1]);
				}
			}
			ModelFBX model = new ModelFBX();
			for(Node node : root.getNode("Objects").getNodes("Model")) {
				parseObject(node, model);
			}
			for(Node node : root.getNode("Objects").getNodes("Deformer")) {
				parseObject(node, model);
			}
			Map<String, Texture> textures = new HashMap<String, Texture>();
			for(Node node : root.getNode("Objects").getNodes("Texture")) {
				String name = node.getNodeProperties().get(0);
				textures.put(name, TextureLoader.getTexture("png", new FileInputStream(new File(ParserOBJ.class.getClassLoader().getResource("models/"+node.getPrettyProperty("RelativeFilename")).toURI()))));
			}
			for(String[] array : root.getNode("Connections").getMultiProperties()) {
				if(array[3].startsWith("Model::")) {
					if(array[2].startsWith("Texture::")) {
						model.getMeshFromName(array[3]).setTexture(textures.get(array[2]));
					}
				} else if(array[2].startsWith("SubDeformer::")) {
					String[] array2 = array[2].split(" ");
					model.getLimb("Model::"+array2[2]).setMesh(model.getMeshFromName("Model::"+array2[1]));
				}
			}
			for(Node take : root.getNode("Takes").getNodes("Take")) {
				String name = take.getNodeProperties().get(0);
				Take take2 = parseAnimation(take, model);
				take2.getModel().
				//TODO limb should extend mesh???
			}
			model.setFrameRate(Integer.parseInt(root.getNode("Version5").getNode("Settings").getPrettyProperty("FrameRate")));
			for(Limb limb : model.getArmature().values()) {
				limb.generateAnimation();
			}
			Syringe.modelStore.models.put(fileName, model);
			Syringe.modelStore.modelsAnimated.put(fileName, model);
			return model;
		} catch (URISyntaxException | IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public Node parseNode(String currentLine, Node parent) throws IOException {
		String[] nodeSpecs = currentLine.replace("\"", "").replace(" {", "").split(": ", 2);
		String type = nodeSpecs[0];
		String[] nodeProperties = nodeSpecs[1].split(", ");
		nodeProperties[nodeProperties.length-1].replace("{", "");
		Node node = new Node(type, parent, nodeProperties);
		String line;
		while((line = reader.readLine()) != null) {
			line = formatLine(line);
			if(line.startsWith(";")) {
				continue;
			} else if(line.contains("{")) {
				node.addNode(parseNode(line, node));
			} else if(line.contains(":")) {
				if(line.split(": ")[0].equals("Connect") && node.getType().equals("Connections")) {
					String name = line.split(":")[0];
					if(line.split(": ").length > 1) {
						String[] properties = line.replace("\"", "").split(": ")[1].split(", ");
						String[] property = new String[properties.length+1];
						property[0] = name;
						for(int i = 0; i < property.length-1; i++) {
							property[i+1] = properties[i];
						}
						node.addMultiProperty(property);
					}
				} else {
					String[] property = parseProperty(line);
					node.addProperty(property[0], property[1]);
				}
			} else if(line.contains("}")) {
				break;
			} else {
				System.err.println("Unknown line "+line);
			}
		}
		return node;
	}

	public String[] parseProperty(String currentLine) throws IOException {
		String[] property = currentLine.split(":", 2);
		reader.mark(2550);
		while((currentLine = reader.readLine()) != null) {
			currentLine = formatLine(currentLine);
			if(currentLine.contains(":") || currentLine.contains("}")) {
				reader.reset();
				break;
			} else {
				reader.mark(2550);
				property[1] = property[1] + currentLine;
			}
		}
		return property;
	}

	public void parseObject(Node object, ModelFBX model) throws FileNotFoundException, IOException, URISyntaxException {
		String name = object.getNodeProperties().get(0);
		if(object.getNodeProperties().contains("Mesh")) {
			System.out.println("adding mesh "+name);
			model.meshes.add(new Mesh(name, parseMesh(object)));
		} else if(object.getNodeProperties().contains("Limb")) {
			model.addLimb(new Limb(name));
		} else if(object.getNodeProperties().contains("Cluster")) {
			parseCluster(object, model);
		} else {
			System.err.println("unknown object: "+object);
		}
	}

	private int parseMesh(Node mesh) {
		String[] vertices = mesh.getProperty("Vertices").split(",");
		String[] normals = mesh.getNode("LayerElementNormal").getProperty("Normals").split(",");
		String[] faces = mesh.getProperty("PolygonVertexIndex").split(",");

		String[] uv = null;
		String[] uvIndex = null;
		if(mesh.getNodes("LayerElementUV") != null) {
			uv = mesh.getNode("LayerElementUV").getProperty("UV").split(",");
			uvIndex = mesh.getNode("LayerElementUV").getProperty("UVIndex").split(",");
		}

		int list = GL11.glGenLists(1);
		GL11.glNewList(list, GL11.GL_COMPILE);

		int polygons = 3;
		if(polygons == 3) {
			GL11.glBegin(GL11.GL_TRIANGLES);
		} else if(polygons == 3) {
			GL11.glBegin(GL11.GL_QUADS);
		} else {
			GL11.glBegin(GL11.GL_POLYGON);
		}

		for(int i = 0; i < faces.length; i++) {
			int index = Integer.parseInt(faces[i].replace(" ", ""));
			if(index < 0) {
				index *= -1;
				index--;
			}

			if(uv != null && uvIndex != null) {
				int texIndex = Integer.parseInt(uvIndex[i].trim());

				float texCoord1 = Float.parseFloat(uv[texIndex*2]);
				float texCoord2 = Float.parseFloat(uv[texIndex*2+1]);
				GL11.glTexCoord3f(texCoord1, 1f-texCoord2, 0);
			}

			float normX = Float.parseFloat(normals[i*3]);
			float normY = Float.parseFloat(normals[i*3+1]);
			float normZ = Float.parseFloat(normals[i*3+2]);
			GL11.glNormal3f(normX, normY, normZ);

			float vertX = Float.parseFloat(vertices[index*3]);
			float vertY = Float.parseFloat(vertices[index*3+1]);
			float vertZ = Float.parseFloat(vertices[index*3+2]);
			GL11.glVertex3f(vertX, vertY, vertZ);
		}
		GL11.glEnd();
		GL11.glEndList();
		return list;
	}

	private void parseCluster(Node object, ModelFBX model) {
		String meshName = object.getNodeProperties().get(0).split(" ")[1];
		String limbName = object.getNodeProperties().get(0).split(" ")[2];
		Limb limb = model.getLimb("Model::"+limbName);
		Mesh mesh = model.getMeshFromName(meshName);
		limb.setMesh(mesh);
		if(object.getPrettyProperty("Indexes").isEmpty()) {
			return;
		}
		String[] indexes = object.getPrettyProperty("Indexes").split(",");
		String[] weights = object.getPrettyProperty("Weights").split(",");
		for(int i = 0; i < indexes.length; i++) {
			int index = Integer.parseInt(indexes[i]);
			double weight = Double.parseDouble(weights[i]);
			limb.addVertex(index, weight);
		}
	}

	public Take parseAnimation(Node takeNode, ModelFBX model) {
		Take take = new Take(takeNode.getNodeProperties().get(0));
		for(Node object : takeNode.getNodes("Model")) {
			take.addChannel(parseChannel(object.getNode("Channel"), null));
			take.setModel(model.getMeshFromName(object.getNodeProperties().get(0)));
		}
		return take;
	}

	public Channel parseChannel(Node node, Channel parent) {
		Channel channel = null;
		String type = node.getNodeProperties().get(0);
		if(node.getNodes("Channel") == null || node.getNodes("Channel").size() == 0) {
			int keyCount = Integer.parseInt(node.getPrettyProperty("KeyCount"));
			double defaultKey = Double.parseDouble(node.getPrettyProperty("Default"));
			String[] keyArray = node.getPrettyProperty("Key").split(",");
			List<Double> keys = new ArrayList<Double>();
			for(int i = 0; i < keyArray.length/3; i++) {
				keys.add(Double.parseDouble(keyArray[i*3+1]));
			}
			channel = new Channel(type, parent, keyCount, defaultKey, keys);
		} else {
			channel = new Channel(type, parent);
			for(Node child : node.getNodes("Channel")) {
				channel.addChild(parseChannel(child, channel));
			}
		}
		return channel;
	}

	public String formatLine(String line) {
		while(true) {
			if(line.startsWith("\t")) {
				line = line.replaceFirst("\t", "");
			} else if(line.startsWith(" ")) {
				line = line.replaceFirst(" ", "");
			} else {
				break;
			}
		}
		return line;
	}
}
