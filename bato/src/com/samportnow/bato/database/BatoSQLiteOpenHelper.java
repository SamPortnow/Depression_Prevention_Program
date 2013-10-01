package com.samportnow.bato.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BatoSQLiteOpenHelper extends SQLiteOpenHelper
{
	private static final String DATABASE_NAME = "bato.db";
	private static final int DATABASE_VERSION = 1;
	
	public static final String KEY_ROWID = "_id";
	
	public static final String TABLE_THOUGHTS = "thoughts";
	public static final String TABLE_CHALLENGING = "challenging";
	public static final String TABLE_POINT_RECORDS = "point_records";
	public static final String TABLE_HIGH_SCORES = "high_scores";
	
	public static final String COLUMN_CREATED = "created";
	public static final String COLUMN_ACTIVITY = "activity";
	public static final String COLUMN_FEELING = "feeling";
	public static final String COLUMN_CONTENT = "content";
	public static final String COLUMN_NEGATIVE_TYPE = "negative_type";
	public static final String COLUMN_COPING_STRATEGY = "strategy";
	
	public static final String COLUMN_BELIEVE = "believe";
	public static final String COLUMN_HELPFUL = "helpful";
	public static final String COLUMN_THOUGHT_ID = "thought_id";
	
	public static final String COLUMN_TYPE = "type";
	public static final String COLUMN_POINTS = "points";
	
	public static final String COLUMN_GAME_TYPE = "game";
	public static final String COLUMN_SCORE = "score";
	
	private static final String CREATE_TABLE_THOUGHTS =
		" CREATE TABLE " + TABLE_THOUGHTS +
		" (" +
		"   " + KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
		"   " + COLUMN_CREATED + " INTEGER NOT NULL," +
		"   " + COLUMN_ACTIVITY + " TEXT NOT NULL," +
		"   " + COLUMN_FEELING + " INTEGER NOT NULL," +
		"   " + COLUMN_CONTENT + " TEXT NOT NULL," +
		"   " + COLUMN_NEGATIVE_TYPE + " INTEGER," +
		"   " + COLUMN_COPING_STRATEGY + " TEXT" + 
		" )";
	
	private static final String CREATE_TABLE_CHALLENGING =
		" CREATE TABLE " + TABLE_CHALLENGING + 
		" (" +
		"    " + KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
		"    " + COLUMN_CREATED + " INTEGER NOT NULL," +
		"    " + COLUMN_CONTENT + " TEXT NOT NULL," +
		"    " + COLUMN_BELIEVE + " INTEGER NOT NULL," +
		"    " + COLUMN_HELPFUL + " INTEGER NOT NULL," +
		"    " + COLUMN_THOUGHT_ID + " INTEGER NOT NULL," +
		"    FOREIGN KEY(" + COLUMN_THOUGHT_ID + ") REFERENCES " + TABLE_THOUGHTS + "(" + KEY_ROWID + ")" +
		" )";

	private static final String CREATE_TABLE_POINT_RECORDS =
		" CREATE TABLE " + TABLE_POINT_RECORDS +
		" (" +
		"    " + KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
		"    " + COLUMN_CREATED + " INTEGER NOT NULL," +
		"    " + COLUMN_TYPE + " INTEGER NOT NULL, " +
		"    " + COLUMN_POINTS + " INTEGER NOT NULL" +
		" )";
	
	private static final String CREATE_TABLE_HIGH_SCORES =
		" CREATE TABLE " + TABLE_HIGH_SCORES +
		" (" +
		"   " + KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
		"   " + COLUMN_CREATED + " INTEGER NOT NULL," +
		"   " + COLUMN_GAME_TYPE + " INTEGER NOT NULL," +
		"   " + COLUMN_SCORE + " INTEGER NOT NULL" +
		" )";
	
	public BatoSQLiteOpenHelper(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
		db.execSQL(CREATE_TABLE_THOUGHTS);
		db.execSQL(CREATE_TABLE_CHALLENGING);
		db.execSQL(CREATE_TABLE_POINT_RECORDS);
		db.execSQL(CREATE_TABLE_HIGH_SCORES);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		
	}
}