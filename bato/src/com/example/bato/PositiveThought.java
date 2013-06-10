package com.example.bato;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class PositiveThought extends TextView
{
	CaptureActivity mCapture;
	int width;
	int height;
	int xPos;
	int yPos;
	
	public PositiveThought(Context context, AttributeSet attrs) 
	{	
		super(context, attrs);
		mCapture = (CaptureActivity) context;
		//set the background and text size
    	this.setGravity(Gravity.CENTER);
    	this.setTextSize(15);
    	this.setTextColor(Color.RED);
    	this.setTypeface(Typeface.DEFAULT_BOLD);
    	this.setShadowLayer(5, 2, 2, Color.YELLOW);
    	Typeface sans = Typeface.create("sans-serif-condensed", Typeface.BOLD);
    	this.setTypeface(sans);
    	this.setDrawingCacheEnabled(true);
    	this.setBackgroundResource(R.drawable.whitecloud);
	    this.setFocusableInTouchMode(true);
	    this.setVisibility(View.INVISIBLE);
    	RelativeLayout container = (RelativeLayout) mCapture.findViewById(R.id.container);
    	width = container.getWidth()/4;
    	height = container.getHeight()/4;
	    this.layout(0, 0, width, height);
	}	
	
	public void init()
	{
		
	}
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) 
	{
		//set the height and width of the view
		int width = mCapture.findViewById(R.id.container).getWidth()/4;
		int height = mCapture.findViewById(R.id.container).getHeight()/4;
		this.setMeasuredDimension(width, height);

	}
	
}