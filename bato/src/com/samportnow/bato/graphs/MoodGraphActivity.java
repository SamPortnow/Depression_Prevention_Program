package com.samportnow.bato.graphs;

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
			.replace(android.R.id.content, new MoodGraphFragment())
			.commit();
	}
}