package com.samportnow.bato.capture;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Handler;
import android.view.View;

public class EndGame extends View
{
	CaptureActivity mCapture;
	int xPos[] = new int[4];
	int yPos;
	int FRAME_RATE=30;
	Handler h = new Handler();
	PositiveThought mPos[] = new PositiveThought[4];
	LaserBeam mLaser[] = new LaserBeam[4];
	boolean mGameOver;
	
	public EndGame(Context context) 
	{	
		super(context);
		mCapture = (CaptureActivity) context;
		mPos = mCapture.mPos;
		mLaser = mCapture.mLaserBeam;
		yPos = mCapture.mPos[0].yPos;

	}
	
	private Runnable r= new Runnable() 
	{

		@Override
		public void run() {
			invalidate();
			
		}

	};
	
	@Override
	protected void onDraw(Canvas canvas)
	{
		for (int i =0; i < 4; i++)
		{
			canvas.drawBitmap(mPos[i].getDrawingCache(), mLaser[i].xOfCenter - mPos[i].width/2, yPos, null);
			if (mLaser[0].xOfCenter > mCapture.mBattle.container_width && mGameOver == false)
			{
				mCapture.endGame();
				mGameOver = true;
			}
		}
		h.postDelayed(r, FRAME_RATE);
	}
}
