package com.example.bato;

import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

public class ScaleStatsFragment extends Fragment
{
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.fragment_scale_stats, container, false);

		view.findViewById(R.id.scale_stats_play_game).setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				Intent intent = new Intent(getActivity(), ScaleActivity.class);
				startActivity(intent);
			}
		});		

		return view;
	}
	
	@Override
	public void onResume()
	{
		super.onResume();
		
		View view = getView();
		
		TextView valueTextView = (TextView) view.findViewById(R.id.scale_stats_positive_thoughts_generated_value);
		valueTextView.setText(String.valueOf(getNumberOfPositiveThoughts()));
		
		if (isUnlocked() == true)
		{
			view.findViewById(R.id.scale_stats_notice).setVisibility(View.GONE);			
		}
		else
		{
			view.findViewById(R.id.scale_stats_play_game).setVisibility(View.GONE);
			view.findViewById(R.id.scale_stats_thoughts_generated_container).setVisibility(View.GONE);
		}
	}
	
	private boolean isUnlocked()
	{
		CalendarDbAdapter adapter = new CalendarDbAdapter(getActivity()).open();
		Cursor cursor = adapter.fetchThoughts();
		
		boolean flag = false;

		while (cursor.moveToNext())
		{
			String thought = cursor.getString(cursor.getColumnIndexOrThrow(CalendarDbAdapter.COLUMN_NAME_THOUGHT));

			if ((thought.length() > 0) && (thought.charAt(0) == '-'))
			{
				flag = true;
				break;
			}
		}	
		
		cursor.close();
		adapter.close();

		return flag;
	}
	
	private int getNumberOfPositiveThoughts()
	{
		ScaleDbAdapter adapter = new ScaleDbAdapter(getActivity()).open();
		Cursor cursor = adapter.fetchPositives();
		
		int count = cursor.getCount();
		
		cursor.close();
		adapter.close();
		
		return count;
	}
}
