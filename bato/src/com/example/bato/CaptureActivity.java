package com.example.bato;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
 
public class CaptureActivity extends Activity
{
	
	private static Set<String> mNegativeWords;
	TextView mind_reading;
	TextView fortunetelling; 
	TextView catastrophizing;
	TextView labeling;
	TextView dark_glasses;
	TextView discounting_positives;
	TextView black_and_white_thinking;
	TextView overgeneralizing;
	TextView personalizing;
	TextView shoulds;
	TextView unfair_comparisons;
    HorizontalScrollView train;
    LinearLayout mEditTextContainer;
    RelativeLayout container;
    NegativeThought mNeg;
    PositiveThought [] mPos = new PositiveThought[4];
	LaserBeam [] mLaserBeam = new LaserBeam[4];
	AlphaAnimation mGone;
	AlphaAnimation mGo;
	int width;
	BattleField mBattle;
	Context mContext;
	int mPosCounter;
	LinearLayout mPosHolder;
	AutoCompleteTextView mChallengingThought;
	String inputLine;
	private String[] inputTokens;
	private Pattern four_letter_words = Pattern.compile("not|cant|cnt|can't"); 
	Button mCreateThought;
	LayoutInflater inflater;
	AlphaAnimation mGoAddLaser;
	TranslateAnimation mGameOver;
	CalendarDbAdapter mCalHelper;
	View view;
	EndGame mEndGame;
	String mChallenging;
	int countBounds;
	
