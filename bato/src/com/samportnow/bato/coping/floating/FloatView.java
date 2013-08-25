package com.samportnow.bato.coping.floating;

import android.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

public class FloatView extends View
{
	Handler h;
    int FRAME_RATE = 30;
    FloatActivity FloatA; 
    
	public FloatView(Context context, AttributeSet attrs) 
	{
		super(context);
		FloatA = (FloatActivity) context;
		h = new Handler();
		this.setBackgroundColor(Color.parseColor("#00C0FF"));
	}
	
	private Runnable r= new Runnable() 
	{

		@Override
		public void run() {
			invalidate();

		}

	};

	@Override
	public void onDraw(Canvas canvas)
	{
		
		for (int i = 0; i < 3; i ++)
		{
			FloatA.mThoughts[i].drawIt(canvas);
			FloatA.mThoughts[i].xPos += 5;
			if (FloatA.mThoughts[i].xPos > FloatA.width)
			{
				FloatA.mThoughts[i] = FloatA.createNew();
				FloatA.reset(FloatA.mThoughts[i]);
			}
		}
			
		h.postDelayed(r, FRAME_RATE);
	}

}
