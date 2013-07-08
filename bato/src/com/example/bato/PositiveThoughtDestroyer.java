package com.example.bato;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.view.Gravity;
import android.widget.TextView;

@SuppressLint("ViewConstructor")
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
		Typeface typeFace=Typeface.createFromAsset(this.getContext().getAssets(),"fonts/BlackBoysOnMopeds.ttf");
		setTypeface(typeFace);
    	setDrawingCacheEnabled(true);
    	DestroyerGame mDestroy = (DestroyerGame) this.getContext();
    	DestroyerGameView gameView = (DestroyerGameView) mDestroy.findViewById(R.id.anim_view);
    	width = gameView.getWidth()/3;
    	height = gameView.getHeight()/3;
    	yPos = height * mCount;
    	xPos = gameView.width;
    	layout(0, 0, width, height);
	}

	
}