package com.sirolf2009.syringe.client.renderers;

import static org.lwjgl.opengl.GL11.GL_COMPILE;
import static org.lwjgl.opengl.GL11.GL_POLYGON;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glCallList;
import static org.lwjgl.opengl.GL11.glDeleteLists;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glEndList;
import static org.lwjgl.opengl.GL11.glGenLists;
import static org.lwjgl.opengl.GL11.glNewList;
import static org.lwjgl.opengl.GL11.glNormal3f;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glVertex3f;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import com.sirolf2009.syringe.client.models.Model;
import com.sirolf2009.syringe.parsers.parserOBJ;

public class ModelRenderSimple implements IModelRenderer {
	
	public int modelDisplayList = 0;
	public Model model = null;
	public String modelLocation = "";
	
	public ModelRenderSimple(String modelLocation) {
		this.modelLocation = modelLocation;
		loadModel();
	}

	@Override
	public void loadModel() {
		modelDisplayList = glGenLists(1);
        glNewList(modelDisplayList, GL_COMPILE);
        {
            Model m = null;
            try {
                m = parserOBJ.loadTexturedModel(new File(getClass().getClassLoader().getResource(modelLocation).toURI()));
                m.setSmoothShadingEnabled(true);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Display.destroy();
                System.exit(1);
            } catch (IOException e) {
            	System.err.println("Something went wrong with loading "+modelLocation);
                e.printStackTrace();
                Display.destroy();
                System.exit(1);
            } catch (URISyntaxException e) {
            	System.err.println("Something went wrong with loading "+modelLocation);
				e.printStackTrace();
                Display.destroy();
                System.exit(1);
			}
            glBegin(GL_POLYGON);
            int currentTexture=0;
            for (Model.Face face : m.getFaces()) {
            	if(face.getMaterial() != null && face.getMaterial().texture != null && currentTexture != face.getMaterial().texture.getTextureID()) {
            		currentTexture = face.getMaterial().texture.getTextureID();
                    glBindTexture(GL_TEXTURE_2D, face.getMaterial().texture.getTextureID());
            	}
                Vector3f n1 = m.getNormals().get(face.getNormalIndices()[0] - 1);
                glNormal3f(n1.x, n1.y, n1.z);
                Vector2f t1 = m.getTextureCoordinates().get((int) face.getTextureCoordinateIndices()[0] -1);
                glTexCoord2f(t1.x, t1.y);
                Vector3f v1 = m.getVertices().get(face.getVertexIndices()[0] - 1);
                glVertex3f(v1.x, v1.y, v1.z);
                
                Vector3f n2 = m.getNormals().get(face.getNormalIndices()[1] - 1);
                glNormal3f(n2.x, n2.y, n2.z);
                Vector2f t2 = m.getTextureCoordinates().get((int) face.getTextureCoordinateIndices()[1] -1);
                glTexCoord2f(t2.x, t2.y);
                Vector3f v2 = m.getVertices().get(face.getVertexIndices()[1] - 1);
                glVertex3f(v2.x, v2.y, v2.z);
                
                Vector3f n3 = m.getNormals().get(face.getNormalIndices()[2] - 1);
                glNormal3f(n3.x, n3.y, n3.z);
                Vector2f t3 = m.getTextureCoordinates().get((int) face.getTextureCoordinateIndices()[2] -1);
                glTexCoord2f(t3.x, t3.y);
                Vector3f v3 = m.getVertices().get(face.getVertexIndices()[2] - 1);
                glVertex3f(v3.x, v3.y, v3.z);
            }
            glEnd();
        }
        glEndList();
	}

	@Override
	public void renderModel() {
		glCallList(modelDisplayList);
	}

	@Override
	public void disposeModel() {
		glDeleteLists(modelDisplayList, 1);
	}

}
