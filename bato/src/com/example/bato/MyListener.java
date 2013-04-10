package com.example.bato;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class MyListener implements OnTouchListener
{
	int position; 
	
	public MyListener(int position)
	{
		this.position = position;
	}
	
	public int getPosition()
	{
		return position;
	}
	
	@Override
	public boolean onTouch(View v, MotionEvent event) 
	{
		return true;
	}

}
