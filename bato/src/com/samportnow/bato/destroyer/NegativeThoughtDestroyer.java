package com.samportnow.bato.destroyer;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.view.Gravity;
import android.widget.TextView;

import com.samportnow.bato.R;

public class NegativeThoughtDestroyer extends TextView
{
	Handler h;
	int FRAME_RATE = 30;
	int height;
	int width;

	
	public NegativeThoughtDestroyer(Context context) 
	{	
		super(context);
		init(context);
	}	
	
	
	public void init(Context context)
	{
		//set our drawing variables to be the same as the starting points. so we start drawing from there
		//set the background and text size
 		setBackgroundResource(R.drawable.graycloud);
    	setGravity(Gravity.CENTER);
    	setTextSize(15);
    	setTextColor(Color.BLACK);
        Typeface typeFace=Typeface.createFromAsset(context.getAssets(),"fonts/BlackBoysOnMopeds.ttf");
        setTypeface(typeFace);
    	setDrawingCacheEnabled(true);
    	DestroyerGame mDestroy = (DestroyerGame) this.getContext();
    	DestroyerGameView gameView = (DestroyerGameView) mDestroy.findViewById(R.id.anim_view);
    	width = gameView.width/3;
    	height = gameView.height/4;
    	layout(0, 0, width, height);
	}
	
	
	
	
}