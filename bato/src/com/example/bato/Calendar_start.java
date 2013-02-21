package com.example.bato;
import java.util.Calendar;

import android.app.Activity;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;


//just get all the strings from the db and set text for each. that was easy do do brain. 
//Day is by day of year!! easy!! 

public class Calendar_start extends ListFragment { //going to have to autoincrement for number of activities within an hour!!!
/** Called when the activity is first created. */
private static int HOURS_PER_DAY = 23;
private long mRowId;
private long Day;
private long within = Long.valueOf(0);
private int position;
private ImageView sad;
private EditText activity_write;
private ImageView happy; 
private SeekBar mood;
private EditText thought;
private LinearLayout eventsLL;
private ActivityDbAdapter mDbHelper;
private CalendarDbAdapter mCalendarDbHelper;
Activity mContext;


@Override
public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mContext = this.getActivity();
    Calendar cal=Calendar.getInstance();
    Day=cal.get(Calendar.DAY_OF_YEAR);

	
    setListAdapter(new ListAdapter(){

        @Override
        public boolean areAllItemsEnabled() {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public boolean isEnabled(int arg0) {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return HOURS_PER_DAY;
        }

        @Override
        public Object getItem(int arg0) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long getItemId(int arg0) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public int getItemViewType(int arg0) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public View getView(final int position, View arg1, ViewGroup arg2) {
            // TODO Auto-generated method stub
            LayoutInflater inflater = mContext.getLayoutInflater();
            final View listItem = (View) inflater.inflate(R.layout.activity_calendar_start, getListView(),false);
            TextView hourTV = (TextView) listItem.findViewById(R.id.hourTV);
            TextView amTV = (TextView) listItem.findViewById(R.id.amTV);
            hourTV.setTextColor(Color.BLUE);
            amTV.setTextColor(Color.BLUE);
            final LinearLayout eventsLL = (LinearLayout) listItem.findViewById(R.id.eventsLL);
            mCalendarDbHelper=new CalendarDbAdapter(mContext);
    	    mCalendarDbHelper.open();
    	    populateFields(listItem,position, eventsLL);
    	    
            if (position==0)
            {
            	hourTV.setText(String.valueOf(12));
            }
            else if (position>0 && position<11)
            {
            	hourTV.setText(String.valueOf(position));
            }
            else if (position==11)
            {
            	hourTV.setText(String.valueOf(12));
            	
            }
            else
            {
             hourTV.setText(String.valueOf(position-11));
            }            //I set am/pm for each entry ... you could specify which entries
            if(position<11)
                amTV.setText("am");
            else
                amTV.setText("pm");
            
            eventsLL.setOnClickListener(new OnClickListener(){

                @Override
                public void onClick(View arg0) 
                {
                			
                			// here I am creating TWO edit texts and a seek bar. and on text changed I am going to need 
                			// to update the db.
                			// so I need different db methods right? Yes! 
                			final Long Within = within++;
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
                			mood.setMinimumWidth(350);
                        	activity_write.setHint("enter an activity");
                        	activity_write.setHintTextColor(Color.parseColor("#FF4444"));
                        	activity_write.setTextColor(Color.parseColor("#1E90FF"));
                        	// this is how you set a max length for the edit text programmatically 
                        	InputFilter[] setLength = new InputFilter[1];
                        	setLength[0] = new InputFilter.LengthFilter(100);
                        	activity_write.setFilters(setLength);
                        	mood.setBackgroundColor(Color.parseColor("#FFFFFF"));
                        	thought.setMaxWidth(200);
                        	thought.setHint("enter a thought");
                        	thought.setHintTextColor(Color.parseColor("#FF4444"));
                        	thought.setTextColor(Color.parseColor("#1E90FF"));
                        	thought.setFilters(setLength);
                        	RelativeLayout.LayoutParams params_edit = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                        	activity_write.setId(200);
                        	activity_write.setMaxWidth(200);
                			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                			params.addRule(RelativeLayout.RIGHT_OF, activity_write.getId());
                			
                			// sad face 
                			RelativeLayout.LayoutParams params_sad = new RelativeLayout.LayoutParams(50, 35);
                			params_sad.addRule(RelativeLayout.BELOW, activity_write.getId());
        	    			params_sad.addRule(RelativeLayout.BELOW, thought.getId());

                			// seek bar
                			RelativeLayout.LayoutParams params_seekbar = new RelativeLayout.LayoutParams(350, RelativeLayout.LayoutParams.WRAP_CONTENT);
                			params_seekbar.setMargins(55, 0, 0, 0);
                			params_seekbar.addRule(RelativeLayout.BELOW, activity_write.getId());
        	    			params_seekbar.addRule(RelativeLayout.BELOW, thought.getId());
                			params_seekbar.addRule(RelativeLayout.RIGHT_OF, sad.getId());
                			// happy face 
                			RelativeLayout.LayoutParams params_happy = new RelativeLayout.LayoutParams(50, 35);
                			params_happy.addRule(RelativeLayout.BELOW, activity_write.getId());
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
                        	eventsLL.addView(holder);
                        	activity_write.addTextChangedListener(new TextWatcher() {

							@Override
							public void afterTextChanged(Editable arg0) 
							{
							save_state(activity_write, mood, thought, position, Within);	
							}

							@Override
							public void beforeTextChanged(CharSequence arg0,
									int arg1, int arg2, int arg3) {
								// TODO Auto-generated method stub
								
							}

							@Override
							public void onTextChanged(CharSequence arg0,
									int arg1, int arg2, int arg3) {
								// TODO Auto-generated method stub
								
							}
                        	});
                        mood.setOnSeekBarChangeListener( new OnSeekBarChangeListener()
                        {

							@Override
							public void onProgressChanged(SeekBar arg0,
									int mood_value, boolean arg2) {

								
							}

							@Override
							public void onStartTrackingTouch(SeekBar arg0) {
								// TODO Auto-generated method stub
								
							}

							@Override
							public void onStopTrackingTouch(SeekBar arg0) 
							
							{
								save_state(activity_write, mood, thought, position, Within);	
	
							}
                        	
                        });
                		
                        thought.addTextChangedListener(new TextWatcher() {
                
                        
                        	@Override
							public void afterTextChanged(Editable arg0) {

    							save_state(activity_write, mood, thought, position, Within);	

							}

							@Override
							public void beforeTextChanged(CharSequence arg0,
									int arg1, int arg2, int arg3) {
								// TODO Auto-generated method stub
								
							}

							@Override
							public void onTextChanged(CharSequence arg0,
									int arg1, int arg2, int arg3) {
								// TODO Auto-generated method stub
								
							}
                        	});
                        }; 
                		


            });
            return listItem;
        }

        @Override
        public int getViewTypeCount() {
            // TODO Auto-generated method stub
            return 1;
        }

        @Override
        public boolean hasStableIds() {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public boolean isEmpty() {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public void registerDataSetObserver(DataSetObserver arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void unregisterDataSetObserver(DataSetObserver arg0) {
            // TODO Auto-generated method stub

        }
        
        public void populateFields(View listItem,int position, LinearLayout eventsLL)
        {    	    
        	Cursor activity = mCalendarDbHelper.fetchWholeCalendar(Long.valueOf(Day),Long.valueOf(position));
				while (activity.moveToNext())
				{
				RelativeLayout entries = new RelativeLayout(mContext);
				EditText stored_activity = new EditText(mContext);
				EditText stored_thought = new EditText(mContext);
				SeekBar  stored_mood = new SeekBar(mContext);
				stored_mood.setBackgroundColor(Color.parseColor("#FFFFFF"));
				final ImageView stored_happy = new ImageView(mContext);
				final ImageView stored_sad = new ImageView(mContext);
				stored_happy.setImageResource(R.drawable.smiley);
				stored_sad.setImageResource(R.drawable.sad);
				stored_activity.setId(100);
				stored_thought.setId(200);
				stored_mood.setId(300);
				
            	RelativeLayout.LayoutParams params_activity = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
    			RelativeLayout.LayoutParams params_thought = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
    			params_thought.addRule(RelativeLayout.RIGHT_OF, stored_activity.getId());
    			
    			
				stored_activity.setTextColor(Color.parseColor("#1E90FF"));
				stored_activity.setText(activity.getString(activity.getColumnIndexOrThrow(CalendarDbAdapter.COLUMN_NAME_ACTIVITY)));
				stored_activity.setMaxWidth(200);
				
				stored_thought.setTextColor(Color.parseColor("#1E90FF"));
				stored_thought.setId(200);
				stored_thought.setText(activity.getString(activity.getColumnIndexOrThrow(CalendarDbAdapter.COLUMN_NAME_THOUGHT)));
				
				stored_thought.setMaxWidth(200);
				entries.addView(stored_activity, params_activity);
               	entries.addView(stored_thought, params_thought);

				
				//if there is a stored mood display the smiles
				if (activity.getString(activity.getColumnIndexOrThrow(CalendarDbAdapter.COLUMN_NAME_FEELING)).isEmpty() != true)
				{
	    			// sad face 
	    			RelativeLayout.LayoutParams params_sad = new RelativeLayout.LayoutParams(50, 35);
	    			params_sad.addRule(RelativeLayout.BELOW, stored_activity.getId());
	    			params_sad.addRule(RelativeLayout.BELOW, stored_thought.getId());

	    			// seek bar
	    			RelativeLayout.LayoutParams params_seekbar = new RelativeLayout.LayoutParams(350, RelativeLayout.LayoutParams.WRAP_CONTENT);
	    			params_seekbar.setMargins(55, 0, 0, 0);
	    			params_seekbar.addRule(RelativeLayout.BELOW, stored_activity.getId());
	    			params_seekbar.addRule(RelativeLayout.BELOW, stored_thought.getId());
	    			params_seekbar.addRule(RelativeLayout.RIGHT_OF, stored_sad.getId());
	    			
	    			// happy face 
	    			RelativeLayout.LayoutParams params_happy = new RelativeLayout.LayoutParams(50, 35);
	    			params_happy.addRule(RelativeLayout.BELOW, stored_activity.getId());
	    			params_happy.addRule(RelativeLayout.BELOW, stored_thought.getId());
	    			params_happy.addRule(RelativeLayout.RIGHT_OF, stored_mood.getId());
					
					stored_mood.setProgress(activity.getInt(activity.getColumnIndexOrThrow(CalendarDbAdapter.COLUMN_NAME_FEELING)));
					
	            	entries.addView(stored_happy, params_happy);
	            	entries.addView(stored_mood, params_seekbar);
	            	entries.addView(stored_sad, params_sad);
 
				}
				eventsLL.addView(entries);
				
			}
        }	
        

        
        private void save_state(EditText activity_write, SeekBar mood, EditText thought, int position, long Within)
        {
        	mCalendarDbHelper=new CalendarDbAdapter(mContext);
    	    mCalendarDbHelper.open();
    	    String activity_text = activity_write.getText().toString();
    	    String thought_text = thought.getText().toString();
    	    int mood_val = mood.getProgress();
    	    
    	    Cursor activity=mCalendarDbHelper.fetchCalendar(Long.valueOf(Day),Long.valueOf(position), Within);
    	    if (activity.moveToFirst())
    	    {
    	    Log.e("this is","an update");
    	    mCalendarDbHelper.updateCalendar(Long.valueOf(Day), Long.valueOf(position), Within, activity_text, mood_val, thought_text);	
    	    }
    	    else
    	    {
    	    Log.e("this is","a create");	
        	mCalendarDbHelper.createCalendar(Long.valueOf(Day), Long.valueOf(position), Within, activity_text, mood_val, thought_text);	
	
    	    }
        }
    });



}




}