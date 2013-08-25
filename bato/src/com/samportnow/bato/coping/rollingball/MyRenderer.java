package com.samportnow.bato.coping.rollingball;


import java.util.Random;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLSurfaceView.Renderer;
import android.util.Log;

public class MyRenderer implements Renderer {
	
	float x;
	private Path path;
	private Ball ball;
	private Ring ring[] = new Ring[3];
	private float mAngle;
	private Context context;
	private float mStartTime;
	int mNumberOnScreen;

	public MyRenderer(Context ctx) {
		context = ctx;
		ball = new Ball(10,10,0.10f, 1.0f);
		path = new Path();
		for (int i = 0; i < 3; i++)
		{
			ring[i] = new Ring(ctx);
		}
	}

	/**
	 * The Surface is created/init()
	 */
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {	
		
		
		// load the textures and the start positions...
		for (int i = 0; i < 3; i++)
		{
			ring[i].loadTexture(gl, this.context);

		}
		generateRingInfo(ring[0]);
		mNumberOnScreen++;
	
		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		gl.glShadeModel(GL10.GL_SMOOTH); 			//Enable Smooth Shading
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f); 	//Black Background
		gl.glClearDepthf(1.0f); 					//Depth Buffer Setup
		gl.glEnable(GL10.GL_DEPTH_TEST); 			//Enables Depth Testing
		gl.glDepthFunc(GL10.GL_LEQUAL); 			//The Type Of Depth Testing To Do
		
		//Really Nice Perspective Calculations
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST); 
		mStartTime = System.nanoTime();
		
		
	}

	/**
	 * Here we do our drawing
	 */
	public void onDrawFrame(GL10 gl) 
	{
		//Clear Screen And Depth Buffer
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);	
		gl.glLoadIdentity();					//Reset The Current Modelview Matrix
		gl.glTranslatef(0.0f, 0.0f, -2.5f);
		gl.glDisable(GL10.GL_TEXTURE_2D);
		path.draw(gl);	
		// translate to 0, 0, 0
		gl.glTranslatef(0.0f, 0.0f, 0.0f);
        for (int i = 0; i < mNumberOnScreen; i++)
        {
        	// save the matrix
        	gl.glPushMatrix();
        	// translate it
        	gl.glTranslatef(ring[i].xPos, ring[i].yPos, 0.0f);
        	// scale it
        	gl.glScalef(ring[i].scale, ring[i].scale, 0.0f);
        	//rotate it
            gl.glRotatef(mAngle, 0, 1, 0);
            //enable texture binding
    		gl.glEnable(GL10.GL_TEXTURE_2D);
        	ring[i].draw(gl);
        	// pop it
        	gl.glPopMatrix();
    		ring[i].xPos += ring[i].xChange;
    		ring[i].yPos -= .02;
        	ring[i].scale += .001;
        	
        	if (ring[i].yPos < .5f)
        	{
				if (mNumberOnScreen < 2)
				{
	        		generateRingInfo(ring[mNumberOnScreen]);
					mNumberOnScreen++;
				}
        	}
        	// simple collision detection

        	if (x >= ring[i].xPos-.1 && x <= ring[i].xPos+.1 && ring[i].yPos < 0.0f)
        	{
        		generateRingInfo(ring[i]);
        	}
        	else if (ring[i].yPos < -1.5f)
        	{
        		generateRingInfo(ring[i]);
        	}
        		
        }
		gl.glTranslatef(x, 0, 0);
        gl.glRotatef(mAngle, 1, 0, 0);
		gl.glDisable(GL10.GL_TEXTURE_2D);
		ball.draw(gl);
		mAngle -= 2.0;
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
		
		//gl.glEnable(GL10.GL_NORMALIZE);
		
		aspectRatio=(float)width/(float)height;				//h/w clamps the fov to the height, flipping it would make it relative to the width
		
		//Set the OpenGL projection matrix
		
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		
		size = zNear * (float)(Math.tan((double)(fieldOfView/2.0f)));
		gl.glFrustumf(-size, size, -size/aspectRatio, size /aspectRatio, zNear, zFar);
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		//Make the OpenGL modelview matrix the default
	}
	
	
	public float generateChangeInX()
	{
		Random rando = new Random();
		float xChange = (float) ((rando.nextFloat() * (0.01)) * (float) (Math.random() < 0.5 ? -1 : 1));
		return xChange;
//		return (float) (.02/slope);
	}

	public float generateRandomTime()
	{
		Random rando = new Random();
		float fl_rando = (float) (rando.nextFloat() * (2.5-1.25) + 1.25);
		return fl_rando;
	}
	public void generateRingInfo(Ring ring)
	{
		// generate the information
		ring.scale = 0.1f;
		ring.yPos = 1.0f;
		ring.xChange= generateChangeInX();
		ring.xPos = 0.0f;
	}
}
