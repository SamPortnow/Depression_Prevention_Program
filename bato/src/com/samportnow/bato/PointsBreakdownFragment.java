package com.samportnow.bato;

import java.util.GregorianCalendar;
import java.util.List;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.samportnow.bato.database.BatoDataSource;
import com.samportnow.bato.database.dao.PointRecordDao;

public class PointsBreakdownFragment extends DialogFragment
{	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.fragment_points_breakdown, container, false);
		
		final String[] typeLabels = getResources().getStringArray(R.array.point_record_type_labels);
		
		final BatoDataSource dataSource = new BatoDataSource(getActivity()).open();
		final List<PointRecordDao> pointRecords = dataSource.getAllPointRecords();		
		
		dataSource.close();
		
		BaseAdapter adapter = new BaseAdapter()
		{					
			@Override
			public View getView(int position, View convertView, ViewGroup parent)
			{
				PointRecordDao pointRecord = pointRecords.get(position);
				
				if (convertView == null)
					convertView = LayoutInflater.from(getActivity()).inflate(R.layout.listitem_point_record, parent, false);		
				
				GregorianCalendar calendar = new GregorianCalendar();
				calendar.setTimeInMillis(pointRecord.getCreated());
				
				((TextView) convertView.findViewById(R.id.point_record_type_label)).setText(typeLabels[pointRecord.getType()]);
				((TextView) convertView.findViewById(R.id.point_record_points_value)).setText(pointRecord.getPoints() + " pts.");
				((TextView) convertView.findViewById(R.id.point_record_created_value)).setText(calendar.getTime().toString());
				
				return convertView;
			}
			
			@Override
			public long getItemId(int position)
			{
				return position;
			}
			
			@Override
			public Object getItem(int position)
			{
				return pointRecords.get(position);
			}
			
			@Override
			public int getCount()
			{
				return pointRecords.size();
			}
		};
		
		((ListView) view.findViewById(R.id.points_breakdown_listview)).setAdapter(adapter);
		
		return view;
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState)
	{
		Dialog dialog = super.onCreateDialog(savedInstanceState);
		
		dialog.setTitle(R.string.points_breakdown_dialog_title);
		
		return dialog;
	}
}
