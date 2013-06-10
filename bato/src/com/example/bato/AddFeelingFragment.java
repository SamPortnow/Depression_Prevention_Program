package com.example.bato;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class AddFeelingFragment extends Fragment
{	
	Context mContext;
	SeekBar feeling;
	AddThoughtPager mPager;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
	    super.onCreate(savedInstanceState);
	    mPager = (AddThoughtPager) this.getActivity();
	    View view = inflater.inflate(R.layout.fragment_add_feeling, null);
	    feeling = (SeekBar) view.findViewById(R.id.help);
	    mPager.feeling = feeling.getProgress();
	    feeling.setOnSeekBarChangeListener(new OnSeekBarChangeListener()
	    {

			@Override
			public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) 
			{
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onStartTrackingTouch(SeekBar arg0) 
			{
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onStopTrackingTouch(SeekBar arg0) 
			{
			    mPager.feeling = feeling.getProgress();
			}
	    	
	    });
	    return view;
	}

}
