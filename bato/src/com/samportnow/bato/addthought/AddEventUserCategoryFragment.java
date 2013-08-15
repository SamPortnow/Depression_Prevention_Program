package com.samportnow.bato.addthought;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.samportnow.bato.R;

public class AddEventUserCategoryFragment extends Fragment
{
	private String[] mCategoryTitles = null;
	private String[] mCategoryDescriptions = null;
	private ArrayAdapter<String> mCategoryAdapter = null;
	
	private ListView mCategoryListView = null;
	private TextView mDescriptionTextView = null;
	private Button mNextButton = null;	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
	    super.onCreate(savedInstanceState);	   

	    View view = inflater.inflate(R.layout.fragment_add_event_user_category, null);
	    
	    mCategoryTitles = getResources().getStringArray(R.array.add_event_user_category_titles);
	    mCategoryDescriptions = getResources().getStringArray(R.array.add_event_user_category_descriptions);
	    
	    mCategoryAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_single_choice, mCategoryTitles);
	    
	    mCategoryListView = (ListView) view.findViewById(R.id.user_category_choices);
	    mCategoryListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
	    mCategoryListView.setAdapter(mCategoryAdapter);
	    mCategoryListView.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				mDescriptionTextView.setText(mCategoryDescriptions[position]);
				mNextButton.setEnabled(true);
			}
		});

	    mDescriptionTextView = (TextView) view.findViewById(R.id.user_category_description);
	    mDescriptionTextView.setText(R.string.add_event_user_category_instruction);	  
	    
	    mNextButton = (Button) view.findViewById(R.id.next_fragment);
	    mNextButton.setEnabled(false);
		mNextButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				int position = mCategoryListView.getCheckedItemPosition();
				
				if (position != ListView.INVALID_POSITION)
				{
					Bundle eventBundle = getArguments();
					eventBundle.putString("user_category", mCategoryTitles[position]);
				}
				
				((AddEventActivity) getActivity()).createNewEvent();
			}
		});
	    
	    return view;
	}
}
