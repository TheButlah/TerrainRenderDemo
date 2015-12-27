package me.thebutlah.opengltest;

import android.opengl.GLES20;
import android.util.Log;

/**
 * Represents a shader program in the OpenGL state machine. Convenient abstraction from OpenGL's method of referring to everything as ints.
 * @author Ryan Butler
 */
public class ShaderProgram {

    /** The ID of the shader program as used by OpenGL **/
    public final int programID;

    /**
     * Creates a new GLSL shader program by linking together the given shaders and stores the program's id.
     * This id can then be used in OpenGL calls.
     * @param shaders One or more Shader objects to link together into a ShaderProgram
     */
    public ShaderProgram(Shader... shaders ) {
        if (shaders.length == 0) {
            Log.e(MainActivity.LOGGER_TAG, "Tried to initialize a shader program without any shaders!");
            System.exit(1);
        }
        programID = GLES20.glCreateProgram();
        for (Shader shader : shaders) {
            GLES20.glAttachShader(programID,shader.shaderID);
        }
        GLES20.glLinkProgram(programID);
        int[] linkStatus = new int[1];
        GLES20.glGetProgramiv(programID, GLES20.GL_LINK_STATUS, linkStatus, 0);
        if (linkStatus[0] != GLES20.GL_TRUE) {
            String info = GLES20.glGetProgramInfoLog(programID);
            Log.e(MainActivity.LOGGER_TAG, "Shader program failed to link! Info:\n" + info);
            System.exit(1);
        }
    }

}
