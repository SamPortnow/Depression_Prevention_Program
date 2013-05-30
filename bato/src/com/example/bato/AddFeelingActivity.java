package com.example.bato;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;

public class AddFeelingActivity extends Fragment
{	
	Context mContext;
	SeekBar feeling;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
	    super.onCreate(savedInstanceState);
	    mContext = this.getActivity();
	    View view = inflater.inflate(R.layout.fragment_add_feeling, null);
	    feeling = (SeekBar) view.findViewById(R.id.help);
	    return view;
	}

}
