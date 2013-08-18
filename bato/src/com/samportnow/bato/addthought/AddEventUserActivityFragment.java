package com.samportnow.bato.addthought;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.haarman.listviewanimations.swinginadapters.prepared.SwingRightInAnimationAdapter;
import com.samportnow.bato.R;
import com.samportnow.bato.database.ThoughtsDataSource;

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

		ThoughtsDataSource dataSource = new ThoughtsDataSource(getActivity()).open();

		mHistoryAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1);
		mHistoryAdapter.addAll(dataSource.getAllThoughtActivity());

		dataSource.close();

		mHistoryListView = (ListView) view.findViewById(R.id.user_activity_history);

		SwingRightInAnimationAdapter animationAdapter = new SwingRightInAnimationAdapter(mHistoryAdapter);
		animationAdapter.setAbsListView(mHistoryListView);

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
				InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

				Bundle eventBundle = getArguments();
				eventBundle.putString("user_activity", mActivityEditText.getText().toString());

				Fragment fragment = new AddEventUserFeelingFragment();
				fragment.setArguments(eventBundle);

				getFragmentManager()
					.beginTransaction()
					.replace(R.id.fragment_container, fragment)
					.commit();
			}
		});

		return view;
	}
}
