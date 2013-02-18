package com.example.bato;

import android.app.Activity;
import android.app.AlertDialog;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;


public class ActivityList extends ListFragment

{
private ActivityDbAdapter mDbHelper;
private Long mRowId=Long.valueOf(1);
private Activity mContext;//changed to private
AlertDialog activity_relationship;

@Override 
public void onCreate(Bundle savedInstanceState)
{
	super.onCreate(savedInstanceState);	
	mContext=this.getActivity();
	mDbHelper=new ActivityDbAdapter(mContext);
	mDbHelper.open();

	setListAdapter(new ListAdapter()
	{

		@Override
		public int getCount() {
			return 1;
		}

		@Override
		public Object getItem(int arg0) {
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		@Override
		public int getItemViewType(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(final int position, View arg1, ViewGroup arg2) 
		{
		    Cursor activity = mDbHelper.fetchAll(mRowId);
		    LayoutInflater inflater = mContext.getLayoutInflater();
		    View view = (View) inflater.inflate(R.layout.activity_activity_row, getListView(),false);
		    LinearLayout events=(LinearLayout) view.findViewById(R.id.rows);
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
			   TextView to_relationship=(TextView) view.findViewById(R.id.relationship_value);
			    TextView to_education=(TextView) view.findViewById(R.id.education_value);
				    TextView to_recreation=(TextView) view.findViewById(R.id.recreation_value);
				    TextView to_mind=(TextView) view.findViewById(R.id.mind_value);
				    TextView to_daily=(TextView) view.findViewById(R.id.daily_value); 
					    

					    
					    to_relationship.setOnClickListener(new OnClickListener() {

					            public void onClick(View view) {
					            	onRelationshipClick(view);
					            }

					        });
					    
					    to_education.setOnClickListener(new OnClickListener() {

				            public void onClick(View view) {
				            	onEducationClick(view);
				            }

				        });
					    
					    to_recreation.setOnClickListener(new OnClickListener() {

				            public void onClick(View view) {
				            	onRecreationClick(view);
				            }

				        });
					    
					    to_mind.setOnClickListener(new OnClickListener() {

				            public void onClick(View view) {
				            	onMindClick(view);
				            }

				        });
					    
					    
					    to_daily.setOnClickListener(new OnClickListener() {

				            public void onClick(View view) {
				            	onDailyClick(view);
				            }

				        });
					    


			 return view;	
		}

		@Override
		public int getViewTypeCount() {
			// TODO Auto-generated method stub
			return 1;
		}

		@Override
		public boolean hasStableIds() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean isEmpty() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void registerDataSetObserver(DataSetObserver arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void unregisterDataSetObserver(DataSetObserver arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public boolean areAllItemsEnabled() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean isEnabled(int arg0) {
			// TODO Auto-generated method stub
			return false;
		}
		
	});
	
	}
		
public void onRelationshipClick(View view)
{
	  
	 Cursor activity = mDbHelper.fetchAll(mRowId);
   if (activity.moveToFirst())
   {
		 String mactivity_relationship=activity.getString(												//define all of the Strings. Gahh!!
			activity.getColumnIndexOrThrow(ActivityDbAdapter.COLUMN_NAME_ACTIVITY1_RELATIONSHIP));
	 String mactivity_relationship2=activity.getString(
			activity.getColumnIndexOrThrow(ActivityDbAdapter.COLUMN_NAME_ACTIVITY2_RELATIONSHIP));
	 String mactivity_relationship3=activity.getString(
			activity.getColumnIndexOrThrow(ActivityDbAdapter.COLUMN_NAME_ACTIVITY3_RELATIONSHIP));
	 String mactivity_relationship4=activity.getString(
			activity.getColumnIndexOrThrow(ActivityDbAdapter.COLUMN_NAME_ACTIVITY4_RELATIONSHIP));
	 String mactivity_relationship5=activity.getString(
			activity.getColumnIndexOrThrow(ActivityDbAdapter.COLUMN_NAME_ACTIVITY5_RELATIONSHIP));
		AlertDialog activity_relationship=new AlertDialog.Builder(mContext).create();
  	activity_relationship.setTitle("Relationship Activities");
  	String activities=(mactivity_relationship+"\n"+mactivity_relationship2+"\n"+mactivity_relationship3+
  			"\n"+mactivity_relationship4+"\n"+mactivity_relationship5);
  	activity_relationship.setMessage(activities);
  	Log.e("I","Should be building");
  	activity_relationship.show();
   }
}

public void onEducationClick(View view)
{
	 Cursor activity = mDbHelper.fetchAll(mRowId);
   if (activity.moveToFirst())
   {
	  String mactivity_education=activity.getString(												//define all of the Strings. Gahh!!
				activity.getColumnIndexOrThrow(ActivityDbAdapter.COLUMN_NAME_ACTIVITY1_EDUCATION));
		String mactivity_education2=activity.getString(
				activity.getColumnIndexOrThrow(ActivityDbAdapter.COLUMN_NAME_ACTIVITY2_EDUCATION));
		String mactivity_education3=activity.getString(
				activity.getColumnIndexOrThrow(ActivityDbAdapter.COLUMN_NAME_ACTIVITY3_EDUCATION));
		String mactivity_education4=activity.getString(
				activity.getColumnIndexOrThrow(ActivityDbAdapter.COLUMN_NAME_ACTIVITY4_EDUCATION));
		String mactivity_education5=activity.getString(
				activity.getColumnIndexOrThrow(ActivityDbAdapter.COLUMN_NAME_ACTIVITY5_EDUCATION));
		AlertDialog activity_education=new AlertDialog.Builder(mContext).create();
  	activity_education.setTitle("Relationship Activities");
  	String activities_education=(mactivity_education+"\n"+mactivity_education2+"\n"+mactivity_education3+
  			"\n"+mactivity_education4+"\n"+mactivity_education5);
  	activity_education.setMessage(activities_education);
  	Log.e("I","Should be building");
  	activity_education.show();
	  }
   

 }

public void onRecreationClick(View view)
{
 	 Cursor activity = mDbHelper.fetchAll(mRowId);
   if (activity.moveToFirst())
   {  
 		 String mactivity_recreation=activity.getString(												//define all of the Strings. Gahh!!
				activity.getColumnIndexOrThrow(ActivityDbAdapter.COLUMN_NAME_ACTIVITY1_RECREATION));
		 String mactivity_recreation2=activity.getString(
				activity.getColumnIndexOrThrow(ActivityDbAdapter.COLUMN_NAME_ACTIVITY2_RECREATION));
		 String mactivity_recreation3=activity.getString(
				activity.getColumnIndexOrThrow(ActivityDbAdapter.COLUMN_NAME_ACTIVITY3_RECREATION));
		 String mactivity_recreation4=activity.getString(
				activity.getColumnIndexOrThrow(ActivityDbAdapter.COLUMN_NAME_ACTIVITY4_RECREATION));
		 String mactivity_recreation5=activity.getString(
				activity.getColumnIndexOrThrow(ActivityDbAdapter.COLUMN_NAME_ACTIVITY5_RECREATION));
			AlertDialog activity_recreation=new AlertDialog.Builder(mContext).create();
      	activity_recreation.setTitle("Recreation Activities");
      	String activities_recreation=(mactivity_recreation+"\n"+mactivity_recreation2+"\n"+mactivity_recreation3+
      			"\n"+mactivity_recreation4+"\n"+mactivity_recreation5);
      	activity_recreation.setMessage(activities_recreation);
      	Log.e("I","Should be building");
      	activity_recreation.show();
   }
	  
}

public void onMindClick(View view)
{
  	 Cursor activity = mDbHelper.fetchAll(mRowId);
       if (activity.moveToFirst())
       {  
			String mactivity_mind=activity.getString(												//define all of the Strings. Gahh!!
					activity.getColumnIndexOrThrow(ActivityDbAdapter.COLUMN_NAME_ACTIVITY1_MIND));
			String mactivity_mind2=activity.getString(
					activity.getColumnIndexOrThrow(ActivityDbAdapter.COLUMN_NAME_ACTIVITY2_MIND));
			String mactivity_mind3=activity.getString(
					activity.getColumnIndexOrThrow(ActivityDbAdapter.COLUMN_NAME_ACTIVITY3_MIND));
			String mactivity_mind4=activity.getString(
					activity.getColumnIndexOrThrow(ActivityDbAdapter.COLUMN_NAME_ACTIVITY4_MIND));
			String mactivity_mind5=activity.getString(
					activity.getColumnIndexOrThrow(ActivityDbAdapter.COLUMN_NAME_ACTIVITY5_MIND)); 
			AlertDialog activity_mind=new AlertDialog.Builder(mContext).create();
      	activity_mind.setTitle("Mind Activities");
      	String activities_mind=(mactivity_mind+"\n"+mactivity_mind2+"\n"+mactivity_mind3+
      			"\n"+mactivity_mind4+"\n"+mactivity_mind5);
      	activity_mind.setMessage(activities_mind);
      	Log.e("I","Should be building");
      	activity_mind.show();
       }
	  
}

public void onDailyClick(View view)
{
	 Cursor activity = mDbHelper.fetchAll(mRowId);
if (activity.moveToFirst())
{  
    	String mactivity_daily=activity.getString(												//define all of the Strings. Gahh!!
				activity.getColumnIndexOrThrow(ActivityDbAdapter.COLUMN_NAME_ACTIVITY1_DAILY));
		String mactivity_daily2=activity.getString(
				activity.getColumnIndexOrThrow(ActivityDbAdapter.COLUMN_NAME_ACTIVITY2_DAILY));
		String mactivity_daily3=activity.getString(
				activity.getColumnIndexOrThrow(ActivityDbAdapter.COLUMN_NAME_ACTIVITY3_DAILY));
		String mactivity_daily4=activity.getString(
				activity.getColumnIndexOrThrow(ActivityDbAdapter.COLUMN_NAME_ACTIVITY4_DAILY));
		String mactivity_daily5=activity.getString(
				activity.getColumnIndexOrThrow(ActivityDbAdapter.COLUMN_NAME_ACTIVITY5_DAILY));
		AlertDialog activity_daily=new AlertDialog.Builder(mContext).create();
  	activity_daily.setTitle("Daily Activities");
  	String activities_daily=(mactivity_daily+"\n"+mactivity_daily2+"\n"+mactivity_daily3+
  			"\n"+mactivity_daily4+"\n"+mactivity_daily5);
  	activity_daily.setMessage(activities_daily);
  	Log.e("I","Should be building");
  	activity_daily.show();
}
}
	 		 


}



