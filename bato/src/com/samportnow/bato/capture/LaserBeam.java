package com.samportnow.bato.capture;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;

import com.samportnow.bato.R;

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
	RectF oval;
	int mLeftX; 
	int mTopY;
	int mRightX;
	int mBottomY;
	int mCenterX;
	int mCenterY;
	int xOfCenter;
	int yOfCenter;
	float offset;

	public LaserBeam(Context context)
	{
		super(context);
	}

	public LaserBeam(Context context, int mPosition)
	{
		this(context);

		Position = mPosition;
		mCapture = (CaptureActivity) context;
		mLaserPaint = new Paint();
		//mLaserPaint.setStyle(Paint.Style.FILL);
		//mLaserPaint.setStrokeWidth(15);
		mLaserPaint.setColor(Color.parseColor("#FFFF99"));
		mLaserPaint.setAlpha(150);
		mBattle = (BattleField) mCapture.findViewById(R.id.battle_field);
		yOfCenter = mCapture.mBattle.getHeight() - mCapture.mPos[0].getHeight();
		xOfCenter = (mCapture.mPos[mPosition].width * mPosition) + mCapture.mNegativeThought.getWidth()/2;
		offset = offSet(xOfCenter, yOfCenter);
		oval = new RectF();

	}


	public float radius(int x, int y)
	{
		double A2 = Math.pow(((x + mCapture.mNegativeThought.width/2) - xOfCenter),2);
		double B2 = Math.pow((yOfCenter - y),2);
		return (float) Math.sqrt(A2+B2);
	}

	public float startAngle(int x, int y)
	{
		return (float)(Math.atan2(x - xOfCenter, yOfCenter -y) * 180/Math.PI + 360 ) % 360;
	}

	public float endAngle(int x, int y)
	{
		return (float)((Math.atan2((x + mCapture.mNegativeThought.width)-xOfCenter, yOfCenter - y) * 180/Math.PI + 360 ) % 360)- 30;

	}

	public float offSet(int x, int y)
	{
		return (float)(Math.atan2(-xOfCenter, yOfCenter) * 180/Math.PI + 360 ) % 360;

	}

	public void draw_it(Canvas canvas)
	{
		float radius = radius(mBattle.x, mBattle.y);
		float startAngle = startAngle(mBattle.x, mBattle.y);
		float endAngle = endAngle(mBattle.x, mBattle.y);
		oval.set(xOfCenter - radius, yOfCenter - radius, xOfCenter + radius, yOfCenter + radius);
		if (startAngle + endAngle -90 + (360-offset) > 360 )
		{
			startAngle-=360;
			endAngle-=360;
		}
		canvas.drawArc(oval, -90 + (360-offset) + startAngle, endAngle, true, mLaserPaint);
		if (mGameOver)
		{
			xOfCenter += 15;
		}
	}

}