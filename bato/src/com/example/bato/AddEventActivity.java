package com.example.bato;

import android.app.Activity;
import android.os.Bundle;

public class AddEventActivity extends Activity
{
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_add_event);
		
		getFragmentManager().beginTransaction().replace(R.id.fragment_container, new AddEventUserActivityFragment()).commit();
	}
}
