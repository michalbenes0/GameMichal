package renderEngine.entities;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

import renderEngine.terrains.Terrain;
import utils.math.degToRad;

public class Camera {

    private Vector3f position = new Vector3f(0, 0, 0);
    private float pitch;
    private float yaw;
    private float roll;
    private float speed = 0.05f;
    private Terrain terrain;
    private float cameraHeight = 1;
    private float maxJump = 0.6f;
    private boolean isRising = false;
    private boolean isFalling = false;
    private float fallSpeed = 0.1f;
    private float jumpHeight = 1f;
    private float jumpFromHeight = 0f;

    public Camera() {
    }

    public void setTerrain(Terrain terrain) {
        this.terrain = terrain;
    }

    private void gravity() {
        if (isFalling || isRising) {
            return;
        }
        float terrainHeight = this.terrain.getHeightOfTerrain(this.getPosition().x, this.getPosition().z);
        if (this.getPosition().y != terrainHeight + cameraHeight) {
            Vector3f newPosition = new Vector3f(this.getPosition().x, terrainHeight + cameraHeight, this.getPosition().z);
            this.setPosition(newPosition);
            //System.out.print(terrainHeight);
            //System.out.println();
        }
    }

    private boolean isLegal(float dx, float dz) {
        return true;
        /*
		for (int i=1;i<=10;i++) {
			float multiplier = i / 10;
			float posX = this.getPosition().x + (multiplier * dx);
			float posZ = this.getPosition().z + (multiplier * dz);
			float terrainHeight = this.terrain.getHeightOfTerrain(posX,posZ);
			float posY = this.getPosition().y-cameraHeight;
			System.out.println(terrainHeight-posY);
			if (terrainHeight-posY>maxJump) {
				return false;
			}
		}
		return true;
         */
    }

    private float[] WhenIsLegal(float dx, float dz) {
        for (int i = 1; i <= 10; i++) {
            float multiplier = i / 10;
            float posX = this.getPosition().x + (multiplier * dx);
            float posZ = this.getPosition().z + (multiplier * dz);
            float terrainHeight = this.terrain.getHeightOfTerrain(posX, posZ);
            float posY = this.getPosition().y - cameraHeight;
            System.out.println(terrainHeight - posY);
            if (terrainHeight - posY > maxJump) {
                float[] answer = new float[2];
                if (multiplier > 0) {
                    answer[0] = (multiplier - 1) * dx;
                    answer[1] = (multiplier - 1) * dz;
                } else {
                    answer[0] = 0;
                    answer[1] = 0;
                }
                return answer;
            }
        }
        float[] answer = new float[2];
        answer[0] = dx;
        answer[1] = dz;
        return answer;
    }

    /*private float getSpeed(float dx,float dz) {
		float posX = this.getPosition().x + dx;
		float posZ = this.getPosition().z + dz;
		float terrainHeight = this.terrain.getHeightOfTerrain(posX,posZ);
		float posY = this.getPosition().y-1;
		return ((Math.max(terrainHeight, posY) - Math.min(terrainHeight, posY)) / Math.max(terrainHeight, posY)) * speed;
	}*/
    public void move() {

        if (isFalling) {
            float terrainHeight = this.terrain.getHeightOfTerrain(this.getPosition().x, this.getPosition().z);
            if (this.getPosition().y - fallSpeed > terrainHeight + cameraHeight) {
                Vector3f newPosition = new Vector3f(this.getPosition().x, this.getPosition().y - fallSpeed, this.getPosition().z);
                this.setPosition(newPosition);
            } else {
                Vector3f newPosition = new Vector3f(this.getPosition().x, terrainHeight + cameraHeight, this.getPosition().z);
                this.setPosition(newPosition);
            }
            if (this.getPosition().y == terrainHeight + cameraHeight) {
                isFalling = false;
            }
        }

        if (isRising) {
            if (jumpFromHeight + jumpHeight > position.y + fallSpeed) {
                position.y += fallSpeed;
            } else {
                position.y = jumpFromHeight + jumpHeight;
            }
            if (jumpFromHeight + jumpHeight <= position.y) {
                isFalling = true;
                isRising = false;
            }
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
            //position.z-=0.02f;
            /*
			if (yaw < 91) {
				position.z-=(0.02f*(90-yaw))/90;
				position.x+=(0.02f*(yaw))/90;
			}
			if (yaw > 269) {
				position.z-=(0.02f*(yaw))/360;
				position.x+=(0.02f*(360-yaw))/360;
			}
             */
            float positionXIncrease = 0f;
            float positionZIncrease = 0f;
            positionZIncrease -= speed * Math.cos(degToRad.DegToRad((float) yaw));
            positionXIncrease += speed * Math.sin(degToRad.DegToRad((float) yaw));
            if (isLegal(positionXIncrease, positionZIncrease)) {
                if (isRising || isFalling) {
                    positionXIncrease *= 3;
                    positionZIncrease *= 3;
                }
                position.z += positionZIncrease;
                position.x += positionXIncrease;
                gravity();
            } else {
                float[] newIncreases = WhenIsLegal(positionXIncrease, positionZIncrease);
                positionXIncrease = newIncreases[0];
                positionZIncrease = newIncreases[1];
                if (isRising || isFalling) {
                    positionXIncrease *= 3;
                    positionZIncrease *= 3;
                }
                position.z += positionZIncrease;
                position.x += positionXIncrease;
                gravity();
            }
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
            //position.z+=0.02f;
            /*
			if (yaw < 91) {
				position.z+=(0.02f*(90-yaw))/90;
				position.x-=(0.02f*(yaw))/90;
			}
			if (yaw > 269) {
				position.z+=(0.02f*(yaw))/360;
				position.x-=(0.02f*(360-yaw))/360;
			}
             */
            gravity();
            position.z += speed * Math.cos(degToRad.DegToRad((float) yaw));
            position.x -= speed * Math.sin(degToRad.DegToRad((float) yaw));
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
            //position.x+=0.02f;
            gravity();
            position.x += speed * Math.cos(degToRad.DegToRad((float) yaw));
            position.z += speed * Math.sin(degToRad.DegToRad((float) yaw));
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
            //position.x-=0.02f;
            gravity();
            position.x -= speed * Math.cos(degToRad.DegToRad((float) yaw));
            position.z -= speed * Math.sin(degToRad.DegToRad((float) yaw));
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
            if (!isFalling && !isRising) {
                position.y += jumpHeight;
                isRising = true;
                jumpFromHeight = position.y;
            }
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            gravity();
            position.y -= speed;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
            pitch -= 0.5f;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
            pitch += 0.5f;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
            yaw -= 0.5f;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
            yaw += 0.5f;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_PRIOR)) {
            speed += 0.01f;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_NEXT)) {
            speed -= 0.01f;
        }
    }

    public Vector3f getPosition() {
        return position;
    }

    public float getPitch() {
        return pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public float getRoll() {
        return roll;
    }

    public float getSpeed() {
        return speed;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public void setRoll(float roll) {
        this.roll = roll;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

}
