package com.example.bato;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class AddThoughtActivity extends Activity
{
	RadioGroup radioPosGroup;
	RadioButton radioPosButton;
	Context mContext;
	TextView thought;
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
	    super.onCreate(savedInstanceState);
	    mContext = this;
	    setContentView(R.layout.fragment_add_thought);
	    thought = (TextView) findViewById(R.id.add_event_user_thought);
	    radioPosGroup = (RadioGroup) findViewById(R.id.pos_neg);
	    radioPosGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
	    {

			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1)
			{
			
				int selectedId = radioPosGroup.getCheckedRadioButtonId();
			    radioPosButton = (RadioButton) findViewById(selectedId);
			    Log.e("text is", "" + radioPosButton.getText());
			    if (radioPosButton.getText().toString().equals("Yes"))
			    {
					SharedPreferences preferences = mContext.getSharedPreferences(mContext.getPackageName(), Context.MODE_PRIVATE);
					preferences.edit().putString("thought", thought.getText().toString()).commit();
			    	finish();
					Intent i = new Intent(mContext, TrainActivity.class);				
					mContext.startActivity(i);	
			    }
			    
			    else
			    {
					finish();
					Intent i = new Intent(mContext, MainActivity.class);				
					mContext.startActivity(i);	
			    }
			}
	    	
	    });
	}

}
