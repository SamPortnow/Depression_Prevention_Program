package com.samportnow.bato;

import java.util.GregorianCalendar;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.samportnow.bato.database.BatoDataSource;
import com.samportnow.bato.database.dao.ChallengingThoughtDao;
import com.samportnow.bato.database.dao.HighScoreDao;
import com.samportnow.bato.destroyer.DestroyerGame;

public class CannonSummaryFragment extends Fragment
{
	private List<HighScoreDao> mHighScores = null;
	
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
			
			BatoDataSource dataSource = new BatoDataSource(getActivity()).open();
			
			mHighScores = dataSource.getTopHighScores(10);
			dataSource.close();

			String highScore = mHighScores.isEmpty() ? "0" : String.valueOf(mHighScores.get(0).getScore());
			((TextView) view.findViewById(R.id.cannon_summary_high_score)).setText(highScore);

			view.findViewById(R.id.cannon_summary_see_more).setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					createHighScoresDialog().show();
				}
			});
		}
		else
		{
			view.findViewById(R.id.cannon_summary_locked_container).setVisibility(View.VISIBLE);
			view.findViewById(R.id.cannon_summary_container).setVisibility(View.GONE);
		}
	}
	
	private Dialog createHighScoresDialog()
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		
		BaseAdapter adapter = new BaseAdapter()
		{
			public View getView(int position, View convertView, ViewGroup parent)
			{
				if (convertView == null)
					convertView = LayoutInflater.from(getActivity()).inflate(R.layout.listitem_high_score, parent, false);
				
				HighScoreDao highScore = mHighScores.get(position);
				
				GregorianCalendar calendar = new GregorianCalendar();
				calendar.setTimeInMillis(highScore.getCreated());
				
				((TextView) convertView.findViewById(R.id.high_score_value)).setText(String.valueOf(highScore.getScore()));
				((TextView) convertView.findViewById(R.id.high_score_timestamp)).setText(calendar.getTime().toString());
				
				return convertView;
			}
			
			public long getItemId(int position)
			{
				return mHighScores.get(position).getId();
			}
			
			public Object getItem(int position)
			{
				return mHighScores.get(position);
			}
			
			public int getCount()
			{
				return mHighScores.size();
			}
		};
		
		builder.setPositiveButton(android.R.string.ok, null);
		builder.setAdapter(adapter, null);
		
		return builder.create();
	}

	private boolean isUnlocked()
	{
		BatoDataSource dataSource = new BatoDataSource(getActivity()).open();
		List<ChallengingThoughtDao> challenges = dataSource.getAllChallengingThoughts();
		
		dataSource.close();
		
		return (challenges.size() > 0);
	}
}
