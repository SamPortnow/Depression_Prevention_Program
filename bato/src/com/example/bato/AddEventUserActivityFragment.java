package com.example.bato;

import android.app.Fragment;
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
	private static final String[] TEST = new String[] {"Rawr", "Rawring", "Rawrzil;a", "Rawwwwr", "Rararara"};
	
	private ArrayAdapter<String> mAdapter = null;
	
	private EditText mActivityEditText = null;
	private ListView mHistoryListView = null;
	
	private Button mNextButton = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		View view = inflater.inflate(R.layout.fragment_add_event_user_activity, null);
		
		mAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, TEST);
		
		mHistoryListView = (ListView) view.findViewById(R.id.user_activity_history);
		mHistoryListView.setAdapter(mAdapter);
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
				mAdapter.getFilter().filter(s);
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after)
			{
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s)
			{
				mNextButton.setEnabled(s.length() > 0);
			}
		});
		
		mNextButton = (Button) view.findViewById(R.id.next_fragment);
		mNextButton.setEnabled(false);
		
		return view;
	}
}
