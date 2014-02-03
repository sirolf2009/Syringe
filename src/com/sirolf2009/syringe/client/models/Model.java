package com.sirolf2009.syringe.client.models;

import java.util.ArrayList;
import java.util.List;
import org.lwjgl.util.vector.Vector3f;
import com.sirolf2009.syringe.collision.Material;

/**
 * The Model3D class
 * Used to store lists containing vertices
 * 
 * @author sirolf2009
 *
 */
public class Model {

	/** Vertex coords */
	public ArrayList<float[]> vertexsets = new ArrayList<float[]>();
	/** Vertex normal coords */
	public ArrayList<float[]> vertexsetsnorms = new ArrayList<float[]>();
	/** Vertex texture coords */
	public ArrayList<float[]> vertexsetstexs = new ArrayList<float[]>(); // Vertex Coordinates Textures
	/** Faces */
	public ArrayList<int[]> faces = new ArrayList<int[]>(); // Array of Faces (vertex sets)
	/** Face textures */
	public ArrayList<int[]> facestexs = new ArrayList<int[]>(); // Array of of Faces textures
	/** Face normals */
	public ArrayList<int[]> facesnorms = new ArrayList<int[]>(); // Array of Faces normals

	/** The OpenGL object list */
	public int objectlist;
	/** The amount of polygons */
	public int numpolys = 0;

	////Statisitcs for drawing ////
	/** The highest point of the model */
	public float toppoint = 0;
	/** The lowest point of the model */
	public float bottompoint = 0;
	/** The most left point of the model */
	public float leftpoint = 0;
	/** The most right point of the model */
	public float rightpoint = 0;
	/** The furthest point of the model */
	public float farpoint = 0;
	/** The nearest point of the model */
	public float nearpoint = 0;

	////Statisitcs for collision ////
	/** The bottom-left X coord of the model */
	public float posX1 = 0;
	/** The bottom-left Y coord of the model */
	public float posY1 = 0;
	/** The bottom-left Z coord of the model */
	public float posZ1 = 0;
	/** The top-right X coord of the model */
	public float posX2 = 0;
	/** The top-right Y coord of the model */
	public float posY2 = 0;
	/** The top-right Z coord of the model */
	public float posZ2 = 0;
	
	public List<Mesh> meshes = new ArrayList<Mesh>();

	/** The {@link AABB} of the model */
	public AABB AABB;

	public Material material;
	public int weight = 1;

	/** Destroy all lists */
	public void cleanUp() {
		vertexsets.clear();
		vertexsetsnorms.clear();
		vertexsetstexs.clear();
		faces.clear();
		facestexs.clear();
		facesnorms.clear();
	}

	/** The width of the model */
	public float getXWidth() {
		float returnval = 0;
		returnval = rightpoint - leftpoint;
		return returnval;
	}

	/** The height of the model */
	public float getYHeight() {
		float returnval = 0;
		returnval = toppoint - bottompoint;
		return returnval;
	}

	/** The depth of the model */
	public float getZDepth() {
		float returnval = 0;
		returnval = nearpoint - farpoint;
		return returnval;
	}

	/** 
	 * The amount of polygons 
	 * 
	 * @return The amount of polygons
	 */
	public int numpolygons() {
		return numpolys;
	}

	/** Bind the texture and draw the model */
	public void render() {
		for(Mesh mesh : meshes) {
			mesh.render();
		}
	}

	public boolean intersects(Model other) {
		for(int[] face : faces) {
			//remember these? ;)
			float posX1 = 0;
			float posY1 = 0;
			float posZ1 = 0;
			float posX2 = 0;
			float posY2 = 0;
			float posZ2 = 0;
			for(int index : face) {
				float[] vertex = vertexsets.get(index);
				if(vertex[0] < posX1) {
					posX1 = vertex[0];
				}
				if(vertex[1] < posY1) {
					posY1 = vertex[1];
				}
				if(vertex[2] < posZ1) {
					posZ1 = vertex[2];
				}
				if(vertex[0] > posX2) {
					posX2 = vertex[0];
				}
				if(vertex[1] > posY2) {
					posY2 = vertex[1];
				}
				if(vertex[2] > posZ2) {
					posZ2 = vertex[2];
				}
			}
			AABB AABB = new AABB(posX1, posY1, posZ1, posX2, posY2, posZ2);
			for(int[] faceOther : other.faces) {
				//remember these? ;)
				float posX1Other = 0;
				float posY1Other = 0;
				float posZ1Other = 0;
				float posX2Other = 0;
				float posY2Other = 0;
				float posZ2Other = 0;
				for(int index : faceOther) {
					float[] vertex = vertexsets.get(index);
					if(vertex[0] < posX1Other) {
						posX1Other = vertex[0];
					}
					if(vertex[1] < posY1Other) {
						posY1Other = vertex[1];
					}
					if(vertex[2] < posZ1Other) {
						posZ1Other = vertex[2];
					}
					if(vertex[0] > posX2Other) {
						posX2Other = vertex[0];
					}
					if(vertex[1] > posY2Other) {
						posY2Other = vertex[1];
					}
					if(vertex[2] > posZ2Other) {
						posZ2Other = vertex[2];
					}
				}
				AABB AABBother = new AABB(posX1Other, posY1Other, posZ1Other, posX2Other, posY2Other, posZ2Other);
				if(AABB.intersects(AABBother)) {
					return true;
				}
			}
		}
		return false;
	}

