package com.samportnow.bato;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.samportnow.bato.coping.blowfish.Blowfish;
import com.samportnow.bato.coping.floating.FloatActivity;
import com.samportnow.bato.coping.rollingball.Rolling;
import com.samportnow.bato.database.BatoDataSource;

public class CopingFragment extends Fragment
{
	private boolean mIsFloatingActivityUnlocked = false;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.fragment_coping, container, false);
		
		view.findViewById(R.id.breathing_learn).setOnClickListener(new View.OnClickListener()
		{		
			public void onClick(View v)
			{
				new AlertDialog.Builder(getActivity())
					.setTitle(R.string.breathing_title)
					.setMessage(R.string.breathing_description)
					.setPositiveButton(android.R.string.ok, null)
					.create()
					.show();
			}
		});
		
		view.findViewById(R.id.rolling_learn).setOnClickListener(new View.OnClickListener()
		{			
			public void onClick(View v)
			{
				new AlertDialog.Builder(getActivity())
					.setTitle(R.string.rolling_ball_title)
					.setMessage(R.string.rolling_ball_description)
					.setPositiveButton(android.R.string.ok, null)
					.create()
					.show();
			}
		});
		
		view.findViewById(R.id.float_learn).setOnClickListener(new View.OnClickListener()
		{			
			public void onClick(View v)
			{
				new AlertDialog.Builder(getActivity())
					.setTitle(R.string.floating_title)
					.setMessage(R.string.floating_description)
					.setPositiveButton(android.R.string.ok, null)
					.create()
					.show();
			}
		});
		
		view.findViewById(R.id.breathing_play).setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				Intent intent = new Intent(getActivity(), Blowfish.class);
				startActivity(intent);
			}
		});
		
		view.findViewById(R.id.rolling_play).setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				Intent intent = new Intent(getActivity(), Rolling.class);
				startActivity(intent);
			}
		});
		
		view.findViewById(R.id.float_play).setOnClickListener(new View.OnClickListener()
		{			
			public void onClick(View v)
			{
				if (mIsFloatingActivityUnlocked)
				{
					Intent intent = new Intent(getActivity(), FloatActivity.class);
					startActivity(intent);
				}
				else
				{
					Toast.makeText(getActivity(), R.string.floating_unavailable, Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		return view;
	}
	
	@Override
	public void onResume()
	{
		super.onResume();
		
		BatoDataSource dataSource = new BatoDataSource(getActivity()).open();
		
		// Floating activity is unlocked if there exists at least one thought.
		mIsFloatingActivityUnlocked = (dataSource.getRandomThought() != null);
		
		dataSource.close();
	}
}
