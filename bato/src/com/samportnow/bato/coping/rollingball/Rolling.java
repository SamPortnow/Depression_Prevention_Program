package com.samportnow.bato.coping.rollingball;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.os.Bundle;
import android.util.Log;


public class Rolling extends Activity implements SensorEventListener {
	
	private SensorManager sensorManager;
	private MyRenderer MyRenderer;
	double ax, ay, az;
	float xVal[] = new float[2];
	GLSurfaceView glSurface;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		glSurface = new GLSurfaceView(this);
		glSurface.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
		MyRenderer = new MyRenderer();
		glSurface.setRenderer(MyRenderer);
		setContentView(glSurface);
        sensorManager=(SensorManager) getSystemService(SENSOR_SERVICE);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME);
	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType()==Sensor.TYPE_ACCELEROMETER)
        {
            xVal[0] = event.values[0];
            float delta = xVal[0] - xVal[1];
            Log.e("x is", "" + xVal[0]);
            xVal[1] = xVal[0];
            if (Math.abs(delta) > .02f)
            {
            if (!(MyRenderer.x < -.5) && !(MyRenderer.x > .5))
            	{
            		if (xVal[0] < 1)
            		{
            			MyRenderer.x += .010;
            		}
            		else
            		{
            			MyRenderer.x -= .010;
            		}
            	}
            }
        }
	}
}
