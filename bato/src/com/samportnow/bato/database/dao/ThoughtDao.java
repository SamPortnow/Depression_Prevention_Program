package com.samportnow.bato.database.dao;

public class ThoughtDao
{
	private long mId;
	private long mCreated;
	private String mActivity;
	private int mFeeling;
	private String mThought;
	private int mNegativeType;
	
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
	
	public String getThought()
	{
		return mThought;
	}
	
	public void setThought(String thought)
	{
		mThought = thought;
	}
	
	public int getNegativeType()
	{
		return mNegativeType;
	}
	
	public void setNegativeType(int negativeType)
	{
		mNegativeType = negativeType;
	}
}
