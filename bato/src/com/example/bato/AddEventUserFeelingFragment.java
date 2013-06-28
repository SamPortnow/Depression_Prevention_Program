package com.example.bato;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class AddEventUserFeelingFragment extends Fragment
{	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
	    super.onCreate(savedInstanceState);

	    View view = inflater.inflate(R.layout.fragment_add_event_user_feeling, null);

	    return view;
	}

}
