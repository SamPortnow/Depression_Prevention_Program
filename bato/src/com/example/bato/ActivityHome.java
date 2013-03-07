package com.example.bato;

import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ActivityHome extends Fragment
{
	Context mContext;
	GameDbAdapter mDbHelper;
	TextView destroyers;
	int successes; 
	CalendarDbAdapter mCalHelper;
	int score;
	TextView scientist;
	GameDbAdapter mScoresHelper;
	
	@Override 
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.activity_home, container, false);
		LinearLayout scoring = (LinearLayout) view.findViewById(R.id.scoring);
		mContext = this.getActivity();
	    mDbHelper= new GameDbAdapter(mContext);
	    mDbHelper.open();
	    destroyers = (TextView) view.findViewById(R.id.body_count);
	    Cursor activity = mDbHelper.fetchGames();
	    if (activity.moveToFirst())
	    {
    		while (activity.moveToNext())
    		{
    			if (activity.getString(activity.getColumnIndexOrThrow(GameDbAdapter.COLUMN_NAME_SUCCESS)).contains("Yes"))
    			{
    				successes++;
    			}
    			
    			
    		}
    		destroyers.append(" "+successes);
	    }
	    activity.close();
	    successes = 0;
	    
	    mCalHelper=new CalendarDbAdapter(mContext);
	    mCalHelper.open();
	    Cursor points = mCalHelper.fetchThoughts();
	    if (points.moveToFirst())
	    {
	    	while (points.moveToNext())
	    	{
	    		score++;
	    	}
	    }
	    if (score > 0)
	    {
	    	score = score * 25;
	    }
	    
	    scientist = (TextView) view.findViewById(R.id.scientist);
	    scientist.append(" " + score);

	    
	    points.close();
	    score = 0;
	    
	    mScoresHelper = new GameDbAdapter(mContext);
	    mScoresHelper.open();
	    Cursor scores = mScoresHelper.fetchHighScores();
	    while (scores.moveToNext())
	    {
	    	TextView high = new TextView(mContext);
	    	high.setText(scores.getString(scores.getColumnIndexOrThrow(GameDbAdapter.COLUMN_NAME_SCORE)));
	    	high.setGravity(Gravity.CENTER);
	    	scoring.addView(high);
	    	
	    	
	    }
		scores.close();
		
		view.findViewById(R.id.add_event).setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View view) 
			{
				AddEventFragment addThoughtFragment = new AddEventFragment();
				addThoughtFragment.show(getFragmentManager(), null);
			}
			
		});
		
		return view;
	};
	
	@Override
	public void onDestroyView()
	{
		super.onDestroyView();
		mDbHelper.close();
	}
}