package com.samportnow.bato.database.dao;

public class ThoughtDao
{
	private long mId;
	private long mCreated;
	private String mActivity;
	private int mFeeling;
	private String mContent;
	private int mNegativeType;
	private String mCopingStrategy;
	
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
	
	public String getActivity()
	{
		return mActivity;
	}
	
	public void setActivity(String activity)	
	{
		mActivity = activity;
	}
	
	public int getFeeling()
	{
		return mFeeling;
	}
	
	public void setFeeling(int feeling)
	{
		mFeeling = feeling;
	}
	
	public String getContent()
	{
		return mContent;
	}
	
	public void setContent(String content)
	{
		mContent = content;
	}
	
	public int getNegativeType()
	{
		return mNegativeType;
	}
	
	public void setNegativeType(int negativeType)
	{
		mNegativeType = negativeType;
	}
	
	public String getCopingStrategy()
	{
		return mCopingStrategy;
	}
	
	public void setCopingStrategy(String copingStrategy)
	{
		mCopingStrategy = copingStrategy;
	}
}
