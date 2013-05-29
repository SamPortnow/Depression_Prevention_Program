package com.example.bato;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SeekBar;

public class AddFeelingActivity extends Activity
{	
	Context mContext;
	SeekBar feeling;

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
	    super.onCreate(savedInstanceState);
	    mContext = this;
	    setContentView(R.layout.fragment_add_feeling);
	    Button next = (Button) findViewById(R.id.next);
	    feeling = (SeekBar) findViewById(R.id.help);
	    next.setOnClickListener(new OnClickListener()
	    {

			@Override
			public void onClick(View arg0) 
			{
				SharedPreferences preferences = mContext.getSharedPreferences(mContext.getPackageName(), Context.MODE_PRIVATE);
				preferences.edit().putLong("feeling",feeling.getProgress());
				finish();
				Intent i = new Intent(mContext, AddThoughtActivity.class);				
				mContext.startActivity(i);	
			}
	    	
	    });
	}

}
