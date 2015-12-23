package me.thebutlah.opengltest;


import android.opengl.GLES20;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Represents a shader in the OpenGL state machine. Convenient abstraction from OpenGL's method of referring to everything as ints.
 * @author Ryan Butler
 */
public class Shader {
    /** The ID of the shader as used by OpenGL.  */
    public final int shaderID;
    /** The type of shader, such as GL_VERTEX_SHADER or GL_FRAGMENT_SHADER  */
    public final int shaderType;

    /**
     * Creates a new GLSL shader and stores its id. This id can then be used in OpenGL calls.
     * @param type The type of shader, such as GLES20.GL_VERTEX_SHADER or GLES20.GL_FRAGMENT_SHADER
     * @param shaderCode The code of the shader, with newline characters as appropriate.
     */
    public Shader(int type, String shaderCode) {
        shaderType = type;
        shaderID = GLES20.glCreateShader(type);
        GLES20.glShaderSource(shaderID, shaderCode);
        GLES20.glCompileShader(shaderID);
    }

    /**
     * Creates a new GLSL shader and stores its id. This id can then be used in OpenGL calls to do stuff with it.
     * @param type The type of shader, such as GL_VERTEX_SHADER or GL_FRAGMENT_SHADER
     * @param shaderStream An InputStream containing the code of the shader, with newline characters as appropriate.
     */
    public Shader(int type, InputStream shaderStream) {
        this(type,getShaderCodeFromStream(shaderStream));
    }

    /**
     * Reads the shader code from an input stream, which typically is from a file containing the GLSL code of the shader.
     * Will automagically take both unix and windows newline symbols and make them in the proper format for compiling the shader.
     * @param shaderStream The InputStream containing the shader code
     * @return A String containing the shader code
     */
    private static String getShaderCodeFromStream(InputStream shaderStream) {
        BufferedReader shaderReader = new BufferedReader(new InputStreamReader(shaderStream));

        StringBuilder shaderCode = new StringBuilder();
        try {
            String line;
            while(true) {
                line = shaderReader.readLine();
                if (line == null) break;
                shaderCode.append(line);
                shaderCode.append("\n");
            }
        } catch(IOException e) {
            Log.e(MainActivity.LOGGER_TAG, "Exception while trying to read from shader file!", e);
            System.exit(e.hashCode());
        }
        return shaderCode.toString();
    }

}
