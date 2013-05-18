package com.example.bato;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnShowListener;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Toast;

public class AddEventFragment extends DialogFragment implements OnShowListener
{
	private AutoCompleteTextView activityEditText = null;
	private AutoCompleteTextView thoughtEditText = null;
	private Activity activity;
	private RadioGroup radioPosGroup;
	private AlertDialog alertDialog;
	boolean atMaxLength;
	boolean checked;
	int idCheck;
	String thought_tag;
	ArrayList<String> negative_thoughts = new ArrayList<String>();
	ArrayList<String> cal_activities = new ArrayList<String>();
	ArrayAdapter<String> adapter;
	ArrayAdapter<String> activity_adapter;

	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState)
	{ 
		activity = this.getActivity();
		CalendarDbAdapter calendarDbHelper = new CalendarDbAdapter(activity);
		calendarDbHelper.open();
		Cursor thoughts = calendarDbHelper.fetchThoughts();
	    while (thoughts.moveToNext())
    	{
    		if (thoughts.getString(thoughts.getColumnIndexOrThrow(CalendarDbAdapter.COLUMN_NAME_THOUGHT)).length() > 0 && thoughts.getString(thoughts.getColumnIndexOrThrow(CalendarDbAdapter.COLUMN_NAME_THOUGHT)).charAt(0) == '-')
    		{
    			String thought = thoughts.getString(thoughts.getColumnIndexOrThrow(CalendarDbAdapter.COLUMN_NAME_THOUGHT));
    			if (! negative_thoughts.contains(thought))
    			{
    				negative_thoughts.add(thought);
    			}

    		}
   
    	}
	    thoughts.close();
	    
	    Cursor activities = calendarDbHelper.fetchActivities();
	    while (activities.moveToNext())
	    {
			String event = activities.getString(activities.getColumnIndexOrThrow(CalendarDbAdapter.COLUMN_NAME_ACTIVITY));
			if (! cal_activities.contains(event))
			{
				cal_activities.add(event);
			}	
	    }
	    activities.close();
	    calendarDbHelper.close();
	    adapter = new ArrayAdapter<String> (activity, android.R.layout.simple_dropdown_item_1line, negative_thoughts);
	    activity_adapter = new ArrayAdapter<String> (activity, android.R.layout.simple_dropdown_item_1line, cal_activities);
		AlertDialog.Builder builder = new Builder(getActivity());
		Toast.makeText(getActivity(), "TAG YOUR THOUGHTS WITH A + FOR POSITIVE THOUGHTS, AND A - FOR NEGATIVE THOUGHTS", Toast.LENGTH_LONG).show();
		builder.setView(getActivity().getLayoutInflater().inflate(R.layout.fragment_add_event, null));
		builder.setTitle(R.string.add_event_fragment_title);		
		builder.setNegativeButton(android.R.string.cancel, null);
		
		builder.setPositiveButton(android.R.string.ok, new OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				createEvent();
			}			
		});			
		
		AlertDialog dialog = builder.create();
		dialog.setOnShowListener(this);
		
		return dialog;
	}
	
	@Override
	public void onShow(DialogInterface dialog)
	{
		alertDialog = (AlertDialog) dialog;
		alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(false);
		radioPosGroup = (RadioGroup) alertDialog.findViewById(R.id.pos_or_neg);
		activityEditText = ((AutoCompleteTextView) alertDialog.findViewById(R.id.add_event_user_activity));
		thoughtEditText = ((AutoCompleteTextView) alertDialog.findViewById(R.id.add_event_user_thought));
		thoughtEditText.setAdapter(adapter);
		activityEditText.setAdapter(activity_adapter);
		
		TextWatcher textWatcher = new TextWatcher()
		{

			@Override
			public void afterTextChanged(Editable s)
			{
				String activityText = activityEditText.getText().toString();
				String thoughtText = thoughtEditText.getText().toString();
				atMaxLength = ((activityText.length() >= 60) || (thoughtText.length() >= 60));
				
				if (atMaxLength == true)
		          Toast.makeText(getActivity(), "Limit is 60 characters!", Toast.LENGTH_SHORT).show();
				
				((AlertDialog) getDialog()).getButton(DialogInterface.BUTTON_POSITIVE)
				.setEnabled((activityEditText.getText().toString().length() > 0) && checked == true && (thoughtEditText.getText().toString().length() > 0) && (atMaxLength == false));		
											 
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after)
			{
				
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count)
			{
			
			}			
		};
		
		activityEditText.addTextChangedListener(textWatcher);
		thoughtEditText.addTextChangedListener(textWatcher);
		radioPosGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
		{

			@Override
			public void onCheckedChanged(RadioGroup r1, int checked_id) 
			{
				if (checked_id != -1)
				{
					checked = true;
					((AlertDialog) getDialog()).getButton(DialogInterface.BUTTON_POSITIVE)
					.setEnabled((activityEditText.getText().toString().length() > 0) && checked == true && (thoughtEditText.getText().toString().length() > 0) && (atMaxLength == false));	
					RadioButton radio_thought = (RadioButton) (alertDialog.findViewById(checked_id));
					thought_tag = radio_thought.getText().toString();
				}
				
			}
			
		});

	}
	
	public void createEvent()
	{	
		Calendar calendar = Calendar.getInstance();
		int eventYear = calendar.get(Calendar.YEAR);
		int eventDayofYear = calendar.get(Calendar.DAY_OF_YEAR);
		int eventMinuteOfDay = (calendar.get(Calendar.HOUR_OF_DAY) * 60) + calendar.get(Calendar.MINUTE);
		
		String activityText = activityEditText.getText().toString();
		String thoughtText = thoughtEditText.getText().toString();
		int moodValue = ((SeekBar) getDialog().findViewById(R.id.add_event_user_mood)).getProgress();	
		
		Context context = getActivity();
		
		CalendarDbAdapter calendarDbHelper = new CalendarDbAdapter(context);
		calendarDbHelper.open();
		
		Cursor cursor = calendarDbHelper.fetchCalendar(eventYear, eventDayofYear, eventMinuteOfDay);
		
		if (cursor.moveToFirst() == false)
		{
			Log.d("add_event_fragment", "Creating a new event: " + eventDayofYear + ", " + eventMinuteOfDay);
			
			calendarDbHelper.createCalendar(eventYear, eventDayofYear, eventMinuteOfDay, activityText, moodValue, thoughtText, thought_tag);
		}
		else
		{
			Log.d("add_event_fragment", "Updating an existing event: " + eventDayofYear + ", " + eventMinuteOfDay);
			
			calendarDbHelper.updateCalendar(eventYear, eventDayofYear, eventMinuteOfDay, activityText, moodValue, thoughtText, thought_tag);
		}
		
		Toast.makeText(context, R.string.add_event_toast_created_event, Toast.LENGTH_SHORT).show();
		
		calendarDbHelper.close();

		Intent service = new Intent(context, Post.class);		
		context.startService(service);		
		
		Intent back = new Intent(context, MainActivity.class);
		context.startService(back);
		
	}
}
