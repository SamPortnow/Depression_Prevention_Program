package com.example.bato;

import android.app.Fragment;
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

public class ScaleStatsFragment extends Fragment
{
	Cursor cursor;
	CalendarDbAdapter mCalHelper;
	ImageView lock;
	boolean play;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.fragment_scale_stats, container, false);
		LinearLayout lLayout = (LinearLayout) view.findViewById(R.id.lLayout);
		mCalHelper = new CalendarDbAdapter(getActivity());
		mCalHelper.open();
	    Cursor thoughts = mCalHelper.fetchThoughts();
    	while (thoughts.moveToNext())
    	{
    		if (thoughts.getString(thoughts.getColumnIndexOrThrow(CalendarDbAdapter.COLUMN_NAME_THOUGHT)).length() > 0 && thoughts.getString(thoughts.getColumnIndexOrThrow(CalendarDbAdapter.COLUMN_NAME_THOUGHT)).charAt(0) == '-')
    	{
    		    play = true;
    			break;
    	}
    	}
    	
    	if (play == true)
    	{
		view.findViewById(R.id.scale_stats_play_game).setOnClickListener(new OnClickListener()
		{			
			@Override
			public void onClick(View view)
			{
				Intent intent = new Intent(getActivity(), ScaleActivity.class);
				startActivity(intent);
			}
		});
    	}
    	else
	    {
    		Button playGame =  (Button) view.findViewById(R.id.scale_stats_play_game);
    		lLayout.removeView(playGame);
	    	lock = new ImageView(getActivity());
	    	lock.setBackgroundResource(R.drawable.lock);
	    	lLayout.addView(lock);
	    	
	    }
		return view;
	}
}
