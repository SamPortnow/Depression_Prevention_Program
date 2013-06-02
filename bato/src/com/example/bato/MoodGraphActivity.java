package com.example.bato;

import android.app.Activity;
import android.os.Bundle;

public class MoodGraphActivity extends Activity
{
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		getFragmentManager()
			.beginTransaction()
			.replace(android.R.id.content, new Graph())
			.commit();
	}
}
