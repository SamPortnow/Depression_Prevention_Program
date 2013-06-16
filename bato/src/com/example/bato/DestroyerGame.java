package com.example.bato;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnDragListener;
import android.view.View.OnTouchListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class DestroyerGame extends Activity
{
	PositiveThoughtMissile [] positive = new PositiveThoughtMissile[2];
	Context mContext;
	Bitmap cloud;
	public DestroyerGameView mDestroyerShooter;
    int width;
    int height;
    Canvas canvas;
    RelativeLayout layout;
    Animation in;
    Animation out;
    HashMap<String, int[]> mThoughtInfo = new HashMap<String, int[]>();
    ArrayList<String> mPositives = new ArrayList<String>();
    LinearLayout mPosHolder;
    ImageView cannon;
    ImageView rCannon;
    float slope;
    RelativeLayout.LayoutParams params;
    AnimationSet set;
    AlphaAnimation fade;
    TextView score;
    Score mScore;
    ScaleArrayAdapter arrayAdapter;
    int count;
    Typeface sans;
    NegativeThoughtDestroyer mNeg;
    String mNegThought;
    ArrayList <LaserBeamDestroyer> mLaserBeam = new ArrayList<LaserBeamDestroyer>();
    ArrayList <PositiveThoughtDestroyer> mPositive = new ArrayList<PositiveThoughtDestroyer>();
    PositiveThoughtDestroyer mPositiveThought;
    LaserBeamDestroyer mLaserBeamDraw;
    float x;
    float y;
    ExplodeView mExplosion;
    ArrayList <PositiveThoughtDestroyer> mStationPositive = new ArrayList<PositiveThoughtDestroyer>();
    ListView listView;
    int tracker;
    int mStop;
    //I need a new one every time! not just when the screen reloads! Also, I destroy with the correct thought!!
    
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
	    super.onCreate(savedInstanceState);
	    this.getActionBar().hide();
   	 	sans = Typeface.create("sans-serif-condensed", Typeface.BOLD);
	    mContext = this;
	    setContentView(R.layout.activity_destroyer_shooter);
	    //I will hold the thoughts here
	    mPosHolder = new LinearLayout(mContext);
	    mPosHolder.setOrientation(LinearLayout.HORIZONTAL);
	    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
	    mPosHolder.setLayoutParams(params);
	    //the score, just the text
	    score = (TextView) findViewById(R.id.score);
	    score.setText("SCORE");
	    //get the listview 
	    listView = (ListView) findViewById(R.id.listview);
	    //where most of the animations "happen"
	    mDestroyerShooter = (DestroyerGameView) findViewById(R.id.anim_view);
	    //get the thoughts
	    getTheThoughts();
	    //the part that updates
	    mScore = (Score) findViewById(R.id.score_view);
	    //holds everything!
	    layout = (RelativeLayout) findViewById(R.id.game_view);
	    
	    //the cannon
	    cannon = (ImageView) findViewById(R.id.cannon);
	    //the red cannon. make sure to set it to invisible first
	    rCannon = (ImageView) findViewById(R.id.rcannon);
	    rCannon.setVisibility(View.INVISIBLE);
	    
	    //instructions go here! 
		SharedPreferences preferences = this.getSharedPreferences(this.getPackageName(), Context.MODE_PRIVATE);
		if (preferences.getString("shooter instructions", null) == null)
		{
			
		AlertDialog.Builder builder = new Builder(mContext);
		builder.setTitle("Instructions");
		builder.setMessage("Pick the correct positive thought and shoot down the negative thought!");
		builder.setPositiveButton(android.R.string.ok, null);
		builder.create().show();				
		preferences.edit().putString("shooter instructions", "Yes").commit();
		}
	    //set up the on drag listener for the cannon
		cannon.setOnDragListener(new OnDragListener()
		{

			@Override
			public boolean onDrag(View arg0, DragEvent arg1) 
			{
				switch (arg1.getAction())
				{
					
				case DragEvent.ACTION_DRAG_ENTERED:
					cannon.setVisibility(View.INVISIBLE);
					rCannon.setVisibility(View.VISIBLE);
					break;
				case DragEvent.ACTION_DROP:
						View view = (View) arg1.getLocalState();
						TextView positive = (TextView) view.findViewById(android.R.id.text1);
						//createPositive(positive.getText().toString());
						break;
						
				case DragEvent.ACTION_DRAG_EXITED:
					cannon.setVisibility(View.VISIBLE);
					rCannon.setVisibility(View.INVISIBLE);
				
				}
				
				return true;
			}
			
		});
		
	}
	
	@Override
	 public void onWindowFocusChanged(boolean hasFocus) {
	    super.onWindowFocusChanged(hasFocus);
	    width = mDestroyerShooter.width;
	    height = mDestroyerShooter.height;
	    mExplosion = new ExplodeView(mContext);
	    mDestroyerShooter.setOnTouchListener(new OnTouchListener()
	    {

			@Override
			public boolean onTouch(View arg0, MotionEvent event) {
				int action = event.getActionMasked();
				if (rCannon.getVisibility() == View.VISIBLE)
				{
				if (action == MotionEvent.ACTION_DOWN)
				{
			       mDestroyerShooter.mMoveX = width/3;
			       mDestroyerShooter.mMoveY = height;
			    	//starting from width/3 and going to the touch point. dividing by frame rate
			       mDestroyerShooter.mMoveByX = (Math.round(event.getX()) - (width/3))/30;
			       //going to 0, so just height divided by frame rate
			       mDestroyerShooter.mMoveByY = height/30;
			       mDestroyerShooter.mDestroy = true;
				   rCannon.setVisibility(View.INVISIBLE);
				   cannon.setVisibility(View.VISIBLE);
				   
			       
			}
			       return true;
			}
				else
				{
					return true;
				}
			}
	    });


		//start the game up. the starts by moving the thoughts;
		//so first, get the thoughts, then move them
		getTheThoughts();
		MoveTheThoughts();
}
	public void createPositive(String positive_string)
	{
		mPositive.get(0).setText(positive_string);
	}

	protected void clear(Context context)
	{
		cannon.setVisibility(View.VISIBLE);
	}
	
	protected void update(Context context)
	{
		mScore.fin += count;
	}
	 
	 public void game_over()
	 {
			AlertDialog.Builder builder = new Builder(mContext);
			builder.setTitle("Great Job!");
			builder.setPositiveButton("Play Again", new DialogInterface.OnClickListener()
    		{

				@Override
				public void onClick(DialogInterface dialog, int which) 
				{
					Intent i = new Intent(mContext, DestroyerShooterView.class);				
    				mContext.startActivity(i);	
				}
				
    		});
			builder.setNegativeButton("Go Home", new DialogInterface.OnClickListener()
    		{

				@Override
				public void onClick(DialogInterface dialog, int which) 
				{
					finish();
					Intent i = new Intent(mContext, MainActivity.class);				
    				mContext.startActivity(i);	
				}
				
    		});
			builder.create().show();	
	 }
	   
	 
	public void clearTheThoughts()
	{
		count+=1;
		switch (count)
		{
			case 1: 
				mDestroyerShooter.setBackgroundColor(Color.parseColor("#B0C0C4"));
				break;
			case 2:
				mDestroyerShooter.setBackgroundColor(Color.parseColor("#A0C0C8"));
				break;
			case 3:
				mDestroyerShooter.setBackgroundColor(Color.parseColor("#90C0D2"));
				break;
			case 4:
				mDestroyerShooter.setBackgroundColor(Color.parseColor("#80C0D6"));
				break;
			case 5:
				mDestroyerShooter.setBackgroundColor(Color.parseColor("#70C0E0"));
				break;
			case 6:
				mDestroyerShooter.setBackgroundColor(Color.parseColor("#60C0E4"));
				break;
			case 7:
				mDestroyerShooter.setBackgroundColor(Color.parseColor("#40C0E8"));
				break;
			case 8:
				mDestroyerShooter.setBackgroundColor(Color.parseColor("#20C0F2"));
				break;
			case 9:
				mDestroyerShooter.setBackgroundColor(Color.parseColor("#00C0FF"));
				break;
		}
		mPositives.clear();
		mPositive.clear();
		mThoughtInfo.clear();
		mLaserBeam.clear();
		listView.setAdapter(null);
	}
	 
 	public void getTheThoughts()
 	{
	    CalendarDbAdapter mDbHelper=new CalendarDbAdapter(mContext);
	    mDbHelper.open();
	    Cursor cursor = mDbHelper.fetchNeg();
	    if (cursor.moveToFirst())
	    {	
	    	mNegThought=cursor.getString(cursor.getColumnIndexOrThrow(CalendarDbAdapter.COLUMN_NAME_NEGATIVE_THOUGHT));
	    }
	    
	    cursor.close();
	    Cursor cursorChallenging = mDbHelper.fetchChallenging(mNegThought);
	    	while (cursorChallenging.moveToNext())
	    	{
	    		String positive_thought = cursorChallenging.getString(cursorChallenging.getColumnIndexOrThrow(CalendarDbAdapter.COLUMN_NAME_COUNTER_THOUGHT));
	    		int helpful = cursorChallenging.getInt(cursorChallenging.getColumnIndexOrThrow(CalendarDbAdapter.COLUMN_NAME_COUNTER_THOUGHT_HELPFUL));
	    		int believe = cursorChallenging.getInt(cursorChallenging.getColumnIndexOrThrow(CalendarDbAdapter.COLUMN_NAME_COUNTER_THOUGHT_HELPFUL));
	    		if (! mThoughtInfo.containsKey(positive_thought))
	    			{
	    				mPositives.add(positive_thought);
	    				mThoughtInfo.put(positive_thought, new int[] {helpful, believe});
	    			}
	    	}
	    	cursor.close();
	    	mDbHelper.close();
 	
 	}
 	
 	
 	public void MoveTheThoughts()
 	{
 		if (mThoughtInfo.size() > 4)
 		{
 			mStop = 5;
 		}
 		else
 		{
 			mStop = mThoughtInfo.size();
 		}
 		for (int i = 0; i <  mStop; i++)
 		{
 			mPositiveThought = new PositiveThoughtDestroyer(mContext, i);
 			mPositiveThought.setText("You're DUMB");
 			mPositive.add(mPositiveThought);
 			mLaserBeamDraw = new LaserBeamDestroyer(mContext, i);
 			mLaserBeam.add(mLaserBeamDraw);
 			layout.addView(mLaserBeamDraw);
 		}
		//the thought that will go back and forth
		mNeg = new NegativeThoughtDestroyer(mContext);
		mNeg.setText(mNegThought);
		mDestroyerShooter.mMovePos = true;
 	}
 	
	public void explode()
	{

		RelativeLayout.LayoutParams mExplosionParams = new RelativeLayout.LayoutParams(width/3, height/4);
		mDestroyerShooter.mMoveNeg = false;
		mExplosionParams.leftMargin = mDestroyerShooter.mNegX + listView.getWidth();
		mExplosionParams.topMargin = mPositive.get(0).yPos;
		layout.addView(mExplosion, mExplosionParams);
		mExplosion.explodeIt( mDestroyerShooter.mNegX,mPositive.get(0).yPos);
		
	}
	
	public void stopExplode()
	{
		layout.removeView(mExplosion);
		//remove the explosion, clear the thoughts, get some new ones, and start moving!!
		mStationPositive.add(mPositive.get(0));
		mDestroyerShooter.mDrawPos=true;
		
		clearTheThoughts();
		getTheThoughts();
		MoveTheThoughts();
	}
	
	
	public void populateListView()
	{
		//have to set the explosion back to false
		mDestroyerShooter.explode = false;
		for (int i = 0; i < mStop; i++)
		{
			mLaserBeam.get(i).setVisibility(View.INVISIBLE);
		}
		arrayAdapter = 	new ScaleArrayAdapter(this, R.layout.positives, android.R.id.text1, mPositives);
	    listView.setAdapter(arrayAdapter);
	    AlphaAnimation mGo = new AlphaAnimation(0.0f, 1.0f);
		mGo.setDuration(500);
		mGo.setFillAfter(true);
		mGo.setAnimationListener(new AnimationListener()
		{

			@Override
			public void onAnimationEnd(Animation arg0) 
			{
					new Handler().post(new Runnable() {
				        public void run()
				        {
				    		for (int i = 0; i < mStop; i++ )
				    	    {
				    			layout.removeView(mLaserBeam.get(i));
				    	    }
				        }
				   });
			}

			@Override
			public void onAnimationRepeat(Animation arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onAnimationStart(Animation arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
		listView.startAnimation(mGo);
	}
	 
 
	@Override
	public void onDestroy()
	{
		super.onDestroy();
	}
		
		
}

