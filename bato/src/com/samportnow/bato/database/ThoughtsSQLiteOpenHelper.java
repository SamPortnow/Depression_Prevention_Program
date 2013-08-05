package com.samportnow.bato.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ThoughtsSQLiteOpenHelper extends SQLiteOpenHelper
{
	private static final String DATABASE_NAME = "thoughts.db";
	private static final int DATABASE_VERSION = 1;
	
	public static final String KEY_ROWID = "_id";
	
	public static final String TABLE_THOUGHTS = "thoughts";
	public static final String COLUMN_CREATED = "created";
	public static final String COLUMN_ACTIVITY = "activity";
	public static final String COLUMN_FEELING = "feeling";
	public static final String COLUMN_THOUGHT = "thought";
	public static final String COLUMN_NEGATIVE_TYPE = "negative_type";
	
	private static final String DATABASE_CREATE =
		" CREATE TABLE " + TABLE_THOUGHTS +
		" (" +
		"   " + KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
		"   " + COLUMN_CREATED + " INTEGER NOT NULL, " +
		"   " + COLUMN_ACTIVITY + " TEXT NOT NULL, " +
		"   " + COLUMN_FEELING + " INTEGER NOT NULL, " +
		"   " + COLUMN_THOUGHT + " TEXT NOT NULL, " +
		"   " + COLUMN_NEGATIVE_TYPE + " INTEGER" +
		" )";
	
	public ThoughtsSQLiteOpenHelper(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
		db.execSQL(DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		
	}
}