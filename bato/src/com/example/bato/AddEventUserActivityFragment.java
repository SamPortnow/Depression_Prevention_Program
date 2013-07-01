package com.example.bato;

import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class AddEventUserActivityFragment extends Fragment
{		
	private ArrayAdapter<String> mHistoryAdapter = null;
	
	private EditText mActivityEditText = null;
	private ListView mHistoryListView = null;	
	private Button mNextButton = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		View view = inflater.inflate(R.layout.fragment_add_event_user_activity, null);

		CalendarDbAdapter calendarDbAdapter = new CalendarDbAdapter(getActivity()).open();
		Cursor cursor = calendarDbAdapter.fetchActivities();
		int index = cursor.getColumnIndexOrThrow(CalendarDbAdapter.COLUMN_NAME_ACTIVITY);
		
		String[] activities = new String[cursor.getCount()];
		
		while (cursor.moveToNext() == true)
			activities[cursor.getPosition()] = cursor.getString(index);
		
		cursor.close();
		
		mHistoryAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, activities);
		
		mHistoryListView = (ListView) view.findViewById(R.id.user_activity_history);
		mHistoryListView.setAdapter(mHistoryAdapter);
		mHistoryListView.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				String item = (String) mHistoryListView.getItemAtPosition(position);
				
				mActivityEditText.setText(item);
				mActivityEditText.setSelection(item.length());
			}			
		});
		
		mActivityEditText = (EditText) view.findViewById(R.id.user_activity);
		mActivityEditText.addTextChangedListener(new TextWatcher()
		{
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count)
			{
				mHistoryAdapter.getFilter().filter(s);
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after)
			{

			}
			
			@Override
			public void afterTextChanged(Editable s)
			{
				mNextButton.setEnabled(s.length() > 0);
			}
		});
		
		mNextButton = (Button) view.findViewById(R.id.next_fragment);
		mNextButton.setEnabled(false);
		mNextButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				getFragmentManager()
					.beginTransaction()
					.replace(R.id.fragment_container, new AddEventUserFeelingFragment())
					.commit();
			}
		});
		
		return view;
	}
}
