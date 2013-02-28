package com.example.bato;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class WelcomeFragment extends Fragment implements OnClickListener
{
	public interface OnBeginSetupListener
	{
		public void onBeginSetupClick();		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.fragment_welcome, container, false);
		
		Button beginSetup = (Button) view.findViewById(R.id.step1);
		beginSetup.setOnClickListener(this);
		
		return view;
	}

	@Override
	public void onClick(View view)
	{
		OnBeginSetupListener listener = (OnBeginSetupListener) getActivity();
		
		if (listener == null)
			return;
		
		listener.onBeginSetupClick();
	}
}
