package com.example.bato;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

@SuppressLint("ViewConstructor")
public class PositiveThought extends TextView
{
	CaptureActivity mCapture;
	int width;
	int height;
	boolean game_over;
	int xPos;
	int yPos;
	
	public PositiveThought(Context context, AttributeSet attrs, String text) 
	{	
		super(context, attrs);
		mCapture = (CaptureActivity) context;
		//set the background and text size
    	this.setGravity(Gravity.CENTER);
    	this.setTextSize(15);
    	this.setTextColor(Color.RED);
		Typeface typeFace=Typeface.createFromAsset(this.getContext().getAssets(),"fonts/BlackBoysOnMopeds.ttf");
		setTypeface(typeFace);
    	this.setDrawingCacheEnabled(true);
    	this.setBackgroundResource(R.drawable.whitecloud);
	    this.setFocusableInTouchMode(true);
	    this.setVisibility(View.INVISIBLE);
    	RelativeLayout container = (RelativeLayout) mCapture.findViewById(R.id.container);
    	width = container.getWidth()/4;
    	height = container.getHeight()/4;
	    this.layout(0, 0, width, height);
	    this.setText(text);
	}	
	

	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) 
	{
		//set the height and width of the view
		int this_coords[] = {0,0};
		this.getLocationOnScreen(this_coords);
	    xPos = Math.round(this_coords[0]);
	    yPos = Math.round(this_coords[1]);
		int width = mCapture.findViewById(R.id.container).getWidth()/4;
		int height = mCapture.findViewById(R.id.container).getHeight()/4;
		this.setMeasuredDimension(width, height);

	}
	
	
}