package com.example.bato;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class BattleField extends View
{
	int x;
	int y;
	int min = 0;
	int container_height;
	int container_width;
    private int xVelocity = 10;
    private int yVelocity = 5;
    int FRAME_RATE = 30;
    Handler h;
    CaptureActivity mCapture;
    boolean mSetBounds;
    boolean mGameOver;
    
    
	public BattleField(Context context, AttributeSet attrs) 
	{	
		super(context, attrs);
		h = new Handler();
		mCapture = (CaptureActivity) context;

	}
	
	@Override 
	 protected void onSizeChanged(int xNew, int yNew, int xOld, int yOld)
	{
	     super.onSizeChanged(xNew, yNew, xOld, yOld);
	     container_width = xNew;
	     container_height = yNew;
		 x = container_width/2;
		 y = container_height;
	}
	
	@Override
	public void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
        if (mCapture.mNeg != null && mCapture.mNeg.getDrawingCache() != null)
        {
        	x += xVelocity;
        	y -= yVelocity;
        	//reverse if you're out of bounds
        	if (x > (container_width - mCapture.mNeg.width)  || (x < 0))
        		{

        			if (! mGameOver)
        			{
        				xVelocity = xVelocity*-1;
        			}
        			
        			else
        			{
        				if (x < 0)
        				{
        					xVelocity = xVelocity*-1;
        				}
        			}
        		}
        	if (y < this.getHeight()/2 && ! mSetBounds)
        	{
        		mSetBounds = true;
        		container_height = this.getHeight()/2;
        	}
        	if (y > (container_height) || (y < 0)) 
     			{

        			yVelocity = yVelocity*-1;

     			}
        //here is where the drawing hpapens
        canvas.drawBitmap(mCapture.mNeg.getDrawingCache(), x, y, null);
        }
	    try 
	    {  
	      Thread.sleep(30);  
	         
	    } catch (InterruptedException e) { }
	       
	       invalidate();		
	}


}
