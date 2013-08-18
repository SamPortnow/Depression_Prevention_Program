package com.samportnow.bato;

import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.samportnow.bato.capture.CaptureActivity;
import com.samportnow.bato.database.CalendarDbAdapter;
import com.samportnow.bato.database.ScaleDbAdapter;

public class ScalesSummaryFragment extends Fragment
{
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.fragment_scales_summary, container, false);
	}
	
	@Override
	public void onResume()
	{
		super.onResume();
		
		View view = getView();		
		
		if (isUnlocked() == true)
		{
			view.findViewById(R.id.scales_summary_container).setVisibility(View.VISIBLE);
			view.findViewById(R.id.scales_summary_locked_container).setVisibility(View.GONE);
			
			ImageView badgeImageView = (ImageView) view.findViewById(R.id.scales_summary_badge);
					
			badgeImageView.setOnClickListener(new OnClickListener()
			{			
				@Override
				public void onClick(View view)
				{
					Intent intent = new Intent(getActivity(), CaptureActivity.class);
					startActivity(intent);
				}
			});
			
			int count = getPositiveThoughtsCount();
			((TextView) view.findViewById(R.id.scales_summary_count)).setText(String.valueOf(count));
		}
		else
		{
			view.findViewById(R.id.scales_summary_locked_container).setVisibility(View.VISIBLE);
			view.findViewById(R.id.scales_summary_container).setVisibility(View.GONE);
		}
	}
	
	private boolean isUnlocked()
	{
		CalendarDbAdapter adapter = new CalendarDbAdapter(getActivity()).open();
		Cursor cursor = adapter.fetchNegs();
		
		boolean flag = false;

		if (cursor.moveToFirst())
			flag = true;	
		
		cursor.close();
		adapter.close();
		
		// TODO: remove me
		flag = true;

		return flag;
	}
	
	private int getPositiveThoughtsCount()
	{
		ScaleDbAdapter adapter = new ScaleDbAdapter(getActivity()).open();
		Cursor cursor = adapter.fetchPositives();
		
		int count = cursor.getCount();
		
		cursor.close();
		adapter.close();
		
		return count;
	}
}
