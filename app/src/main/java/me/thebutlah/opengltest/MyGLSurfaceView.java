package me.thebutlah.opengltest;

import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.MotionEvent;

class MyGLSurfaceView extends GLSurfaceView {
    private final MyGLRenderer renderer;

    private float lastX,lastY;
    public MyGLSurfaceView(MainActivity context) {
        super(context);
        setEGLContextClientVersion(2);

        renderer = new MyGLRenderer(context);
        setRenderer(renderer);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.v(MainActivity.LOGGER_TAG, "onTouchEvent: " + event.getAction());
        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = event.getX(0);
                lastY = event.getY(0);
                break;
            case MotionEvent.ACTION_MOVE:
                float dx = event.getX(0) - lastX;
                float dy = event.getY(0) - lastY;
                Log.v(MainActivity.LOGGER_TAG, String.format("dx: %5.3f, dy: %5.3f", dx, dy));
                renderer.camera.changeRotationBy(dx, dy, 0);
                break;
            default: break;
        }
        return super.onTouchEvent(event);
    }
}