package com.samportnow.bato.capture;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.view.Gravity;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.samportnow.bato.R;

public class NegativeThought extends TextView
{
	CaptureActivity mCapture;
	Handler h;
	int FRAME_RATE = 30;
	int height;
	int width;
	int container_height;
	int container_width;

	public NegativeThought(Context context)
	{
		super(context);
		mCapture = (CaptureActivity) context;
		init(context);
	}

	public void init(Context context)
	{
		// set our drawing variables to be the same as the starting points. so
		// we start drawing from there
		// set the background and text size
		setBackgroundResource(R.drawable.graycloud);
		setGravity(Gravity.CENTER);
		setTextSize(15);
		setTextColor(Color.BLACK);
		setDrawingCacheEnabled(true);
		Typeface typeFace = Typeface.createFromAsset(context.getAssets(), "fonts/BlackBoysOnMopeds.ttf");
		setTypeface(typeFace);
		RelativeLayout container = (RelativeLayout) mCapture.findViewById(R.id.container);
		width = container.getWidth() / 3;
		height = container.getHeight() / 4;
		layout(0, 0, width, height);
	}	
}