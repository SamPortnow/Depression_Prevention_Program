package com.example.bato;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class Setup extends Fragment
{
	UserNameDbAdapter UserNameDbHelper;
	EditText username;
	
	@Override 
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.activity_setup, container, false);
		username = (EditText) view.findViewById(R.id.username);
		Button finish = (Button) view.findViewById(R.id.finish);
		finish.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0) 
			{
				SharedPreferences prefs = getActivity().getSharedPreferences(
					      "com.example.app", Context.MODE_PRIVATE);
				prefs.edit().putString("username", username.getText().toString()).commit();
				FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.addToBackStack(null);            
                fragmentTransaction.replace(R.id.fragment_container, new DestroyerStatsFragment());
                fragmentTransaction.commit();
				
	
			}
			
		});
		return view;
	}
	

	@Override
	public void onDestroyView()
	{
		super.onDestroyView();
	}

}
