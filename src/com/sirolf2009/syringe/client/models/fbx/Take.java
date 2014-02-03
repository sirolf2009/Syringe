package com.sirolf2009.syringe.client.models.fbx;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import com.sirolf2009.syringe.client.models.Mesh;

public class Take {
	
	protected List<Channel> channels;
	protected String name;
	protected Mesh model;
	
	protected final int TRANSFORM = 0;

	protected final int TRANSLATION = 0;
	protected final int ROTATION = 1;

	protected final int X = 0;
	protected final int Y = 1;
	protected final int Z = 2;

	public Take(String name) {
		this.name = name;
		channels = new ArrayList<Channel>();
	}
	
	public void addChannel(Channel channel) {
		channels.add(channel);
	}
	
	public void applyTake(int currentKeyFrame) {
		applyRotation(currentKeyFrame);
	}
	
	public void applyRotation(int currentKeyFrame) {
		for(int i = 0; i < 3; i++) {
			Channel channel = channels.get(TRANSFORM).children.get(ROTATION).children.get(i);
			if(currentKeyFrame > channel.keyCount) {
				currentKeyFrame %= channel.keyCount;
				GL11.glRotated(channel.keys.get(currentKeyFrame), 1, 0, 0);
			}
		}
	}

	public Mesh getModel() {
		return model;
	}

	public void setModel(Mesh mesh) {
		this.model = mesh;
	}

}
