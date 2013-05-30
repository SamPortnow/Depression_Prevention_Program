package com.example.bato;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.View;

public class AddThoughtPager extends Activity
{
    UserNameDbAdapter UserNameDbHelper;
    Context mContext;
    String thought;
    String event;
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
					return new AddEventActivity();
					
				case 1:
					return new AddFeelingActivity();
					
				case 2:
					return new AddThoughtActivity();
				
				case 3:
					return new TrainActivity();
				
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
        setContentView(R.layout.activity_add_event);
        Fragment addEventActivity = new AddEventActivity();
        Fragment addFeelingActivity = new AddFeelingActivity();
        Fragment addThoughtActivity = new AddThoughtActivity();
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
		Fragment trainActivity = new TrainActivity();
		frags.add(trainActivity);
		mFragmentTitles.add("Send the Thought Away");
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        
        return true;
    }
    
}
