package com.samportnow.bato.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.samportnow.bato.database.dao.ChallengingThoughtDao;
import com.samportnow.bato.database.dao.PointRecordDao;
import com.samportnow.bato.database.dao.ThoughtDao;

public class BatoDataSource
{
	private static final String[] THOUGHTDAO_QUERY_COLUMNS = 
	{
		BatoSQLiteOpenHelper.KEY_ROWID,
		BatoSQLiteOpenHelper.COLUMN_CREATED,
		BatoSQLiteOpenHelper.COLUMN_ACTIVITY,
		BatoSQLiteOpenHelper.COLUMN_FEELING,
		BatoSQLiteOpenHelper.COLUMN_CONTENT,
		BatoSQLiteOpenHelper.COLUMN_NEGATIVE_TYPE,
		BatoSQLiteOpenHelper.COLUMN_COPING_STRATEGY
	};
	
	private static final String[] CHALLENGINGDAO_QUERY_COLUMNS =
	{
		BatoSQLiteOpenHelper.KEY_ROWID,
		BatoSQLiteOpenHelper.COLUMN_CREATED,
		BatoSQLiteOpenHelper.COLUMN_CONTENT,
		BatoSQLiteOpenHelper.COLUMN_BELIEVE,
		BatoSQLiteOpenHelper.COLUMN_HELPFUL,
		BatoSQLiteOpenHelper.COLUMN_THOUGHT_ID
	};
	
	private static final String[] POINT_RECORD_DAO_QUERY_COLUMNS =
	{
		BatoSQLiteOpenHelper.KEY_ROWID,
		BatoSQLiteOpenHelper.COLUMN_CREATED,
		BatoSQLiteOpenHelper.COLUMN_TYPE,
		BatoSQLiteOpenHelper.COLUMN_POINTS
	};
	
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
	
	public long createThought(long created, String activity, int feeling, String thought, int negativeType, String copingStrategy)
	{
		ContentValues values = new ContentValues();
		
		values.put(BatoSQLiteOpenHelper.COLUMN_CREATED, created);
		values.put(BatoSQLiteOpenHelper.COLUMN_ACTIVITY, activity);
		values.put(BatoSQLiteOpenHelper.COLUMN_FEELING, feeling);
		values.put(BatoSQLiteOpenHelper.COLUMN_CONTENT, thought);
		values.put(BatoSQLiteOpenHelper.COLUMN_NEGATIVE_TYPE, negativeType);
		
		if (copingStrategy != null)
			values.put(BatoSQLiteOpenHelper.COLUMN_COPING_STRATEGY, copingStrategy);
		
		return mDatabase.insert(BatoSQLiteOpenHelper.TABLE_THOUGHTS, null, values);
	}
	
	public long createChallengingThought(long created, String content, int believe, int helpful, long thoughtId)
	{
		ContentValues values = new ContentValues();
		
		values.put(BatoSQLiteOpenHelper.COLUMN_CREATED, created);
		values.put(BatoSQLiteOpenHelper.COLUMN_CONTENT, content);
		values.put(BatoSQLiteOpenHelper.COLUMN_BELIEVE, believe);
		values.put(BatoSQLiteOpenHelper.COLUMN_HELPFUL, helpful);
		values.put(BatoSQLiteOpenHelper.COLUMN_THOUGHT_ID, thoughtId);
		
		return mDatabase.insert(BatoSQLiteOpenHelper.TABLE_CHALLENGING, null, values);
	}
	
	public List<ThoughtDao> getAllThoughts()
	{
		return getThoughts(null, null);
	}
	
	public List<ThoughtDao> getThoughtsBetween(Long startTimestamp, Long endTimestamp)
	{
		String selection =
			BatoSQLiteOpenHelper.COLUMN_CREATED + " >= " + startTimestamp +
			" AND " +
			BatoSQLiteOpenHelper.COLUMN_CREATED + " <= " + endTimestamp;
		
		return getThoughts(selection, null);
	}
	
	public List<ThoughtDao> getAllNegativeThoughts()
	{
		String selection =
			BatoSQLiteOpenHelper.COLUMN_NEGATIVE_TYPE + " != -1";
		
		return getThoughts(selection, null);
	}
	
	public List<ThoughtDao> getNegativeThoughts(int negativeType)
	{
		String selection =
			BatoSQLiteOpenHelper.COLUMN_NEGATIVE_TYPE + " = " + negativeType;
		
		return getThoughts(selection, null);
	}
	
	private List<ThoughtDao> getThoughts(String selection, String[] selectionArgs)
	{
		Cursor cursor =
			mDatabase.query(
				false,
				BatoSQLiteOpenHelper.TABLE_THOUGHTS,
				THOUGHTDAO_QUERY_COLUMNS,
				selection, selectionArgs,
				null, null,
				BatoSQLiteOpenHelper.COLUMN_CREATED + " ASC",
				null);
		
		ArrayList<ThoughtDao> thoughts = new ArrayList<ThoughtDao>(cursor.getCount());
		
		while (cursor.moveToNext())
		{
			ThoughtDao thought = createThoughtFromCursor(cursor);			
			thoughts.add(thought);
		}
		
		cursor.close();
		
		return thoughts;
	}
	
	public ThoughtDao getRandomThought()
	{
		Cursor cursor =
			mDatabase.query(
				false,
				BatoSQLiteOpenHelper.TABLE_THOUGHTS,
				THOUGHTDAO_QUERY_COLUMNS,
				null, null,
				null, null,
				"RANDOM()",
				"1");
		
		ThoughtDao thought = null;
		
		if (cursor.moveToNext())
			thought = createThoughtFromCursor(cursor);
		
		cursor.close();
		
		return thought;
	}
	
