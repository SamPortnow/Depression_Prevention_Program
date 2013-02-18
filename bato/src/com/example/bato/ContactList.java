package com.example.bato;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;


public class ContactList extends ListFragment

	{
	private ActivityDbAdapter mDbHelper;
	private Long mRowId=Long.valueOf(1);
	Activity mContext;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	        Bundle savedInstanceState) 
	{
	    mContext=getActivity();
	    mDbHelper=new ActivityDbAdapter(mContext);
	    mDbHelper.open();
	    Cursor activity = mDbHelper.fetchContacts(mRowId);

	    String[] from = new String[]{ActivityDbAdapter.COLUMN_NAME_CONTACT1 ,
	            ActivityDbAdapter.COLUMN_NAME_CONTACT2,ActivityDbAdapter.COLUMN_NAME_CONTACT3,
	            ActivityDbAdapter.COLUMN_NAME_CONTACT4, ActivityDbAdapter.COLUMN_NAME_CONTACT5};


	    int[] to = new int[]{R.id.contacts,R.id.contact2, R.id.contact3,
	    		R.id.contact4, R.id.contact5};

	    SimpleCursorAdapter contacts = 
	        new SimpleCursorAdapter(mContext, R.layout.activity_contact_row, activity, from, to);

	    setListAdapter(contacts);

	    return inflater.inflate(R.layout.activity_contact_list, container, false);

	}
	}
