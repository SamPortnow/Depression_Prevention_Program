package com.example.bato;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ActivityHome extends Fragment
{	
	@Override 
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.activity_home, container, false);
		
		if (savedInstanceState == null)
		{
			FragmentTransaction transaction = getFragmentManager().beginTransaction();
		
			transaction.add(R.id.home_fragment_container, new ScaleStatsFragment(), "scale_stats_fragment");
			transaction.add(R.id.home_fragment_container, new DestroyerStatsFragment(), "destroyer_stats_fragment");
			transaction.commit();
		}
		
		return view;
	};
}