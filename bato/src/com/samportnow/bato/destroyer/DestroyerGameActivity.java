package com.samportnow.bato.destroyer;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnDragListener;
import android.view.View.OnTouchListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.samportnow.bato.MainActivity;
import com.samportnow.bato.R;
import com.samportnow.bato.ScaleArrayAdapter;
import com.samportnow.bato.database.CalendarDbAdapter;

public class DestroyerGameActivity extends Activity
{
	HashMap<String, int[]> mThoughtInfo = new HashMap<String, int[]>();
	ArrayList<String> mPositives = new ArrayList<String>();
	Score mScore;
	int count;
	NegativeThoughtDestroyer mNeg;
	String mNegThought;
	ArrayList<LaserBeamDestroyer> mLaserBeam = new ArrayList<LaserBeamDestroyer>();
	ArrayList<PositiveThoughtDestroyer> mPositive = new ArrayList<PositiveThoughtDestroyer>();
	ExplodeView mExplosion;
	ArrayList<PositiveThoughtDestroyer> mStationPositive = new ArrayList<PositiveThoughtDestroyer>();
	int mStop;
	PositiveThoughtDestroyer[] mPosCannon = new PositiveThoughtDestroyer[1];

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		this.getActionBar().hide();
		Context mContext = this;
		setContentView(R.layout.activity_destroyer_shooter);
		// I will hold the thoughts here
		LinearLayout mPosHolder = new LinearLayout(mContext);
		mPosHolder.setOrientation(LinearLayout.HORIZONTAL);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		mPosHolder.setLayoutParams(params);
		// the score, just the text
		TextView score = (TextView) findViewById(R.id.score);
		score.setText("SCORE");

		// get the thoughts
		getTheThoughts();
		// the part that updates
		// holds everything!

		// the cannon
		final ImageView cannon = (ImageView) findViewById(R.id.cannon);
		// the red cannon. make sure to set it to invisible first
		final ImageView rCannon = (ImageView) findViewById(R.id.rcannon);
		rCannon.setVisibility(View.INVISIBLE);

		SharedPreferences preferences = this.getSharedPreferences(this.getPackageName(), Context.MODE_PRIVATE);
		if (preferences.getString("destroyer instructions", null) == null)
		{
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			final Context context = this;
			LayoutInflater inflation = LayoutInflater.from(this);
			LinearLayout lLayout = (LinearLayout) inflation.inflate(R.layout.custom_xml, null);
			TextView instructions = (TextView) lLayout.findViewById(R.id.instructions);
			instructions.setTextColor(Color.BLUE);
			instructions.setText("Here, you can destroy your negative thoughts");
			builder.setView(lLayout);
			builder.setPositiveButton("Next", new android.content.DialogInterface.OnClickListener()
			{

				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					AlertDialog.Builder builder = new AlertDialog.Builder(context);
					LayoutInflater inflation = LayoutInflater.from(context);
					LinearLayout lLayout = (LinearLayout) inflation.inflate(R.layout.custom_xml, null);
					TextView instructions = (TextView) lLayout.findViewById(R.id.instructions);
					instructions.setTextColor(Color.BLUE);
					instructions.setText("A list of the all the thoughts you came up with to challenge your negative thoughts will display on the left.");
					builder.setView(lLayout);
					builder.setPositiveButton("Next", new android.content.DialogInterface.OnClickListener()
					{

						@Override
						public void onClick(DialogInterface dialog, int which)
						{
							AlertDialog.Builder builder = new AlertDialog.Builder(context);
							LayoutInflater inflation = LayoutInflater.from(context);
							LinearLayout lLayout = (LinearLayout) inflation.inflate(R.layout.custom_xml, null);
							TextView instructions = (TextView) lLayout.findViewById(R.id.instructions);
							instructions.setTextColor(Color.BLUE);
							instructions.setText("Your task is to drag a challenging thought into the cannon and tap the screen where you want the challenging thought to go. " + "If it hits the negative thought, great job! You destroyed it. If it doesn't, no worries," + " try again. The game is over when the screen is filled with challenging thoughts!");
							builder.setView(lLayout);
							builder.setPositiveButton("Next", new android.content.DialogInterface.OnClickListener()
							{

								@Override
								public void onClick(DialogInterface dialog, int which)
								{
									AlertDialog.Builder builder = new AlertDialog.Builder(context);
									LayoutInflater inflation = LayoutInflater.from(context);
									LinearLayout lLayout = (LinearLayout) inflation.inflate(R.layout.custom_xml, null);
									TextView instructions = (TextView) lLayout.findViewById(R.id.instructions);
									instructions.setTextColor(Color.BLUE);
									instructions.setText("Try to remember which thoughts you believe, and which ones " + "make you feel good. You'll get extra points for those thoughts!");
									builder.setView(lLayout);
									builder.setPositiveButton("Next", new android.content.DialogInterface.OnClickListener()
									{

										@Override
										public void onClick(DialogInterface dialog, int which)
										{
											final Dialog sketchDialog = new Dialog(context);
											sketchDialog.setTitle("An Example, Click to Play!");
											sketchDialog.setContentView(R.layout.custom_destroyer_dialog);
											ImageView mSketch = (ImageView) sketchDialog.findViewById(R.id.sketch_destroyer);
											mSketch.setOnClickListener(new OnClickListener()
											{

												@Override
												public void onClick(View arg0)
												{
													sketchDialog.dismiss();
												}

											});
											sketchDialog.show();
										}

									});
									builder.create().show();
								}
							});
							builder.create().show();
						}

					});
					builder.create().show();

				}

			});
			builder.create().show();

