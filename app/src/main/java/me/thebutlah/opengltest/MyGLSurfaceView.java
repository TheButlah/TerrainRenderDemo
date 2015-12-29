package me.thebutlah.opengltest;

import android.opengl.GLSurfaceView;
import android.view.MotionEvent;
import android.view.View;

public class MyGLSurfaceView extends GLSurfaceView {

    public static final float LOOK_SPEED = .1f;

    private final MyGLRenderer renderer;

    private float lastX,lastY;
    public MyGLSurfaceView(MainActivity context) {
        super(context);
        setEGLContextClientVersion(2);

        renderer = new MyGLRenderer(context);
        setRenderer(renderer);
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        lastX = event.getX(0);
                        lastY = event.getY(0);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        float dx = event.getX(0) - lastX;
                        float dy = event.getY(0) - lastY;
                        lastX = event.getX(0);
                        lastY = event.getY(0);
                        renderer.camera.changeRotationBy(dy*LOOK_SPEED, -dx*LOOK_SPEED, 0);
                        break;
                    default: break;
                }
                return true;
            }
        });
    }
}