	public static boolean populatePositiveWords(Context context)
	{
		mNegativeWords = new HashSet<String>();
		
		
		try
		{
			BufferedReader reader = new BufferedReader(new InputStreamReader(context.getAssets().open("negative_words.txt")));			
			String line = reader.readLine();
			
			while (line != null)
			{
				mNegativeWords.add(line.toLowerCase(Locale.US));
				line = reader.readLine();
			}
			
			reader.close();
		}
		catch (IOException exception)
		{
			return false;
		}
		
		return true;
		//TODO list of negative words 
	}
	
	

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
	    this.getActionBar().hide();
	    mContext = this;
	    //layout inflater
	    inflater = LayoutInflater.from(this);
	    populatePositiveWords(mContext);
	    //set content view
	    setContentView(R.layout.activity_capture);
	    //the autocompletetextview for creating challenging thoughts
	    mChallengingThought = (AutoCompleteTextView) findViewById(R.id.thoughts);
	    //the relativelayout that will contain the "dancing" cloud
	    container = (RelativeLayout) findViewById(R.id.container);
	    //the scroll that contains the train that will jump into the view
	    //width of the view
		Display display = this.getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		//need width to animate the train. it needs to moved off the screen
		width = size.x;
	    train = (HorizontalScrollView) findViewById(R.id.scroller);
	    //the battle field!
	    mBattle = (BattleField) findViewById(R.id.battle_field);
	    //the layout that contains the edit text and button, set to invisible at the begining
	    mEditTextContainer = (LinearLayout) findViewById(R.id.edit_container);
		//setup the animation for the train coming in
		TranslateAnimation mSlide = new TranslateAnimation(width, 0, 0, 0);
		//game over translate animation
		mGameOver = new TranslateAnimation(0, width, 0, 0);
		mGameOver.setFillAfter(true);
		mGameOver.setDuration(2000);
		mSlide.setDuration(100);
		mSlide.setFillAfter(true);
		train.setAnimation(mSlide);
		train.startAnimation(mSlide);
		//the animation listener that will remove the train
		AnimationListener remove = new AnimationListener()
		{

			@Override
			public void onAnimationEnd(Animation arg0) 
			{
				container.removeView(train);
			}

			@Override
			public void onAnimationRepeat(Animation arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onAnimationStart(Animation arg0) {
				// TODO Auto-generated method stub
				
			}
			
		};
		//set up the other animations
		mGone = new AlphaAnimation(1.0f, 0.0f);
		mGone.setAnimationListener(remove);
		mGo = new AlphaAnimation(0.0f, 1.0f);
		mGone.setDuration(2000);
		mGone.setFillAfter(true);
		mGo.setDuration(2000);
		mGoAddLaser = new AlphaAnimation(0.0f, 1.0f);
		mGoAddLaser.setFillAfter(true);
		mGoAddLaser.setDuration(2000);
		//layout that will contain the positive thoughts
		mPosHolder = (LinearLayout) findViewById(R.id.pos_container);
		//onclick listener for each member of the train! woof!
		mind_reading = (TextView) findViewById(R.id.mind_reading);
		fortunetelling = (TextView) findViewById(R.id.fortune_telling);
		catastrophizing = (TextView) findViewById(R.id.catastrophizing);
		labeling = (TextView) findViewById(R.id.labeling);
		dark_glasses = (TextView) findViewById(R.id.dark_glass);
		discounting_positives = (TextView) findViewById(R.id.discounting_positives);
		black_and_white_thinking = (TextView) findViewById(R.id.black_and_white_thinking);
		overgeneralizing = (TextView) findViewById(R.id.overgeneralizing);
		personalizing = (TextView) findViewById(R.id.personalizing);
		shoulds = (TextView) findViewById(R.id.shoulds);
		unfair_comparisons = (TextView) findViewById(R.id.unfair_comparisons);
		mCreateThought = (Button) findViewById(R.id.scale_it);
		//disable the button after 4 thoughts
		if (mPosCounter == 4)
		{
			mCreateThought.setClickable(false);
		}
		//set an onclicklistener for the create button
		//make sure that there are no neg words, negating statements
		mCreateThought.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0) 
			{
				inputLine = mChallengingThought.getText().toString();
				inputTokens = inputLine.split(" ");
				
				if (inputLine.isEmpty())
				{
					Toast.makeText(mContext, "You have to write something!", Toast.LENGTH_SHORT).show();
					return;					
				}
				
				if (inputTokens.length < 3)
				{
					Toast.makeText(mContext, "At least three words are required.", Toast.LENGTH_SHORT).show();
					return;
				}				
				
				if (four_letter_words.matcher(inputLine).find() == true)
				{
					Toast.makeText(mContext, "Make an affirmative statement!", Toast.LENGTH_SHORT).show();
					return;
				}
				
				boolean matchesToken = false;
				
				for (int i = 0; i < inputTokens.length; i++)
				{
					String token = inputTokens[i];
					if (mNegativeWords.contains(token.toLowerCase(Locale.US)))
					{
						matchesToken = true;
						break;
					}
				}
				
				if (matchesToken == true)
				{
					Toast.makeText(mContext, "Use positive words!", Toast.LENGTH_SHORT).show();
					return;
				}
	    		
				else
				{
					mCreateThought.setClickable(false);
					mChallenging = mChallengingThought.getText().toString();
					mPos[mPosCounter] = new PositiveThought(mContext, null, inputLine);
					mLaserBeam[mPosCounter] = new LaserBeam(mContext, mPosCounter);
					mPos[mPosCounter].setText(inputLine);
					mPosHolder.addView(mPos[mPosCounter]);
					mGoAddLaser.setAnimationListener(new AnimationListener()
					{

						@Override
						public void onAnimationEnd(Animation arg0) 
						{
							AlertDialog.Builder build_believe = new AlertDialog.Builder(mContext);	
							view =  inflater.inflate(R.layout.believe_dialog, null);
							build_believe.setView(view);
							build_believe.setTitle("Rate your thought");
							build_believe.setPositiveButton("OK", new DialogInterface.OnClickListener()
							{

								@Override
								public void onClick(DialogInterface dialog,
										int which) 
								{
									CalendarDbAdapter mCalHelper = new CalendarDbAdapter(mContext);
									mCalHelper.open();
									int belief = ((SeekBar) view.findViewById(R.id.believe)).getProgress();
									int helpful = ((SeekBar) view.findViewById(R.id.help)).getProgress();
									mCalHelper.createChallenging(mNeg.getText().toString(), 
											mChallenging, belief, helpful);
									mCalHelper.close();
									container.addView(mLaserBeam[mPosCounter]);
									mLaserBeam[mPosCounter].startAnimation(mGo);
									mBattle.xLessBound += mBattle.container_width/12;
									if (mBattle.x  < mBattle.xLessBound)
									{
										mBattle.boundLess = true;
									}
									if (mBattle.x > mBattle.xGreatBound)
									{
										mBattle.boundGreat = true;
									}
									mBattle.xGreatBound -= mBattle.container_width/12;
									
									if (mBattle.xVelocity < 0)
									{
										mBattle.xVelocity += 2;
									}
									else
									{
										mBattle.xVelocity -=2;

									}
									if (mBattle.yVelocity < 0)
									{
										mBattle.yVelocity +=2;
									}
									else
									{
										mBattle.yVelocity -=2;

									}
									mCreateThought.setClickable(true);
									mPosCounter+=1;
									if (mPosCounter == 4)
									{
										//mPosHolder.startAnimation(mGameOver);
										mBattle.xVelocity = 5;
										for (int i=0; i <4; i++)
										{
											mPosHolder.removeView(mPos[i]);
											mLaserBeam[i].mGameOver = true;
										}
										mEndGame = new EndGame(mContext);
										mPosHolder.addView(mEndGame);
										mBattle.mGameOver = true;
									}
									else
									{
										Toast.makeText(mContext, "Great job! Come up with another thought!", Toast.LENGTH_SHORT).show();

									}
									}

							
							});
							build_believe.create().show();
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
					mGo.setFillAfter(true);
					mGo.setDuration(2000);
					mPos[mPosCounter].startAnimation(mGoAddLaser);
					mChallengingThought.setText(null);
					InputMethodManager imm = (InputMethodManager)mContext.getSystemService(
					      Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(mChallengingThought.getWindowToken(), 0);
				
				}
			}
		});

		OnClickListener mStartGameListener = new OnClickListener()
			{

				@Override
				public void onClick(View v) 
				{

					CalendarDbAdapter mCalHelper = new CalendarDbAdapter(mContext);
					mCalHelper.open();
					Cursor cursor = mCalHelper.fetchNegsByType(((TextView) v).getText().toString());
					if (cursor.moveToFirst())
					{
						String mNegThought = cursor.getString(cursor.getColumnIndexOrThrow(CalendarDbAdapter.COLUMN_NAME_NEGATIVE_THOUGHT));
						//the thought that will dance on the screen
						mNeg = new NegativeThought(mContext);
						mNeg.setText(mNegThought);
					
					cursor.close();
					mCalHelper.close();
					train.startAnimation(mGone);
					mEditTextContainer.setVisibility(View.VISIBLE);
					mEditTextContainer.startAnimation(mGo);
					//should set button clickable here
					}
					else
					{
						Toast.makeText(mContext, "No negative thoughts in this train", Toast.LENGTH_SHORT).show();
					}	
				}
				
			};
		mind_reading.setOnClickListener(mStartGameListener);
		fortunetelling.setOnClickListener(mStartGameListener);
		catastrophizing.setOnClickListener(mStartGameListener);
		labeling.setOnClickListener(mStartGameListener);
		dark_glasses.setOnClickListener(mStartGameListener);
		discounting_positives.setOnClickListener(mStartGameListener);
		black_and_white_thinking.setOnClickListener(mStartGameListener);
		overgeneralizing.setOnClickListener(mStartGameListener);
		personalizing.setOnClickListener(mStartGameListener);
		shoulds.setOnClickListener(mStartGameListener);
		unfair_comparisons.setOnClickListener(mStartGameListener);
	}

	public void endGame()
	{
        mPosHolder.removeView(mEndGame);
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Great Job!");
        builder.setNegativeButton("Go Home", new DialogInterface.OnClickListener()
         			{

						@Override
						public void onClick(DialogInterface dialog,int which)
						{
							{
								finish();
								Intent i = new Intent(mContext, MainActivity.class);				
	            				mContext.startActivity(i);	
							}

						}

            		
            		});
            		
            		builder.setPositiveButton("Play again!", new DialogInterface.OnClickListener()
            		{
            			@Override
            			public void onClick(DialogInterface dialog, int which)
            			{
            				Intent i = new Intent(mContext, CaptureActivity.class);				
            				mContext.startActivity(i);
            			}
            			
            		});			
            		
            		builder.create().show();
	}



}
