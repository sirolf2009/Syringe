package com.sirolf2009.syringe.client.models;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import com.sirolf2009.syringe.parsers.ParserOBJ;

public class Mesh {
	
	protected int defaultObjectList;
	protected Map<String, Integer> animations;
	protected Texture texture;
	protected String name;

	public Mesh(String name, int objectList) throws FileNotFoundException, IOException, URISyntaxException {
		this(name, objectList,  TextureLoader.getTexture("png", new FileInputStream(new File(ParserOBJ.class.getClassLoader().getResource("img/MissingTexture.png").toURI()))));
	}
	
	public Mesh(String name, int objectList, Texture texture) {
		this.name = name;
		this.defaultObjectList = objectList;
		this.texture = texture;
		this.animations = new HashMap<String, Integer>();
	}
	
	public void render() {
		texture.bind();
		GL11.glCallList(defaultObjectList);
	}
	
	public String getName() {
		return name;
	}
	
	public void setTexture(Texture texture) {
		this.texture = texture;
	}
	
	public void addAnimation(String name, int list) {
		animations.put(name, list);
	}

}
