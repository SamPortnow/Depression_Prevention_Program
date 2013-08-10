package com.samportnow.bato.coping.rollingball;


import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLSurfaceView.Renderer;
import android.util.Log;

/**
 * This is a port of the {@link http://nehe.gamedev.net} OpenGL 
 * tutorials to the Android 1.5 OpenGL ES platform. Thanks to 
 * NeHe and all contributors for their great tutorials and great 
 * documentation. This source should be used together with the
 * textual explanations made at {@link http://nehe.gamedev.net}.
 * The code is based on the original Visual C++ code with all
 * comments made. It has been altered and extended to meet the
 * Android requirements. The Java code has according comments.
 * 
 * If you use this code or find it helpful, please visit and send
 * a shout to the author under {@link http://www.insanitydesign.com/}
 * 
 * @DISCLAIMER
 * This source and the whole package comes without warranty. It may or may
 * not harm your computer or cell phone. Please use with care. Any damage
 * cannot be related back to the author. The source has been tested on a
 * virtual environment and scanned for viruses and has passed all tests.
 * 
 * 
 * This is an interpretation of "Lesson 02: Your First Polygon"
 * for the Google Android platform.
 * 
 * @author Savas Ziplies (nea/INsanityDesign)
 */
public class MyRenderer implements Renderer {
	
	/** Triangle instance */
	private Path path;
	private Ball ball;
	private float mTransY;
	private float mAngle;
	float x = 0.0f;
	float y = 0.0f;
	float z = 0.0f;
	/** Square instance */
	
	/**
	 * Instance the Triangle and Square objects
	 */
	public MyRenderer() {
		ball = new Ball(10,10,0.10f, 1.0f);
		path = new Path();
	}

	/**
	 * The Surface is created/init()
	 */
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {		
		gl.glShadeModel(GL10.GL_SMOOTH); 			//Enable Smooth Shading
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f); 	//Black Background
		gl.glClearDepthf(1.0f); 					//Depth Buffer Setup
		gl.glEnable(GL10.GL_DEPTH_TEST); 			//Enables Depth Testing
		gl.glDepthFunc(GL10.GL_LEQUAL); 			//The Type Of Depth Testing To Do
		
		//Really Nice Perspective Calculations
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST); 
	}

	/**
	 * Here we do our drawing
	 */
	public void onDrawFrame(GL10 gl) {
		//Clear Screen And Depth Buffer
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);	
		gl.glLoadIdentity();					//Reset The Current Modelview Matrix
		
		/*
		 * Minor changes to the original tutorial
		 * 
		 * Instead of drawing our objects here,
		 * we fire their own drawing methods on
		 * the current instance
		 */
		
		gl.glTranslatef(0.0f, 0.0f, -2.5f);
		path.draw(gl);
//		Log.e("x is", ""+x);
//		Log.e("y is", ""+y);
		Log.e("z is", "" +z);
		gl.glTranslatef(x, -z, 0);
		gl.glRotatef(mAngle, 1, 0, 0);
		ball.draw(gl);
        mAngle-=2.0;
	}	

	/**
	 * If the surface changes, reset the view
	 */
	public void onSurfaceChanged(GL10 gl, int width, int height) {
	     gl.glViewport(0, 0, width, height);
	 	
	     /*
	      * Set our projection matrix. This doesn't have to be done
	      * each time we draw, but usually a new projection needs to
	      * be set when the viewport is resized.
	      */
	     
	 	float aspectRatio;
		float zNear =.1f;
		float zFar =1000f;
		float fieldOfView = 30.0f/57.3f;
		float	size;
		
		gl.glEnable(GL10.GL_NORMALIZE);
		
		aspectRatio=(float)width/(float)height;				//h/w clamps the fov to the height, flipping it would make it relative to the width
		
		//Set the OpenGL projection matrix
		
		gl.glMatrixMode(GL10.GL_PROJECTION);
		
		size = zNear * (float)(Math.tan((double)(fieldOfView/2.0f)));
		gl.glFrustumf(-size, size, -size/aspectRatio, size /aspectRatio, zNear, zFar);
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		//Make the OpenGL modelview matrix the default
	}
}
