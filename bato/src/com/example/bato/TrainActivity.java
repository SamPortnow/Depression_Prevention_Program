package com.example.bato;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnDragListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TrainActivity extends Activity
{
	Context mContext;
	LinearLayout mTrain;
	Animation animateTrain;
	TextView negative_thought;
	int width;
	HorizontalScrollView scroller;
	LinearLayout layout;
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.fragment_train);
	    mContext = this;
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		width = size.x;
	    negative_thought = (TextView) findViewById(R.id.negative_thought);
	    scroller = (HorizontalScrollView) findViewById(R.id.scroller);
		SharedPreferences preferences = mContext.getSharedPreferences(mContext.getPackageName(), Context.MODE_PRIVATE);
		String neg = preferences.getString("thought", null);
		negative_thought.setText(neg);
	    negative_thought.setOnTouchListener(new OnTouchListener()
	    {
			@Override
			public boolean onTouch(View v, MotionEvent event) 
			{ 
			    ClipData data = ClipData.newPlainText(negative_thought.getText().toString(), null);
			    DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
			    v.startDrag(data, shadowBuilder, v, 0);
			    return true;
			    }
		
		
		});
		animateTrain = new TranslateAnimation(0, width, 0, 0);
		animateTrain.setDuration(500);
		animateTrain.setFillAfter(true);
		TextView mind_reading = (TextView) findViewById(R.id.mind_reading);
		TextView fortunetelling = (TextView) findViewById(R.id.fortune_telling);
		TextView catastrophizing = (TextView) findViewById(R.id.catastrophizing);
		TextView labeling = (TextView) findViewById(R.id.labeling);
		TextView dark_glasses = (TextView) findViewById(R.id.dark_glass);
		TextView discounting_positives = (TextView) findViewById(R.id.discounting_positives);
		TextView black_and_white_thinking = (TextView) findViewById(R.id.black_and_white_thinking);
		TextView overgeneralizing = (TextView) findViewById(R.id.overgeneralizing);
		TextView personalizing = (TextView) findViewById(R.id.personalizing);
		TextView shoulds = (TextView) findViewById(R.id.shoulds);
		TextView unfair_comparisons = (TextView) findViewById(R.id.unfair_comparisons);
		mind_reading.setOnDragListener(new OnDragListener()
		{

			@Override
			public boolean onDrag(View arg0, DragEvent arg1) 
			{
				switch (arg1.getAction())
				{
					
				case DragEvent.ACTION_DRAG_ENTERED:
					break;
				
				case DragEvent.ACTION_DROP:
					scroller.startAnimation(animateTrain);
					break;
				}
				return true;
			}
			
		});
		fortunetelling.setOnDragListener(new OnDragListener()
		{

			@Override
			public boolean onDrag(View arg0, DragEvent arg1) 
			{
				switch (arg1.getAction())
				{
					
				case DragEvent.ACTION_DRAG_ENTERED:
					break;
				
				case DragEvent.ACTION_DROP:
					scroller.startAnimation(animateTrain);
					break;
				}
				return true;
			}
			
		});
		catastrophizing.setOnDragListener(new OnDragListener()
		{

			@Override
			public boolean onDrag(View arg0, DragEvent arg1) 
			{
				switch (arg1.getAction())
				{
					
				case DragEvent.ACTION_DRAG_ENTERED:
					break;
				
				case DragEvent.ACTION_DROP:
					scroller.startAnimation(animateTrain);
					break;
				}
				return true;
			}
			
		});
		labeling.setOnDragListener(new OnDragListener()
		{

			@Override
			public boolean onDrag(View arg0, DragEvent arg1) 
			{
				switch (arg1.getAction())
				{
					
				case DragEvent.ACTION_DRAG_ENTERED:
					break;
				
				case DragEvent.ACTION_DROP:
					scroller.startAnimation(animateTrain);
					break;
				}
				return true;
			}
			
		});
		dark_glasses.setOnDragListener(new OnDragListener()
		{

			@Override
			public boolean onDrag(View arg0, DragEvent arg1) 
			{
				switch (arg1.getAction())
				{
					
				case DragEvent.ACTION_DRAG_ENTERED:
					break;
				
				case DragEvent.ACTION_DROP:
					scroller.startAnimation(animateTrain);
					break;
				}
				return true;
			}
			
		});
		discounting_positives.setOnDragListener(new OnDragListener()
		{

			@Override
			public boolean onDrag(View arg0, DragEvent arg1) 
			{
				switch (arg1.getAction())
				{
					
				case DragEvent.ACTION_DRAG_ENTERED:
					break;
				
				case DragEvent.ACTION_DROP:
					scroller.startAnimation(animateTrain);
					break;
				}
				return true;
			}
			
		});
		black_and_white_thinking.setOnDragListener(new OnDragListener()
		{

			@Override
			public boolean onDrag(View arg0, DragEvent arg1) 
			{
				switch (arg1.getAction())
				{
					
				case DragEvent.ACTION_DRAG_ENTERED:
					break;
				
				case DragEvent.ACTION_DROP:
					scroller.startAnimation(animateTrain);
					break;
				}
				return true;
			}
			
		});
		shoulds.setOnDragListener(new OnDragListener()
		{

			@Override
			public boolean onDrag(View arg0, DragEvent arg1) 
			{
				switch (arg1.getAction())
				{
					
				case DragEvent.ACTION_DRAG_ENTERED:
					break;
				
				case DragEvent.ACTION_DROP:
					scroller.startAnimation(animateTrain);
					break;
				}
				return true;
			}
			
		});
		overgeneralizing.setOnDragListener(new OnDragListener()
		{

			@Override
			public boolean onDrag(View arg0, DragEvent arg1) 
			{
				switch (arg1.getAction())
				{
					
				case DragEvent.ACTION_DRAG_ENTERED:
					break;
				
				case DragEvent.ACTION_DROP:
					scroller.startAnimation(animateTrain);
					break;
				}
				return true;
			}
			
		});
		personalizing.setOnDragListener(new OnDragListener()
		{

			@Override
			public boolean onDrag(View arg0, DragEvent arg1) 
			{
				switch (arg1.getAction())
				{
					
				case DragEvent.ACTION_DRAG_ENTERED:
					break;
				
				case DragEvent.ACTION_DROP:
					scroller.startAnimation(animateTrain);
					break;
				}
				return true;
			}
			
		});
		unfair_comparisons.setOnDragListener(new OnDragListener()
		{

			@Override
			public boolean onDrag(View arg0, DragEvent arg1) 
			{
				switch (arg1.getAction())
				{
					
				case DragEvent.ACTION_DRAG_ENTERED:
					break;
				
				case DragEvent.ACTION_DROP:
					scroller.startAnimation(animateTrain);
					break;
				}
				return true;
			}
			
		});
	}

}
