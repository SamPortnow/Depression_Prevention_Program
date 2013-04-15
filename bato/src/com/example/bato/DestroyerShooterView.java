package com.example.bato;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
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
    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
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
    
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
	    super.onCreate(savedInstanceState);
	    this.getActionBar().hide();
	    mContext = this;
	    mDbHelper=new ScaleDbAdapter(mContext);
	    mDbHelper.open();
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
	    mScore = (Score) findViewById(R.id.score_view);
	    layout = (RelativeLayout) findViewById(R.id.game_view);
	    cannon = (ImageView) findViewById(R.id.cannon);
	    rCannon = (ImageView) findViewById(R.id.rcannon);
	    rCannon.setVisibility(View.INVISIBLE);
	    in = new AlphaAnimation(0.0f, 1.0f);
	    in.setDuration(300);
	    out = new AlphaAnimation(1.0f, 0.0f);
	    out.setDuration(300);
        gestureDetector = new GestureDetector(this, new MyGestureDetector());
        gestureListener = new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        };

	    
	    
	}
	
	@Override
	 public void onWindowFocusChanged(boolean hasFocus) {
	    super.onWindowFocusChanged(hasFocus);
	    positive[0] = new TextView(mContext);
	    Log.e("what the crap!", "DAWG");
	    Log.e("mdestroyer", "" + mDestroyerShooter.width);
	    params = new RelativeLayout.LayoutParams(mDestroyerShooter.width/3, mDestroyerShooter.height/4);
	    params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
	    params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
	    positive[0].layout(0, 0, mDestroyerShooter.width/3, mDestroyerShooter.height/4);
		positive[0].setGravity(Gravity.CENTER);
		positive[0].setTextSize(15);
		positive[0].setTextColor(Color.RED);
		positive[0].setTypeface(Typeface.DEFAULT_BOLD);
		positive[0].setShadowLayer(5, 2, 2, Color.YELLOW);
		positive[0].setDrawingCacheEnabled(true);
		positive[0].setBackgroundResource(R.drawable.whitecloud);
	    positive[0].setText(mPositive.get((int) (Math.random() * mPositive.size())));
	    positive[0].setClickable(true);
        positive[0].setOnTouchListener(gestureListener);
        layout.addView(positive[0], params);
        LeftToRight = new TranslateAnimation(0, mDestroyerShooter.width/3, 0, 0);
        LeftToRight.setDuration(1000);
        LeftToRight.setFillEnabled(true);
        LeftToRight.setFillAfter(true);
        set = new AnimationSet(true);
	    fade = new AlphaAnimation(1.0f, 0.0f);
	    fade.setDuration(300);
        set.addAnimation(LeftToRight);
        set.addAnimation(fade);
        set.setFillEnabled(true);
        set.setFillAfter(true);
	    out.setAnimationListener(new AnimationListener() {

	        @Override
	        public void onAnimationEnd(Animation animation) {
	            positive[0].startAnimation(in);

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
	    
	    
	    positive[0].setOnTouchListener(new OnTouchListener()
	    {

			@Override
			public boolean onTouch(View v, MotionEvent event) 
			{
				int action = event.getActionMasked();
				if (action == MotionEvent.ACTION_DOWN)
				{
				positive[0].startAnimation(out);
				if (mDestroyerShooter.match == false)
				{
	            positive[0].setText(mPositive.get((int) (Math.random() * mPositive.size())));
				}
				}
				return true;
			}
	    	
	    });
	    
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
		layout.removeView(positive[0]);
	    positive[0].setText(mPositive.get((int) (Math.random() * mPositive.size())));
		layout.addView(positive[0], params);
	}
	
	protected void update(Context context)
	{
		mScore.update = true;
	}


	   class MyGestureDetector extends SimpleOnGestureListener {
		   
	        @Override
	        public boolean onDown(MotionEvent e) 
	        {
	            return true;
	        }
	        
	        @Override
	        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
	            try {
	                if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
	                    return false;
	                // right to left swipe
	                if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) 
	                {
	                    Toast.makeText(mContext, "Left Swipe", Toast.LENGTH_SHORT).show();
	                }  
	                else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) 
	                {
	        	    	width = mDestroyerShooter.width;
	        	    	height = mDestroyerShooter.height;
	                	mDestroyerShooter.move = true;
	                }
	            } catch (Exception e) {
	                // nothing
	            }
	            return false;
	          
	        }


	    }
	   
	   
		@Override
		public void onDestroy()
		{
			super.onDestroy();
			mDbHelper.close();
		}
}
