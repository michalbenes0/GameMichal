package gameFunctions.Updates;

import renderEngine.entities.Camera;

public class CameraKeyboard extends Thread {

    private Camera camera;

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    @Override
    public void run() {
        while (true) {
            try {
                camera.move();
                this.sleep(1000 / 50);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
