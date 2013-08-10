package com.samportnow.bato.destroyer;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceView;

public class DestroyerGameView extends SurfaceView
{
	Context mContext; 
	DestroyerGame mDestroyer;
	NegativeThoughtDestroyer mNegThought;
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
    int xVelocity = 15;
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
    int mMoveStationX[] = new int[2];
    int mMoveStationVelocity[] = new int[2];
    int count;
    float mScale;
    ArrayList <PositiveThoughtDestroyer> mPositive = new ArrayList<PositiveThoughtDestroyer>();
    PositiveThoughtDestroyer mPosCannon;
    HashMap<PositiveThoughtDestroyer, int[]> mThoughtInfo = new HashMap<PositiveThoughtDestroyer, int[]>();



	public DestroyerGameView(Context context, AttributeSet attrs) 
	{
		super(context, attrs);
		mMoveStationVelocity[0] = -2;
		mMoveStationVelocity[1] = 2;
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
		
		
		
		if (mDrawPos)
		{
			for (int i = 1; i < mDestroyer.mStationPositive.size() + 1; i++)
			{
				canvas.drawBitmap(mDestroyer.mStationPositive.get(i-1).getDrawingCache(), 
						mDrawStationX + mMoveStationX[i%2], mDrawStationY,  null);
				mDrawStationX += width/3;
		    	if (i % 3 == 0)
		 		{
		    		 mDrawStationY += height/4;
		 			 mDrawStationX = 0;
		 		}

			}
			
			if (mMoveStationX[0] < -width || mMoveStationX[0] > width)
			{
				mMoveStationVelocity[0] = mMoveStationVelocity[0] * -1;
			}
			
			if (mMoveStationX[1] < -width || mMoveStationX[1] > width)
			{
				mMoveStationVelocity[1] = mMoveStationVelocity[1] * -1;
			}
				
			mMoveStationX[1] += mMoveStationVelocity[1];
				
			mMoveStationX[0] += mMoveStationVelocity[0];
			
	    	mDrawStationX = 0;
	    	mDrawStationY = 0;
	
		}
		if (mMovePos)
		{
			canvas.drawBitmap(mDestroyer.mNeg.getDrawingCache(), mNegX, 0, null);

			for (int i = 0; i < mPositive.size(); i++)
			{
				canvas.drawBitmap(mPositive.get(i).getDrawingCache(), mPosX, mPositive.get(i).yPos, null);
				mDestroyer.mLaserBeam.get(i).draw_it(canvas);
				if (mPosX < 0)
				{
					//if it gets to 0, populate the listview and stop drawing the positive thought
					//set mNeg to true so the negative thought can start moving
					mDestroyer.mPositive.clear();
					mDestroyer.populateListView();
					mMovePos = false;
					mMoveNeg = true;
				}
			}
			mPosX -= 15;
			mNegX -=15;
		}
		if (mMoveNeg)
		{
			//moving the negative thought. it moves back and forth on the screen 
			if (mNegX < 0 || mNegX > width - mDestroyer.mNeg.getWidth())
			{
				xVelocity = xVelocity *-1;
			}
			canvas.drawBitmap(mDestroyer.mNeg.getDrawingCache(), mNegX, 0, null);
			mNegX -= xVelocity;
		}
		
		if (mDestroy)
		{
			//logic for destroying thoughts goes here
			//if fits within the bounds and greater than 0
			if ( mMoveX >= mNegX - mDestroyer.mNeg.width/2 && mMoveX <= mNegX + mDestroyer.mNeg.width && mMoveY > 0 && mMoveY < 
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
	    		mScale = 0;
	    		explode = true;
	    		mDestroy=false;
	    	}
    		if(mMoveY < -mDestroyer.mNeg.height)
    		{
    			mScale = 0;
    		}
			if (mScale < 1.0f)
			{
				canvas.save();
				canvas.scale(mScale, mScale, mMoveX, mMoveY);
				canvas.drawBitmap(mDestroyer.mPosCannon[0].getDrawingCache(), mMoveX, mMoveY,  null);
				canvas.restore();
				mScale += .05;
			}
			else
			{
				canvas.drawBitmap(mDestroyer.mPosCannon[0].getDrawingCache(), mMoveX, mMoveY,  null);
				
			}
			mMoveX += mMoveByX;
			mMoveY -= mMoveByY;
		}
		h.postDelayed(r, FRAME_RATE);
	}
}


