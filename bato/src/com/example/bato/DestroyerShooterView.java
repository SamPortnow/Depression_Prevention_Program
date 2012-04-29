package com.example.bato;

import java.util.ArrayList;

import com.mobeta.android.dslv.DragSortListView;
import com.mobeta.android.dslv.DragSortListView.DropListener;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class DestroyerShooterView extends Activity
{
	TextView [] positive = new TextView[2];
	Context mContext;
	Bitmap cloud;
	private DestroyerShooter mDestroyerShooter;
    private GestureDetector gestureDetector;
    View.OnTouchListener gestureListener;
    int width;
    int height;
    Canvas canvas;
    RelativeLayout layout;
    Animation in;
    Animation out;
    TranslateAnimation LeftToRight;
    ScaleDbAdapter mDbHelper;
    GameDbAdapter mGameDbHelper;
    ArrayList<String> mPositive = new ArrayList<String>();
    ArrayList <String> mMatchNeg = new ArrayList<String>();
    ImageView cannon;
    ImageView rCannon;
    float slope;
    RelativeLayout.LayoutParams params;
    AnimationSet set;
    AlphaAnimation fade;
    TextView score;
    Score mScore;
    ArrayAdapter<String> arrayAdapter;
    
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
	    super.onCreate(savedInstanceState);
	    this.getActionBar().hide();
	    mContext = this;
	    mDbHelper=new ScaleDbAdapter(mContext);
	    mDbHelper.open();
	    mGameDbHelper = new GameDbAdapter(mContext);
	    mGameDbHelper.open();
	    Cursor cursor = mDbHelper.fetchPositives();
	    	while (cursor.moveToNext())
	    	{
	    		mPositive.add(cursor.getString(cursor.getColumnIndexOrThrow(ScaleDbAdapter.COLUMN_NAME_POSITIVE)));
	    	}
	    cursor.close();
	    setContentView(R.layout.activity_destroyer_shooter);
	    score = (TextView) findViewById(R.id.score);
	    score.setText("SCORE");
	    mDestroyerShooter = (DestroyerShooter) findViewById(R.id.anim_view);
	    arrayAdapter = new ArrayAdapter<String>(this, R.layout.positives, android.R.id.text1, mPositive);
	    DragSortListView listView = (DragSortListView) findViewById(R.id.listview);
	    listView.setDropListener(new DropListener()
	    {

			@Override
			public void drop(int from, int to) 
			{
				String item=arrayAdapter.getItem(from);
				arrayAdapter.remove(item);
				arrayAdapter.insert(item, to);
				if (to == (mPositive.size() - 1))
				{
					mSwitch(item);
				}
			}


	    	
	    });
	    listView.setAdapter(arrayAdapter);
	    mScore = (Score) findViewById(R.id.score_view);
	    layout = (RelativeLayout) findViewById(R.id.game_view);
	    cannon = (ImageView) findViewById(R.id.cannon);
	    rCannon = (ImageView) findViewById(R.id.rcannon);
	    rCannon.setVisibility(View.INVISIBLE);
        LeftToRight = new TranslateAnimation(0, mDestroyerShooter.width/2, 0, 0);
        LeftToRight.setDuration(1000);
        LeftToRight.setFillEnabled(true);
        LeftToRight.setFillAfter(true);
        set = new AnimationSet(true);
        fade = new AlphaAnimation(1.0f, 0.0f);
        fade.setDuration(300);
        set.addAnimation(LeftToRight);
        set.addAnimation(fade);
        set.setFillAfter(false);
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
	    
	}
	
	@Override
	 public void onWindowFocusChanged(boolean hasFocus) {
	    super.onWindowFocusChanged(hasFocus);
	    positive[0] = (TextView) findViewById(R.id.positive);
	    params = new RelativeLayout.LayoutParams(mDestroyerShooter.width/3, mDestroyerShooter.height/4);
	    positive[0].layout(0, 0, mDestroyerShooter.width/3, mDestroyerShooter.height/4);
		positive[0].setGravity(Gravity.CENTER);
		positive[0].setTextSize(15);
		positive[0].setTextColor(Color.RED);
		positive[0].setTypeface(Typeface.DEFAULT_BOLD);
		positive[0].setShadowLayer(5, 2, 2, Color.YELLOW);
		positive[0].setDrawingCacheEnabled(true);
		positive[0].setBackgroundResource(R.drawable.whitecloud);
	    positive[0].setText(mPositive.get((int) (Math.random() * mPositive.size())));
	     
	    mDestroyerShooter.setOnTouchListener(new OnTouchListener()
	    {

			@Override
			public boolean onTouch(View arg0, MotionEvent event) {
				int action = event.getActionMasked();
				if (action == MotionEvent.ACTION_DOWN)
				{
			       float x = event.getX();
			       float y = 0;
			       positive[1] = new TextView (mContext);
			       positive[1].layout(0, 0, mDestroyerShooter.width/3, mDestroyerShooter.height/4);
				   positive[1].setGravity(Gravity.CENTER);
				   positive[1].setTextSize(15);
				   positive[1].setTextColor(Color.RED);
				   positive[1].setTypeface(Typeface.DEFAULT_BOLD);
				   positive[1].setShadowLayer(5, 2, 2, Color.YELLOW);
				   positive[1].setDrawingCacheEnabled(true);
				   positive[1].setBackgroundResource(R.drawable.whitecloud);
				   positive[1].setText(positive[0].getText().toString());
				   positive[0].startAnimation(set);
				   rCannon.setVisibility(View.VISIBLE);
				   cannon.setVisibility(View.INVISIBLE);

			       Cursor mMatch = mDbHelper.fetchThought(positive[1].getText().toString());
			       while (mMatch.moveToNext())
			       {
			    	   mMatchNeg.add(mMatch.getString(mMatch.getColumnIndexOrThrow(ScaleDbAdapter.COLUMN_NAME_NEGATIVE)));
			    	   if (mMatchNeg.contains(mDestroyerShooter.negative.getText().toString()))
			    	   {
	    					mDestroyerShooter.match = true;
	    					if (mDestroyerShooter.match == true)
	    					{
	    						break;
	    					}

			    	   }
			       }
			       
			       mMatch.close();
			       mDestroyerShooter.touched = true;
			       mDestroyerShooter.clear = true;
			       
			       mDestroyerShooter.positive = positive[1];
			       mDestroyerShooter.move_to_x = x;
			       mDestroyerShooter.move_to_y = y;
			       
			}
			       return true;

			}
	    });



}

	protected void clear(Context context)
	{
		rCannon.setVisibility(View.GONE);
		cannon.setVisibility(View.VISIBLE);
	}
	
	protected void update(Context context)
	{
		mScore.update = true;
	}
	
	protected void updateDb()
	{
		mGameDbHelper.createGame(mScore.count);
	}



	   
	   public void mSwitch(String item)
	   {
		   positive[0].setText(item);
	   }
	   
	   public void mSwitchSuccess()
	   {
		   String item=arrayAdapter.getItem(mPositive.size() - 1);
		   arrayAdapter.remove(item);
		   arrayAdapter.remove(item);
	   }
	   
		@Override
		public void onDestroy()
		{
			super.onDestroy();
			updateDb();
			mDbHelper.close();
			mGameDbHelper.close();
		}
}
