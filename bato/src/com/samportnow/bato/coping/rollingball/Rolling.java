package com.samportnow.bato.coping.rollingball;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.opengl.GLSurfaceView;
import android.os.Bundle;


public class Rolling extends Activity implements SensorEventListener {
	
	private SensorManager sensorManager;
	double ax, ay, az;
	
	GLSurfaceView glSurface;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		glSurface = new GLSurfaceView(this);
		glSurface.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
		glSurface.setRenderer(new MyRenderer(this));
		setContentView(glSurface);
        sensorManager=(SensorManager) getSystemService(SENSOR_SERVICE);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType()==Sensor.TYPE_ACCELEROMETER){
            ax=event.values[0];
                    ay=event.values[1];
                    az=event.values[2];		
        	}
	}
}
