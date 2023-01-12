package renderEngine.shaders;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import renderEngine.entities.Camera;
import renderEngine.entities.Light;
import utils.math.createViewMatrix;

public class StaticShader extends ShaderProgram {

    private static final String VERTEX_FILE = "/renderEngine/shaders/vertexShader.txt";
    private static final String FRAGMENT_FILE = "/renderEngine/shaders/fragmentShader.txt";

    private int location_transformationMatrix;
    private int location_projectionMatrix;
    private int location_viewMatrix;
    private int location_lightPosition;
    private int location_lightColour;
    private int location_shineDamper;
    private int location_reflectivity;
    private int location_skyColor;
    private int location_fogDensity;
    private int location_parentMatrix;
    private int location_cameraPosition;
    private int location_enviroMap;
    private int location_reflectionFactor;

    public StaticShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void bindAttributes() {
        super.bindAttributes(0, "position");
        super.bindAttributes(1, "textureCoords");
    }

    @Override
    protected void getAllUniformLocations() {
        location_transformationMatrix = super.getUniformLocation("transformationMatrix");
        location_projectionMatrix = super.getUniformLocation("projectionMatrix");
        location_viewMatrix = super.getUniformLocation("viewMatrix");
        location_lightPosition = super.getUniformLocation("lightPosition");
        location_lightColour = super.getUniformLocation("lightColour");
        location_shineDamper = super.getUniformLocation("shineDamper");
        location_reflectivity = super.getUniformLocation("reflectivity");
        location_skyColor = super.getUniformLocation("skyColor");
        location_fogDensity = super.getUniformLocation("density");
        location_parentMatrix = super.getUniformLocation("parentMatrix");
        location_cameraPosition = super.getUniformLocation("cameraPosition");
        location_enviroMap = super.getUniformLocation("enviroMap");
        location_reflectionFactor = super.getUniformLocation("reflectionFactor");
    }

    public void loadReflectionFactor(float reflectionFactor) {
        super.loadFloat(location_reflectionFactor, reflectionFactor);
    }

    public void loadEnviroMap() {
        super.loadInt(location_enviroMap, 1);
    }

    public void loadCameraPosition(Vector3f cameraPosition) {
        super.loadVector(location_cameraPosition, cameraPosition);
    }

    public void loadParentMatrix(Matrix4f parentMatrix) {
        super.loadMatrix(location_parentMatrix, parentMatrix);
    }

    public void loadFogDensity(float fogDensity) {
        super.loadFloat(location_fogDensity, fogDensity);
    }

    public void loadSkyColor(Vector3f skyColor) {
        super.loadVector(location_skyColor, skyColor);
    }

    public void loadShineVariables(float damper, float reflectivity) {
        super.loadFloat(location_shineDamper, damper);
        super.loadFloat(location_reflectivity, reflectivity);
    }

    public void loadTransformationMatrix(Matrix4f matrix) {
        super.loadMatrix(location_transformationMatrix, matrix);
    }

    public void loadViewMatrix(Camera camera) {
        Matrix4f viewMatrix = createViewMatrix.createViewMatrix(camera);
        super.loadMatrix(location_viewMatrix, viewMatrix);
    }

    public void loadLight(Light light) {
        super.loadVector(location_lightPosition, light.getPosition());
        super.loadVector(location_lightColour, light.getColour());
    }

    public void loadProjectionMatrix(Matrix4f projection) {
        super.loadMatrix(location_projectionMatrix, projection);
    }
}
