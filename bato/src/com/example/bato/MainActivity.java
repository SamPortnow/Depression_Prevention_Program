package com.example.bato;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_main);
        
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();			        
        fragmentTransaction.replace(R.id.fragment_container, new ActivityHome(), "activity_home_fragment");        
        fragmentTransaction.commit();
        
        WelcomeFragment welcomeFragment = new WelcomeFragment();
        welcomeFragment.setCancelable(false);
        welcomeFragment.show(getFragmentManager(), "welcome_fragment");        
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
    	if (item.getItemId() == R.id.menu_destroyer_game)
    	{
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.addToBackStack(null);            
            fragmentTransaction.replace(R.id.fragment_container, new DestroyerView());
            fragmentTransaction.commit();
    	}
    	
    	return true;
    }   
}
