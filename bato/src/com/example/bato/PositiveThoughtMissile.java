package com.example.bato;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Handler;
import android.view.View;

public class PositiveThoughtMissile extends View
{
	DestroyerGame mDestroyer;
	Handler h;
	int height;
	int width;

	
	public PositiveThoughtMissile(Context context) 
	{	
		super(context);
		mDestroyer = (DestroyerGame) context; 
		init();
	}	
	
	
	public void init()
	{
		//set our drawing variables to be the same as the starting points. so we start drawing from there
		//set the background and text size
    	width = mDestroyer.width/3;
    	height = mDestroyer.height/4;
    	layout(0, 0, width, height);
	}
	
	
	@Override
	protected void onDraw(Canvas canvas)
	{
		
	}
	
	
}