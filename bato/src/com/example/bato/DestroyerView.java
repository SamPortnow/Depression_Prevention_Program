package com.example.bato;

import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class DestroyerView extends Fragment
{



	private Context mContext;
	Paint paint = new Paint();
	private AnimatedNegative PositiveAnimatedNegative;
	boolean play_again;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	        Bundle savedInstanceState) 
	{
	    super.onCreate(savedInstanceState);
	    mContext = this.getActivity();
	    View view = inflater.inflate(R.layout.activity_destroyer, container, false);
	    Button fire = (Button) view.findViewById(R.id.destroy);
	    final EditText positive_thought = (EditText) view.findViewById(R.id.destroyer);
	    PositiveAnimatedNegative = (AnimatedNegative) view.findViewById(R.id.anim_view);
	    fire.setOnClickListener(new OnClickListener()
	    	{
	    	@Override
	    	public void onClick(View arg0) 
	    		{
	    		//if the button is clicked invalidate the ondraw method and pass in the text of the positive word 
				Log.e("positive thought is",""+ positive_thought.getText().toString());
	
	    		if (positive_thought.getText().toString().isEmpty() != true)
        				{
        					Log.e("I am", "here");
        					PositiveAnimatedNegative.invalidate();
        					PositiveAnimatedNegative.add = true;
        					PositiveAnimatedNegative.positive_word = positive_thought.getText().toString();
        					positive_thought.setText(null);
        				}
        			else
        				{
        					PositiveAnimatedNegative.add = false;
        				}
	    		}
	    	});
	   return view;
	   
	}

}