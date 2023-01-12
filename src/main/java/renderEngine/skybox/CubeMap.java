package renderEngine.skybox;

import renderEngine.models.RawModel;
import renderEngine.Loader;

public class CubeMap {

    private static final float SIZE = 400f;

    private static final float[] VERTICES = {
        -SIZE, SIZE, -SIZE,
        -SIZE, -SIZE, -SIZE,
        SIZE, -SIZE, -SIZE,
        SIZE, -SIZE, -SIZE,
        SIZE, SIZE, -SIZE,
        -SIZE, SIZE, -SIZE,
        -SIZE, -SIZE, SIZE,
        -SIZE, -SIZE, -SIZE,
        -SIZE, SIZE, -SIZE,
        -SIZE, SIZE, -SIZE,
        -SIZE, SIZE, SIZE,
        -SIZE, -SIZE, SIZE,
        SIZE, -SIZE, -SIZE,
        SIZE, -SIZE, SIZE,
        SIZE, SIZE, SIZE,
        SIZE, SIZE, SIZE,
        SIZE, SIZE, -SIZE,
        SIZE, -SIZE, -SIZE,
        -SIZE, -SIZE, SIZE,
        -SIZE, SIZE, SIZE,
        SIZE, SIZE, SIZE,
        SIZE, SIZE, SIZE,
        SIZE, -SIZE, SIZE,
        -SIZE, -SIZE, SIZE,
        -SIZE, SIZE, -SIZE,
        SIZE, SIZE, -SIZE,
        SIZE, SIZE, SIZE,
        SIZE, SIZE, SIZE,
        -SIZE, SIZE, SIZE,
        -SIZE, SIZE, -SIZE,
        -SIZE, -SIZE, -SIZE,
        -SIZE, -SIZE, SIZE,
        SIZE, -SIZE, -SIZE,
        SIZE, -SIZE, -SIZE,
        -SIZE, -SIZE, SIZE,
        SIZE, -SIZE, SIZE
    };

    public int skyboxId = 0;
    public int texture;
    public int nightTexture;
    private RawModel cube;
    private String[] textureFiles = {"right", "left", "top", "bottom", "back", "front"};
    private String[] night_textureFiles = {"right", "left", "top", "bottom", "back", "front"};
    public float r;
    public float g;
    public float b;

    public CubeMap(Loader loader, float r, float g, float b) {
        cube = loader.loadToVAO(VERTICES, 3);
        //texture = loader.loadCubeMap(textureFiles);
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public RawModel getCube() {
        return cube;
    }

    public String[] getTextureFiles() {
        return textureFiles;
    }

    public String[] getNightTextureFiles() {
        return night_textureFiles;
    }

}
