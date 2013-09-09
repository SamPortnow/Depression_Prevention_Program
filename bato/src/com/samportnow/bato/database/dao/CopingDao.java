package com.samportnow.bato.database.dao;


public class CopingDao
{
	private long mId;
	private long mCreated;
	private String mContent;
	
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
	
}
