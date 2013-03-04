package com.example.bato;

import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

public class ActivityHome extends Fragment
{
	Context mContext;
	GameDbAdapter mDbHelper;
	TextView destroyers;
	int successes; 
	
	@Override 
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.activity_home, container, false);
		mContext = this.getActivity();
	    mDbHelper=new GameDbAdapter(mContext);
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
	    
	    successes = 0;
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
}