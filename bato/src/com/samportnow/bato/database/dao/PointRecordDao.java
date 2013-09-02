package com.samportnow.bato.database.dao;

public class PointRecordDao
{
	private long mId;
	private long mCreated;
	private int mType;
	private int mPoints;
	
	public long getId()
	{
		return mId;
	}
	
	public void setId(long id)
	{
		mId = id;
	}
	
	public long getCreated()
	{
		return mCreated;
	}
	
	public void setCreated(long created)
	{
		mCreated = created;
	}
	
	public int getType()
	{
		return mType;
	}
	
	public void setType(int type)
	{
		mType = type;
	}
	
	public int getPoints()
	{
		return mPoints;
	}
	
	public void setPoints(int points)
	{
		mPoints = points;
	}
}
