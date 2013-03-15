package com.example.bato;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.IntentService;
import android.content.Intent;
import android.database.Cursor;
import android.os.IBinder;


public class Post extends IntentService
{
	CalendarDbAdapter mCalHelper;
	Cursor cal;
	int game_count;
	int cal_count;
	int current;
	CalendarPushDbAdapter mPushDbHelper = new CalendarPushDbAdapter(this);

	  public Post() 
	  {
		    super("Post");
		    
		  
	  }

	  @Override
	  protected void onHandleIntent(Intent intent) 
	  {
		mCalHelper = new CalendarDbAdapter(this);
		JSONArray jArrayCal = new JSONArray();
		mPushDbHelper.open();
		Cursor database = mPushDbHelper.fetchPush();
		if (database.moveToFirst())
		{
			current = database.getInt(database.getColumnIndexOrThrow(CalendarPushDbAdapter.KEY_ROWID));
		}
		mCalHelper.open();
		cal = mCalHelper.fetchLatest(current);
		
		while (cal.moveToNext())
		{

			try 
			{
				JSONObject jObjectCal = new JSONObject();
				jObjectCal.put("Year", cal.getInt(cal.getColumnIndexOrThrow(CalendarDbAdapter.COLUMN_NAME_YEAR)));
				jObjectCal.put("Day", cal.getInt(cal.getColumnIndexOrThrow(CalendarDbAdapter.COLUMN_NAME_DAY)));
				jObjectCal.put("Minutes", cal.getInt(cal.getColumnIndexOrThrow(CalendarDbAdapter.COLUMN_NAME_MINUTES)));
				jObjectCal.put("Activity", cal.getString(cal.getColumnIndexOrThrow(CalendarDbAdapter.COLUMN_NAME_ACTIVITY)));
				jObjectCal.put("Feeling", cal.getInt(cal.getColumnIndexOrThrow(CalendarDbAdapter.COLUMN_NAME_FEELING)));
				jObjectCal.put("Thought", cal.getString(cal.getColumnIndexOrThrow(CalendarDbAdapter.COLUMN_NAME_THOUGHT)));
				jArrayCal.put(jObjectCal);
				current++;
			} 
			
			catch (IllegalArgumentException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		cal.close();
		mCalHelper.close();
		try 
		{
			
		PostData(jArrayCal, current);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
		
		private void PostData(JSONArray jArrayCal, int current) throws ClientProtocolException, IOException
		{
		HttpClient httpclientcal = new DefaultHttpClient();
		HttpPost httpostcal = new HttpPost("http://10.0.2.2:5000/");
		StringEntity sCal;

		try
		{
			sCal = new StringEntity(jArrayCal.toString());
			httpostcal.setEntity(sCal);
			httpostcal.setHeader("Accept", "application/json");
			httpostcal.setHeader("Content-type", "application/json");
			HttpResponse postResponse = httpclientcal.execute(httpostcal);
			if (postResponse.getStatusLine().getStatusCode() == 200)
			{
					current++;
					mPushDbHelper.createPush(current);
			}
			
			mPushDbHelper.close();
			
		}
		catch (UnsupportedEncodingException e) 
		{
			e.printStackTrace();
		}
			
			

			
			
		}
	



@Override
public IBinder onBind(Intent arg0) {
	// TODO Auto-generated method stub
	return null;
}




}
