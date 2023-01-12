package renderEngine;

import java.awt.Font;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.plaf.FontUIResource;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;

import renderEngine.entities.Camera;
import renderEngine.entities.Entity;
import renderEngine.entities.Light;
import renderEngine.entities.Text;
import renderEngine.models.*;
import renderEngine.shaders.StaticShader;
import renderEngine.shaders.TerrainShader;
import renderEngine.skybox.CubeMap;
import renderEngine.skybox.SkyboxRenderer;
import renderEngine.terrains.Terrain;
import utils.math.createTransformation;

public class renderer {

    private static final float FOV = 90;
    private static final float NEAR_PLANE = 0.1f;
    private static final float FAR_PLANE = 700;
    //private static TrueTypeFont font = Text.prepareFont("Pieces of Eight");
    private static TrueTypeFont font = new TrueTypeFont(new Font("Times New Roman", Font.BOLD, 16), false);
    private StaticShader shader = new StaticShader();
    private Matrix4f projectionMatrix;
    private EntityRenderer renderer;
    private Map<TexturedModel, List<Entity>> entities = new HashMap<TexturedModel, List<Entity>>();
    private List<Terrain> terrains = new ArrayList<Terrain>();
    private TerrainRenderer terrainRenderer;
    private TerrainShader terrainShader = new TerrainShader();
    private SkyboxRenderer skyboxRenderer;
    private Loader loader;
    private Camera camera;

    public float skyColor_red = 0f;
    public float skyColor_green = 0f;
    public float skyColor_blue = 0f;

    public float fogDensity = 0;

    /*
	public renderer(StaticShader shader){
		//GL11.glEnable(GL11.GL_CULL_FACE);
		//GL11.glCullFace(GL11.GL_BACK);
		createProjectionMatrix();
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}
     */

    public renderer(Camera camera, Loader loader) {
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glCullFace(GL11.GL_BACK);
        this.loader = loader;
        this.camera = camera;
        createProjectionMatrix();
        renderer = new EntityRenderer(shader, projectionMatrix, camera, loader);
        terrainRenderer = new TerrainRenderer(terrainShader, projectionMatrix);
        skyboxRenderer = new SkyboxRenderer(loader, projectionMatrix);
    }

    public void prepare() {
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glClearColor(skyColor_red, skyColor_green, skyColor_blue, 1);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        createProjectionMatrix();
        renderer.newProjectionMatrix(projectionMatrix);
        terrainRenderer.newProjectionMatrix(projectionMatrix);
        skyboxRenderer.newProjectionMatrix(projectionMatrix);
    }

    public void render(Light sun, Camera camera) {
        prepare();
        shader.start();
        shader.loadSkyColor(new Vector3f(skyColor_red, skyColor_green, skyColor_blue));
        shader.loadLight(sun);
        shader.loadViewMatrix(camera);
        shader.loadFogDensity(fogDensity);
        renderer.render(entities);
        shader.stop();
        terrainShader.start();
        terrainShader.loadSkyColor(new Vector3f(skyColor_red, skyColor_green, skyColor_blue));
        terrainShader.loadLight(sun);
        terrainShader.loadViewMatrix(camera);
        terrainShader.loadFogDensity(fogDensity);
        terrainRenderer.render(terrains);
        terrainShader.stop();
        skyboxRenderer.render(camera);
        terrains.clear();
        entities.clear();
    }

    public void processCubeMap(CubeMap cubeMap) {
        skyboxRenderer.addCubeMap(cubeMap);
    }

    public void processTerrain(Terrain terrain) {
        terrains.add(terrain);
    }

    public void processEntity(Entity entity) {
        TexturedModel entityModel = entity.getModel();
        List<Entity> batch = entities.get(entityModel);
        if (batch != null) {
            batch.add(entity);
        } else {
            List<Entity> newBatch = new ArrayList<Entity>();
            newBatch.add(entity);
            entities.put(entityModel, newBatch);
        }
    }

    public void render(/*TexturedModel texturedModel*/Entity entity, StaticShader shader) {
        TexturedModel model = entity.getModel();
        RawModel rawModel = model.getRawModel();
        GL30.glBindVertexArray(rawModel.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        Matrix4f transformationMatrix = createTransformation.createTransformation(
                entity.getPosition(),
                entity.getRotX(),
                entity.getRotY(),
                entity.getRotZ(),
                entity.getScale()
        );
        shader.loadTransformationMatrix(transformationMatrix);
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getID());
        //GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, model.getVertexCount());
        GL11.glDrawElements(GL11.GL_TRIANGLES, rawModel.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL30.glBindVertexArray(0);
    }

    /*
	public void renderText(String text) {
		
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL30.glBindVertexArray(0);
		GL11.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);                
		GL11.glClearDepth(1); 
		GL11.glViewport(0,0,600,400);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, 600, 400, 0, -1, 1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glLoadIdentity();
		Color.white.bind();
		Color.black.bind();
		font.drawString(2,2, text);
		//font.drawString(FOV, FAR_PLANE, text, null, 0, 0);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GLU.gluPerspective(FOV, 4/3.0f, NEAR_PLANE, FAR_PLANE);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthFunc(GL11.GL_LEQUAL);
		GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		
	}
     */
    public void createProjectionMatrix() {
        float aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();
        float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))) * aspectRatio);
        float x_scale = y_scale / aspectRatio;
        float frustum_length = FAR_PLANE - NEAR_PLANE;

        projectionMatrix = new Matrix4f();
        projectionMatrix.m00 = x_scale;
        projectionMatrix.m11 = y_scale;
        projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustum_length);
        projectionMatrix.m23 = -1;
        projectionMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustum_length);
        projectionMatrix.m33 = 0;
    }

    public SkyboxRenderer getSkyboxRenderer() {
        return skyboxRenderer;
    }

}
