package com.example.bato;

import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ScientistPointsFragment extends Fragment
{
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.fragment_scientist_points, container, false);
	}
	
	@Override
	public void onResume()
	{
		super.onResume();
		
		((TextView) getView().findViewById(R.id.scientist_points_total_earned_value)).setText(String.valueOf(getPoints()));
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
				points += 25;

			subCursor.close();
		}

		cursor.close();
		adapter.close();

		return points;
	}
}
