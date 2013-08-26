package com.samportnow.bato.coping.blowfish;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLU;

public class MyRenderer implements Renderer
{
	private Square square;
	private float startTime;
	float xScale = .003f;
	float yScale = .003f;
	float mStart[] = {.5f, .5f};
	float rot;
	Context context;
	
	public MyRenderer(Context ctx)
	{
		context = ctx;
		square = new Square(ctx);
	}

	@Override
	public void onDrawFrame(GL10 gl) 
	{
		if ((System.nanoTime() - startTime) < 4000000000L)
		{
			if (mStart[0] + xScale > .5f)
			{
				mStart[0] += xScale;
				mStart[1] += yScale;
			}
		}
		if ((System.nanoTime() - startTime) > 5000000000L)
		{
			xScale = xScale * -1;
			yScale = yScale * -1;
			startTime = System.nanoTime();
		}
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		gl.glLoadIdentity();
		gl.glTranslatef(0.0f, 0.0f, -6.0f);
		gl.glScalef(mStart[0], mStart[1], 0.0f);
		gl.glRotatef(270.0f, 0.0f, 0.0f, 1.0f);	//Rotate The Triangle On The Y axis ( NEW )
		square.draw(gl);
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) 
	{
		if (height == 0)
		{
			height = 1;
		}
		
		gl.glViewport(0, 0, width, height);
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		
		GLU.gluPerspective(gl, 45.0f, (float)width / (float)height, 0.1f, 100.0f);
		
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();

	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig arg1) 
	{
		square.loadTexture(gl, this.context);
		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		gl.glClearColor(0.0f, 0.0f, 1.0f, 0.5f);
		gl.glClearDepthf(1.0f);
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
		startTime = System.nanoTime();
	}
	

}
