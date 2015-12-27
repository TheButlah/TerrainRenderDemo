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
    private final int numVertices;
    private int vertexVBOHandle;

    private ShaderProgram program;
    private int attrib_vPosition;
    private int uniform_mMVPMatrix;

    private float[] location = {0,0,0};
    private float[] rotation = {0,0,0};
    private float[] scale = {1,1,1};

    private boolean matrixNeedsUpdating = false;
    private float[] modelMatrix = new float[16];

    public StaticMesh(float [] vertexData) {
        numVertices = vertexData.length;
        //Represents the vertex data, but stored in a native buffer in RAM. Note that this is not a VBO.
        //vertices.length * 4 because 1 float = 4 bytes
        vertices = ByteBuffer.allocateDirect(vertexData.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        //load the vertex data from the Java heap into the native heap
        vertices.put(vertexData);
        //set the pointer to the data as the first element
        vertices.position(0);

        Matrix.setIdentityM(modelMatrix, 0);
    }

    public void initialize(ShaderProgram program) {
        this.program = program;
        this.attrib_vPosition = GLES20.glGetAttribLocation(program.programID, "vPosition");
        this.uniform_mMVPMatrix = GLES20.glGetUniformLocation(program.programID, "mMVPMatrix");

        int[] temp = new int[1];
        //Get the handle for a VBO
        GLES20.glGenBuffers(1, temp, 0);
        vertexVBOHandle = temp[0];
        //Set the VBO as active, future calls will be referring to it.
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vertexVBOHandle);
        //Actually load the VBO, this places the data onto the GPU memory instead of RAM. The native and Java data can now be GCed
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, 4 * vertices.capacity(), vertices, GLES20.GL_STATIC_DRAW);
        //Unbind the VBO. Think of this as setting the VBO that is currently active to null. Future OpenGL calls will now not affect VBO.
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
    }

    public void setLocation(float x, float y, float z) {
        location[0] = x;
        location[1] = y;
        location[2] = z;
        matrixNeedsUpdating = true;
    }

    public void setRotation(float rx, float ry, float rz) {
        rotation[0] = rx;
        rotation[1] = ry;
        rotation[2] = rz;
        matrixNeedsUpdating = true;
    }

    public void setScale(float xFactor, float yFactor, float zFactor) {
        scale[0] = xFactor;
        scale[1] = yFactor;
        scale[2] = zFactor;
        matrixNeedsUpdating = true;
    }





    public void draw(float[] viewProjectionMatrix) {
        if (matrixNeedsUpdating) {
            Matrix.setIdentityM(modelMatrix, 0);
            Matrix.scaleM(modelMatrix, 0, scale[0], scale[1], scale[2]);
            Matrix.rotateM(modelMatrix, 0, rotation[0], 1, 0, 0);
            Matrix.rotateM(modelMatrix, 0, rotation[1], 0, 1, 0);
            Matrix.rotateM(modelMatrix,0,rotation[2],0,0,1);
            Matrix.translateM(modelMatrix,0,location[0],location[1],location[2]);
            matrixNeedsUpdating = false;
        }
        float[] mvpMatrix = new float[16];
        Matrix.multiplyMM(mvpMatrix, 0, viewProjectionMatrix, 0, modelMatrix, 0);

        GLES20.glUseProgram(program.programID);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vertexVBOHandle);
        GLES20.glEnableVertexAttribArray(attrib_vPosition);
        GLES20.glVertexAttribPointer(attrib_vPosition, 4, GLES20.GL_FLOAT, false, 0, 0);
        GLES20.glUniformMatrix4fv(uniform_mMVPMatrix, 1, false, mvpMatrix, 0);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, numVertices);
        GLES20.glDisableVertexAttribArray(attrib_vPosition);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);

    }


}
