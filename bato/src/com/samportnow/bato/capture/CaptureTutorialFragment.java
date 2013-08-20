package com.samportnow.bato.capture;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.samportnow.bato.R;

public class CaptureTutorialFragment extends DialogFragment
{
	private String[] mInstructions = null;
	private TextView mInstructionsTv = null;
	
	private int mIndex = 0;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.fragment_capture_tutorial, container, false);
		
		mInstructions = getResources().getStringArray(R.array.capture_tutorial_instructions);
		
		mInstructionsTv = (TextView) view.findViewById(R.id.capture_instructions_label);
		mInstructionsTv.setText(mInstructions[mIndex++]);
		
		view.findViewById(R.id.capture_ok_button).setOnClickListener(new View.OnClickListener()
		{			
			@Override
			public void onClick(View v)
			{				
				if (mInstructionsTv.getVisibility() == View.VISIBLE)
				{
					if (mIndex < mInstructions.length)
					{
						mInstructionsTv.setText(mInstructions[mIndex++]);
					}
					else
					{
						mInstructionsTv.setVisibility(View.GONE);					
						getView().findViewById(R.id.capture_sketch).setVisibility(View.VISIBLE);
					}
				}
				else
				{
					CaptureTutorialFragment.this.dismiss();
				}
			}
		});	
		
		return view;
	}
}
