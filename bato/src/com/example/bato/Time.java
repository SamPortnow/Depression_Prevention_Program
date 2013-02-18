package com.example.bato;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TimePicker; 

// need to put an if statement ensuring they don't put in an alarm BEFORE the current time

public class Time extends Activity {
	
	private TimePicker timepicker;
	private TimePicker timepicker_evening;
	private int hour;
	private int minute;
	static final int TIME_DIALOG_ID=1;
	static final int TIME_DIALOG_ID2=2;
	private ActivityDbAdapter mDbHelper;
	private long mRowId;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mDbHelper=new ActivityDbAdapter(this);
		mDbHelper.open();
		setContentView(R.layout.activity_time);
		timepicker=(TimePicker) findViewById(R.id.timePicker);
		timepicker_evening=(TimePicker) findViewById(R.id.timePicker_evening);
		timepicker.setClickable(true);
		timepicker_evening.setClickable(true);
		Button confirmButton=(Button) findViewById(R.id.continue_on);
		setCurrentTimeOnView();
		setCurrentTimeOnViewEvening();
		timepicker.setOnClickListener(new OnClickListener() //morning set up
		{
			@Override
			public void onClick(View view)
			{
				showDialog(TIME_DIALOG_ID);
			}
		});
		
		timepicker_evening.setOnClickListener(new OnClickListener() //evening set up
		{
			@Override
			public void onClick(View view)
			{
				showDialog(TIME_DIALOG_ID2);
			}
		});
		
		confirmButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
            	setResult(RESULT_OK);
            	to_contacts(view);
            	
            }

        });
	}
	
	
	public void setCurrentTimeOnView() //set the morning current time
	{
		timepicker=(TimePicker) findViewById(R.id.timePicker);
		mRowId=Long.valueOf(1);
		Cursor activity = mDbHelper.fetchTimeMorning(mRowId);
		if (activity.moveToFirst())
		{
			startManagingCursor(activity);
			hour=activity.getInt(activity.getColumnIndexOrThrow(ActivityDbAdapter.COLUMN_NAME_MORNING_HOUR));
			minute=activity.getInt(activity.getColumnIndexOrThrow(ActivityDbAdapter.COLUMN_NAME_MORNING_MINUTE));
			timepicker.setCurrentHour(hour);
			timepicker.setCurrentMinute(minute);
			
		}			
		else
		{
			final Calendar c = Calendar.getInstance();
			hour=c.get(Calendar.HOUR_OF_DAY);
			minute=c.get(Calendar.MINUTE);
			timepicker.setCurrentHour(hour);
			timepicker.setCurrentMinute(minute);
		}
	}
	
	public void setCurrentTimeOnViewEvening()
	{
		timepicker_evening=(TimePicker) findViewById(R.id.timePicker_evening);
		mRowId=Long.valueOf(1);
		Cursor activity = mDbHelper.fetchTimeEvening(mRowId);
		if (activity.moveToFirst())
		{
			startManagingCursor(activity);
			hour=activity.getInt(activity.getColumnIndexOrThrow(ActivityDbAdapter.COLUMN_NAME_EVENING_HOUR));
			minute=activity.getInt(activity.getColumnIndexOrThrow(ActivityDbAdapter.COLUMN_NAME_EVENING_MINUTE));
			timepicker_evening.setCurrentHour(hour);
			timepicker_evening.setCurrentMinute(minute);	
		}			
		else
		{
			final Calendar c = Calendar.getInstance();
			hour=c.get(Calendar.HOUR_OF_DAY);
			minute=c.get(Calendar.MINUTE);
			timepicker_evening.setCurrentHour(hour);
			timepicker_evening.setCurrentMinute(minute);
		}
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case TIME_DIALOG_ID: //one for each dialog
			// set time picker as current time
			return new TimePickerDialog(this, 
                                        timePickerListener_morning, hour, minute,false);
		case TIME_DIALOG_ID2:
				return new TimePickerDialog(this,
						timePickerListener_evening,hour,minute,false);
 
		}
		return null;
	}
