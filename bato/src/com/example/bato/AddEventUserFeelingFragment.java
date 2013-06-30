package com.example.bato;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

public class AddEventUserFeelingFragment extends Fragment
{	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
	    super.onCreate(savedInstanceState);	   

	    View view = inflater.inflate(R.layout.fragment_add_event_user_feeling, null);
	    
	    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
	    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

	    return view;
	}

}
