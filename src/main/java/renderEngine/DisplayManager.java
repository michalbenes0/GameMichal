package renderEngine;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;

public class DisplayManager {

    public static Display display;
    public static final int WIDTH = 1920;
    public static final int HEIGHT = 1080;

    public static void createDisplay() {
        ContextAttribs attribs = new ContextAttribs(3, 2);
        attribs.withForwardCompatible(true);
        attribs.withProfileCore(true);
        try {
//			display.setFullscreen(true);
            display.setResizable(true);
            display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
            display.create(new PixelFormat().withDepthBits(24), attribs);
        } catch (LWJGLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        GL11.glViewport(0, 0, WIDTH, HEIGHT);
    }

    public static void updateDisplay(int fps) {
        display.sync(fps);
        if (Display.wasResized()) {
            GL11.glViewport(0, 0, Display.getWidth(), Display.getHeight());
        };
        display.update();
    }

    public static void closeDisplay() {
        display.destroy();
        System.exit(0);
    }
}