/// had to create two separate listeners!! whoof!! lulz
	
	private TimePickerDialog.OnTimeSetListener timePickerListener_morning = 
            new TimePickerDialog.OnTimeSetListener() {
		public void onTimeSet(TimePicker view, int selectedHour,
				int selectedMinute) {
			hour = selectedHour;
			minute = selectedMinute;
			// set selected time into timepicker
			timepicker.setCurrentHour(hour);
			timepicker.setCurrentMinute(minute);
	    	mRowId=Long.valueOf(1);
			Cursor activity = mDbHelper.fetchTimeMorning(mRowId);
			if (activity.moveToFirst())
			{
	       
			mDbHelper.updateTimeMorning(mRowId, minute, hour);        	
	        } 
			else
			{
				mDbHelper.createMorningTime(mRowId, minute, hour);
			}
			to_reminder(view);
 
		}
	};
	
	private TimePickerDialog.OnTimeSetListener timePickerListener_evening = 
            new TimePickerDialog.OnTimeSetListener() {
		public void onTimeSet(TimePicker view, int selectedHour,
				int selectedMinute) {
			hour = selectedHour;
			minute = selectedMinute;
			// set selected time into timepicker
			timepicker_evening.setCurrentHour(hour);
			timepicker_evening.setCurrentMinute(minute);
	    	mRowId=Long.valueOf(1);
			Cursor activity = mDbHelper.fetchTimeEvening(mRowId);
			if (activity.moveToFirst())
			{
	        mDbHelper.updateTimeEvening(mRowId, minute, hour); 
	        Log.e("Am I","updating");
	        } 
			else
			{
		        Log.e("Am I","creating");
				mDbHelper.createEveningTime(mRowId, minute, hour);
			}
			to_reminder_evening(view);
		}
	};
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_time, menu);
		return true;
	}
	
	public void to_reminder(View view)
	{
		Intent intent=new Intent(this,Notification_morning.class);
		AlarmManager manager=(AlarmManager)getSystemService(Activity.ALARM_SERVICE);
		PendingIntent pendingIntent=PendingIntent.getService(this,
				0,intent, 0);
		Cursor activity = mDbHelper.fetchTimeMorning(mRowId);
		Calendar cal=Calendar.getInstance();
		if (activity.moveToFirst())
		{
		Log.e("Am I","Setting");
		hour=(int)(activity.getInt(activity.getColumnIndexOrThrow(ActivityDbAdapter.COLUMN_NAME_MORNING_HOUR)));
		minute=(int)(activity.getInt(activity.getColumnIndexOrThrow(ActivityDbAdapter.COLUMN_NAME_MORNING_MINUTE)));
		Log.e("hour",""+hour);
		Log.e("minute",""+minute);
		cal.set(Calendar.HOUR_OF_DAY, hour);
		cal.set(Calendar.MINUTE, minute);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		manager.setRepeating(AlarmManager.RTC_WAKEUP,cal.getTimeInMillis(),24*60*60*1000,pendingIntent);
		}
	}
	
	public void to_reminder_evening(View view)
	{
		Intent intent=new Intent(this,Notification_evening.class);
		AlarmManager manager=(AlarmManager)getSystemService(Activity.ALARM_SERVICE);
		PendingIntent pendingIntent=PendingIntent.getService(this,
				0,intent, 0);
		Cursor activity = mDbHelper.fetchTimeEvening(mRowId);
		Calendar cal=Calendar.getInstance();
		if (activity.moveToFirst())
		{
		hour=(int)(activity.getInt(activity.getColumnIndexOrThrow(ActivityDbAdapter.COLUMN_NAME_EVENING_HOUR)));
		minute=(int)(activity.getInt(activity.getColumnIndexOrThrow(ActivityDbAdapter.COLUMN_NAME_EVENING_MINUTE)));	
		cal.set(Calendar.HOUR_OF_DAY, hour);
		cal.set(Calendar.MINUTE,minute);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		manager.setRepeating(AlarmManager.RTC_WAKEUP,cal.getTimeInMillis(),24*60*60*1000,pendingIntent);
		}
	}
	
	
	public void to_contacts(View view)
	{
		Intent intent=new Intent(this,Contacts.class);
		startActivity(intent);
	}

}
