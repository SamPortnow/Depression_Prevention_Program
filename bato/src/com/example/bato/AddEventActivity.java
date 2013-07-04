package com.example.bato;

import java.util.Calendar;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class AddEventActivity extends Activity
{
	private Bundle mEventBundle = new Bundle();
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_add_event);
		
		Fragment fragment = new AddEventUserActivityFragment();
		fragment.setArguments(mEventBundle);
		
		getFragmentManager()
			.beginTransaction()
			.replace(R.id.fragment_container, fragment)
			.commit();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.activity_add_event, menu);
		
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (item.getItemId() == R.id.cancel_task)
		{
			Intent intent = new Intent(this, MainActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			
			startActivity(intent);
		}
		else
			return super.onOptionsItemSelected(item);
		
		return true;
	}
	
	public void createNewEvent()
	{
		String userActivity = mEventBundle.getString("user_activity");
		int userFeeling = mEventBundle.getInt("user_feeling", -1);
		String userThought = mEventBundle.getString("user_thought");
		
		boolean isValid = (userActivity != null) && (userFeeling >= 0) && (userThought != null);
		
		if (isValid)
		{
			String userCategory = mEventBundle.getString("user_category");
			String thoughtTag = userCategory != null ? "Yes" : "No";
		
			Calendar calendar = Calendar.getInstance();
			
			int eventYear = calendar.get(Calendar.YEAR);
			int eventDayofYear = calendar.get(Calendar.DAY_OF_YEAR);
			int eventMinuteOfDay = (calendar.get(Calendar.HOUR_OF_DAY) * 60) + calendar.get(Calendar.MINUTE);
			
			CalendarDbAdapter calendarDbAdapter = new CalendarDbAdapter(this);
			calendarDbAdapter.open();
			
			calendarDbAdapter.createCalendar(eventYear, eventDayofYear, eventMinuteOfDay, userActivity, userFeeling, userThought, thoughtTag);
			
			if (userCategory != null)
				calendarDbAdapter.createType(userThought, userCategory);
						
			calendarDbAdapter.close();
			
			Toast.makeText(this, R.string.add_event_create_success, Toast.LENGTH_SHORT).show();
		}
		
		Intent intent = new Intent(this, MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		
		startActivity(intent);
	}
}
