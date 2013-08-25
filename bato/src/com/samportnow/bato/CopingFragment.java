package com.samportnow.bato;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.samportnow.bato.coping.blowfish.Blowfish;
import com.samportnow.bato.coping.floating.FloatActivity;
import com.samportnow.bato.coping.rollingball.Rolling;

public class CopingFragment extends Fragment
{
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.fragment_coping, container, false);
	}
	
	@Override
	public void onResume()
	{
		super.onResume();
		
		View view = getView();		
		
		Button breath_learn = (Button) view.findViewById(R.id.breathing_learn);
		Button breath_play = (Button) view.findViewById(R.id.breathing_play);
		Button rolling_learn = (Button) view.findViewById(R.id.rolling_learn);
		Button rolling_play = (Button) view.findViewById(R.id.rolling_play);
		Button float_learn = (Button) view.findViewById(R.id.float_learn);
		Button float_play = (Button) view.findViewById(R.id.float_play);
		
		breath_play.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0) 
			{
				Intent intent = new Intent(getActivity(), Blowfish.class);
				startActivity(intent);
			}
			
		});
		
		rolling_play.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0) 
			{
				Intent intent = new Intent(getActivity(), Rolling.class);
				startActivity(intent);
			}
			
		});
		
		float_play.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0) 
			{
				Intent intent = new Intent(getActivity(), FloatActivity.class);
				startActivity(intent);	
			}
			
		});
		
		}
		
	
}
	
	

