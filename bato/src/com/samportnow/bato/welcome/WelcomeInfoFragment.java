package com.samportnow.bato.welcome;

import com.samportnow.bato.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class WelcomeInfoFragment extends Fragment
{
	private String mHeaderText;
	private String mContentText;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.fragment_welcome_info, container, false);
		
		((TextView) view.findViewById(R.id.info_header)).setText(mHeaderText);
		((TextView) view.findViewById(R.id.info_content)).setText(mContentText);
		
		return view;
	}
	
	public void setHeaderText(String headerText)
	{
		mHeaderText = headerText;
	}
	
	public void setContentText(String contentText)
	{
		mContentText = contentText;
	}
}
