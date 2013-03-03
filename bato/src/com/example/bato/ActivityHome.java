package com.example.bato;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

public class ActivityHome extends Fragment
{
	@Override 
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.activity_home, container, false);
		
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