package com.samportnow.bato.destroyer;

import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.samportnow.bato.R;

public class DestroyerGameTutorialFragment extends DialogFragment
{
	private String[] mInstructions = null;
	private TextView mInstructionsTv = null;
	
	private int mIndex = 0;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.fragment_destroyer_game_tutorial, container, false);
		
		mInstructions = getResources().getStringArray(R.array.destroyer_tutorial_instructions);
		Log.e("instructions are", "" + mInstructions);
		mInstructionsTv = (TextView) view.findViewById(R.id.destroyer_instructions_label);
		Log.e("TextView", "" + mInstructionsTv);
		mInstructionsTv.setText(mInstructions[mIndex++]);
		
		view.findViewById(R.id.destroyer_ok_button).setOnClickListener(new View.OnClickListener()
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
						getView().findViewById(R.id.destroyer_sketch).setVisibility(View.VISIBLE);
					}
				}
				else
				{
					DestroyerGameTutorialFragment.this.dismiss();
				}
			}
		});	
		
		return view;
	}
}