package com.samportnow.bato.capture;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.samportnow.bato.R;

public class CaptureSelectActivity extends Activity
{
	private String[] mCategoryTitles = null;
	private String[] mCategoryDescriptions = null;
		
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_capture_select);
		
		mCategoryTitles = getResources().getStringArray(R.array.add_event_user_category_titles);
		mCategoryDescriptions = getResources().getStringArray(R.array.add_event_user_category_descriptions);
		
		final List<View.OnClickListener> traincarListeners = new ArrayList<View.OnClickListener>(mCategoryTitles.length);
		
		for (int i = 0; i < mCategoryTitles.length; i++)		
		{		
			ViewGroup trainVg = (ViewGroup) findViewById(R.id.train);
			View view = getLayoutInflater().inflate(R.layout.block_traincar, trainVg, false);
			
			((TextView) view.findViewById(R.id.traincar_title)).setText(mCategoryTitles[i]);
			((TextView) view.findViewById(R.id.traincar_description)).setText(mCategoryDescriptions[i]);
			
			View.OnClickListener listener = new View.OnClickListener()
			{				
				@Override
				public void onClick(View v)
				{
					int index = traincarListeners.indexOf(this);
					
					Intent intent = new Intent(CaptureSelectActivity.this, CaptureActivity.class);
					intent.putExtra("negative_category_type", index);
					
					CaptureSelectActivity.this.startActivity(intent);
				}
			};
			
			view.setOnClickListener(listener);
			traincarListeners.add(listener);
			
			trainVg.addView(view);
		}
		
//		SharedPreferences preferences = this.getSharedPreferences(this.getPackageName(), Context.MODE_PRIVATE);
//		if (preferences.getString("capture instructions", null) == null)
//		{
//			CaptureTutorialFragment tutorialFragment = new CaptureTutorialFragment();
//			
//			tutorialFragment.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
//			tutorialFragment.show(getFragmentManager(), null);			
//
//			// preferences.edit().putString("capture instructions",
//			// "Yes").commit();
//			// change this after the presentation
//		}
	}
}
