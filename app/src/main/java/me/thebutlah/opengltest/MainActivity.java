package me.thebutlah.opengltest;

import android.app.Activity;
import android.os.Bundle;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends Activity {

    public static final String LOGGER_TAG = "MainActivity";
    private MyGLSurfaceView glSurfaceView;

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        glSurfaceView = new MyGLSurfaceView(this);
        setContentView(glSurfaceView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        glSurfaceView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.v(LOGGER_TAG, "MainActivity.onResume() called!");
        glSurfaceView.onResume();
    }

    class MyGLSurfaceView extends GLSurfaceView {
        private final MyGLRenderer renderer;

        public MyGLSurfaceView(MainActivity context) {
            super(context);
            setEGLContextClientVersion(2);

            renderer = new MyGLRenderer(context);
            setRenderer(renderer);
        }


        @Override
        public boolean onTouchEvent(MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
            }
            return super.onTouchEvent(event);
        }
    }
}
