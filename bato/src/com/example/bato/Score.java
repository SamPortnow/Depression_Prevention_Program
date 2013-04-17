package com.example.bato;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

public class Score extends View
{
	Context mContext;
	DestroyerShooterView mDestroyer;
	Handler h;
	int size;
	int width;
	int height;
	int count;
	int FRAME_RATE = 30;
	TextPaint score = new TextPaint();
	boolean update;
	
	public Score(Context context, AttributeSet attrs) 
	{
		super(context, attrs);
        mContext = this.getContext();
		mDestroyer = (DestroyerShooterView) context;
        h = new Handler();
        size = getResources().getDimensionPixelSize(R.dimen.myFontSize);
    	score.setAntiAlias(true);
    	score.setTypeface(Typeface.DEFAULT_BOLD);
    	score.setTextSize(size);
	    score.setColor(Color.CYAN);
	    score.setShadowLayer(1, 1, 1, Color.RED);
        
	}
	
	private Runnable r= new Runnable() 
	{

		@Override
		public void run() {
			invalidate();
			
	}

	};
	
	
	@Override
	 protected void onSizeChanged(int xNew, int yNew, int xOld, int yOld)
	{
	     super.onSizeChanged(xNew, yNew, xOld, yOld);
	     width = xNew;
	     height = yNew;
	}
	
	@Override
	protected void onDraw (Canvas canvas)
    {		
		super.onDraw(canvas);
		canvas.drawText("" + count, 0, height, score);
		if (update == true)
		{
			count += 1;
			if (count % 25 == 0)
			{
				update = false;
			}
		}
	    h.postDelayed(r, FRAME_RATE);
    }
	

	
	
	
}
