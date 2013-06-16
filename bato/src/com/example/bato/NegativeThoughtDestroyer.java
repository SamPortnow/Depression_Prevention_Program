package com.example.bato;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class NegativeThoughtDestroyer extends TextView
{
	DestroyerGame mDestroyer;
	Handler h;
	int FRAME_RATE = 30;
	int height;
	int width;

	
	public NegativeThoughtDestroyer(Context context) 
	{	
		super(context);
		mDestroyer = (DestroyerGame) context; 
		init();
	}	
	
	
	public void init()
	{
		//set our drawing variables to be the same as the starting points. so we start drawing from there
		//set the background and text size
 		setBackgroundResource(R.drawable.graycloud);
    	setGravity(Gravity.CENTER);
    	setTextSize(15);
    	setTextColor(Color.BLACK);
    	setDrawingCacheEnabled(true);
    	width = mDestroyer.width/3;
    	height = mDestroyer.height/4;
    	layout(0, 0, width, height);
    	Typeface sans = Typeface.create("sans-serif-condensed", Typeface.BOLD);
     	setTypeface(sans);
	}
	
	
	
	
}