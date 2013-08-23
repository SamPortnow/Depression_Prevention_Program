package com.samportnow.bato.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.samportnow.bato.database.dao.ThoughtDao;

public class BatoDataSource
{
	private SQLiteDatabase mDatabase;
	private BatoSQLiteOpenHelper mHelper;
	
	public BatoDataSource(Context context)
	{
		mHelper = new BatoSQLiteOpenHelper(context);
	}
	
	public BatoDataSource open() throws SQLException
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
		
		values.put(BatoSQLiteOpenHelper.COLUMN_CREATED, created);
		values.put(BatoSQLiteOpenHelper.COLUMN_ACTIVITY, activity);
		values.put(BatoSQLiteOpenHelper.COLUMN_FEELING, feeling);
		values.put(BatoSQLiteOpenHelper.COLUMN_CONTENT, thought);
		values.put(BatoSQLiteOpenHelper.COLUMN_NEGATIVE_TYPE, negativeType);
		
		return mDatabase.insert(BatoSQLiteOpenHelper.TABLE_THOUGHTS, null, values);
	}
	
	public List<ThoughtDao> getAllThoughts()
	{
		return getThoughts(null);
	}
	
	public List<ThoughtDao> getThoughtsBetween(Long startTimestamp, Long endTimestamp)
	{
		String selection =
			BatoSQLiteOpenHelper.COLUMN_CREATED + " >= " + startTimestamp +
			" AND " +
			BatoSQLiteOpenHelper.COLUMN_CREATED + " <= " + endTimestamp;
		
		return getThoughts(selection);
	}
	
	
	public List<ThoughtDao> getNegativeThoughts(int negativeType)
	{
		String selection =
			BatoSQLiteOpenHelper.COLUMN_NEGATIVE_TYPE + " = " + negativeType;
		
		return getThoughts(selection);
	}	
	
	private List<ThoughtDao> getThoughts(String selection)
	{
		Cursor cursor =
			mDatabase.query(
				false,
				BatoSQLiteOpenHelper.TABLE_THOUGHTS,
				new String[]
				{
					BatoSQLiteOpenHelper.KEY_ROWID,
					BatoSQLiteOpenHelper.COLUMN_CREATED,
					BatoSQLiteOpenHelper.COLUMN_ACTIVITY,
					BatoSQLiteOpenHelper.COLUMN_FEELING,
					BatoSQLiteOpenHelper.COLUMN_CONTENT,
					BatoSQLiteOpenHelper.COLUMN_NEGATIVE_TYPE
				},
				selection, null,
				null, null,
				BatoSQLiteOpenHelper.COLUMN_CREATED + " ASC",
				null);
		
		ArrayList<ThoughtDao> thoughts = new ArrayList<ThoughtDao>(cursor.getCount());
		
		while (cursor.moveToNext())
		{
			ThoughtDao thought = new ThoughtDao();
			
			thought.setId(cursor.getLong(0));
			thought.setCreated(cursor.getLong(1));
			thought.setActivity(cursor.getString(2));
			thought.setFeeling(cursor.getInt(3));
			thought.setContent(cursor.getString(4));
			thought.setNegativeType(cursor.getInt(5));
			
			thoughts.add(thought);
		}
		
		cursor.close();
		
		return thoughts;
	}
	
	public List<String> getAllThoughtActivity()
	{
		Cursor cursor = 
			mDatabase.query(
				true,
				BatoSQLiteOpenHelper.TABLE_THOUGHTS,
				new String[] { BatoSQLiteOpenHelper.COLUMN_ACTIVITY },
				null, null, null, null,
				BatoSQLiteOpenHelper.COLUMN_ACTIVITY + " ASC",
				null);
		
		ArrayList<String> activities = new ArrayList<String>(cursor.getCount());
		
		while (cursor.moveToNext())
			activities.add(cursor.getString(0));
		
		cursor.close();
		
		return activities;
	}
	
	public List<String> getAllThoughtContent()
	{
		Cursor cursor = 
			mDatabase.query(
				true,
				BatoSQLiteOpenHelper.TABLE_THOUGHTS,
				new String[] { BatoSQLiteOpenHelper.COLUMN_CONTENT },
				null, null, null, null,
				BatoSQLiteOpenHelper.COLUMN_CONTENT + " ASC",
				null);
		
		ArrayList<String> contents = new ArrayList<String>(cursor.getCount());
		
		while (cursor.moveToNext())
			contents.add(cursor.getString(0));
		
		cursor.close();
		
		return contents;
	}
	
	public int getPoints()
	{		
		Cursor cursor = 
			mDatabase.query(
				false,
				BatoSQLiteOpenHelper.TABLE_THOUGHTS,
				new String[] { BatoSQLiteOpenHelper.COLUMN_ACTIVITY },
				null, null, null, null, null, null);
		
		String tableThoughts = BatoSQLiteOpenHelper.TABLE_THOUGHTS;
		String columnActivity = BatoSQLiteOpenHelper.COLUMN_ACTIVITY;
		String columnFeeling = BatoSQLiteOpenHelper.COLUMN_FEELING;
	
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
