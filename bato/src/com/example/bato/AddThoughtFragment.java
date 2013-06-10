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
import android.widget.Toast;

public class AddThoughtFragment extends Fragment
{
	RadioGroup radioPosGroup;
	RadioButton radioPosButton;
	Context mContext;
	TextView thought;
	View view;
	AddThoughtPager pager;
	EditText add_thought;
	TextView neg_question;
	boolean added; 
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
	    super.onCreate(savedInstanceState);
	    mContext = this.getActivity();
	    pager = (AddThoughtPager) getActivity();
	    view = inflater.inflate(R.layout.fragment_add_thought, null);
	    radioPosGroup = (RadioGroup) view.findViewById(R.id.pos_neg);
	    radioPosGroup.setVisibility(View.INVISIBLE);
	    neg_question = (TextView) view.findViewById(R.id.pos_or_neg);
	    neg_question.setVisibility(View.INVISIBLE);
	    add_thought = (EditText) view.findViewById(R.id.add_event_user_thought);
		TextWatcher textWatcher = new TextWatcher()
		{

			@Override
			public void afterTextChanged(Editable arg0) 
			{
				String thought = add_thought.getText().toString();
				boolean atMaxLength = (thought.length() >= 60);
				if (atMaxLength == true)
		          Toast.makeText(getActivity(), "Limit is 60 characters!", Toast.LENGTH_SHORT).show();
				pager.thought = thought;
			    radioPosGroup.setVisibility(View.VISIBLE);
			    neg_question.setVisibility(View.VISIBLE);

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
				pager.tag = radioPosButton.getText().toString();
	
			}
	    	
	    });
	    
	    return view;
	}

}
