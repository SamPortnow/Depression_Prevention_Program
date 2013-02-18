package com.example.bato;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SimpleCursorAdapter;

public class CustomCursorAdapter extends CursorAdapter
{
//goal here is to make a custom cursor adapter
//and I want to bind it to a view in the bindView method
//in new view I want to return the view 
private LayoutInflater mInflater;
private Context mContext;
private ActivityDbAdapter mDbHelper;
private Long mRowId=Long.valueOf(1);


public CustomCursorAdapter(Context context, Cursor c) 
{
	super(context, c);
	mContext=context;
	mInflater=LayoutInflater.from(context);
	mDbHelper=new ActivityDbAdapter(mContext);
	mDbHelper.open();
}


	@Override
	public void bindView(View view, Context context, Cursor arg2) 
	{
	    Cursor activity = mDbHelper.fetchAll(mRowId);
       
	   // Button relationship = (Button) view.findViewById(R.id.show_relationship_activities);
		if (activity.moveToFirst())
		{
		    
			String[] from = new String[]{ActivityDbAdapter.COLUMN_NAME_VALUE1_RELATIONSHIP , //from and too. of course
		            ActivityDbAdapter.COLUMN_NAME_VALUE1_EDUCATION,
		    		ActivityDbAdapter.COLUMN_NAME_VALUE1_RECREATION,
		            ActivityDbAdapter.COLUMN_NAME_VALUE1_MIND, ActivityDbAdapter.COLUMN_NAME_VALUE1_DAILY};


		    int[] to = new int[]{R.id.relationship_value,R.id.education_value, R.id.recreation_value,
		    		R.id.mind_value, R.id.daily_value};

		    SimpleCursorAdapter contacts = 
		        new SimpleCursorAdapter(mContext, R.layout.activity_activity_row, activity, from, to); //my cursor adapter 
		   
		    contacts.bindView(view, mContext, activity);
		}
		
	}

	@Override
	public View newView(Context arg0, Cursor arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		return null;
	}

}
