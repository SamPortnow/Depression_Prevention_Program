package com.samportnow.bato;

import java.util.List;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.samportnow.bato.capture.CaptureSelectActivity;
import com.samportnow.bato.database.BatoDataSource;
import com.samportnow.bato.database.dao.ChallengingThoughtDao;

public class CaptureSummaryFragment extends Fragment
{
	private static final int MINIMUM_NEGATIVE_THOUGHTS_TO_UNLOCK = 4;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.fragment_capture_summary, container, false);
	}

	@Override
	public void onResume()
	{
		super.onResume();

		View view = getView();

		if (isUnlocked() == true)
		{
			view.findViewById(R.id.capture_summary_container).setVisibility(View.VISIBLE);
			view.findViewById(R.id.capture_summary_locked_container).setVisibility(View.GONE);

			ImageView badgeImageView = (ImageView) view.findViewById(R.id.capture_summary_badge);

			badgeImageView.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View view)
				{
					Intent intent = new Intent(getActivity(), CaptureSelectActivity.class);
					startActivity(intent);
				}
			});

			int count = getPositiveThoughtsCount();
			((TextView) view.findViewById(R.id.capture_summary_count)).setText(String.valueOf(count));
		}
		else
		{
			view.findViewById(R.id.capture_summary_locked_container).setVisibility(View.VISIBLE);
			view.findViewById(R.id.capture_summary_container).setVisibility(View.GONE);
		}
	}

	private boolean isUnlocked()
	{
		BatoDataSource dataSource = new BatoDataSource(getActivity()).open();
		boolean flag = dataSource.getAllNegativeThoughts().size() >= MINIMUM_NEGATIVE_THOUGHTS_TO_UNLOCK;

		dataSource.close();

		return flag;
	}

	private int getPositiveThoughtsCount()
	{
		BatoDataSource dataSource = new BatoDataSource(getActivity()).open();
		List<ChallengingThoughtDao> thoughts = dataSource.getAllChallengingThoughts();
		return thoughts.size();
	}
}