	public ThoughtDao getThoughtById(long thoughtId)
	{
		String selection = 
			BatoSQLiteOpenHelper.COLUMN_THOUGHT_ID + " = " + thoughtId;
			
		List<ThoughtDao> thoughts = getThoughts(selection, null);
		
		if (thoughts.size() < 1)
			return null;
		
		return thoughts.get(0);
	}
	
	public List<ThoughtDao> getRelatedThoughtsByActivity(String activity)
	{
		String selection =
			"LOWER(" + BatoSQLiteOpenHelper.COLUMN_ACTIVITY + ") = LOWER(?)";
		
		return getThoughts(selection, new String[] { activity } );
	}
	
	public List<ChallengingThoughtDao> getAllChallengingThoughts()
	{
		return getChallengingThoughts(null);
	}
	
	
	private List<ChallengingThoughtDao> getChallengingThoughts(String selection)
	{
		Cursor cursor =
			mDatabase.query(
				false,
				BatoSQLiteOpenHelper.TABLE_CHALLENGING,
				CHALLENGINGDAO_QUERY_COLUMNS,
				selection, null,
				null, null,
				BatoSQLiteOpenHelper.COLUMN_CREATED + " ASC",
				null);
		
		ArrayList<ChallengingThoughtDao> challenges = new ArrayList<ChallengingThoughtDao>(cursor.getCount());
		
		while (cursor.moveToNext())
		{
			ChallengingThoughtDao challenging = createChallengingThoughtFromCursor(cursor);			
			challenges.add(challenging);
		}
		
		cursor.close();
		
		return challenges;
	}
	
	private ThoughtDao createThoughtFromCursor(Cursor cursor)
	{
		ThoughtDao thought = new ThoughtDao();
		
		thought.setId(cursor.getLong(0));
		thought.setCreated(cursor.getLong(1));
		thought.setActivity(cursor.getString(2));
		thought.setFeeling(cursor.getInt(3));
		thought.setContent(cursor.getString(4));
		thought.setNegativeType(cursor.getInt(5));
		thought.setCopingStrategy(cursor.getString(6));
		
		return thought;
	}
	
	public ChallengingThoughtDao createChallengingThoughtFromCursor(Cursor cursor)
	{
		ChallengingThoughtDao challenging = new ChallengingThoughtDao();
		
		challenging.setId(cursor.getLong(0));
		challenging.setCreated(cursor.getLong(1));
		challenging.setContent(cursor.getString(2));
		challenging.setBelieve(cursor.getInt(3));
		challenging.setHelpful(cursor.getInt(4));
		challenging.setThoughtId(cursor.getLong(5));
		
		return challenging;
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
	
	public List<String> getAllThoughtCopingStrategy()
	{
		Cursor cursor = 
			mDatabase.query(
				true,
				BatoSQLiteOpenHelper.TABLE_THOUGHTS,
				new String[] { BatoSQLiteOpenHelper.COLUMN_COPING_STRATEGY },
				null, null, null, null,
				BatoSQLiteOpenHelper.COLUMN_COPING_STRATEGY + " ASC",
				null);
		
		ArrayList<String> contents = new ArrayList<String>(cursor.getCount());
		
		while (cursor.moveToNext())
			contents.add(cursor.getString(0));
		
		cursor.close();
		
		return contents;
	}	
	
	public List<String> getAllChallengingThoughtContent()
	{
		Cursor cursor =
			mDatabase.query(
				true,
				BatoSQLiteOpenHelper.TABLE_CHALLENGING,
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
	
	public long insertPointRecord(long created, int type, int points)
	{
		ContentValues values = new ContentValues();
		
		values.put(BatoSQLiteOpenHelper.COLUMN_CREATED, created);
		values.put(BatoSQLiteOpenHelper.COLUMN_TYPE, type);
		values.put(BatoSQLiteOpenHelper.COLUMN_POINTS, points);
		
		return mDatabase.insert(BatoSQLiteOpenHelper.TABLE_POINT_RECORDS, null, values);			
	}
	
	private PointRecordDao createPointRecordDao(Cursor cursor)
	{
		PointRecordDao pointRecord = new PointRecordDao();
		
		pointRecord.setId(cursor.getLong(0));
		pointRecord.setCreated(cursor.getLong(1));
		pointRecord.setType(cursor.getInt(2));
		pointRecord.setPoints(cursor.getInt(3));
		
		return pointRecord;
	}
	
	private List<PointRecordDao> getPointRecords(String selection, String[] selectionArgs)
	{
		Cursor cursor =
			mDatabase.query(
				true,
				BatoSQLiteOpenHelper.TABLE_POINT_RECORDS,
				POINT_RECORD_DAO_QUERY_COLUMNS,
				selection, selectionArgs,
				null, null,
				BatoSQLiteOpenHelper.COLUMN_CREATED + " DESC",
				null);
		
		ArrayList<PointRecordDao> pointRecords = new ArrayList<PointRecordDao>(cursor.getCount());
		
		while (cursor.moveToNext())
			pointRecords.add(createPointRecordDao(cursor));
		
		cursor.close();
		
		return pointRecords;		
	}
	
	public List<PointRecordDao> getAllPointRecords()
	{
		return getPointRecords(null, null);
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
