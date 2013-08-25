package com.samportnow.bato.coping.floating;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.view.Gravity;
import android.widget.TextView;


@SuppressLint("ViewConstructor")
public class Thought extends TextView
{
	int height;
	int width;
	int xPos;
	int yPos;
	
	public Thought(Context context) 
	{	
		super(context);
		init(context);
	}	
	
	
	public void init(Context context)
	{
    	setGravity(Gravity.CENTER);
    	setTextSize(15);
        Typeface typeFace=Typeface.createFromAsset(context.getAssets(),"fonts/BlackBoysOnMopeds.ttf");
        setTypeface(typeFace);
    	setDrawingCacheEnabled(true);
    	FloatActivity floatA = (FloatActivity) this.getContext();
    	width = floatA.width/3;
    	height = floatA.height/3;
    	layout(0, 0, width, height);
	}
	
	public void drawIt(Canvas canvas)
	{
		canvas.drawBitmap(this.getDrawingCache(), xPos, yPos, null);
	}

	
}