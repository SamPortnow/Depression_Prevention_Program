package com.samportnow.bato.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
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
	
	public ThoughtsDataSource open() throws SQLException
	{
		mDatabase = mHelper.getWritableDatabase();
		
		return this;
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
	
	public List<String> getActivities()
	{
		Cursor cursor = 
			mDatabase.query(
				true,
				ThoughtsSQLiteOpenHelper.TABLE_THOUGHTS,
				new String[] { ThoughtsSQLiteOpenHelper.COLUMN_ACTIVITY },
				null, null, null, null,
				ThoughtsSQLiteOpenHelper.COLUMN_ACTIVITY + " ASC",
				null);
		
		ArrayList<String> activities = new ArrayList<String>(cursor.getCount());
		
		while (cursor.moveToNext())
			activities.add(cursor.getString(0));
		
		cursor.close();
		
		return activities;
	}
	
	public List<String> getThoughts()
	{
		Cursor cursor = 
			mDatabase.query(
				true,
				ThoughtsSQLiteOpenHelper.TABLE_THOUGHTS,
				new String[] { ThoughtsSQLiteOpenHelper.COLUMN_THOUGHT },
				null, null, null, null,
				ThoughtsSQLiteOpenHelper.COLUMN_THOUGHT + " ASC",
				null);
		
		ArrayList<String> thoughts = new ArrayList<String>(cursor.getCount());
		
		while (cursor.moveToNext())
			thoughts.add(cursor.getString(0));
		
		cursor.close();
		
		return thoughts;
	}
	
	public int getPoints()
	{		
		Cursor cursor = 
			mDatabase.query(
				false,
				ThoughtsSQLiteOpenHelper.TABLE_THOUGHTS,
				new String[] { ThoughtsSQLiteOpenHelper.COLUMN_ACTIVITY },
				null, null, null, null, null, null);
		
		String tableThoughts = ThoughtsSQLiteOpenHelper.TABLE_THOUGHTS;
		String columnActivity = ThoughtsSQLiteOpenHelper.COLUMN_ACTIVITY;
		String columnFeeling = ThoughtsSQLiteOpenHelper.COLUMN_FEELING;
	
		String rawSql =
			" SELECT " + tableThoughts + "." + columnActivity +
			" FROM " + tableThoughts +
			" LEFT OUTER JOIN" +
			" (" +
			"   SELECT " + columnActivity+ ", COUNT(*) AS count" +
			"   FROM " + tableThoughts +
			"   GROUP BY LOWER(" + columnActivity + ")" +
			" ) AS T1" +
			" ON LOWER(" + tableThoughts + "." + columnActivity + ") = LOWER(T1." + columnActivity + ")" +
			" WHERE " + columnFeeling + " >= 5" +
			"   AND T1.count > 1";
			
		Cursor rawCursor = mDatabase.rawQuery(rawSql, null);
		
		int points = cursor.getCount();
		int bonusPoints = rawCursor.getCount();	
		
		cursor.close();
		rawCursor.close();
				
		return (points * 25 + (bonusPoints * 50));
	}
}
