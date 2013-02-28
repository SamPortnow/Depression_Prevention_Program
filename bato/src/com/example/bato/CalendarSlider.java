package com.example.bato;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

public class CalendarSlider extends FragmentActivity 
{
	 private static final int page_left = 0;
	 private static final int page_middle = 1;
	 private static final int page_right = 2;
	 private CalendarPagerAdapter mCalendarPagerAdapter;
	 private ViewPager mPager;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_slider);
        mCalendarPagerAdapter = new CalendarPagerAdapter();
        mPager = (ViewPager) findViewById(R.id.calendar_pager);
        mPager.setAdapter(mCalendarPagerAdapter);
        mPager.setCurrentItem(page_middle);
        mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				
			}
        	
        });
    }	


public static class CalendarPagerAdapter extends PagerAdapter
{

	@Override
	public int getCount() {
		return 0;
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return false;
	}

}
}

