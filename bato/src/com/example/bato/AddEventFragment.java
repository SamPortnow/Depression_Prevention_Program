package com.example.bato;

import java.util.Calendar;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnShowListener;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;

public class AddEventFragment extends DialogFragment implements OnShowListener
{
	private EditText activityEditText = null;
	private EditText thoughtEditText = null;
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState)
	{ 
		AlertDialog.Builder builder = new Builder(getActivity());
		
		builder.setView(getActivity().getLayoutInflater().inflate(R.layout.fragment_add_event, null));
		builder.setTitle(R.string.add_event_fragment_title);
		
		builder.setNegativeButton(android.R.string.cancel, new OnClickListener()
		{		
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();
			}
		});
		
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
		AlertDialog alertDialog = (AlertDialog) dialog;
		alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(false);
		
		activityEditText = ((EditText) alertDialog.findViewById(R.id.add_event_user_activity));
		thoughtEditText = ((EditText) alertDialog.findViewById(R.id.add_event_user_thought));
		
		TextWatcher textWatcher = new TextWatcher()
		{

			@Override
			public void afterTextChanged(Editable s)
			{
				String activityText = activityEditText.getText().toString();
				String thoughtText = thoughtEditText.getText().toString();
				
				((AlertDialog) getDialog()).getButton(DialogInterface.BUTTON_POSITIVE)
					.setEnabled(activityText.length() > 0 && thoughtText.length() > 0);					
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
	}
	
	public void createEvent()
	{	
		Calendar calendar = Calendar.getInstance();
		
		int eventDayofYear = calendar.get(Calendar.DAY_OF_YEAR);
		int eventMinuteOfDay = (calendar.get(Calendar.HOUR_OF_DAY) * 60) + calendar.get(Calendar.MINUTE);
		
		String activityText = activityEditText.getText().toString();
		String thoughtText = thoughtEditText.getText().toString();
		int moodValue = ((SeekBar) getDialog().findViewById(R.id.add_event_user_mood)).getProgress();	
		
		CalendarDbAdapter calendarDbHelper = new CalendarDbAdapter(getActivity());
		calendarDbHelper.open();
		
		Cursor cursor = calendarDbHelper.fetchCalendar(eventDayofYear, eventMinuteOfDay);
		
		if (cursor.moveToFirst() == false)
		{
			Log.d("add_event_fragment", "Creating a new event: " + eventDayofYear + ", " + eventMinuteOfDay);
			
			calendarDbHelper.createCalendar(eventDayofYear, eventMinuteOfDay, activityText, moodValue, thoughtText);
		}
		else
		{
			Log.d("add_event_fragment", "Updating an existing event: " + eventDayofYear + ", " + eventMinuteOfDay);
			
			calendarDbHelper.updateCalendar(eventDayofYear, eventMinuteOfDay, activityText, moodValue, thoughtText);
		}
		
		Toast.makeText(getActivity(), R.string.add_event_toast_created_event, Toast.LENGTH_SHORT).show();
	}
}
