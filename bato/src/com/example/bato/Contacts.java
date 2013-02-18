package com.example.bato;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.widget.EditText;

public class Contacts extends Activity {
	private ActivityDbAdapter mDbHelper;
	private EditText mcontact1;
	private EditText mcontact2;
	private EditText mcontact3;
	private EditText mcontact4;
	private EditText mcontact5;
	private Long mRowId=Long.valueOf(1);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mDbHelper=new ActivityDbAdapter(this);
		mDbHelper.open();
		setContentView(R.layout.activity_contacts);
		mcontact1=(EditText) findViewById(R.id.contact1);
		mcontact2=(EditText) findViewById(R.id.contact2);
		mcontact3=(EditText) findViewById(R.id.contact3);
		mcontact4=(EditText) findViewById(R.id.contact4);
		mcontact5=(EditText) findViewById(R.id.contact5);
        mRowId = (savedInstanceState == null) ? null :
            (Long) savedInstanceState.getSerializable(ActivityDbAdapter.KEY_ROWID);
        if (mRowId == null) {
            Bundle extras = getIntent().getExtras();
            mRowId = extras != null ? extras.getLong(ActivityDbAdapter.KEY_ROWID)
                                    : null;
        }
        populateFields();
        
	}
	private void populateFields()
{
		Cursor activity = mDbHelper.fetchContacts(mRowId);
		if (activity.moveToFirst())
		{
		startManagingCursor(activity);
		mcontact1.setText(activity.getString(
				activity.getColumnIndexOrThrow(ActivityDbAdapter.COLUMN_NAME_CONTACT1)));
		mcontact2.setText(activity.getString(
				activity.getColumnIndexOrThrow(ActivityDbAdapter.COLUMN_NAME_CONTACT2)));
		mcontact3.setText(activity.getString(
				activity.getColumnIndexOrThrow(ActivityDbAdapter.COLUMN_NAME_CONTACT3)));
		mcontact4.setText(activity.getString(
				activity.getColumnIndexOrThrow(ActivityDbAdapter.COLUMN_NAME_CONTACT4)));
		mcontact5.setText(activity.getString(
				activity.getColumnIndexOrThrow(ActivityDbAdapter.COLUMN_NAME_CONTACT5)));
		
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
    	String contact1 = mcontact1.getText().toString();
        String contact2 = mcontact2.getText().toString();
        String contact3= mcontact3.getText().toString();
        String contact4=mcontact4.getText().toString();
        String contact5=mcontact5.getText().toString();
        mDbHelper.updateContacts(mRowId,contact1,contact2, contact3,
        		contact4, contact5);	
        } 
		else
		{
	    	String contact1 = mcontact1.getText().toString();
	        String contact2 = mcontact2.getText().toString();
	        String contact3= mcontact3.getText().toString();
	        String contact4=mcontact4.getText().toString();
	        String contact5=mcontact5.getText().toString();
	        mDbHelper.createContacts(mRowId, contact1,contact2, contact3,
	        		contact4, contact5);		
		}
    }


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_contacts, menu);
		return true;
	}

}
