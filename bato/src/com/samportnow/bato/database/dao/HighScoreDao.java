package com.samportnow.bato.database.dao;

public class HighScoreDao
{
	private long mId;
	private long mCreated;
	private int mGameType;
	private long mScore;
	
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
	
	public int getGameType()
	{
		return mGameType;
	}
	
	public void setGameType(int gameType)
	{
		mGameType = gameType;
	}
	
	public long getScore()
	{
		return mScore;
	}
	
	public void setScore(long score)
	{
		mScore = score;
	}
}