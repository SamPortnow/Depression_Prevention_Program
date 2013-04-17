package com.example.bato;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DestroyerStatsFragment extends Fragment
{
	private String[] mHighScores = null;
	int count;
	int Year;
	int Day;
	Calendar cal;
	Date date;
	String sDate;
	String mood;
	String activity;
	HashMap <String, String []> mValues;
	Context mContext;
	Cursor scientist_cursor;
	int personalPoints;
	CalendarDbAdapter mCalHelper;
	int iMood;
	Cursor act;
	Cursor scaleCursor;
	ScaleDbAdapter mScaleDbHelper;

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		mContext = this.getActivity();
		View view = inflater.inflate(R.layout.fragment_destroyer_stats, container, false);
		mScaleDbHelper = new ScaleDbAdapter(getActivity());
		mScaleDbHelper.open();
		scaleCursor = mScaleDbHelper.fetchPositives();
		if (scaleCursor.getCount() > 8)
		{
		view.findViewById(R.id.destroyer_stats_play_game).setOnClickListener(new OnClickListener()
		{			
			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(getActivity(), DestroyerShooterView.class);				
				startActivity(intent);
			}
		});
		}
		else
		{
			LinearLayout lLayout = (LinearLayout)view.findViewById(R.id.destroyerLayout);
			Button play = (Button) view.findViewById(R.id.destroyer_stats_play_game);
			lLayout.removeView(play);
			ImageView lock = new ImageView (getActivity());
			lock.setBackgroundResource(R.drawable.lock);
			lLayout.addView(lock);
			
		}
		
		Cursor cursor = null;
		
		CalendarDbAdapter calendarDbHelper = new CalendarDbAdapter(getActivity());
		calendarDbHelper.open();
		
		cursor = calendarDbHelper.fetchAll();
		while (cursor.moveToNext())
		{
			iMood = cursor.getInt(cursor.getColumnIndexOrThrow(CalendarDbAdapter.COLUMN_NAME_FEELING));
			activity = cursor.getString(cursor.getColumnIndexOrThrow(CalendarDbAdapter.COLUMN_NAME_ACTIVITY));
			mCalHelper = new CalendarDbAdapter(mContext);
			mCalHelper.open();
			act = mCalHelper.fetchActivity(activity);
			if (iMood > 4 && act.getCount() > 1)
			{
				personalPoints += 50;
			}
			
			else
			{
				personalPoints += 25;
			}
		}
		cursor.close();
		
		scientist_cursor = calendarDbHelper.fetchAll();
		
		if (scientist_cursor.getCount() > 0)
		{
			view.findViewById(R.id.personal_scientist_breakdown).setOnClickListener(new OnClickListener()
			{

				@Override
				public void onClick(View arg0) 
				{
		               AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		               builder.setTitle("Personal Scientist Points");
		               builder.setAdapter(new PointsAdapter(mContext, scientist_cursor), null);
		               builder.setPositiveButton("OK", null);
		               AlertDialog alert = builder.create();
		               alert.show();
					
				}
				
			});
		}
		
		else
		{
			view.findViewById(R.id.personal_scientist_breakdown).setEnabled(false);
		}
		calendarDbHelper.close();	
		

		
		((TextView) view.findViewById(R.id.destroyer_stats_personal_points)).setText(String.valueOf(personalPoints));
		
		ScaleDbAdapter scaleDbHelper = new ScaleDbAdapter(getActivity());
		scaleDbHelper.open();
		
		cursor = scaleDbHelper.fetchPositives();
		while (cursor.moveToNext() )
		{
			count ++;
		}
		cursor.close();
		
		((TextView) view.findViewById(R.id.destroyer_stats_bank)).setText(String.valueOf(count));  
		
		scaleDbHelper.close();
		
		GameDbAdapter gameDbHelper = new GameDbAdapter(getActivity());		
		gameDbHelper.open();		
		
		cursor = gameDbHelper.fetchScores();
		int column = cursor.getColumnIndexOrThrow(GameDbAdapter.COLUMN_NAME_SCORE);	
		
		cursor.moveToFirst();
				
		if (cursor.getCount() > 0)
		{
			mHighScores = new String[cursor.getCount()];
			
			cursor.moveToFirst();
			
			for (int i = 0; i < mHighScores.length; i++)
			{					
				mHighScores[i] = cursor.getString(column);
				cursor.moveToNext();
			}		
		}
		else
		{
			mHighScores = new String[1];
			mHighScores[0] = "0";
			
			view.findViewById(R.id.destroyer_stats_more_scores).setEnabled(false);	
		}		
		
		cursor.close();
		gameDbHelper.close();
		
		((TextView) view.findViewById(R.id.destroyer_stats_high_score)).setText(mHighScores[0]);
		
		view.findViewById(R.id.destroyer_stats_more_scores).setOnClickListener(new OnClickListener()
		{							
			@Override
			public void onClick(View v)
			{
				AlertDialog.Builder builder = new Builder(getActivity());
				
				builder.setTitle(R.string.high_scores_title);
				builder.setItems(mHighScores, null);
				builder.setPositiveButton(android.R.string.ok, null);
				builder.create().show();				
			}
		});
		
		return view;
	}
}
