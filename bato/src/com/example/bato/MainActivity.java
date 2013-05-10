package com.example.bato;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity
{
    UserNameDbAdapter UserNameDbHelper;
    
    private String[] mFragmentTitles = { "Personal Scientist Points", "Scale Game", "Destroyer Game" };
    
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
					return new ScaleStatsFragment();
					
				case 2:
					return new DestroyerStatsFragment();
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
        
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) 
        {
//        	FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();			        
//        	fragmentTransaction.replace(R.id.fragment_container, new ActivityHome(), "activity_home_fragment");
//        	fragmentTransaction.commit();            

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
    		AddEventFragment fragment = new AddEventFragment();
    		fragment.show(getFragmentManager(), "add_event_fragment");
    	}
    	
    	if (item.getItemId() == R.id.menu_daily_mood)
    	{
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.addToBackStack(null);            
            fragmentTransaction.replace(R.id.fragment_container, new Graph());
            fragmentTransaction.commit();
    	}
    	
    	return true;
    }   
}
