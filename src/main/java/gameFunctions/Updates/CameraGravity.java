package gameFunctions.Updates;

import org.lwjgl.util.vector.Vector3f;

import renderEngine.HeightsGenerator;
import renderEngine.entities.Camera;
import renderEngine.terrains.Terrain;

public class CameraGravity extends Thread {

    private Camera camera;
    private Terrain terrain;
    //private HeightsGenerator generator;

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    /*public void setGenerator(HeightsGenerator generator) {
	 *	this.generator = generator;
	}*/
    public void run() {
        while (true) {
            if (camera.getPosition().y != this.terrain.getHeightOfTerrain(camera.getPosition().x, camera.getPosition().z) + 1) {
                Vector3f newPosition = new Vector3f(camera.getPosition().x, this.terrain.getHeightOfTerrain(camera.getPosition().x, camera.getPosition().z) + 1, camera.getPosition().z);
                camera.setPosition(newPosition);
                System.out.println(this.terrain.getHeightOfTerrain(camera.getPosition().x, camera.getPosition().z) + 1);
            }
        }
    }

    public void setTerrain(Terrain terrain) {
        this.terrain = terrain;
    }
}
