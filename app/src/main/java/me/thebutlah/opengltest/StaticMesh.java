package me.thebutlah.opengltest;

import android.opengl.GLES20;
import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * WIP NOT FINISHED DONT USE
 */
public class StaticMesh {

    private final FloatBuffer vertices;
    private int vertexDataHandle;

    private double[] location = {0,0,0};
    private float[] modelMatrix;

    public StaticMesh(float [] vertexData) {
        //Represents the vertex data, but stored in a native buffer in RAM. Note that this is not a VBO.
        //vertices.length * 4 because 1 float = 4 bytes
        vertices = ByteBuffer.allocateDirect(vertexData.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        //load the vertex data from the Java heap into the native heap
        vertices.put(vertexData);
        //set the pointer to the data as the first element
        vertices.position(0);

        Matrix.setIdentityM(modelMatrix,0);
    }

    public void initialize() {
        int[] temp = new int[1];
        //Get the handle for a VBO
        GLES20.glGenBuffers(1, temp, 0);
        vertexDataHandle = temp[0];
        //Set the VBO as active, future calls will be referring to it.
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vertexDataHandle);
        //Actually load the VBO, this places the data onto the GPU memory instead of RAM. The native and Java data can now be GCed
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, 4 * vertices.capacity(), vertices, GLES20.GL_STATIC_DRAW);
        //Unbind the VBO. Think of this as setting the VBO that is currently active to null. Future OpenGL calls will now not affect VBO.
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER,0);
    }

    public void setLocation(double x, double y, double z) {
        location[0] = x;
        location[1] = y;
        location[2] = z;
    }

    public void draw() {
    }


}
