package com.sirolf2009.syringe.parsers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;

import com.sirolf2009.syringe.client.models.Model;

public class ParserFBX implements IParser {

	private BufferedReader reader;
	private Node root;

	public Model parse(String fileName) {
		try {
			root = new Node("root");
			File file = new File(getClass().getClassLoader().getResource(fileName).toURI());
			reader = new BufferedReader(new FileReader(file));
			String line;
			while((line = reader.readLine()) != null) {
				line = line.trim();
				if(line.startsWith(";")) {
					continue;
				} else if(line.contains("{")) {
					root.addNode(parseNode(line, root));
				} else if(line.contains(":")) {
					String[] property = parseProperty(line);
					root.addProperty(property[0], property[1]);
				}
			}
			Model model = new Model();
			for(Node node : root.getNode("Objects").getNodes("Model")) {
				String name = node.nodeProperties.get(0).replace("Model::", "");
				model.lists.put(name, parseMesh(node));
			}
			return model;
		} catch (URISyntaxException | IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public Node parseNode(String currentLine, Node parent) throws IOException {
		String[] nodeSpecs = currentLine.replace("\"", "").replace("{", "").replaceAll(" ", "").split(":", 2);
		String type = nodeSpecs[0];
		String[] nodeProperties = nodeSpecs[1].split(",");
		nodeProperties[nodeProperties.length-1].replace("{", "");
		Node node = new Node(type, parent, nodeProperties);
		String line;
		while((line = reader.readLine()) != null) {
			line = line.trim();
			if(line.startsWith(";")) {
				continue;
			} else if(line.contains("{")) {
				node.addNode(parseNode(line, node));
			} else if(line.contains(":")) {
				String[] property = parseProperty(line);
				node.addProperty(property[0], property[1]);
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
			currentLine = currentLine.trim();
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
	
	public int parseMesh(Node mesh) {
		String[] vertices = mesh.getProperty("Vertices").split(",");
		String[] normals = mesh.getNode("LayerElementNormal").getProperty("Normals").split(",");
		String[] faces = mesh.getProperty("PolygonVertexIndex").split(",");

		String[] uv = null;
		if(mesh.getNodes("LayerElementUV") != null) {
			uv = mesh.getNode("LayerElementUV").getProperty("UV").split(",");
		}

		int list = GL11.glGenLists(1);
		GL11.glNewList(list, GL11.GL_COMPILE);

		int polygons = 4;
		if(polygons == 3) {
			GL11.glBegin(GL11.GL_TRIANGLES);
		} else if(polygons == 4) {
			GL11.glBegin(GL11.GL_TRIANGLES);
		} else {
			GL11.glBegin(GL11.GL_POLYGON);
		}

		for(int i = 0; i < faces.length; i++) {
			int index = Integer.parseInt(faces[i].replace(" ", ""));
			if(index < 0) {
				index *= -1;
				index--;
			}
			float vertX = Float.parseFloat(vertices[index*3]);
			float vertY = Float.parseFloat(vertices[index*3+1]);
			float vertZ = Float.parseFloat(vertices[index*3+2]);
			GL11.glVertex3f(vertX, vertY, vertZ);
		}
		GL11.glEnd();
		GL11.glEndList();
		return list;
	}

	private class Node {
		private Map<String, List<Node>> childNodes = new HashMap<String, List<Node>>();
		private Map<String, String> properties = new HashMap<String, String>();
		private List<String> nodeProperties = new ArrayList<String>();

		private final String type;
		private final Node parent;

		public Node(String type) {
			this(type, null);
		}

		public Node(String type, Node parent) {
			this(type, parent, null);
		}

		public Node(String type, Node parent, String[] nodeProperties) {
			this.type = type;
			this.parent = parent;
			if(nodeProperties != null) {
				this.nodeProperties.addAll(Arrays.asList(nodeProperties));
			}
		}

		public void addNode(Node node) {
			if(childNodes.get(node.getType()) == null) {
				childNodes.put(node.getType(), new ArrayList<Node>());
			}
			childNodes.get(node.getType()).add(node);
		}

		public List<Node> getNodes(String type) {
			return childNodes.get(type);
		}

		public Node getNode(String type) {
			return childNodes.get(type).get(0);
		}

		public Node getNode(String type, List<String> nodeProperties) {
			for(Node node : getNodes(type)) {
				if(node.nodeProperties.containsAll(nodeProperties)) {
					return node;
				}
			}
			return null;
		}

		public void addProperty(String name, String value) {
			properties.put(name, value);
		}

		public String getProperty(String name) {
			return properties.get(name);
		}

		public String getType() {
			return type;
		}

		public Node getParent() {
			return parent;
		}

		@Override
		public String toString() {
			return getType()+" "+nodeProperties;
		}
	}
}
