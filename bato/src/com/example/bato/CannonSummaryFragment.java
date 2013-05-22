package com.example.bato;

import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class CannonSummaryFragment extends Fragment
{
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.fragment_cannon_summary, container, false);
	}
	
	@Override
	public void onResume()
	{
		super.onResume();
		
		View view = getView();		
		
		if (isUnlocked() == true)
		{
			view.findViewById(R.id.cannon_summary_container).setVisibility(View.VISIBLE);
			view.findViewById(R.id.cannon_summary_locked_container).setVisibility(View.GONE);
			
			ImageView badgeImageView = (ImageView) view.findViewById(R.id.cannon_summary_badge);
			
			badgeImageView.setImageResource(R.drawable.ic_cannon);			
			badgeImageView.setOnClickListener(new OnClickListener()
			{			
				@Override
				public void onClick(View v)
				{
					Intent intent = new Intent(getActivity(), DestroyerShooterView.class);
					startActivity(intent);
				}
			});
		}
		else
		{
			view.findViewById(R.id.cannon_summary_container).setVisibility(View.GONE);
			view.findViewById(R.id.cannon_summary_locked_container).setVisibility(View.VISIBLE);
		}
	}
	
	private boolean isUnlocked()
	{
		ScaleDbAdapter adapter = new ScaleDbAdapter(getActivity()).open();
		Cursor cursor = adapter.fetchPositives();

		int count = cursor.getCount();

		cursor.close();
		adapter.close();

		return (count > 8);
	}
}
