package com.samportnow.bato.database.dao;

public class ChallengingThoughtDao
{
	private long mId;
	private long mCreated;
	private String mContent;
	private int mBelieve;
	private int mHelpful;
	private long mThoughtId;
	
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
	
	public String getContent()
	{
		return mContent;
	}
	
	public void setContent(String content)
	{
		mContent = content;
	}
	
	public int getBelieve()
	{
		return mBelieve;
	}
	
	public void setBelieve(int believe)
	{
		mBelieve = believe;
	}
	
	public int getHelpful()
	{
		return mHelpful;
	}
	
	public void setHelpful(int helpful)
	{
		mHelpful = helpful;
	}
	
	public long getThoughtId()
	{
		return mThoughtId;
	}
	
	public void setThoughtId(long thoughtId)
	{
		mThoughtId = thoughtId;
	}
}
