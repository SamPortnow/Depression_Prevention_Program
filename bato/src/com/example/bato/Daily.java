package com.example.bato;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class Daily extends Activity {

	private ActivityDbAdapter mDbHelper;
	//daily values
	private EditText mvalue_daily;
	private EditText mactivity_daily;
	private EditText mactivity_daily2;
	private EditText mactivity_daily3;
	private EditText mactivity_daily4;
	private EditText mactivity_daily5;
	private Long mRowId=Long.valueOf(1);


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mDbHelper=new ActivityDbAdapter(this);
		mDbHelper.open();
		setContentView(R.layout.activity_daily);
		mvalue_daily=(EditText) findViewById(R.id.value_daily);
		mactivity_daily=(EditText) findViewById(R.id.activity_daily);
		mactivity_daily2=(EditText) findViewById(R.id.activity_daily2);
		mactivity_daily3=(EditText) findViewById(R.id.activity_daily3);
		mactivity_daily4=(EditText) findViewById(R.id.activity_daily4);
		mactivity_daily5=(EditText) findViewById(R.id.activity_daily5);
		//then create the button
		Button confirmButton=(Button) findViewById(R.id.step1);
		
		//what does this do!? //use the value or null if not present. will give you the saved instance state. OK. 
        mRowId = (savedInstanceState == null) ? null :
            (Long) savedInstanceState.getSerializable(ActivityDbAdapter.KEY_ROWID);
        if (mRowId == null) {
            Bundle extras = getIntent().getExtras();
            mRowId = extras != null ? extras.getLong(ActivityDbAdapter.KEY_ROWID)
                                    : null;
        }

		Spinner spinner_enjoyment = (Spinner) findViewById(R.id.spinnerenjoyment); //create all the spinners. there should be another way to do this. it's a PIA! should have done a for loop!!!
		ArrayAdapter<CharSequence> adapter_enjoyment =ArrayAdapter.createFromResource(this,
				R.array.likert_enjoyment,android.R.layout.simple_spinner_item);
		adapter_enjoyment.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_enjoyment.setAdapter(adapter_enjoyment);
		spinner_enjoyment.setSelection(3);
		Spinner spinner_importance = (Spinner) findViewById(R.id.spinnerimportance);
		ArrayAdapter<CharSequence> adapter_important =ArrayAdapter.createFromResource(this,
				R.array.likert_importance,android.R.layout.simple_spinner_item);
		adapter_important.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_importance.setAdapter(adapter_important);
		spinner_importance.setSelection(3);
		Spinner spinner_enjoyment2 = (Spinner) findViewById(R.id.spinnerenjoyment2); //and now on to the second 
		ArrayAdapter<CharSequence> adapter_enjoyment2 =ArrayAdapter.createFromResource(this,
				R.array.likert_enjoyment,android.R.layout.simple_spinner_item);
		adapter_enjoyment2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_enjoyment2.setAdapter(adapter_enjoyment);
		spinner_enjoyment2.setSelection(3);
		Spinner spinner_importance2 = (Spinner) findViewById(R.id.spinnerimportance2);
		ArrayAdapter<CharSequence> adapter_important2 =ArrayAdapter.createFromResource(this,
				R.array.likert_importance,android.R.layout.simple_spinner_item);
		adapter_important2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_importance2.setAdapter(adapter_important);
		spinner_importance2.setSelection(3); 
		Spinner spinner_enjoyment3 = (Spinner) findViewById(R.id.spinnerenjoyment3); //and now on to the third
		ArrayAdapter<CharSequence> adapter_enjoyment3 =ArrayAdapter.createFromResource(this,
				R.array.likert_enjoyment,android.R.layout.simple_spinner_item);
		adapter_enjoyment3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_enjoyment3.setAdapter(adapter_enjoyment3);
		spinner_enjoyment3.setSelection(3);
		Spinner spinner_importance3 = (Spinner) findViewById(R.id.spinnerimportance3);
		ArrayAdapter<CharSequence> adapter_important3 =ArrayAdapter.createFromResource(this,
				R.array.likert_importance,android.R.layout.simple_spinner_item);
		adapter_important3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_importance3.setAdapter(adapter_important3);
		spinner_importance3.setSelection(3); 
		Spinner spinner_enjoyment4 = (Spinner) findViewById(R.id.spinnerenjoyment4); //and now on to the fourth
		ArrayAdapter<CharSequence> adapter_enjoyment4 =ArrayAdapter.createFromResource(this,
				R.array.likert_enjoyment,android.R.layout.simple_spinner_item);
		adapter_enjoyment4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_enjoyment4.setAdapter(adapter_enjoyment4);
		spinner_enjoyment4.setSelection(3);
		Spinner spinner_importance4 = (Spinner) findViewById(R.id.spinnerimportance4);
		ArrayAdapter<CharSequence> adapter_important4 =ArrayAdapter.createFromResource(this,
				R.array.likert_importance,android.R.layout.simple_spinner_item);
		adapter_important4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_importance4.setAdapter(adapter_important4);
		spinner_importance4.setSelection(3); 
		
		Spinner spinner_enjoyment5 = (Spinner) findViewById(R.id.spinnerenjoyment5); //and now on to the fifth
		ArrayAdapter<CharSequence> adapter_enjoyment5 =ArrayAdapter.createFromResource(this,
				R.array.likert_enjoyment,android.R.layout.simple_spinner_item);
		adapter_enjoyment5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_enjoyment5.setAdapter(adapter_enjoyment5);
		spinner_enjoyment5.setSelection(3);
		Spinner spinner_importance5 = (Spinner) findViewById(R.id.spinnerimportance5);
		ArrayAdapter<CharSequence> adapter_important5 =ArrayAdapter.createFromResource(this,
				R.array.likert_importance,android.R.layout.simple_spinner_item);
		adapter_important5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_importance5.setAdapter(adapter_important5);
		spinner_importance5.setSelection(3); 
	    
		populateFields();
		confirmButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
            	setResult(RESULT_OK);
            	to_time(view);
            	
            }

        });
	
	}

	private void populateFields()
{

		Cursor activity = mDbHelper.fetchDailyActivities(mRowId);
		if (activity.moveToFirst())
		{
		startManagingCursor(activity);
		mvalue_daily.setText(activity.getString(
				activity.getColumnIndexOrThrow(ActivityDbAdapter.COLUMN_NAME_VALUE1_DAILY)));
		mactivity_daily.setText(activity.getString(
				activity.getColumnIndexOrThrow(ActivityDbAdapter.COLUMN_NAME_ACTIVITY1_DAILY)));
		mactivity_daily2.setText(activity.getString(
				activity.getColumnIndexOrThrow(ActivityDbAdapter.COLUMN_NAME_ACTIVITY2_DAILY)));
		mactivity_daily3.setText(activity.getString(
				activity.getColumnIndexOrThrow(ActivityDbAdapter.COLUMN_NAME_ACTIVITY3_DAILY)));
		mactivity_daily4.setText(activity.getString(
				activity.getColumnIndexOrThrow(ActivityDbAdapter.COLUMN_NAME_ACTIVITY4_DAILY)));
		mactivity_daily5.setText(activity.getString(
				activity.getColumnIndexOrThrow(ActivityDbAdapter.COLUMN_NAME_ACTIVITY5_DAILY)));
		
	}
	
}
	
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveState();
        outState.putSerializable(ActivityDbAdapter.KEY_ROWID, mRowId);
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveState();
    }

    @Override
    protected void onResume() {
        super.onResume();
        populateFields();
    }


    private void saveState() { //here you save state. for when the activity ends! 
    	mRowId=Long.valueOf(1);
		Cursor activity = mDbHelper.fetchDailyActivities(mRowId);
		if (activity.moveToFirst())
		{
    	String value_daily = mvalue_daily.getText().toString();
        String activity1_daily = mactivity_daily.getText().toString();
        String activity2_daily= mactivity_daily2.getText().toString();
        String activity3_daily=mactivity_daily3.getText().toString();
        String activity4_daily=mactivity_daily4.getText().toString();
        String activity5_daily=mactivity_daily5.getText().toString();
        mDbHelper.updateDaily(mRowId, value_daily,  
        		activity1_daily,  activity2_daily,  activity3_daily,
        		activity4_daily,  activity5_daily);	
        } 
		else
		{
	    	String value_daily = mvalue_daily.getText().toString();
	        String activity1_daily = mactivity_daily.getText().toString();
	        String activity2_daily= mactivity_daily2.getText().toString();
	        String activity3_daily=mactivity_daily3.getText().toString();
	        String activity4_daily=mactivity_daily4.getText().toString();
	        String activity5_daily=mactivity_daily5.getText().toString();
	        mDbHelper.createDaily(mRowId, value_daily,  
	        		activity1_daily,  activity2_daily,  activity3_daily,
	        		activity4_daily,  activity5_daily);		
		}
    }


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_step3, menu);
		return true;
	}
	
	public void to_time(View view)
	{
		Intent intent=new Intent(this,Time.class);
		startActivity(intent);
	}
}

