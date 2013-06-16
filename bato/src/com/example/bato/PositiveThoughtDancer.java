package com.example.bato;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.view.Gravity;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class PositiveThoughtDancer extends TextView
{
	CaptureActivity mCapture;
	Handler h;
	int FRAME_RATE = 30;
	int height;
	int width;
	int container_height;
	int container_width;

	
	public PositiveThoughtDancer(Context context) 
	{	
		super(context);
		mCapture = (CaptureActivity) context; 
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
    	// NEED TO CHANGE THIS
    	RelativeLayout container = (RelativeLayout) mCapture.findViewById(R.id.container);
    	width = container.getWidth()/3;
    	height = container.getHeight()/4;
    	layout(0, 0, width, height);
    	Typeface sans = Typeface.create("sans-serif-condensed", Typeface.BOLD);
     	setTypeface(sans);
	}
	
	
	
}