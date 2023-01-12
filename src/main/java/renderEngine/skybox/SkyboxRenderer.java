package renderEngine.skybox;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.entities.Camera;
import renderEngine.models.RawModel;

public class SkyboxRenderer {

    /*
	private static final float SIZE = 100000f;
	
	private static final float[] VERTICES = {        
	    -SIZE,  SIZE, -SIZE,
	    -SIZE, -SIZE, -SIZE,
	    SIZE, -SIZE, -SIZE,
	     SIZE, -SIZE, -SIZE,
	     SIZE,  SIZE, -SIZE,
	    -SIZE,  SIZE, -SIZE,

	    -SIZE, -SIZE,  SIZE,
	    -SIZE, -SIZE, -SIZE,
	    -SIZE,  SIZE, -SIZE,
	    -SIZE,  SIZE, -SIZE,
	    -SIZE,  SIZE,  SIZE,
	    -SIZE, -SIZE,  SIZE,

	     SIZE, -SIZE, -SIZE,
	     SIZE, -SIZE,  SIZE,
	     SIZE,  SIZE,  SIZE,
	     SIZE,  SIZE,  SIZE,
	     SIZE,  SIZE, -SIZE,
	     SIZE, -SIZE, -SIZE,

	    -SIZE, -SIZE,  SIZE,
	    -SIZE,  SIZE,  SIZE,
	     SIZE,  SIZE,  SIZE,
	     SIZE,  SIZE,  SIZE,
	     SIZE, -SIZE,  SIZE,
	    -SIZE, -SIZE,  SIZE,

	    -SIZE,  SIZE, -SIZE,
	     SIZE,  SIZE, -SIZE,
	     SIZE,  SIZE,  SIZE,
	     SIZE,  SIZE,  SIZE,
	    -SIZE,  SIZE,  SIZE,
	    -SIZE,  SIZE, -SIZE,

	    -SIZE, -SIZE, -SIZE,
	    -SIZE, -SIZE,  SIZE,
	     SIZE, -SIZE, -SIZE,
	     SIZE, -SIZE, -SIZE,
	    -SIZE, -SIZE,  SIZE,
	     SIZE, -SIZE,  SIZE
	};
	
	private static String[] TEXTURE_FILES = {"right","left","top","bottom","back","front"};
	private static int skyboxId = 0;
	
	private RawModel cube;
	private int texture;
	
     */

    private List<CubeMap> cubes = new ArrayList<>();
    private Loader loader;
    private SkyboxShader shader;
    public float time = 0;

    public SkyboxRenderer(Loader loader, Matrix4f projectionMatrix) {
        //cube = loader.loadToVAO(VERTICES, 3);
        this.loader = loader;
        shader = new SkyboxShader();
        shader.start();
        shader.connectTextureUnits();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();

    }

    public void newProjectionMatrix(Matrix4f projectionMatrix) {
        shader.start();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();
    }

    public void addCubeMap(CubeMap cubeMap) {
        String[] TEXTURE_FILES_TMP = new String[cubeMap.getTextureFiles().length];
        for (int i = 0; i < cubeMap.getTextureFiles().length; i++) {
            TEXTURE_FILES_TMP[i] = "skybox/" + Integer.toString(cubeMap.skyboxId) + "/" + cubeMap.getTextureFiles()[i];
        }
        String[] NIGHT_TEXTURE_FILES_TMP = new String[cubeMap.getNightTextureFiles().length];
        for (int i = 0; i < cubeMap.getNightTextureFiles().length; i++) {
            NIGHT_TEXTURE_FILES_TMP[i] = "skybox/" + Integer.toString(cubeMap.skyboxId) + "_NIGHT/" + cubeMap.getNightTextureFiles()[i];
        }
        cubeMap.texture = loader.loadCubeMap(TEXTURE_FILES_TMP);
        cubeMap.nightTexture = loader.loadCubeMap(NIGHT_TEXTURE_FILES_TMP);
        cubes.add(cubeMap);
    }

    public void render(Camera camera) {
        shader.start();
        shader.loadViewMatrix(camera);
        for (CubeMap cubeMap : cubes) {
            shader.loadFogColor(new Vector3f(cubeMap.r, cubeMap.g, cubeMap.b));
            GL30.glBindVertexArray(cubeMap.getCube().getVaoID());
            GL20.glEnableVertexAttribArray(0);
            //GL13.glActiveTexture(GL13.GL_TEXTURE0);
            //GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, cubeMap.texture);
            bindTextures(cubeMap);
            GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, cubeMap.getCube().getVertexCount());
            GL20.glDisableVertexAttribArray(0);
            GL30.glBindVertexArray(0);
        }
        shader.stop();
    }

    public SkyboxShader getShader() {
        return shader;
    }

    private void bindTextures(CubeMap cubeMap) {
        //time += DisplayManager.getFrameTimeSeconds() * 1000;
        int timeNight = 5000;
        int timeDay = 8000;
        int timeDayNight = 21000;
        int maxTime = 24000;
        time %= 24000;
        int texture1;
        int texture2;
        float blendFactor;

        if (time >= 0 && time < timeNight) {
            texture1 = cubeMap.nightTexture;
            texture2 = cubeMap.nightTexture;
            blendFactor = (time - 0) / (timeNight - 0);
        } else if (time >= timeNight && time < timeDay) {
            texture1 = cubeMap.nightTexture;
            texture2 = cubeMap.texture;
            blendFactor = (time - timeNight) / (timeDay - timeNight);
        } else if (time >= 8000 && time < 21000) {
            texture1 = cubeMap.texture;
            texture2 = cubeMap.texture;
            blendFactor = (time - timeDay) / (timeDayNight - timeDay);
        } else {
            texture1 = cubeMap.texture;
            texture2 = cubeMap.nightTexture;
            blendFactor = (time - timeDayNight) / (maxTime - timeDayNight);
        }

//		texture2 = cubeMap.nightTexture;
//		texture1 = cubeMap.texture;
//		blendFactor = time / 24000;
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texture1);
        GL13.glActiveTexture(GL13.GL_TEXTURE1);
        GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texture2);
        shader.loadBlendFactor(blendFactor);
    }

}
