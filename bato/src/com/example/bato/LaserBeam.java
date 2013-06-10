package com.example.bato;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;


public class LaserBeam extends View
{
	Paint mLaserPaint;
	CaptureActivity mCapture;
	int mStartX;
	int mStartY;
	int mStopX;
	int mStopY;
	int xRange;
	int yRange;
	int mHoldStopY;
	int mSmallBounds;
	int mLargeBounds;
	boolean hooked;
	boolean xBounds;
	BattleField mBattle;
	boolean mGameOver;
	int Position;
	
	public LaserBeam(Context context, int mPosition)
	{
		super(context);
		Position = mPosition;
		mCapture = (CaptureActivity) context;
		mLaserPaint = new Paint();
		mLaserPaint.setStyle(Paint.Style.STROKE);
		mLaserPaint.setStrokeWidth(5);
		mLaserPaint.setColor(Color.BLUE);
		mLaserPaint.setShadowLayer(5, 5, 5, Color.CYAN);
		mBattle = (BattleField) mCapture.findViewById(R.id.battle_field);
		mStartX = (mCapture.mPos[mPosition].width/2) + (mCapture.mPos[mPosition].width * mPosition);
		mStartY = mCapture.container.getHeight() - mCapture.mEditTextContainer.getHeight()-(mCapture.mPos[mPosition].height);
		mStopX = (mCapture.mPos[mPosition].getWidth()/3) * (mPosition + 1);
		mStopY = 0;
		mHoldStopY = 0;
	}
	
	@Override 
	public void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
		canvas.drawLine(mStartX, mStartY, mBattle.x + mCapture.mNeg.width/2, 
				mBattle.y + mCapture.mNeg.height/2, mLaserPaint);
		if (mGameOver)
		{
			mStartX += mCapture.width/20;
		}
	}

}
