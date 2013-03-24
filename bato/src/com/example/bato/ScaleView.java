package com.example.bato;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class ScaleView extends Activity
{	
	Context mContext; 
	Scale mScale;
	EditText positive_thought;
	Button scale_it;
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
	    super.onCreate(savedInstanceState);
	    this.getActionBar().hide();
	    mContext = this;
	    mScale = (Scale) findViewById(R.id.anim_view);
	    positive_thought = (EditText) findViewById(R.id.thoughts);
	    scale_it = (Button) findViewById(R.id.scale_it);

	}

}
