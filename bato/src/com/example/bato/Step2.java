package com.example.bato;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

public class Step2 extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_step2); 
		//NumberPicker npl1=(NumberPicker) findViewById(R.id.numberpickerlikert1); //customize the numberpicker
		//npl1.setMaxValue(7);
		//npl1.setMinValue(1); 
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_step2, menu);
		return true;
	}

	public void to_step3(View view)
	{
		Intent intent= new Intent(this, Step3.class);
    	startActivity(intent);
	}
}
