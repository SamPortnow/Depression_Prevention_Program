package com.example.bato;

import java.util.Calendar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

public class ActivityHome extends Fragment
{
private Context mContext;
private ActivityDbAdapter mDbHelper;
private CalendarDbAdapter mCalendarDbHelper;
private View view;
private Button add_event;
private Calendar cal;

	
	@Override 
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	        Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);	
		mContext=this.getActivity();
		mDbHelper=new ActivityDbAdapter(mContext);
		mDbHelper.open();
		view = inflater.inflate(R.layout.activity_home, container, false);
		add_event = (Button) view.findViewById(R.id.add_event);
		add_event.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0) 
			{
    			cal = Calendar.getInstance();
    			final int Day = cal.get(Calendar.DAY_OF_YEAR);
    			int Hour = cal.get(Calendar.HOUR_OF_DAY);
    			int Minute = cal.get(Calendar.MINUTE);
    			int hour_minutes = Hour * 60;
    			final int minutes = hour_minutes + Minute;

				AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
    			final RelativeLayout holder = new RelativeLayout(mContext);
    			final EditText activity_write = new EditText(mContext);
    			final SeekBar mood = new SeekBar(mContext);
    			final EditText thought = new EditText(mContext);
    			final ImageView happy = new ImageView(mContext);
    			final ImageView sad = new ImageView(mContext);
    			happy.setImageResource(R.drawable.smiley);
    			sad.setImageResource(R.drawable.sad);
    			happy.setId(250);
    			mood.setId(350);
    			thought.setId(400);
            	activity_write.setHint("what are you doing?");
            	activity_write.setTextColor(Color.parseColor("#1E90FF"));
            	// this is how you set a max length for the edit text programmatically 
            	InputFilter[] setLength = new InputFilter[1];
            	setLength[0] = new InputFilter.LengthFilter(100);
            	activity_write.setFilters(setLength);
            	mood.setBackgroundColor(Color.parseColor("#FFFFFF"));
            	thought.setHint("what are you thinking?");
            	thought.setTextColor(Color.parseColor("#1E90FF"));
            	thought.setFilters(setLength);
            	RelativeLayout.LayoutParams params_edit = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            	activity_write.setId(200);
    			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
    			params.addRule(RelativeLayout.BELOW, activity_write.getId());
    			
    			// sad face 
    			RelativeLayout.LayoutParams params_sad = new RelativeLayout.LayoutParams(50, 35);
    			params_sad.addRule(RelativeLayout.BELOW, thought.getId());

    			// seek bar
    			RelativeLayout.LayoutParams params_seekbar = new RelativeLayout.LayoutParams(view.getWidth()-view.getWidth()/3, RelativeLayout.LayoutParams.WRAP_CONTENT);
    			params_seekbar.setMargins(55, 0, 0, 0);
    			params_seekbar.addRule(RelativeLayout.BELOW, thought.getId());
    			params_seekbar.addRule(RelativeLayout.RIGHT_OF, sad.getId());
    			// happy face 
    			RelativeLayout.LayoutParams params_happy = new RelativeLayout.LayoutParams(50, 35);
    			params_happy.addRule(RelativeLayout.BELOW, thought.getId());
    			params_happy.addRule(RelativeLayout.RIGHT_OF, mood.getId());
   
            	holder.addView(activity_write, params_edit);
            	holder.addView(thought, params);
            	holder.addView(happy, params_happy);
            	holder.addView(mood, params_seekbar);
            	holder.addView(sad, params_sad);
    			//make holder a relative layout. right??? 
    			//TODO make holder a relative layout
    			//params.addRule(RelativeLayout.RIGHT_OF, activity_write.getId());
            	alert.setView(holder);
                alert.setPositiveButton("Add", new DialogInterface.OnClickListener() { 
                    public void onClick(DialogInterface dialog, int whichButton) {
                    	save_state(activity_write, mood, thought, Day, minutes);
                    } 
                }); 

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() { 
                    public void onClick(DialogInterface dialog, int whichButton) {
                    } 
                }); 
            	alert.show();	
			
			}
			
		}
				);
		return view;

}

    private void save_state(EditText activity_write, SeekBar mood, EditText thought, int Day, int minutes)
    {
    	mCalendarDbHelper=new CalendarDbAdapter(mContext);
	    mCalendarDbHelper.open();
	    String activity_text = activity_write.getText().toString();
	    String thought_text = thought.getText().toString();
	    int mood_val = mood.getProgress();
	    
	    Cursor activity=mCalendarDbHelper.fetchCalendar(Long.valueOf(Day),Long.valueOf(minutes));
	    if (activity.moveToFirst())
	    {
	    Log.e("this is","an update");
	    mCalendarDbHelper.updateCalendar(Long.valueOf(Day), Long.valueOf(minutes), activity_text, mood_val, thought_text);	
	    }
	    else
	    {
	    Log.e("this is","a create");	
    	mCalendarDbHelper.createCalendar(Long.valueOf(Day), Long.valueOf(minutes), activity_text, mood_val, thought_text);	

	    }
    }

}