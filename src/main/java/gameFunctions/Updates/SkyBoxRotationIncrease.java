package gameFunctions.Updates;

import renderEngine.skybox.SkyboxShader;

public class SkyBoxRotationIncrease extends Thread {

    private SkyboxShader Skybox;

    public void setSkybox(SkyboxShader skybox) {
        Skybox = skybox;
    }

    public void run() {
        while (true) {
            //Skybox.rotation += Skybox.getRotateSpeed();
            try {
                this.sleep(50);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
