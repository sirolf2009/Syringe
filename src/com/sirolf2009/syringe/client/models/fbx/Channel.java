package com.sirolf2009.syringe.client.models.fbx;

import java.util.ArrayList;
import java.util.List;

public class Channel {
	
	protected String type;
	protected Channel parent;
	protected List<Channel> children;
	
	protected List<Double> keys;
	protected double defaultKey;
	protected int keyCount;

	public Channel(String type, Channel parent) {
		this.type = type;
		this.parent = parent;
		children = new ArrayList<Channel>();
	}
	
	public Channel(String type, Channel parent, int keyCount, double defaultKey, List<Double> keys) {
		this(type, parent);
		this.keyCount = keyCount;
		this.defaultKey = defaultKey;
		this.keys = keys;
	}
	
	public void addChild(Channel channel) {
		children.add(channel);
	}

}
