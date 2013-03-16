package com.example.bato;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity
{
    UserNameDbAdapter UserNameDbHelper;
	
	@Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
		UserNameDbHelper = new UserNameDbAdapter(this);
		UserNameDbHelper.open();
		Cursor username = UserNameDbHelper.fetchUserName();
        
        setContentView(R.layout.activity_main);

        if(savedInstanceState == null) 
        {
        	FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();			        
        	fragmentTransaction.replace(R.id.fragment_container, new ActivityHome(), "activity_home_fragment");
        	fragmentTransaction.commit();
            

			SharedPreferences prefs = this.getSharedPreferences(
				      "com.example.app", Context.MODE_PRIVATE);
			if (prefs.getString("username", null) == null)
			{
        	WelcomeFragment welcomeFragment = new WelcomeFragment();
        	welcomeFragment.setCancelable(false);
        	welcomeFragment.show(getFragmentManager(), "welcome_fragment");
			}
    		
        }
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
