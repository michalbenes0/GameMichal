package main;

import java.awt.DisplayMode;
import java.awt.Font;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import GUIs.settings.guiSetting;
import gameFunctions.Updates.CameraGravity;
import gameFunctions.Updates.CameraKeyboard;
import gameFunctions.Updates.CameraMouse;
import gameFunctions.Updates.SkyBoxRotationIncrease;
import gameFunctions.Updates.SkyboxTimeIncrease;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.renderer;
import renderEngine.entities.Entity;
import renderEngine.entities.Light;
import renderEngine.entities.Parent;
import renderEngine.entities.Text;
import renderEngine.shaders.StaticShader;
import renderEngine.skybox.CubeMap;
import renderEngine.terrains.Terrain;
import renderEngine.textures.ModelTexture;
import renderEngine.entities.Camera;

public class Main {

    public static void main(String[] args) {

        guiSetting setting = new guiSetting();

        setting.build();

        DisplayManager displayManager = new DisplayManager();
        displayManager.createDisplay();
        Loader loader = new Loader();
        StaticShader shader = new StaticShader();
        Camera camera = new Camera();
        renderer renderer = new renderer(camera, loader);
        //Light light = new Light(new Vector3f(20000,20000,2000),new Vector3f(1,1,1));
        Light light = new Light(new Vector3f(0, 1024, 0), new Vector3f(1, 1, 1));
        CameraKeyboard cameraKeyboard = new CameraKeyboard();
        cameraKeyboard.setCamera(camera);
        cameraKeyboard.start();
        CameraMouse cameraMouse = new CameraMouse();
        cameraMouse.setCamera(camera);
        cameraMouse.setDisplayManager(displayManager);
        cameraMouse.start();
        camera.setPitch(0);
        SkyBoxRotationIncrease skyBoxRotationIncrease = new SkyBoxRotationIncrease();
        skyBoxRotationIncrease.setSkybox(renderer.getSkyboxRenderer().getShader());
        //skyBoxRotationIncrease.start();

        SkyboxTimeIncrease skyboxTimeIncrease = new SkyboxTimeIncrease();
        skyboxTimeIncrease.setSkybox(renderer.getSkyboxRenderer());
        skyboxTimeIncrease.start();

        renderer.skyColor_red = 0.2f;
        renderer.skyColor_green = 0.2f;
        renderer.skyColor_blue = 0.2f;

        camera.setPosition(new Vector3f(1, 0, 1));

        Terrain terrain = new Terrain(0, 0, loader, new ModelTexture(loader.loadTexture("grass")));

        camera.setTerrain(terrain);

        CameraGravity cameraGravity = new CameraGravity();
        cameraGravity.setCamera(camera);
        cameraGravity.setTerrain(terrain);
        //cameraGravity.start();

        //Terrain terrain2 = new Terrain(1,0,loader,new ModelTexture(loader.loadTexture("grass")));
        //Entity entity = new Entity(loader, shader, renderer, camera, vertices, textureCoords, indices, "image");
        //Entity entity2 = new Entity(loader, shader, renderer, camera, vertices2, textureCoords2, indices2, "image2");
        //entity2.setPosition(new Vector3f(0.5f, 0.5f, 0));
        //entity.increasePosition(0, 0, -1);
        CubeMap sky = new CubeMap(loader, 0.2f, 0.2f, 0.2f);
        renderer.processCubeMap(sky);

        Parent parent = new Parent();

        Entity entity = new Entity(loader, shader, renderer, camera, parent, "tree");
        //entity.addDuplicate(new Vector3f(-2,0,0), 0, 0, 0, 1);
        entity.increasePosition(2, 0, 0);
        //entity.setReflectionFactor(1);
        //entity.getModel().getTexture().setReflectivity(1);

        Entity entity2 = new Entity(loader, shader, renderer, camera, parent, "tree");
        //entity.addDuplicate(new Vector3f(-2,0,0), 0, 0, 0, 1);
        entity2.increasePosition(-2, 0, 0);

        camera.setPosition(new Vector3f(100, 0, 100));

        boolean isF11pressed = false;
        boolean isF11released = false;

        /*try {
			for (org.lwjgl.opengl.DisplayMode mode: Display.getAvailableDisplayModes()) {
				if (mode.isFullscreenCapable() &&
					mode.getHeight() == Display.getHeight() &&
					mode.getWidth() == Display.getWidth()
						) {
					displayManager.display.setDisplayModeAndFullscreen(mode);
				}
			}
		} catch (LWJGLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println(displayManager.display.isFullscreen());
         */
        //entity.getParent().setPosition(new Vector3f(1,0,0));
        while (!Display.isCloseRequested()) {

            if (Keyboard.isKeyDown(Keyboard.KEY_F11)) {
                isF11pressed = true;
            } else {
                if (isF11pressed) {
                    isF11released = true;
                    isF11pressed = false;
                }
            }

            if (isF11released) {
                try {
                    displayManager.display.setFullscreen(!displayManager.display.isFullscreen());
                } catch (LWJGLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                isF11released = false;
            }

            renderer.prepare();

            renderer.fogDensity = setting.getFogDensity();

            Vector3f position = parent.getPosition();
            //parent.setPosition(new Vector3f((float) (position.x+0.002),0,0));

            parent.setRotY((float) (entity.getParent().getRotY() + 1));

            //System.out.println(setting.getFogDensity());
            // Text.drawString(Float.toString(camera.getSpeed()), 0, 0, 0.4f, 3);
            //camera.move();
            //shader.start();
            // renderer.render(entity2,shader);
            // renderer.render(entity,shader);
            // shader.stop();
            //entity.addDuplicate(new Vector3f(-0.5f, -0.5f, 0), 0, 0, 0, 1);
            //entity.removeDuplicate(-1);
            //entity.render();
            //entity2.render();
            //renderer.renderText(Float.toString(camera.getSpeed()));
            renderer.processTerrain(terrain);
            //renderer.processTerrain(terrain2);
            entity.render();
            entity2.render();
            //renderer.processEntity(entity);
            renderer.render(light, camera);
            DisplayManager.updateDisplay(setting.getFPS());
        }
        DisplayManager.closeDisplay();

    }

}
