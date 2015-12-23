package me.thebutlah.opengltest;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


public class MyGLRenderer implements GLSurfaceView.Renderer {

    private final Context context;

    public MyGLRenderer(Context context) {
        this.context = context;
    }

    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        Log.v(MainActivity.LOGGER_TAG,"me.thebutlah.opengltest.MyGLRenderer.onSurfaceCreated() called!");
        InputStream basicVertexShaderStream = context.getResources().openRawResource(R.raw.basic_vertexshader);
        Shader basicVertexShader = new Shader(GLES20.GL_VERTEX_SHADER, basicVertexShaderStream);
        try {
            basicVertexShaderStream.close();
        } catch(IOException e) {
            Log.e(MainActivity.LOGGER_TAG, "Could not close the shader stream! However, we have already loaded the shader so it doesn't really matter!", e);
        }

        ShaderProgram shaderProgram = new ShaderProgram(basicVertexShader);
        // Set the background frame color
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
    }

    @Override
    public void onDrawFrame(GL10 unused) {
        // Redraw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
    }

}
