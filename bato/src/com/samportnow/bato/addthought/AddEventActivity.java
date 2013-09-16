package com.samportnow.bato.addthought;

import java.util.Calendar;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.samportnow.bato.BatoConstants;
import com.samportnow.bato.MainActivity;
import com.samportnow.bato.R;
import com.samportnow.bato.database.BatoDataSource;

public class AddEventActivity extends Activity
{
	private Bundle mEventBundle = new Bundle();

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_add_event);

		Fragment fragment = new AddEventUserActivityFragment();
		fragment.setArguments(mEventBundle);

		getFragmentManager()
			.beginTransaction()
			.replace(R.id.fragment_container, fragment)
			.commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.activity_add_event, menu);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (item.getItemId() == R.id.cancel_task)
		{
			Intent intent = new Intent(this, MainActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

			startActivity(intent);
		}
		else
			return super.onOptionsItemSelected(item);

		return true;
	}

	public void createNewEvent()
	{

		String activity = mEventBundle.getString("user_activity");
		int feeling = mEventBundle.getInt("user_feeling", -1);
		String thought = mEventBundle.getString("user_thought");
		String coping = mEventBundle.getString("user_coping");

		boolean isValid = (activity != null) && (feeling >= 0) && (thought != null);

		if (isValid)
		{
			long created = Calendar.getInstance().getTimeInMillis();
			int negativeType = mEventBundle.getInt("negative_type", -1);

			BatoDataSource dataSource = new BatoDataSource(this).open();
			
			long thoughtId = dataSource.createThought(created, activity, feeling, thought, negativeType);
			dataSource.insertPointRecord(created, BatoConstants.POINT_TYPE_NEW_THOUGHT, 25);
			if (coping != null)
			{
				dataSource.createCoping(coping, thoughtId);
			}
			if (feeling >= 5)
			{
				if (dataSource.getRelatedThoughtsByActivity(activity).size() > 0)
					dataSource.insertPointRecord(created, BatoConstants.POINT_TYPE_POSITIVE_THOUGHT_BONUS, 50);
			}
			
			dataSource.close();

			Toast.makeText(this, R.string.add_event_create_success, Toast.LENGTH_SHORT).show();
		}

		Intent intent = new Intent(this, MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		
		startActivity(intent);
	}
}