			// preferences.edit().putString("capture instructions",
			// "Yes").commit();
			// change this after the presentation
		}
		// set up the on drag listener for the cannon
		cannon.setOnDragListener(new OnDragListener()
		{

			@Override
			public boolean onDrag(View arg0, DragEvent arg1)
			{
				switch (arg1.getAction())
				{

					case DragEvent.ACTION_DRAG_ENTERED:
						findViewById(R.id.cannon).setVisibility(View.INVISIBLE);
						findViewById(R.id.rcannon).setVisibility(View.VISIBLE);
						break;
					case DragEvent.ACTION_DROP:
						View view = (View) arg1.getLocalState();
						TextView positive = (TextView) view.findViewById(android.R.id.text1);
						createPositive(positive.getText().toString(), arg0.getContext());
						break;

					case DragEvent.ACTION_DRAG_EXITED:
						findViewById(R.id.cannon).setVisibility(View.VISIBLE);
						findViewById(R.id.rcannon).setVisibility(View.INVISIBLE);

				}

				return true;
			}

		});

	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus)
	{
		super.onWindowFocusChanged(hasFocus);
		final DestroyerGameView mDestroyerShooter = (DestroyerGameView) findViewById(R.id.anim_view);
		final int width = mDestroyerShooter.width;
		final int height = mDestroyerShooter.height;
		// initalize our exploding view!
		mExplosion = new ExplodeView(this);

		mDestroyerShooter.setOnTouchListener(new OnTouchListener()
		{

			@Override
			public boolean onTouch(View arg0, MotionEvent event)
			{
				int action = event.getActionMasked();
				if (findViewById(R.id.rcannon).getVisibility() == View.VISIBLE)
				{
					if (action == MotionEvent.ACTION_DOWN)
					{
						mDestroyerShooter.mMoveX = width / 3;
						mDestroyerShooter.mMoveY = height;
						// starting from width/3 and going to the touch point.
						// dividing by frame rate
						mDestroyerShooter.mMoveByX = (Math.round(event.getX()) - (width / 3)) / 30;
						// going to 0, so just height divided by frame rate
						mDestroyerShooter.mMoveByY = height / 30;
						mDestroyerShooter.mDestroy = true;
						findViewById(R.id.rcannon).setVisibility(View.INVISIBLE);
						findViewById(R.id.cannon).setVisibility(View.VISIBLE);

					}
					return true;
				}
				else
				{
					return true;
				}
			}
		});

		// start the game up. the starts by moving the thoughts;
		// so first, get the thoughts, then move them
		getTheThoughts();
		MoveTheThoughts();
	}

	public void createPositive(String positive_string, Context mContext)
	{
		PositiveThoughtDestroyer mPos = new PositiveThoughtDestroyer(mContext, 0);
		mPos.setText(positive_string);
		mPosCannon[0] = mPos;

	}

	protected void clear(Context context)
	{
		findViewById(R.id.cannon).setVisibility(View.VISIBLE);
	}

	protected void update(int score_update)
	{
		Score mScore = (Score) findViewById(R.id.score_view);
		mScore.fin += score_update;
	}

	public void game_over()
	{
		AlertDialog.Builder builder = new Builder(this);
		final Context mContext = this;
		builder.setTitle("Great Job!");
		builder.setPositiveButton("Play Again", new DialogInterface.OnClickListener()
		{

			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				Intent i = new Intent(mContext, DestroyerGameActivity.class);
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
		count += 1;
		DestroyerGameView mDestroyerShooter = (DestroyerGameView) findViewById(R.id.anim_view);

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
		mThoughtInfo.clear();
		mLaserBeam.clear();
		ListView lView = (ListView) findViewById(R.id.listview);
		lView.setAdapter(null);
	}

	public void getTheThoughts()
	{
		CalendarDbAdapter mDbHelper = new CalendarDbAdapter(this);
		mDbHelper.open();
		Cursor cursor = mDbHelper.fetchNeg();
		mNegThought = null;
		if (cursor.moveToFirst())
		{
			mNegThought = cursor.getString(cursor.getColumnIndexOrThrow(CalendarDbAdapter.COLUMN_NAME_NEGATIVE_THOUGHT));
		}
		cursor.close();
		Cursor cursorChallenging = mDbHelper.fetchChallenging(mNegThought);
		while (cursorChallenging.moveToNext())
		{
			String positive_thought = cursorChallenging.getString(cursorChallenging.getColumnIndexOrThrow(CalendarDbAdapter.COLUMN_NAME_COUNTER_THOUGHT));
			int helpful = cursorChallenging.getInt(cursorChallenging.getColumnIndexOrThrow(CalendarDbAdapter.COLUMN_NAME_COUNTER_THOUGHT_HELPFUL));
			int believe = cursorChallenging.getInt(cursorChallenging.getColumnIndexOrThrow(CalendarDbAdapter.COLUMN_NAME_COUNTER_THOUGHT_HELPFUL));
			if (!mThoughtInfo.containsKey(positive_thought))
			{
				mPositives.add(positive_thought);
				mThoughtInfo.put(positive_thought, new int[] { helpful, believe });
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
		for (int i = 0; i < mStop; i++)
		{
			PositiveThoughtDestroyer mPositiveThought = new PositiveThoughtDestroyer(this, i);
			mPositiveThought.setText(mPositives.get(i).toString());
			mPositive.add(mPositiveThought);
			LaserBeamDestroyer mLaserBeamDraw = new LaserBeamDestroyer(this, i);
			mLaserBeam.add(mLaserBeamDraw);
			RelativeLayout rLayout = (RelativeLayout) findViewById(R.id.game_view);
			rLayout.addView(mLaserBeamDraw);
		}
		// the thought that will go back and forth
		mNeg = new NegativeThoughtDestroyer(this);
		mNeg.setText(mNegThought);
		DestroyerGameView mDestroyerShooter = (DestroyerGameView) findViewById(R.id.anim_view);
		mDestroyerShooter.mMovePos = true;
	}

	public void explode()
	{
		DestroyerGameView mDestroyerShooter = (DestroyerGameView) findViewById(R.id.anim_view);
		RelativeLayout.LayoutParams mExplosionParams = new RelativeLayout.LayoutParams(mDestroyerShooter.width / 3, mDestroyerShooter.height / 4);
		mDestroyerShooter.mMoveNeg = false;
		ListView list = (ListView) findViewById(R.id.listview);
		mExplosionParams.leftMargin = mDestroyerShooter.mNegX + list.getWidth();
		mExplosionParams.topMargin = 0;
		RelativeLayout rLayout = (RelativeLayout) findViewById(R.id.game_view);
		rLayout.addView(mExplosion, mExplosionParams);
		mExplosion.explodeIt(mDestroyerShooter.mNegX, 0);

	}

	public void stopExplode()
	{
		RelativeLayout rLayout = (RelativeLayout) findViewById(R.id.game_view);
		rLayout.removeView(mExplosion);
		// remove the explosion, clear the thoughts, get some new ones, and
		// start moving!!
		DestroyerGameView mDestroyerShooter = (DestroyerGameView) findViewById(R.id.anim_view);
		mDestroyerShooter.mDrawPos = true;
		mStationPositive.add(mPosCannon[0]);
		int mHelp_mBelieve[] = mThoughtInfo.get(mPosCannon[0].getText().toString());
		if (mHelp_mBelieve[0] > 3 && mHelp_mBelieve[1] > 3)
		{
			update(50);
		}
		else
		{
			update(25);
		}
		clearTheThoughts();
		getTheThoughts();
		MoveTheThoughts();
	}

	public void populateListView()
	{
		// have to set the explosion back to false
		DestroyerGameView mDestroyerShooter = (DestroyerGameView) findViewById(R.id.anim_view);
		mDestroyerShooter.explode = false;
		for (int i = 0; i < mStop; i++)
		{
			mLaserBeam.get(i).setVisibility(View.INVISIBLE);
		}
		ScaleArrayAdapter arrayAdapter = new ScaleArrayAdapter(this, R.layout.positives, android.R.id.text1, mPositives);
		ListView listView = (ListView) findViewById(R.id.listview);
		listView.setAdapter(arrayAdapter);
		AlphaAnimation mGo = new AlphaAnimation(0.0f, 1.0f);
		mGo.setDuration(500);
		mGo.setFillAfter(true);
		mGo.setAnimationListener(new AnimationListener()
		{

			@Override
			public void onAnimationEnd(Animation arg0)
			{
				new Handler().post(new Runnable()
				{
					public void run()
					{
						for (int i = 0; i < mStop; i++)
						{
							RelativeLayout rLayout = (RelativeLayout) findViewById(R.id.game_view);
							rLayout.removeView(mLaserBeam.get(i));
						}
					}
				});
			}

			@Override
			public void onAnimationRepeat(Animation arg0)
			{
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationStart(Animation arg0)
			{
				// TODO Auto-generated method stub

			}

		});
		findViewById(R.id.listview).startAnimation(mGo);
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
	}
}