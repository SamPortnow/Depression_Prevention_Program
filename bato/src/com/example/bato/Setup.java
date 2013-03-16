package com.example.bato;

import android.app.Fragment;
import android.app.FragmentTransaction;
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
		UserNameDbHelper = new UserNameDbAdapter(getActivity());
		UserNameDbHelper.open();
		UserNameDbHelper.createUserName(username.getText().toString());
		Button finish = (Button) view.findViewById(R.id.finish);
		finish.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0) 
			{
				UserNameDbHelper.createUserName(username.getText().toString());
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
		UserNameDbHelper.close();
	}

}
