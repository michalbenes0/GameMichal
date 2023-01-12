package renderEngine.skybox;

import org.lwjgl.opengl.GL13;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import renderEngine.entities.Camera;

import renderEngine.shaders.ShaderProgram;
import utils.math.createViewMatrix;

public class SkyboxShader extends renderEngine.shaders.ShaderProgram {

    private static final String VERTEX_FILE = "/renderEngine/skybox/skyboxVertexShader.txt";
    private static final String FRAGMENT_FILE = "/renderEngine/skybox/skyboxFragmentShader.txt";

    //private static final float ROTATE_SPEED = 0.2f;
    private int location_projectionMatrix;
    private int location_viewMatrix;
    private int location_fogColor;
    private int location_blendFactor;
    private int location_cubeMap;
    private int location_cubeMap2;

    public float rotation = 0;

    public SkyboxShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    public void loadProjectionMatrix(Matrix4f matrix) {
        super.loadMatrix(location_projectionMatrix, matrix);
    }

    public void connectTextureUnits() {
        super.loadInt(location_cubeMap, 0);
        super.loadInt(location_cubeMap2, 1);
    }

    public void loadViewMatrix(Camera camera) {
        Matrix4f matrix = createViewMatrix.createViewMatrix(camera);
        matrix.m30 = 0;
        matrix.m31 = 0;
        matrix.m32 = 0;
        Matrix4f.rotate((float) Math.toRadians(rotation), new Vector3f(0, 1, 0), matrix, matrix);
        super.loadMatrix(location_viewMatrix, matrix);
    }

    @Override
    protected void getAllUniformLocations() {
        location_projectionMatrix = super.getUniformLocation("projectionMatrix");
        location_viewMatrix = super.getUniformLocation("viewMatrix");
        location_fogColor = super.getUniformLocation("fogColor");
        location_blendFactor = super.getUniformLocation("blendFactor");
        location_cubeMap = super.getUniformLocation("cubeMap");
        location_cubeMap2 = super.getUniformLocation("cubeMap2");
    }

    public void loadFogColor(Vector3f color) {
        super.loadVector(location_fogColor, color);
    }

    @Override
    protected void bindAttributes() {
        super.bindAttributes(0, "position");
    }

    public void loadBlendFactor(float blendFactor) {
        super.loadFloat(location_blendFactor, blendFactor);
    }

}
