package com.example.bato;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class PointsAdapter extends CursorAdapter
{

	CalendarDbAdapter calendarDbHelper;
	LayoutInflater inflater;
	int Year;
	int Day;
	Calendar cal;
	Date date;
	String sDate;
	int mood;
	String activity;
	CalendarDbAdapter mCalHelper;
	Cursor act;

	public PointsAdapter(Context context, Cursor cursor)
	{
		super(context, cursor);
		inflater = LayoutInflater.from(context);

	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent)
	{
		LayoutInflater inflater = LayoutInflater.from(parent.getContext());
		View retView = inflater.inflate(R.layout.scientist, parent, false);
		return retView;
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor)
	{
		Year = cursor.getInt(cursor.getColumnIndexOrThrow(CalendarDbAdapter.COLUMN_NAME_YEAR));
		Day = cursor.getInt(cursor.getColumnIndexOrThrow(CalendarDbAdapter.COLUMN_NAME_DAY));
		cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, Year);
		cal.set(Calendar.DAY_OF_YEAR, Day);
		date = cal.getTime();
		sDate = new SimpleDateFormat("MM/dd/yyyy", Locale.US).format(date);
		mood = cursor.getInt(cursor.getColumnIndexOrThrow(CalendarDbAdapter.COLUMN_NAME_FEELING));
		activity = cursor.getString(cursor.getColumnIndexOrThrow(CalendarDbAdapter.COLUMN_NAME_ACTIVITY));
		mCalHelper = new CalendarDbAdapter(context);
		mCalHelper.open();
		TextView date = (TextView) view.findViewById(R.id.date);
		TextView point = (TextView) view.findViewById(R.id.points);
		TextView action = (TextView) view.findViewById(R.id.act);
		TextView rest = (TextView) view.findViewById(R.id.rest);
		act = mCalHelper.fetchActivity(activity);
		if (mood > 4 && act.getCount() > 1)
		{
			point.setText("50");
			action.setText(activity);
			rest.setText("");
		}

		else
		{
			point.setText("25");
			action.setText("");
			rest.setText("added an event");
		}

		date.setText(sDate);

	}

}
