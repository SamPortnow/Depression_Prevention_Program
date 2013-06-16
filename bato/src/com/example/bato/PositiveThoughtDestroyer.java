package com.example.bato;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.widget.TextView;

public class PositiveThoughtDestroyer extends TextView
{
	DestroyerGame mDestroyer;
	Handler h = new Handler();
	int FRAME_RATE = 30;
	int height;
	int width;
	int mCount;
	int xPos;
	int yPos;
	int alpha;
	
	public PositiveThoughtDestroyer(Context context, int count) 
	{	
		super(context);
		mCount = count;
		mDestroyer = (DestroyerGame) context; 
		init();
	}	
	
	
	public void init()
	{
		//set our drawing variables to be the same as the starting points. so we start drawing from there
		//set the background and text size
 		setBackgroundResource(R.drawable.whitecloud);
    	setGravity(Gravity.CENTER);
    	setTextSize(15);
    	setTextColor(Color.RED);
    	setTypeface(Typeface.DEFAULT_BOLD);
    	setShadowLayer(5, 2, 2, Color.YELLOW);
    	setDrawingCacheEnabled(true);
    	width = mDestroyer.width/3;
    	height = mDestroyer.height/4;
    	yPos = height * mCount;
    	xPos = mDestroyer.width;
    	layout(0, 0, width, height);
    	Typeface sans = Typeface.create("sans-serif-condensed", Typeface.BOLD);
     	setTypeface(sans);
	}

	
}