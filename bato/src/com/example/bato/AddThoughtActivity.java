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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class AddThoughtActivity extends Fragment
{
	RadioGroup radioPosGroup;
	RadioButton radioPosButton;
	Context mContext;
	TextView thought;
	View view;
	AddThoughtPager pager;
	EditText add_thought;
	boolean added; 
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
	    super.onCreate(savedInstanceState);
	    mContext = this.getActivity();
	    pager = (AddThoughtPager) getActivity();
	    view = inflater.inflate(R.layout.fragment_add_thought, null);
	    thought = (TextView) view.findViewById(R.id.add_event_user_thought);
	    add_thought = (EditText) view.findViewById(R.id.add_event_user_thought);
		TextWatcher textWatcher = new TextWatcher()
		{

			@Override
			public void afterTextChanged(Editable arg0) 
			{
				pager.thought = add_thought.getText().toString();
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
		add_thought.addTextChangedListener(textWatcher);
	    radioPosGroup = (RadioGroup) view.findViewById(R.id.pos_neg);
	    radioPosGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
	    {

			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1)
			{
			
				int selectedId = radioPosGroup.getCheckedRadioButtonId();
			    radioPosButton = (RadioButton) view.findViewById(selectedId);
				if (radioPosButton.getText().toString().equals("Yes") && added == false)
				{
					pager.addView();
					added = true;
				}
	
			}
	    	
	    });
	    
	    return view;
	}

}
