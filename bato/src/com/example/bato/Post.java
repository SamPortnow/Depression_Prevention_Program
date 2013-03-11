package com.example.bato;

import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;

public class Post extends Activity
{
	CalendarDbAdapter mCalHelper;
	GameDbAdapter mScoresHelper;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		mCalHelper = new CalendarDbAdapter(this);
		mScoresHelper = new GameDbAdapter(this);
	}
	
private class Helper extends AsyncTask <URL, Integer, Long>
{
	
	JSONObject jObject;
	JSONArray jArray = new JSONArray();
	GameDbAdapter mScoresHelper;
	CalendarDbAdapter mCalHelper;

	
	@Override
	protected void onPreExecute()
	{
	
	}

	@Override
	protected Long doInBackground(URL... arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}

}
