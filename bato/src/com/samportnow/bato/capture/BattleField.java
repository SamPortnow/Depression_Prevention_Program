package com.samportnow.bato.capture;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceView;

public class BattleField extends SurfaceView
{
	int x;
	int y;
	int min = 0;
	int container_height;
	int container_width;
    int xVelocity = 10;
    int yVelocity = 5;
    int FRAME_RATE = 30;
    Handler h;
    CaptureActivity mCapture;
    boolean mSetBounds;
    boolean mGameOver;
    boolean mSetX;
    int xLessBound[] = new int[2];
    int xGreatBound[] = new int[2];
    boolean boundLess;
    boolean boundGreat;
    
    
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
		 xLessBound[0] = 0;
		 xGreatBound[0] = container_width - container_width/3;
		 xLessBound[1] = 0;
		 xGreatBound[1] = container_width - container_width/3;
	}

	@Override
	public void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
        if (mCapture.mNeg != null && mCapture.mNeg.getDrawingCache() != null)
        {
        	x += xVelocity;
        	y -= yVelocity;
        	if (mSetX && x < xGreatBound[1] && x > xLessBound[1])
        	{
        		xGreatBound[0] = xGreatBound[1];
        		xLessBound[0] = xLessBound[1];
        		Log.e("switch", "hey hey");
        		mSetX = false;
        	}
        	if ((x > xGreatBound[0] || x < xLessBound[0]))
        		{

        			if (! mGameOver)
        			{
   
        				xVelocity = xVelocity*-1;
        				
        			}
        			
        			else if (mGameOver)
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
        canvas.drawBitmap(mCapture.mNeg.getDrawingCache(), x, y, null);
        if (mCapture.laser_created)
        {
        	for (int i=0; i < mCapture.mPosCounter; i++)
        	{
        		mCapture.mPos[i].draw_it(canvas, this.getHeight() - mCapture.mPos[i].getHeight(), mCapture.mPos[i].getWidth()*i, i);
        		mCapture.mLaserBeam[i].draw_it(canvas);
        	}
        }
	    try 
	    {  
	      Thread.sleep(30);  

	    } catch (InterruptedException e) { }

	       invalidate();		
        }
	}

}