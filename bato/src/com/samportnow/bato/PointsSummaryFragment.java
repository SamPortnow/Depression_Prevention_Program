package com.samportnow.bato;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.samportnow.bato.addthought.AddEventActivity;
import com.samportnow.bato.database.BatoDataSource;

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
		
		view.findViewById(R.id.points_summary_view_breakdown).setOnClickListener(new View.OnClickListener()
		{			
			@Override
			public void onClick(View v)
			{
				PointsBreakdownFragment fragment = new PointsBreakdownFragment();				
				fragment.show(getFragmentManager(), "points_breakdown_fragment");
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
		BatoDataSource dataSource = new BatoDataSource(getActivity()).open();
		
		int points = dataSource.getPoints();
		dataSource.close();

		return points;
	}
}
