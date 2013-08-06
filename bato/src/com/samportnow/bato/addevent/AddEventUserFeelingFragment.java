package com.samportnow.bato.addevent;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.samportnow.bato.R;

public class AddEventUserFeelingFragment extends Fragment
{	
	private SeekBar mFeelingSeekBar = null;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
	    super.onCreate(savedInstanceState);	   

	    View view = inflater.inflate(R.layout.fragment_add_event_user_feeling, null);
	    
	    mFeelingSeekBar = (SeekBar) view.findViewById(R.id.user_feeling);
	    
		view.findViewById(R.id.next_fragment).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Bundle eventBundle = getArguments();
				eventBundle.putInt("user_feeling", mFeelingSeekBar.getProgress());
				
				Fragment fragment = new AddEventUserThoughtFragment();
				fragment.setArguments(eventBundle);
				
				getFragmentManager()
					.beginTransaction()
					.replace(R.id.fragment_container, fragment)
					.commit();
			}
		});

	    return view;
	}
}
