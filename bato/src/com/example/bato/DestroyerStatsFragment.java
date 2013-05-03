package com.example.bato;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

public class DestroyerStatsFragment extends Fragment
{
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.fragment_destroyer_stats, container, false);

		view.findViewById(R.id.destroyer_stats_play_game).setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(getActivity(), DestroyerShooterView.class);
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
		
		int personalPoints = getPersonalPoints();
		
		if (personalPoints > 0)
		{
			view.findViewById(R.id.personal_scientist_breakdown).setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View view)
				{
					CalendarDbAdapter adapter = new CalendarDbAdapter(getActivity()).open();
					Cursor cursor = adapter.fetchAll();
					
					AlertDialog.Builder builder = new Builder(getActivity());

					builder.setTitle(R.string.personal_points_title);
					builder.setAdapter(new PointsAdapter(getActivity(), cursor), null);
					builder.setPositiveButton(android.R.string.ok, null);

					builder.create().show();
				}

			});
		}
		
		view.findViewById(R.id.personal_scientist_breakdown).setEnabled(personalPoints > 0);

		((TextView) view.findViewById(R.id.destroyer_stats_personal_points)).setText(String.valueOf(personalPoints));				
		((TextView) view.findViewById(R.id.destroyer_stats_bank)).setText(String.valueOf(getThoughtsInBank()));

		String[] highScores = getHighScores();

		if (highScores == null)
		{
			highScores = new String[1];
			highScores[0] = "0";
		}
		
		final String[] dialogHighScorse = highScores;
		
		view.findViewById(R.id.destroyer_stats_more_scores).setEnabled(highScores.length > 1);

		((TextView) view.findViewById(R.id.destroyer_stats_high_score)).setText(highScores[0]);

		view.findViewById(R.id.destroyer_stats_more_scores).setOnClickListener(new OnClickListener()
		{				
			@Override
			public void onClick(View v)
			{
				AlertDialog.Builder builder = new Builder(getActivity());

				builder.setTitle(R.string.high_scores_title);
				builder.setItems(dialogHighScorse, null);
				builder.setPositiveButton(android.R.string.ok, null);
				
				builder.create().show();
			}
		});

		if (isUnlocked() == true)
		{
			view.findViewById(R.id.destroyer_stats_notice).setVisibility(View.GONE);
		}
		else
		{
			view.findViewById(R.id.destroyer_stats_points_container).setVisibility(View.GONE);
			view.findViewById(R.id.destroyer_stats_play_game).setVisibility(View.GONE);
		}
	}

	private boolean isUnlocked()
	{
		ScaleDbAdapter adapter = new ScaleDbAdapter(getActivity()).open();
		Cursor cursor = adapter.fetchPositives();

		int count = cursor.getCount();

		cursor.close();
		adapter.close();

		return (count > 8);
	}

	private int getPersonalPoints()
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

	private int getThoughtsInBank()
	{
		ScaleDbAdapter adapter = new ScaleDbAdapter(getActivity()).open();
		Cursor cursor = adapter.fetchPositives();

		int count = cursor.getCount();

		cursor.close();
		adapter.close();

		return count;
	}

	private String[] getHighScores()
	{
		GameDbAdapter adapter = new GameDbAdapter(getActivity()).open();
		Cursor cursor = adapter.fetchScores();

		String[] values = null;

		if (cursor.getCount() > 0)
		{
			int column = cursor.getColumnIndexOrThrow(GameDbAdapter.COLUMN_NAME_SCORE);
			values = new String[cursor.getCount()];

			cursor.moveToFirst();

			for (int i = 0; i < values.length; i++)
			{
				values[i] = cursor.getString(column);
				cursor.moveToNext();
			}
		}

		cursor.close();
		adapter.close();

		return values;
	}
}
