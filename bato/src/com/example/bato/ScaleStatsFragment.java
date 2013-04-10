package com.example.bato;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

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
}
