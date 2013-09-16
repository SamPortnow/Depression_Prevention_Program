package com.samportnow.bato.addthought;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
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
import com.samportnow.bato.database.BatoDataSource;

public class AddEventUserCoping extends Fragment
{
	private ArrayAdapter<String> mCopingAdapter = null;
	private ListView mCopingListView = null;
	private String[] mCoping = null;
	private ArrayAdapter<String> mCommonCopingAdapter = null;
	private ListView mCommonCopingListView = null;
	private EditText create_coping;

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);	   

		View view = inflater.inflate(R.layout.fragment_add_event_user_coping, null);
		
		create_coping = (EditText) view.findViewById(R.id.create_coping);
		BatoDataSource dataSource = new BatoDataSource(getActivity()).open();
		mCopingAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1);


		mCopingAdapter.addAll(dataSource.getAllCoping());

		dataSource.close();

		mCopingListView = (ListView) view.findViewById(R.id.user_coping_history);

		SwingRightInAnimationAdapter animationAdapter = new SwingRightInAnimationAdapter(mCopingAdapter);
		animationAdapter.setAbsListView(mCopingListView);	

		mCopingListView.setAdapter(mCopingAdapter);

		mCoping = getResources().getStringArray(R.array.add_event_user_coping_strategies_titles);
		mCommonCopingAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, mCoping);	

		mCommonCopingListView = (ListView) view.findViewById(R.id.user_common_coping);
		mCommonCopingListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		
		SwingRightInAnimationAdapter animationAdapterCommon = new SwingRightInAnimationAdapter(mCopingAdapter);
		animationAdapterCommon.setAbsListView(mCopingListView);	

		mCommonCopingListView.setAdapter(mCommonCopingAdapter);
		
		mCopingListView.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				String item = (String) mCopingListView.getItemAtPosition(position);

				create_coping.setText(item);

			}			
		});

		mCommonCopingListView.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				String item = (String) mCommonCopingListView.getItemAtPosition(position);

				create_coping.setText(item);
				
			}			
		});
		
		create_coping.addTextChangedListener(new TextWatcher()
		{

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count)
			{
				//mCopingAdapter.getFilter().filter(s);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after)
			{

			}

			@Override
			public void afterTextChanged(Editable s)
			{
				//mNextButton.setEnabled(s.length() > 0);
			}
		});
		
		Button submit = (Button) view.findViewById(R.id.submit);
		submit.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v) 
			{
				InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

				Bundle eventBundle = getArguments();
				eventBundle.putString("user_coping", create_coping.getText().toString());
				
				((AddEventActivity) getActivity()).createNewEvent();

			}
			
		});
		
		Button to_coping = (Button) view.findViewById(R.id.go_to_coping);
		to_coping.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0) 
			{
				((AddEventActivity) getActivity()).createNewEvent();

			}
			
		});
		return view;
	}
}
