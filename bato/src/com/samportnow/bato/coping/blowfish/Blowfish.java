package com.samportnow.bato.coping.blowfish;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

public class Blowfish extends Activity {
	
	private GLSurfaceView glSurface;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		glSurface = new GLSurfaceView(this);
		glSurface.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
		glSurface.setRenderer(new MyRenderer(this));
		setContentView(glSurface);
	}

	@Override
	protected void onResume() {
		super.onResume();
		glSurface.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		glSurface.onPause();
	}


}
