package com.samportnow.bato.coping.rollingball;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.opengl.GLSurfaceView;
import android.os.Bundle;


public class Rolling extends Activity implements SensorEventListener {
	
	private SensorManager sensorManager;
	private MyRenderer MyRenderer;
	double ax, ay, az;
	static final float ALPHA = 0.8f;
	protected float[] accelVals;
	GLSurfaceView glSurface;
	Sensor accelerometer;
	List<Float> avgVals = new ArrayList<Float>(); 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		glSurface = new GLSurfaceView(this);
		glSurface.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
		MyRenderer = new MyRenderer(this);
		glSurface.setRenderer(MyRenderer);
		setContentView(glSurface);
        sensorManager=(SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
		}
	
	@Override
	protected void onPause() {
		super.onPause();
		sensorManager.unregisterListener(this);
		}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		
	}
	
	@Override
	public void onSensorChanged(SensorEvent event) 
	{
	        accelVals = lowPass( event.values, accelVals );
	        avgVals.remove(0);
	        avgVals.add(accelVals[0]);
	        float sum = 0;
	        for (int i = 0; i < avgVals.size(); i++)
	        {
	        	sum+= avgVals.get(i);
	        }
	        sum = sum/5;
	    	MyRenderer.x = sum;

	    // use smoothed accelVals here; see this link for a simple compass example:
	    // http://www.codingforandroid.com/2011/01/using-orientation-sensors-simple.html
	}

	/**
	 * @see http://en.wikipedia.org/wiki/Low-pass_filter#Algorithmic_implementation
	 * @see http://developer.android.com/reference/android/hardware/Sensor.html#TYPE_ACCELEROMETER
	 */
	protected float[] lowPass( float[] input, float[] output ) {
	    if ( output == null ) return input;

	    for ( int i=0; i<input.length; i++ ) {
	        output[i] = output[i] + ALPHA * (input[i] - output[i]);
	    }
	    return output;
	}

}
