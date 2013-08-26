package com.samportnow.bato.capture;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.samportnow.bato.R;
import com.samportnow.bato.database.BatoDataSource;

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
		final int[] thoughtCounts = new int[mCategoryTitles.length];

		BatoDataSource dataSource = new BatoDataSource(this).open();

		for (int i = 0; i < mCategoryTitles.length; i++)
		{			
			ViewGroup trainVg = (ViewGroup) findViewById(R.id.train);
			View view = getLayoutInflater().inflate(R.layout.block_traincar, trainVg, false);

			((TextView) view.findViewById(R.id.traincar_title)).setText(mCategoryTitles[i]);
			((TextView) view.findViewById(R.id.traincar_description)).setText(mCategoryDescriptions[i]);

			thoughtCounts[i] = dataSource.getNegativeThoughts(i).size();
			
			// TODO: fetch using strings.xml
			((TextView) view.findViewById(R.id.traincar_thought_count)).setText(thoughtCounts[i] + " negative thoughts.");

			View.OnClickListener listener = new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{					
					int index = traincarListeners.indexOf(this);
					
					if (thoughtCounts[index] < 1)
						return;

					Intent intent = new Intent(CaptureSelectActivity.this, CaptureActivity.class);
					intent.putExtra("negative_type", index);

					CaptureSelectActivity.this.startActivity(intent);
				}
			};

			view.setOnClickListener(listener);
			traincarListeners.add(listener);

			trainVg.addView(view);
		}

		dataSource.close();

		SharedPreferences preferences = getSharedPreferences(getLocalClassName(), Context.MODE_PRIVATE);

		if (preferences.getBoolean("capture_tutorial_shown", false) == false)
		{
			CaptureTutorialFragment tutorialFragment = new CaptureTutorialFragment();

			tutorialFragment.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
			tutorialFragment.show(getFragmentManager(), null);

			preferences.edit().putBoolean("capture_tutorial_shown", true).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.activity_capture_select, menu);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (item.getItemId() == R.id.menu_capture_tutorial_instructions)
		{
			CaptureTutorialFragment tutorialFragment = new CaptureTutorialFragment();

			tutorialFragment.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
			tutorialFragment.show(getFragmentManager(), null);
		}
		else
			return super.onOptionsItemSelected(item);

		return true;
	}
}
