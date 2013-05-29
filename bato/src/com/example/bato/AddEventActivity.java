package com.example.bato;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class AddEventActivity extends Activity
{
	Context mContext;
	EditText add_act;
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.fragment_add_activity);
	    add_act = (EditText) findViewById(R.id.add_event_user_activity);
	    Button next = (Button) findViewById(R.id.next);
	    mContext = this;
	    next.setOnClickListener(new OnClickListener()
	    {

			@Override
			public void onClick(View arg0) 
			{
				SharedPreferences preferences = mContext.getSharedPreferences(mContext.getPackageName(), Context.MODE_PRIVATE);
				preferences.edit().putString("activity", add_act.getText().toString()).commit();
				finish();
				Intent i = new Intent(mContext, AddFeelingActivity.class);				
				mContext.startActivity(i);	
			}
	    	
	    });
	}

}
