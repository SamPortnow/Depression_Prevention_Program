package com.samportnow.bato.coping.floating;

import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.WindowManager;

import com.samportnow.bato.R;
import com.samportnow.bato.database.BatoDataSource;
import com.samportnow.bato.database.dao.ThoughtDao;

public class FloatActivity extends Activity
{
	int height;
	int width;
	Thought [] mThoughts;
	
	private List<String> mChallengingThoughtContents = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		BatoDataSource dataSource = new BatoDataSource(this).open();
		mChallengingThoughtContents = dataSource.getAllChallengingThoughtContent();
		
		dataSource.close();
		
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
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
		Random random = new Random();	
		
		if (mChallengingThoughtContents.isEmpty() || random.nextFloat() > 0.66)
		{
			BatoDataSource dataSource = new BatoDataSource(this).open();
			ThoughtDao thought = dataSource.getRandomThought();
			
			dataSource.close();
	
			if (thought.getNegativeType() < 0 || thought.getCopingStrategy() != null)
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
		}
		else
		{
			// FIXME: make this different for challenging thoughts.
			mThought.setBackgroundResource(R.drawable.whitecloud);
			mThought.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
			
			mThought.setText(mChallengingThoughtContents.get(random.nextInt(mChallengingThoughtContents.size())));
		}
		
		int randomNum = random.nextInt(3);
		mThought.xPos = -width/2;
		mThought.yPos = (height/4) * randomNum;
	}
	
	public Thought createNew()
	{
		return new Thought(this);
	}
	

}
