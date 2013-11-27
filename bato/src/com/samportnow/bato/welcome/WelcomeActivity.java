package com.samportnow.bato.welcome;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;

import com.samportnow.bato.R;
import com.viewpagerindicator.UnderlinePageIndicator;

public class WelcomeActivity extends Activity
{
	private PagerAdapter mAdapter = null;
	private ViewPager mPager = null;
	
	private int[] mHeaderTextStringIds =
	{
		R.string.welcome_text_thank_you,
		R.string.welcome_text_personal_scientist,
		R.string.welcome_text_replace_negative,
		R.string.welcome_text_coping,
		R.string.welcome_text_activities_available
	};
	
	private int[] mContentTextStringIds =
	{
		R.string.welcome_text_aim_app,
		R.string.welcome_text_know_activities,
		R.string.welcome_text_know_negatives,
		R.string.welcome_text_know_coping,
		R.string.welcome_text_build_skills
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_welcome);
		
		mAdapter = new FragmentPagerAdapter(getFragmentManager())
		{			
			@Override
			public int getCount()
			{
				return 5;
			}
			
			@Override
			public Fragment getItem(int position)
			{
				WelcomeInfoFragment fragment = new WelcomeInfoFragment();
				
				fragment.setHeaderText(getString(mHeaderTextStringIds[position]));
				fragment.setContentText(getString(mContentTextStringIds[position]));
				
				return fragment;
			}
		};
		
		mPager = (ViewPager) findViewById(R.id.pager);
		mPager.setAdapter(mAdapter);
		
		UnderlinePageIndicator indicator = (UnderlinePageIndicator) findViewById(R.id.indicator);
		indicator.setViewPager(mPager);
		indicator.setFades(false);
		
		Button nextButton = (Button) findViewById(R.id.next);
		
		nextButton.setOnClickListener(new View.OnClickListener()
		{			
			public void onClick(View v)
			{
				if (mPager.getCurrentItem() < 5)
					mPager.setCurrentItem(mPager.getCurrentItem() + 1, true);
			}
		});
	}
}
