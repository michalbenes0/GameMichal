package gameFunctions.Updates;

import java.awt.event.MouseEvent;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import renderEngine.DisplayManager;
import renderEngine.entities.Camera;

public class CameraMouse extends Thread {

    private Camera camera;
    private DisplayManager displayManager;
    private int mPosXTmp = 0;
    private int mPosYTmp = 0;

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    public void setDisplayManager(DisplayManager displayManager) {
        this.displayManager = displayManager;
    }

    public void run() {
        mPosXTmp = this.displayManager.display.getX() - 100;
        mPosYTmp = this.displayManager.display.getY() - 100;
        while (true) {
            try {
                if (Mouse.isInsideWindow()) {
                    // System.out.println("Mouse is in window!");
//					Mouse.setCursorPosition(this.displayManager.display.getX() - 100,
//							this.displayManager.display.getY() - 100);
                    Mouse.setGrabbed(true);
                    while (Mouse.isInsideWindow()) {
                        if ((mPosXTmp != Mouse.getX()) || (mPosYTmp != Mouse.getY())) {

//							float DX = ( Mouse.getX() - mPosXTmp)/9;
//							float DY = (( Mouse.getY() - mPosYTmp)/9)*-1;
                            float DX = Mouse.getDX() / 10;
                            float DY = -Mouse.getDY() / 10;

                            //System.out.print(DX);
                            //System.out.print("  ");
                            //System.out.print(DY + "  " + new Float( camera.getPitch() + DY ));
                            //System.out.println();
                            if (camera.getPitch() + DY >= -90 && camera.getPitch() + DY <= 90) {
                                camera.setPitch(camera.getPitch() + DY);
                            }
                            camera.setYaw(camera.getYaw() + DX);

//							mPosXTmp = this.displayManager.display.getX() - 100;
//							mPosYTmp = this.displayManager.display.getY() - 100;
//							Mouse.setCursorPosition(this.displayManager.display.getX() - 100,
//									this.displayManager.display.getY() - 100);
                        }
                    }
                    Mouse.setGrabbed(false);
                }
                this.sleep(1000 / 50);

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                this.stop();
            }
        }
    }
}
