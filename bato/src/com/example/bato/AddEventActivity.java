package com.example.bato;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

public class AddEventActivity extends Fragment
{
	Context mContext;
	EditText add_act;
	AddThoughtPager pager;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
	    super.onCreate(savedInstanceState);
	    View view = inflater.inflate(R.layout.fragment_add_activity, null);
	    pager = (AddThoughtPager) getActivity();
	    add_act = (EditText) view.findViewById(R.id.add_event_user_activity);
		TextWatcher textWatcher = new TextWatcher()
		{

			@Override
			public void afterTextChanged(Editable arg0) 
			{
				String act = add_act.getText().toString();
				boolean atMaxLength = (act.length() >= 60);
				if (atMaxLength == true)
		          Toast.makeText(getActivity(), "Limit is 60 characters!", Toast.LENGTH_SHORT).show();
				pager.thought = act;
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) 
			{
				
			}

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) 
			{
				
			
			}
			
		};
		add_act.addTextChangedListener(textWatcher);
	    return view;

	}

}
