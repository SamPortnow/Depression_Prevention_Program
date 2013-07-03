package com.example.bato;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class CannonSummaryFragment extends Fragment
{
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.fragment_cannon_summary, container, false);
	}
	
	@Override
	public void onResume()
	{
		super.onResume();
		
		View view = getView();		
		
		if (isUnlocked() == true)
		{
			view.findViewById(R.id.cannon_summary_container).setVisibility(View.VISIBLE);
			view.findViewById(R.id.cannon_summary_locked_container).setVisibility(View.GONE);
			
			ImageView badgeImageView = (ImageView) view.findViewById(R.id.cannon_summary_badge);
		
			badgeImageView.setOnClickListener(new OnClickListener()
			{			
				@Override
				public void onClick(View v)
				{
					Intent intent = new Intent(getActivity(), DestroyerGame.class);
					startActivity(intent);
				}
			});
			
			final String[] highScores = getHighScores();						
			((TextView) view.findViewById(R.id.cannon_summary_high_score)).setText(highScores[0]);
			
			view.findViewById(R.id.cannon_summary_see_more).setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
					
					builder.setTitle(R.string.cannon_summary_high_scores_dialog_title);
					builder.setItems(highScores, null);
					builder.setPositiveButton(android.R.string.ok, null);
					
					builder.create().show();
				}
			});
		}
		else
		{
			view.findViewById(R.id.cannon_summary_locked_container).setVisibility(View.VISIBLE);
			view.findViewById(R.id.cannon_summary_container).setVisibility(View.GONE);
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
	
	private String[] getHighScores()
	{
		GameDbAdapter adapter = new GameDbAdapter(getActivity()).open();
		Cursor cursor = adapter.fetchScores();
		
		String[] values = {"0"};
		
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
