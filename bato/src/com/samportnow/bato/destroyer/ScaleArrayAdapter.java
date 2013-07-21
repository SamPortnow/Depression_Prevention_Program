package com.samportnow.bato.destroyer;

import java.util.ArrayList;

import android.content.ClipData;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class ScaleArrayAdapter extends ArrayAdapter<String> implements OnTouchListener
{
	int index;
	Context mContext;

	public ScaleArrayAdapter(Context context, int resource, int textViewResourceId, ArrayList<String> objects)
	{
		super(context, resource, textViewResourceId, objects);
		mContext = context;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		View v = super.getView(position, convertView, parent);
		index = position;
		v.setOnTouchListener(this);
		return v;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event)
	{
		ClipData data = ClipData.newPlainText("", "");
		DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
		v.startDrag(data, shadowBuilder, v, 0);
		return true;
	}

	public int getItemHere()
	{
		return index;
	}

}
