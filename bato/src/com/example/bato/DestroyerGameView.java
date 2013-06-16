package com.example.bato;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class DestroyerGameView extends View
{
	Context mContext; 
	DestroyerGame mDestroyer;
	Handler h;
    int FRAME_RATE = 30;
    int width;
    int height;
    int thunder_time;
    int mPosX;
    int mPosY;
    int mNegX;
    float alpha=1.0f;
    boolean mMovePos;
    boolean mMoveNeg;
    int xVelocity = 5;
    boolean mDestroy;
    boolean explode;
    boolean mDrawPos;
    int mMoveX;
    int mMoveY;
    int mMoveByX;
    int mMoveByY;
    int mMovePosX;
    int mMovePosY;
    int mDrawStationX;
    int mDrawStationY;
    int mMoveStationX;
    int mMoveStationVelocity = 2;
    ArrayList <PositiveThoughtDestroyer> mPositive = new ArrayList<PositiveThoughtDestroyer>();



	public DestroyerGameView(Context context, AttributeSet attrs) 
	{
		super(context, attrs);
        mContext = this.getContext();
		mDestroyer = (DestroyerGame) context;
        h = new Handler();
	    mPositive = mDestroyer.mPositive;
	    this.setBackgroundColor(Color.parseColor("#C0C0C0"));
		
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
	     mPosX = width;
	     mNegX = (int) (width + (width/1.5));
	     mPosY = 0;
	}
	
	@Override
	protected void onDraw(Canvas canvas)
	{
		
		
		if (mDestroy)
		{
			//logic for destroying thoughts goes here
			//if fits within the bounds and greater than 0
			if ( mMoveX >= mNegX && mMoveX <= mNegX + mDestroyer.mNeg.width && mMoveY > 0 && mMoveY < 
	    	mDestroyer.mNeg.height/2)
	    	{
	 	    		//stay in here if there's a hit
	    		if (! explode)
	    		{
	    			//here are all of our reset methods
	    			mDestroyer.explode();
	    			//reset our variables
	    		    mPosX = width;
	    		    mNegX = (int) (width + (width/1.5));
	    		    mPosY = 0;
	    		}
	    		explode = true;
	    		mDestroy=false;
	    	}
			canvas.drawBitmap(mPositive.get(0).getDrawingCache(), mMoveX, mMoveY,  null);
			mMoveX += mMoveByX;
			mMoveY -= mMoveByY;
		}
		if (mDrawPos)
		{
			for (int i = 1; i < mDestroyer.mStationPositive.size() + 1; i++)
			{
				canvas.drawBitmap(mDestroyer.mStationPositive.get(i-1).getDrawingCache(), 
						mDrawStationX + mMoveStationX, mDrawStationY,  null);
				mDrawStationX += width/3;
		    	if (i % 3 == 0)
		 		{
		    		 mDrawStationY += height/4;
		 			 mDrawStationX = 0;
		 		}

			}
			
			if (mMoveStationX < -width/6 || mMoveStationX > width/6)
			{
				mMoveStationVelocity = mMoveStationVelocity * -1;
			}
			mMoveStationX += mMoveStationVelocity;
	    	mDrawStationX = 0;
	    	mDrawStationY = 0;
	
		}
		if (mMovePos)
		{
			for (int i = 0; i < mPositive.size(); i++)
			{
				canvas.drawBitmap(mPositive.get(i).getDrawingCache(), mPosX, mPositive.get(i).yPos, null);
				if (mPosX < 0)
				{
					//if it gets to 0, populate the listview and stop drawing the positive thought
					//set mNeg to true so the negative thought can start moving
					mDestroyer.populateListView();
					mMovePos = false;
					mMoveNeg = true;
				}
			}
			mPosX -= 5;
			canvas.drawBitmap(mDestroyer.mNeg.getDrawingCache(), mNegX, mPositive.get(0).yPos, null);
			mNegX -=5;
		}
		if (mMoveNeg)
		{
			//moving the negative thought. it moves back and forth on the screen 
			if (mNegX < 0 || mNegX > width)
			{
				xVelocity = xVelocity *-1;
			}
			canvas.drawBitmap(mDestroyer.mNeg.getDrawingCache(), mNegX, mPositive.get(0).yPos, null);
			mNegX -= xVelocity;
		}
		h.postDelayed(r, FRAME_RATE);
	}
}


