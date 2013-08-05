package com.samportnow.bato.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class ThoughtsDataSource
{
	private SQLiteDatabase mDatabase;
	private ThoughtsSQLiteOpenHelper mHelper;
	
	public ThoughtsDataSource(Context context)
	{
		mHelper = new ThoughtsSQLiteOpenHelper(context);
	}
	
	public void open() throws SQLException
	{
		mDatabase = mHelper.getWritableDatabase();
	}
	
	public void close()
	{
		mHelper.close();
	}
	
	public long createThought(long created, String activity, int feeling, String thought, int negativeType)
	{
		ContentValues values = new ContentValues();
		
		values.put(ThoughtsSQLiteOpenHelper.COLUMN_CREATED, created);
		values.put(ThoughtsSQLiteOpenHelper.COLUMN_ACTIVITY, activity);
		values.put(ThoughtsSQLiteOpenHelper.COLUMN_FEELING, feeling);
		values.put(ThoughtsSQLiteOpenHelper.COLUMN_THOUGHT, thought);
		values.put(ThoughtsSQLiteOpenHelper.COLUMN_NEGATIVE_TYPE, negativeType);
		
		return mDatabase.insert(ThoughtsSQLiteOpenHelper.TABLE_THOUGHTS, null, values);
	}
}
