package com.example.bato;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
 

 
public class ScreenSlidePagerActivity extends FragmentActivity {
    private MyAdapter mAdapter;
    private ViewPager mPager;
 
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_slide);
        mAdapter = new MyAdapter(getSupportFragmentManager());
 
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);
    }
 
    public static class MyAdapter extends FragmentPagerAdapter {
        public MyAdapter(FragmentManager fm) {
            super(fm);
        }
 
        @Override
        public int getCount() {
            return 4;
        }
 
        @Override
       public Fragment getItem(int position) {
            switch (position) {
            case 0:
                return new ContactList();
            case 1:
                return new Calendar_start();
            case 2:
                return new ActivityList();
            case 3:
            	return new DestroyerView();
 
            default:
                return null;
            }
        }
    }
}