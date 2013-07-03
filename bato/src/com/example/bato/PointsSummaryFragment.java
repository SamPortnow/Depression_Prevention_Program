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

public class PointsSummaryFragment extends Fragment
{
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.fragment_points_summary, container, false);
		
		view.findViewById(R.id.points_summary_badge).setOnClickListener(new OnClickListener()
		{			
			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(getActivity(), AddEventActivity.class);				
				startActivity(intent);	
			}
		});
		
		return view;
	}
	
	@Override
	public void onResume()
	{
		super.onResume();
		
		((TextView) getView().findViewById(R.id.points_summary_points_value)).setText(String.valueOf(getPoints()));
	}
	
	private int getPoints()
	{
		CalendarDbAdapter adapter = new CalendarDbAdapter(getActivity()).open();
		Cursor cursor = adapter.fetchAll();

		int points = (cursor.getCount() * 25);

		while (cursor.moveToNext())
		{
			if (cursor.getInt(cursor.getColumnIndexOrThrow(CalendarDbAdapter.COLUMN_NAME_FEELING)) < 5)
				continue;

			Cursor subCursor = adapter.fetchActivity(cursor.getString(cursor.getColumnIndexOrThrow(CalendarDbAdapter.COLUMN_NAME_ACTIVITY)));

			if (subCursor.getCount() > 1)
				points += 50;

			subCursor.close();
		}

		cursor.close();
		adapter.close();

		return points;
	}
}
