package me.thebutlah.opengltest;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends Activity {

    public static final String LOGGER_TAG = "OpenGLTest.MainActivity";
    private MyGLSurfaceView glSurfaceView;

    public float[] sensorData;

    @Override
    protected void onStart() {
        super.onStart();
        SensorManager sman = (SensorManager) getSystemService(SENSOR_SERVICE);
        sman.registerListener(new SensorEventListener() {

            @Override
            public void onSensorChanged(SensorEvent event) {
                sensorData = event.values;
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        }, sman.getDefaultSensor(Sensor.TYPE_GYROSCOPE), SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN ,WindowManager.LayoutParams.FLAG_FULLSCREEN);

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
        glSurfaceView.onResume();
    }

    class MyGLSurfaceView extends GLSurfaceView {
        private final MyGLRenderer renderer;

        public MyGLSurfaceView(Context context) {
            super(context);
            setEGLContextClientVersion(2);

            renderer = new MyGLRenderer(context);
            setRenderer(renderer);
        }
    }
}