	public Vector3f getIntersectionDistance(Model other) {
		for(int[] face : faces) {
			//remember these? ;)
			float posX1 = 0;
			float posY1 = 0;
			float posZ1 = 0;
			float posX2 = 0;
			float posY2 = 0;
			float posZ2 = 0;
			for(int index : face) {
				float[] vertex = vertexsets.get(index);
				if(vertex[0] < posX1) {
					posX1 = vertex[0];
				}
				if(vertex[1] < posY1) {
					posY1 = vertex[1];
				}
				if(vertex[2] < posZ1) {
					posZ1 = vertex[2];
				}
				if(vertex[0] > posX2) {
					posX2 = vertex[0];
				}
				if(vertex[1] > posY2) {
					posY2 = vertex[1];
				}
				if(vertex[2] > posZ2) {
					posZ2 = vertex[2];
				}
			}
			AABB AABB = new AABB(posX1, posY1, posZ1, posX2, posY2, posZ2);
			for(int[] faceOther : other.faces) {
				//remember these? ;)
				float posX1Other = 0;
				float posY1Other = 0;
				float posZ1Other = 0;
				float posX2Other = 0;
				float posY2Other = 0;
				float posZ2Other = 0;
				for(int index : faceOther) {
					float[] vertex = vertexsets.get(index);
					if(vertex[0] < posX1Other) {
						posX1Other = vertex[0];
					}
					if(vertex[1] < posY1Other) {
						posY1Other = vertex[1];
					}
					if(vertex[2] < posZ1Other) {
						posZ1Other = vertex[2];
					}
					if(vertex[0] > posX2Other) {
						posX2Other = vertex[0];
					}
					if(vertex[1] > posY2Other) {
						posY2Other = vertex[1];
					}
					if(vertex[2] > posZ2Other) {
						posZ2Other = vertex[2];
					}
				}
				AABB AABBother = new AABB(posX1Other, posY1Other, posZ1Other, posX2Other, posY2Other, posZ2Other);
				if(AABB.intersects(AABBother)) {
					return com.sirolf2009.syringe.client.models.AABB.getDistanceBetween(AABB, AABBother);
				}
			}
		}
		return null;
	}

	public Vector3f getIntersectionDistance(AABB other) {
		for(int[] face : faces) {
			//remember these? ;)
			float posX1 = 0;
			float posY1 = 0;
			float posZ1 = 0;
			float posX2 = 0;
			float posY2 = 0;
			float posZ2 = 0;
			boolean isFirstPass = true;
			for(int index : face) {
				float[] vertex = vertexsets.get(index-1);
				if(isFirstPass) {
					posX1 = vertex[0];
					posY1 = vertex[1];
					posZ1 = vertex[2];
					posX2 = vertex[0];
					posY2 = vertex[1];
					posZ2 = vertex[2];
					isFirstPass=false;
				}
				if(vertex[0] < posX1) {
					posX1 = vertex[0];
				}
				if(vertex[1] < posY1) {
					posY1 = vertex[1];
				}
				if(vertex[2] < posZ1) {
					posZ1 = vertex[2];
				}
				if(vertex[0] > posX2) {
					posX2 = vertex[0];
				}
				if(vertex[1] > posY2) {
					posY2 = vertex[1];
				}
				if(vertex[2] > posZ2) {
					posZ2 = vertex[2];
				}
			}
			AABB AABB = new AABB(posX1, posY1, posZ1, posX2, posY2, posZ2);
			if(AABB.intersects(other)) {
				return com.sirolf2009.syringe.client.models.AABB.getDistanceBetween(AABB, other);
			}
		}
		return null;
	}
	
	public List<Mesh> getMeshes() {
		return meshes;
	}
	
	public Mesh getMeshFromName(String name) {
		for(Mesh mesh : meshes) {
			if(mesh.name.equals(name)) {
				return mesh;
			}
		}
		return null;
	}

}