package com.samportnow.bato.destroyer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.view.Gravity;
import android.widget.TextView;

import com.samportnow.bato.R;

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
		init(context);
	}	
	
	
	public void init(Context context)
	{
		//set our drawing variables to be the same as the starting points. so we start drawing from there
		//set the background and text size
 		setBackgroundResource(R.drawable.whitecloud);
    	setGravity(Gravity.CENTER);
    	setTextSize(15);
        Typeface typeFace=Typeface.createFromAsset(context.getAssets(),"fonts/BlackBoysOnMopeds.ttf");
        setTypeface(typeFace);
    	setTextColor(Color.RED);
    	setDrawingCacheEnabled(true);
    	DestroyerGame mDestroy = (DestroyerGame) this.getContext();
    	DestroyerGameView gameView = (DestroyerGameView) mDestroy.findViewById(R.id.anim_view);
    	width = gameView.getWidth()/2;
    	height = gameView.getHeight()/3;
    	yPos = height * mCount;
    	xPos = gameView.width;
    	layout(0, 0, width, height);
	}

	
}