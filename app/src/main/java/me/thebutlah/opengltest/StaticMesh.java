package me.thebutlah.opengltest;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

/**
 * WIP NOT FINISHED DONT USE
 */
public class StaticMesh {

    public static final int BYTES_PER_FLOAT = 4;
    public static final int BYTES_PER_INT = 4;
    public static final int VERTEX_POSITION_SIZE = 3;
    public static final int VERTEX_TEXTURE_COORDINATE_SIZE = 2;
    public static final int STRIDE = (VERTEX_POSITION_SIZE + VERTEX_TEXTURE_COORDINATE_SIZE) * BYTES_PER_FLOAT;

    private final FloatBuffer vertexData;
    private final int numVertices;
    private int vertexDataHandle;

    /*private final ByteBuffer textureData;
    private final int textureSize;*/
    private final Bitmap texture;
    private int textureHandle;

    private final IntBuffer indices;
    private int indicesHandle;

    private ShaderProgram program;
    private int attrib_vPosition;
    private int attrib_vUVCoords;
    private int uniform_mMVPMatrix;
    private int uniform_textureSampler;

    private float[] location = {0,0,0};
    private float[] rotation = {0,0,0};
    private float[] scale = {1,1,1};

    private boolean matrixNeedsUpdating = false;
    private float[] modelMatrix = new float[16];

    public StaticMesh(float[] vertexData, Bitmap texture, int[] vertexIndices) {
        numVertices = vertexIndices.length;
        {//Set up VBO
            //Represents the vertex data, but stored in a native buffer in RAM. Note that this is not a VBO.
            //vertexData.length * 4 because 1 float = 4 bytes
            this.vertexData = ByteBuffer.allocateDirect(vertexData.length * BYTES_PER_FLOAT).order(ByteOrder.nativeOrder()).asFloatBuffer();
            //load the vertex data from the Java heap into the native heap
            this.vertexData.put(vertexData);
            //set the pointer to the data as the first element
            this.vertexData.position(0);
        }
        {//set up texture
            this.texture = texture;
            /*Log.v(MainActivity.LOGGER_TAG, Integer.toString(this.textureSize));
            this.textureData = ByteBuffer.allocateDirect(textureData.length).order(ByteOrder.nativeOrder());
            this.textureData.put(textureData);
            this.textureData.position(0);*/
        }
        {//Set up IBO
            indices = ByteBuffer.allocateDirect(vertexIndices.length * BYTES_PER_INT).order(ByteOrder.nativeOrder()).asIntBuffer();
            indices.put(vertexIndices);
            indices.position(0);
        }


        Matrix.setIdentityM(modelMatrix, 0);
    }

    public void initialize(ShaderProgram program) {
        this.program = program;
        this.attrib_vPosition = GLES20.glGetAttribLocation(program.programID, "vPosition");
        this.attrib_vUVCoords = GLES20.glGetAttribLocation(program.programID, "vUVCoords");
        this.uniform_mMVPMatrix = GLES20.glGetUniformLocation(program.programID, "mMVPMatrix");
        this.uniform_textureSampler = GLES20.glGetUniformLocation(program.programID, "textureSampler");

        {//Generate texture and load it onto GPU
            int[] temp = new int[1];
            GLES20.glGenTextures(1,temp,0);
            textureHandle = temp[0];
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, texture, 0);
            //GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGB,textureSize,textureSize,0,GLES20.GL_RGB,GLES20.GL_UNSIGNED_BYTE, textureData);
        }
        {//Create and load data into VBO
            int[] temp = new int[1];
            //Get the handle for a VBO
            GLES20.glGenBuffers(1, temp, 0);
            vertexDataHandle = temp[0];
            //Set the VBO as active, future calls will be referring to it.
            GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vertexDataHandle);
            //Actually load the VBO, this places the data onto the GPU memory instead of RAM. The native and Java data can now be GCed
            GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, BYTES_PER_FLOAT * vertexData.capacity(), vertexData, GLES20.GL_STATIC_DRAW);
        }
        {//Create and load data into IBO
            int[] temp = new int[1];
            GLES20.glGenBuffers(1, temp, 0);
            indicesHandle = temp[0];
            GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, indicesHandle);
            GLES20.glBufferData(GLES20.GL_ELEMENT_ARRAY_BUFFER, BYTES_PER_INT * indices.capacity(), indices, GLES20.GL_STATIC_DRAW);
        }
        //Unbind the VBO. Think of this as setting the VBO that is currently active to null. Future OpenGL calls will now not affect VBO.
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
        //Unbind IBO
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, 0);
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

        {//Tell the shader program how the vertex VBO is formatted
            GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vertexDataHandle);
            GLES20.glEnableVertexAttribArray(attrib_vPosition);
            GLES20.glVertexAttribPointer(attrib_vPosition, VERTEX_POSITION_SIZE, GLES20.GL_FLOAT, false, STRIDE, 0);
            GLES20.glEnableVertexAttribArray(attrib_vUVCoords);
            GLES20.glVertexAttribPointer(attrib_vUVCoords, VERTEX_TEXTURE_COORDINATE_SIZE, GLES20.GL_FLOAT, false, STRIDE, VERTEX_POSITION_SIZE * BYTES_PER_FLOAT);
        }
        {//Tell the shader program to use the texture
            //set active texture unit to unit 0
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
            //bind the texture referenced by textureHandle to the texture unit that is now active
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,textureHandle);
            //tell the uniform used in the shader program to use texture unit zero as its sampler
            GLES20.glUniform1i(uniform_textureSampler,0);
        }
        GLES20.glUniformMatrix4fv(uniform_mMVPMatrix, 1, false, mvpMatrix, 0);

        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, indicesHandle);
        //This is the command where everything is drawn. Differs from glDrawArrays() because this one actually uses the IBO in conjunction with the VBO
        GLES20.glDrawElements(GLES20.GL_TRIANGLE_STRIP, indices.capacity(), GLES20.GL_UNSIGNED_INT, 0);
        GLES20.glDisableVertexAttribArray(attrib_vPosition);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, 0);
    }


}
