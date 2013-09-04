package com.samportnow.bato;

import java.util.List;

import com.samportnow.bato.database.dao.ThoughtDao;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ThoughtsHistoryFragment extends DialogFragment
{
	private List<ThoughtDao> mThoughts = null;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.fragment_thoughts_history, container, false);
		
		return view;
	}
	
	public void setThoughts(List<ThoughtDao> thoughts)
	{
		mThoughts = thoughts;
	}
	
	private BaseAdapter createBaseAdapter(final List<ThoughtDao> thoughts)
	{
		if (thoughts == null)
			return null;
		
		BaseAdapter adapter = new BaseAdapter()
		{
			public View getView(int position, View convertView, ViewGroup parent)
			{	
				ThoughtDao thought = thoughts.get(position);
				
				if (convertView == null)
					convertView = LayoutInflater.from(getActivity()).inflate(R.layout.listitem_thought_basic_with_points, parent, false);
				
				((TextView) convertView.findViewById(R.id.thought_content)).setText(thought.getContent());
				((TextView) convertView.findViewById(R.id.thought_activity)).setText(thought.getActivity());
				
				return convertView;
			}
			
			public long getItemId(int position)
			{
				return thoughts.get(position).getId();
			}
			
			public Object getItem(int position)
			{
				return thoughts.get(position);
			}
			
			public int getCount()
			{
				return thoughts.size();
			}
		};
		
		return adapter;
	}
}
