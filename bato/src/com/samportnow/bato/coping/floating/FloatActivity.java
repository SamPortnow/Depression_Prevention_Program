package com.samportnow.bato.coping.floating;

import java.util.Random;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;

import com.samportnow.bato.R;
import com.samportnow.bato.database.BatoDataSource;
import com.samportnow.bato.database.dao.ThoughtDao;

public class FloatActivity extends Activity
{
	int height;
	int width;
	Thought [] mThoughts;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		width = size.x;
		height = size.y;
		mThoughts = new Thought [3];
		for (int i=0; i < 3; i++)
		{
			mThoughts[i] = new Thought(this);
			reset(mThoughts[i]);
			mThoughts[i].xPos = -(width/2) * i;
			mThoughts[i].yPos = (height/4) * i;
		}
		this.setContentView(R.layout.activity_float);
	}
	
	public void reset(Thought mThought)
	{
		Random rand = new Random();
		
		BatoDataSource dataSource = new BatoDataSource(this).open();
		ThoughtDao thought = dataSource.getRandomThought();
		
		dataSource.close();

		if (thought.getNegativeType() < 0)
		{
			mThought.setBackgroundResource(R.drawable.whitecloud);
			mThought.setTextColor(Color.RED);			
		}
		else
		{
			mThought.setBackgroundResource(R.drawable.graycloud);
			mThought.setTextColor(Color.BLACK);
		}
		
		mThought.setText(thought.getContent());
		int randomNum = rand.nextInt(3);
		mThought.xPos = -width/2;
		mThought.yPos = (height/4) * randomNum;
	}
	
	public Thought createNew()
	{
		return new Thought(this);
	}
	

}
