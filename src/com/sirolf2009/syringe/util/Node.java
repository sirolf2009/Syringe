package com.sirolf2009.syringe.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Node {

	private Map<String, List<Node>> childNodes = new HashMap<String, List<Node>>();
	private Map<String, String> properties = new HashMap<String, String>();
	private List<String> nodeProperties = new ArrayList<String>();
	private List<String[]> multiProperties = new ArrayList<String[]>();

	private final String type;
	private final Node parent;

	public Node(String type) {
		this(type, null);
	}

	public Node(String type, Node parent) {
		this(type, parent, null);
	}

	public Node(String type, Node parent, String[] nodeProperties) {
		while(type.length() > 0 && type.charAt(0) == ' ') {
			type = type.substring(1);
		}
		while(type.endsWith(" ")) {
			type = type.substring(0, type.length()-2);
		}
		this.type = type;
		this.parent = parent;
		if(nodeProperties != null) {
			for(String string : nodeProperties) {
				while(string.length() > 0 && string.charAt(0) == ' ') {
					string = string.substring(1);
				}
				while(string.endsWith(" ")) {
					string = string.substring(0, string.length()-2);
				}
			}
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

	public String getPrettyProperty(String name) {
		return properties.get(name).trim().replaceAll("\"", "");
	}

	public void addMultiProperty(String[] props) {
		multiProperties.add(props);
	}

	public List<String[]> getMultiProperties() {
		return multiProperties;
	}

	public String[] getMultiPropertyFromFirstProp(String firstProp) {
		for(String[] string : multiProperties) {
			if(string.length > 1 && string[1].equals(firstProp)) {
				return string;
			}
		}
		return null;
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

	public Map<String, List<Node>> getChildNodes() {
		return childNodes;
	}

	public void setChildNodes(Map<String, List<Node>> childNodes) {
		this.childNodes = childNodes;
	}

	public Map<String, String> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}

	public List<String> getNodeProperties() {
		return nodeProperties;
	}

	public void setNodeProperties(List<String> nodeProperties) {
		this.nodeProperties = nodeProperties;
	}
}