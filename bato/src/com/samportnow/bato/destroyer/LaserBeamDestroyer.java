package com.samportnow.bato.destroyer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;

import com.samportnow.bato.R;
import com.samportnow.bato.capture.BattleField;

@SuppressLint("ViewConstructor")
public class LaserBeamDestroyer extends View
{
	Paint mLaserPaint;
	DestroyerGameActivity mDestroyer;
	DestroyerGameView mDestroyerView;
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
	int mTextWidth;
	int mTextHeight;
	int negY;

	public LaserBeamDestroyer(Context context, int mPosition)
	{
		super(context);
		Position = mPosition;
		mDestroyer = (DestroyerGameActivity) context;
		mLaserPaint = new Paint();
		// mLaserPaint.setStyle(Paint.Style.FILL);
		// mLaserPaint.setStrokeWidth(15);
		mLaserPaint.setColor(Color.parseColor("#FFFF99"));
		mLaserPaint.setAlpha(150);
		oval = new RectF();

	}

	@Override
	protected void onSizeChanged(int xNew, int yNew, int xOld, int yOld)
	{
		super.onSizeChanged(xNew, yNew, xOld, yOld);
		mTextWidth = mDestroyer.mNeg.width;
		mTextHeight = mDestroyer.mNeg.height;
		yOfCenter = mTextHeight * Position;
		// negY = mTextHeight/2;
		mDestroyerView = (DestroyerGameView) mDestroyer.findViewById(R.id.anim_view);
		xOfCenter = mDestroyerView.mPosX + mTextWidth / 2;
		offset = offSet();
	}

	public float radius(int Posx, int Negx)
	{
		double A2 = Math.pow((Negx - Posx), 2);
		double B2 = Math.pow((yOfCenter), 2);
		return (float) Math.sqrt(A2 + B2);
	}

	public float startAngle(int x, int y)
	{
		return (float) (Math.atan2(x - xOfCenter, yOfCenter - y) * 180 / Math.PI + 360) % 360;
	}

	public float endAngle(int x, int y)
	{
		return (float) ((Math.atan2((x + mTextWidth / 2) - xOfCenter, yOfCenter - y) * 180 / Math.PI + 360) % 360) - 30;

	}

	public float offSet()
	{
		return (float) (Math.atan2(-xOfCenter, yOfCenter) * 180 / Math.PI + 360) % 360;
	}

	@Override
	public void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
		xOfCenter = mDestroyerView.mNegX;
		float radius = radius(mDestroyerView.mPosX, mDestroyerView.mNegX);
		float startAngle = startAngle(mDestroyerView.mPosX, mDestroyerView.mNegX);
		float endAngle = endAngle(mDestroyerView.mPosX, mDestroyerView.mNegX);
		oval.set(xOfCenter - radius, yOfCenter - radius, xOfCenter + radius, yOfCenter + radius);
		canvas.drawArc(oval, -45f - (7.5f * Position), startAngle - endAngle, true, mLaserPaint);

	}

}
