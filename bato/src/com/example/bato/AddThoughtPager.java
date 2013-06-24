package com.example.bato;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;

public class AddThoughtPager extends Activity
{
    UserNameDbAdapter UserNameDbHelper;
    Context mContext;
    String thought;
    String event;
    String tag;
    String mType;
    int feeling; 
    
    private ArrayList<String> mFragmentTitles = new ArrayList<String>();    
    private ArrayList<Fragment> frags = new ArrayList<Fragment>();
    
    class MainFragmentPagerAdapter extends FragmentPagerAdapter
    {
		
    	public MainFragmentPagerAdapter(FragmentManager fm)
		{
			super(fm);
		}

		@Override
		public Fragment getItem(int position)
		{
			switch (position)
			{					
				case 0:
					return new AddEventUserActivityFragment();
					
				case 1:
					return new AddFeelingFragment();
					
				case 2:
					return new AddThoughtFragment();
				
				case 3:
					return new TrainFragment();
				
			}
			
			return null;
		}

		@Override
		public int getCount()
		{
			return frags.size();
		}    	
		
		@Override
		public CharSequence getPageTitle(int position)
		{
			if (position < 0 || position >= mFragmentTitles.size())
				return "";
			
			return mFragmentTitles.get(position);
		}
    }
    
	@Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_add_thought_pager);
        Fragment addEventActivity = new AddEventUserActivityFragment();
        Fragment addFeelingActivity = new AddFeelingFragment();
        Fragment addThoughtActivity = new AddThoughtFragment();
        frags.add(addEventActivity);
        frags.add(addFeelingActivity);
        frags.add(addThoughtActivity);
        mFragmentTitles.add("What are you doing?");
        mFragmentTitles.add("How are you feeling?");
        mFragmentTitles.add("What are you thinking?");
        MainFragmentPagerAdapter pagerAdapter = new MainFragmentPagerAdapter(getFragmentManager());
        ViewPager viewPager = (ViewPager) findViewById(R.id.fragment_view_pager);        
        viewPager.setAdapter(pagerAdapter);
    }
	
	public void addView()
	{
		Fragment trainActivity = new TrainFragment();
		frags.add(trainActivity);
		mFragmentTitles.add("Send the Thought Away");
	} 
	
	public void createCalendar()
	{
		CalendarDbAdapter mCalHelper = new CalendarDbAdapter(mContext);
		mCalHelper.open();
		Calendar calendar = Calendar.getInstance();
		int eventYear = calendar.get(Calendar.YEAR);
		int eventDayofYear = calendar.get(Calendar.DAY_OF_YEAR);
		int eventMinuteOfDay = (calendar.get(Calendar.HOUR_OF_DAY) * 60) + calendar.get(Calendar.MINUTE);
		mCalHelper.createCalendar(eventYear, eventDayofYear, eventMinuteOfDay, event, feeling, thought, tag);
		mCalHelper.close();
	}

	public void createTypetable()
	{
		CalendarDbAdapter mCalHelper = new CalendarDbAdapter(mContext);
		mCalHelper.open();
		Log.e("thought is", "" + thought);
		Log.e("type is", "" + mType);
		mCalHelper.createType(thought, mType);
		mCalHelper.close();
	}
}
