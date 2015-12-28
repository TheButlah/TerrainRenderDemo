package me.thebutlah.opengltest;

import android.opengl.Matrix;

public class Camera {

    public float x;
    public float y;
    public float z;
    public float rx;
    public float ry;
    public float rz;

    private final float[] cameraMatrix = new float[16];

    private boolean matrixNeedsUpdating;

    public Camera(float x,float y,float z,float rx, float ry, float rz) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.rx = rx;
        this.ry = ry;
        this.rz = rz;
        updateMatrix();
        matrixNeedsUpdating = false;
    }

    private void updateMatrix() {
        Matrix.setIdentityM(cameraMatrix,0);
        Matrix.translateM(cameraMatrix,0,x,y,z);
        Matrix.rotateM(cameraMatrix,0,rx,1,0,0);
        Matrix.rotateM(cameraMatrix,0,ry,0,1,0);
        Matrix.rotateM(cameraMatrix, 0, rz, 0, 0, 1);
        matrixNeedsUpdating = false;
    }

    /**
     * Gets the camera matrix representing all the necessary transforms required
     * to convert from world-space to camera-space.
     * Note: DO NOT MODIFY THE RETURNED MATRIX!!!
     * This function directly returns the array used internally in this object for maximum speed.
     * Modifying this array will have unforseen consequences and should not be done.
     * @return An array of floats representing the 4x4 camera matrix.
     */
    public float[] getCameraMatrix() {
        if (matrixNeedsUpdating) updateMatrix();
        return cameraMatrix;
    }



}
