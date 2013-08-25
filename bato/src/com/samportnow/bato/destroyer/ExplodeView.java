/*
 * Copyright (C) 2011 Gustavo Claramunt
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.samportnow.bato.destroyer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.View;

import com.samportnow.bato.R;


public class ExplodeView extends View
{
	Bitmap explosionSheet; 
	private static final int COLUMNS = 8;
	private Rect sourceRect;
	private int FPS = 30;
	private int framePeriod = 1000/FPS;
	private long startTime;
	private int [] mFrame = new int[2];
	private DestroyerGame mDestroyer;
	boolean mStop;
	Rect destRect;
	float deltaTime;

	
	public ExplodeView(Context context) 
	{
		super(context);
		mDestroyer = (DestroyerGame) context;
		destRect = new Rect();
		sourceRect = new Rect();
		explosionSheet = BitmapFactory.decodeResource(getResources(), R.drawable.explosion);
		reset();
	}

    public void explode(Canvas canvas) 
    {
    	if (! mStop)
    	{
	    	update();
			canvas.drawBitmap(explosionSheet, sourceRect, destRect, null);
    	}
    }
    
    public void setStartTime()
    {
    	startTime = System.currentTimeMillis();
    }
    
    public void update()
    {
    	deltaTime  = System.currentTimeMillis() - startTime;
    	if (deltaTime > framePeriod)
    	{
    		if (mFrame[0] % COLUMNS == 0)
    		{
    			
    			mFrame[0] = 0;
        		mFrame[1]++;
        		if (mFrame[1] > 6)
        		{
        			mStop = true;
        			mDestroyer.stopExplode();
        			mFrame[0] = 0;
        			mFrame[1] = 0;
        		}
    		}
    		else
    		{
    			mFrame[0]++;
    		}
    	    sourceRect.left = mFrame[0] * explosionSheet.getWidth()/8;
    	    sourceRect.right = this.sourceRect.left + explosionSheet.getWidth()/8;
    	    sourceRect.top = mFrame[1] * explosionSheet.getHeight()/6;
    	    sourceRect.bottom = sourceRect.top + explosionSheet.getHeight()/6;
    		startTime = System.currentTimeMillis();
    	}
    }
    
    public void setXPos(int xPos)
    {
    	destRect.left = xPos;
    	destRect.right = xPos + mDestroyer.mNeg.getWidth();
    	destRect.top = 0;
    	destRect.bottom = mDestroyer.mNeg.getHeight();
    }
    
    public void reset()
    {
		sourceRect.left = 0; 
		sourceRect.right = explosionSheet.getWidth()/8;
		sourceRect.top = 0;
		sourceRect.bottom = explosionSheet.getHeight()/6;
    }
}