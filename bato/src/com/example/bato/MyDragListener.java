package com.example.bato;

import android.view.DragEvent;
import android.view.View;
import android.view.View.OnDragListener;

public class MyDragListener implements OnDragListener
{

	int position; 
	
	public MyDragListener(int position)
	{
		this.position = position;
	}
	
	public int getPosition()
	{
		return position;
	}

	@Override
	public boolean onDrag(View v, DragEvent event) 
	{
		return true;
	}
	
}