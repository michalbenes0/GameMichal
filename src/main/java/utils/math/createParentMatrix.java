package utils.math;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import renderEngine.entities.Parent;

public class createParentMatrix {

    public Matrix4f createParentMatrix(Parent parent) {
        Matrix4f parentMatrix = new Matrix4f();
        parentMatrix.setIdentity();
        Vector3f parentPos = parent.getPosition();

        Matrix4f.translate(parentPos, parentMatrix, parentMatrix);

        Matrix4f.rotate((float) Math.toRadians(parent.getRotX()), new Vector3f(1, 0, 0), parentMatrix, parentMatrix);
        Matrix4f.rotate((float) Math.toRadians(parent.getRotY()), new Vector3f(0, 1, 0), parentMatrix, parentMatrix);
        Matrix4f.rotate((float) Math.toRadians(parent.getRotZ()), new Vector3f(0, 0, 1), parentMatrix, parentMatrix);

        //Vector3f negativeCameraPos = new Vector3f(-cameraPos.x,-cameraPos.y,-cameraPos.z);
        //Matrix4f.translate(parentPos, parentMatrix, parentMatrix);
        return parentMatrix;
    }

    public static Matrix4f createParentMatrixStatic(Parent parent) {
        Matrix4f parentMatrix = new Matrix4f();
        parentMatrix.setIdentity();
        Vector3f parentPos = parent.getPosition();

        Matrix4f.translate(parentPos, parentMatrix, parentMatrix);

        Matrix4f.rotate((float) Math.toRadians(parent.getRotX()), new Vector3f(1, 0, 0), parentMatrix, parentMatrix);
        Matrix4f.rotate((float) Math.toRadians(parent.getRotY()), new Vector3f(0, 1, 0), parentMatrix, parentMatrix);
        Matrix4f.rotate((float) Math.toRadians(parent.getRotZ()), new Vector3f(0, 0, 1), parentMatrix, parentMatrix);

        //Vector3f negativeCameraPos = new Vector3f(-cameraPos.x,-cameraPos.y,-cameraPos.z);
        //Matrix4f.translate(parentPos, parentMatrix, parentMatrix);
        return parentMatrix;
    }
}
