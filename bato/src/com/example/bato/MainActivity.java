package com.example.bato;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity
{
    UserNameDbAdapter UserNameDbHelper;
    Context mContext;
    
    private String[] mFragmentTitles = { "Personal Scientist Points", "Tip the Scales", "Command the Cannon" };
    
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
					return new PointsSummaryFragment();
					
				case 1:
					return new ScalesSummaryFragment();
					
				case 2:
					return new CannonSummaryFragment();
			}
			
			return null;
		}

		@Override
		public int getCount()
		{
			return 3;
		}    	
		
		@Override
		public CharSequence getPageTitle(int position)
		{
			if (position < 0 || position >= mFragmentTitles.length)
				return "";
			
			return mFragmentTitles[position];
		}
    }
    
	@Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) 
        {
			SharedPreferences preferences = this.getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
			String username = preferences.getString("username", null);
			
			if ((username == null) || (username.length() == 0))
			{
				WelcomeFragment welcomeFragment = new WelcomeFragment();
				welcomeFragment.setCancelable(false);
				welcomeFragment.show(getFragmentManager(), "welcome_fragment");
			}    		
        }
        
        MainFragmentPagerAdapter pagerAdapter = new MainFragmentPagerAdapter(getFragmentManager());
        
        ViewPager viewPager = (ViewPager) findViewById(R.id.fragment_view_pager);        
        viewPager.setAdapter(pagerAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
    	if (item.getItemId() == R.id.menu_add_event)
    	{
			Intent i = new Intent(mContext, AddThoughtPager.class);				
			mContext.startActivity(i);	
    	}
    	
    	if (item.getItemId() == R.id.menu_daily_mood)
    	{
    		Intent intent = new Intent(this, MoodGraphActivity.class);
    		startActivity(intent);
    	}
    	
    	return true;
    }   
}
