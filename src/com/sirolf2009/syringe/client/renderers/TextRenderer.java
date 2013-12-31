package com.sirolf2009.syringe.client.renderers;

import java.awt.Font;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.opengl.TextureImpl;

public class TextRenderer {

	public TrueTypeFont font;
	
	public TextRenderer() {
		font = new TrueTypeFont(new Font("Times New Roman", Font.PLAIN, 16), true);
	}
	
	public void renderText(int x, int y, String text) {
		renderText(x, y, text, Color.white);
	}
	
	public void renderText(int x, int y, String text, Color color) {
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glPushMatrix();
		TextureImpl.bindNone();
		GL11.glRotatef(180, 0, 1, 0);
		GL11.glRotatef(180, 0, 0, 0);
		font.drawString(x, y, text, color);
		GL11.glPopMatrix();
		GL11.glDisable(GL11.GL_BLEND);
	}
}
