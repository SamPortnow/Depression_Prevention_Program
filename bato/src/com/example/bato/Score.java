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
	DestroyerGame mDestroyer;
	Handler h;
	int size;
	int width;
	int height;
	int count;
	int FRAME_RATE = 30;
	TextPaint score = new TextPaint();
	boolean update;
	int fin;
	
	public Score(Context context, AttributeSet attrs) 
	{
		super(context, attrs);
        mContext = this.getContext();
		mDestroyer = (DestroyerGame) context;
        h = new Handler();
        fin = 0;
        size = getResources().getDimensionPixelSize(R.dimen.myFontSize);
    	score.setAntiAlias(true);
		Typeface typeFace=Typeface.createFromAsset(mContext.getAssets(),"fonts/BlackBoysOnMopeds.ttf");
    	score.setTypeface(typeFace);
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
		if (count != fin)
		{
			count += 1;
		
		}
	    h.postDelayed(r, FRAME_RATE);
    }
	

	
	
	
}
