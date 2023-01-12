package renderEngine.entities;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector3f;

import renderEngine.Loader;
import renderEngine.OBJLoader;
import renderEngine.renderer;
import renderEngine.models.RawModel;
import renderEngine.models.TexturedModel;
import renderEngine.shaders.StaticShader;
import renderEngine.textures.ModelTexture;

public class Entity {

    private TexturedModel model;
    private Vector3f position;
    private float rotX, rotY, rotZ;
    private float scale;
    private StaticShader shader;
    private renderer renderer;
    private Vector3f positionTmp;
    private float rotXTmp, rotYTmp, rotZTmp;
    private float scaleTmp;
    private List<List> duplicates = new ArrayList();
    private List duplicate = new ArrayList();
    private Camera camera;
    private Parent parent;
    private float reflectionFactor;

    /*
	public Entity(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		super();
		this.model = model;
		this.position = position;
		this.rotX = rotX;
		this.rotY = rotY;
		this.rotZ = rotZ;
		this.scale = scale;
	}
     */
    public Entity(Loader loader, StaticShader shader, renderer renderer, Camera camera, String image) {
        super();
        //RawModel model = loader.loadToVAO(positions,textureCoord,indices);
        RawModel model = OBJLoader.loadObjModel(image, loader);
        ModelTexture texture = new ModelTexture(loader.loadTexture(image));
        TexturedModel textureModel = new TexturedModel(model, texture);

        this.model = textureModel;
        this.position = new Vector3f(0, 0, 0);
        this.rotX = 0;
        this.rotY = 0;
        this.rotZ = 0;
        this.scale = 1;
        this.renderer = renderer;
        this.shader = shader;
        this.camera = camera;
        this.parent = new Parent();
    }

    public Entity(Loader loader, StaticShader shader, renderer renderer, Camera camera, Parent parent, String image) {
        super();
        //RawModel model = loader.loadToVAO(positions,textureCoord,indices);
        RawModel model = OBJLoader.loadObjModel(image, loader);
        ModelTexture texture = new ModelTexture(loader.loadTexture(image));
        TexturedModel textureModel = new TexturedModel(model, texture);

        this.model = textureModel;
        this.position = new Vector3f(0, 0, 0);
        this.rotX = 0;
        this.rotY = 0;
        this.rotZ = 0;
        this.scale = 1;
        this.renderer = renderer;
        this.shader = shader;
        this.camera = camera;
        this.parent = parent;
    }

    public int addDuplicate(Vector3f position, float rotX, float rotY, float rotZ, float scale) {
        duplicate = new ArrayList();
        duplicate.add(position);
        duplicate.add(rotX);
        duplicate.add(rotY);
        duplicate.add(rotZ);
        duplicate.add(scale);
        duplicates.add(duplicate);
        return duplicates.size() - 1;
    }

    public boolean removeDuplicate(int duplicateID) {
        if (!((duplicates.size() - 1) > duplicateID) || (duplicateID < 0)) {
            return false;
        }

        duplicates.remove(duplicateID);

        return true;
    }

    public List getDuplicate(int duplicateID) {
        if (!((duplicates.size() - 1) > duplicateID) || (duplicateID < 0)) {
            return new ArrayList();
        }

        return (List) duplicates.get(duplicateID);
    }

    public void render() {
        //this.shader.start();
        //this.shader.loadViewMatrix(this.camera);
        //this.renderer.render(this, shader);
        this.renderer.processEntity(this);

        for (List duplicateTmp : duplicates) {
            render((Vector3f) duplicateTmp.get(0), (float) duplicateTmp.get(1), (float) duplicateTmp.get(2), (float) duplicateTmp.get(3), (float) duplicateTmp.get(4));

        }

        //this.shader.stop();
    }

    public void render(Vector3f position, float rotX, float rotY, float rotZ, float scale) {
        this.positionTmp = this.position;
        this.rotXTmp = this.rotX;
        this.rotYTmp = this.rotY;
        this.rotZTmp = this.rotZ;
        this.scaleTmp = this.scale;

        this.position = position;
        this.rotX = rotX;
        this.rotY = rotY;
        this.rotZ = rotZ;
        this.scale = scale;

        //this.renderer.render(this, shader);
        this.renderer.processEntity(this);

        this.position = this.positionTmp;
        this.rotX = this.rotXTmp;
        this.rotY = this.rotYTmp;
        this.rotZ = this.rotZTmp;
        this.scale = this.scaleTmp;
    }

    public void increasePosition(float dx, float dy, float dz) {
        this.position.x += dx;
        this.position.y += dy;
        this.position.z += dz;
    }

    public void increaseRotation(float dx, float dy, float dz) {
        this.rotX += dx;
        this.rotY += dy;
        this.rotZ += dz;
    }

    public TexturedModel getModel() {
        return model;
    }

    public void setModel(TexturedModel model) {
        this.model = model;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public float getRotX() {
        return rotX;
    }

    public void setRotX(float rotX) {
        this.rotX = rotX;
    }

    public float getRotY() {
        return rotY;
    }

    public void setRotY(float rotY) {
        this.rotY = rotY;
    }

    public float getRotZ() {
        return rotZ;
    }

    public void setRotZ(float rotZ) {
        this.rotZ = rotZ;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public Parent getParent() {
        return parent;
    }

    public float getReflectionFactor() {
        return reflectionFactor;
    }

    public void setReflectionFactor(float reflectionFactor) {
        this.reflectionFactor = reflectionFactor;
    }

}
