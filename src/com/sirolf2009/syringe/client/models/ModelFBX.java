package com.sirolf2009.syringe.client.models;

import java.util.HashMap;
import java.util.Map;

import org.lwjgl.opengl.GL11;

import com.sirolf2009.syringe.client.models.fbx.Limb;
import com.sirolf2009.syringe.client.models.fbx.Take;

public class ModelFBX extends Model implements IModelAnimated {
	
	protected Map<String, Take> takes;
	protected int currentKeyFrame;
	protected String currentTake;
	protected int frameRate;
	protected long frameProgress;
	
	protected Map<String, Limb> armature;

	public ModelFBX() {
		super();
		takes = new HashMap<String, Take>();
		armature = new HashMap<String, Limb>();
		currentTake = "DefaultTake";
	}
	
	@Override
	public void render() {
		GL11.glPushMatrix();
		GL11.glRotated(90, -1, 0, 0); //Fucking blender
		takes.get("walk_legs_").applyTake(currentKeyFrame);
		super.render();
		GL11.glPopMatrix();
	}
	
	@Override
	public void updateCurrentKeyFrame(long deltaTime) {
		frameProgress += deltaTime*frameRate;
		if(frameProgress/400 > 1) {
			currentKeyFrame++;
			frameProgress %= 400;
		}
	}
	
	public void addTake(String name, Take take) {
		takes.put(name, take);
	}

	public Map<String, Take> getTakes() {
		return takes;
	}

	public void setTakes(Map<String, Take> takes) {
		this.takes = takes;
	}

	public int getCurrentKeyFrame() {
		return currentKeyFrame;
	}

	public void setCurrentKeyFrame(int currentKeyFrame) {
		this.currentKeyFrame = currentKeyFrame;
	}

	public String getCurrentTake() {
		return currentTake;
	}

	public void setCurrentTake(String currentTake) {
		this.currentTake = currentTake;
	}

	public int getFrameRate() {
		return frameRate;
	}

	public void setFrameRate(int frameRate) {
		this.frameRate = frameRate;
	}
	
	public void addLimb(Limb limb) {
		armature.put(limb.getName(), limb);
	}
	
	public Limb getLimb(String name) {
		return armature.get(name);
	}
	
	public Map<String, Limb> getArmature() {
		return armature;
	}

}
