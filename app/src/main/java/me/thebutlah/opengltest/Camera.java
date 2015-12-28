package me.thebutlah.opengltest;

import android.opengl.Matrix;

public class Camera {

    public float x;
    public float y;
    public float z;
    public float rx;
    public float ry;
    public float rz;

    private final float[] viewMatrix = new float[16];

    private boolean matrixNeedsUpdating;

    /**
     * Creates a new Camera.
     * @param x X location in world coordinates
     * @param y Y location in world coordinates
     * @param z Z location in world coordinates
     * @param rx Degrees "up" to look
     * @param ry Degrees "right" to look
     * @param rz Degrees clockwise to rotate head (Will cause world to rotate counter-clockwise)
     */
    public Camera(float x,float y,float z,float rx, float ry, float rz) {
        setLocation(x,y,z);
        setRotation(rx,ry,rz);
        updateMatrix();
    }

    private void updateMatrix() {
        Matrix.setIdentityM(viewMatrix,0);
        Matrix.rotateM(viewMatrix, 0, -rx, 1, 0, 0);
        Matrix.rotateM(viewMatrix,0,ry,0,1,0);
        Matrix.rotateM(viewMatrix, 0, rz, 0, 0, 1);
        Matrix.translateM(viewMatrix, 0, -x, -y, -z);
        matrixNeedsUpdating = false;
    }

    /**
     * Gets the view matrix representing all the necessary transforms required
     * to convert from world-space to camera-space.
     * Note: DO NOT MODIFY THE RETURNED MATRIX!!!
     * This function directly returns the array used internally in this object for maximum speed.
     * Modifying this array will have unforeseen consequences and should not be done.
     * @return An array of floats representing the 4x4 view matrix.
     */
    public float[] getViewMatrix() {
        if (matrixNeedsUpdating) updateMatrix();
        return viewMatrix;
    }

    /**
     * Sets the location of this camera
     * @param x X location in world coordinates
     * @param y Y location in world coordinates
     * @param z Z location in world coordinates
     */
    public void setLocation(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
        matrixNeedsUpdating = true;
    }

    /**
     * Sets the rotation of this camera
     * @param rx Degrees "up" to look
     * @param ry Degrees "right" to look
     * @param rz Degrees clockwise to rotate head (Will cause world to rotate counter-clockwise)
     */
    public void setRotation(float rx, float ry, float rz) {
        this.rx = rx;
        this.ry = ry;
        this.rz = rz;
        matrixNeedsUpdating = true;
    }

    public void changeLocationBy(float x, float y, float z) {
        this.x += x;
        this.y += y;
        this.z += z;
        matrixNeedsUpdating = true;
    }

    /**
     * Changes the rotation of this camera.
     * @param rx Degrees "up" to look
     * @param ry Degrees "right" to look
     * @param rz Degrees clockwise to rotate head (Will cause world to rotate counter-clockwise)
     */
    public void changeRotationBy(float rx, float ry, float rz) {
        this.rx += rx;
        this.ry += ry;
        this.rz += rz;
        matrixNeedsUpdating = true;
    }

    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }

    public float getZ() {
        return this.z;
    }

    public float getRX() {
        return this.rx;
    }

    public float getRY() {
        return this.ry;
    }

    public float getRZ() {
        return this.rz;
    }



}
