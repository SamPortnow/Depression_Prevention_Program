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
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class DestroyerShooterView extends Activity
{
	TextView positive;
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
	    
	    setContentView(R.layout.activity_destroyer_shooter);
	    mDestroyerShooter = (DestroyerShooter) findViewById(R.id.anim_view);
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
	    positive = new TextView(mContext);
	    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(mDestroyerShooter.width/3, mDestroyerShooter.height/4);
	    params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
	    params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
	    positive.layout(0, 0, mDestroyerShooter.width/3, mDestroyerShooter.height/4);
		positive.setGravity(Gravity.CENTER);
		positive.setTextSize(15);
		positive.setTextColor(Color.RED);
		positive.setTypeface(Typeface.DEFAULT_BOLD);
		positive.setShadowLayer(5, 2, 2, Color.YELLOW);
		positive.setDrawingCacheEnabled(true);
		positive.setBackgroundResource(R.drawable.whitecloud);
	    positive.setText(mPositive.get((int) Math.random() * mPositive.size()));
	    positive.setClickable(true);
        positive.setOnTouchListener(gestureListener);
        layout.addView(positive, params);
        LeftToRight = new TranslateAnimation(0, mDestroyerShooter.width/3, 0, 0);
        LeftToRight.setDuration(1000);
        LeftToRight.setFillAfter(true);
	    out.setAnimationListener(new AnimationListener() {

	        @Override
	        public void onAnimationEnd(Animation animation) {
	            positive.startAnimation(in);

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
	    
	    
	    positive.setOnTouchListener(new OnTouchListener()
	    {

			@Override
			public boolean onTouch(View v, MotionEvent event) 
			{
				positive.startAnimation(out);
	            positive.setText(mPositive.get((int) (Math.random() * mPositive.size())));
				return true;
			}
	    	
	    });
	    
	    mDestroyerShooter.setOnTouchListener(new OnTouchListener()
	    {

			@Override
			public boolean onTouch(View arg0, MotionEvent event) {
			       float x = event.getX();
			       float y = event.getY();
			       positive.startAnimation(LeftToRight);
				   rCannon.setVisibility(View.VISIBLE);
				   cannon.setVisibility(View.INVISIBLE);

			       Cursor mMatch = mDbHelper.fetchThought(positive.getText().toString());
			       while (mMatch.moveToNext())
			       {
			    	   mMatchNeg.add(mMatch.getString(mMatch.getColumnIndexOrThrow(ScaleDbAdapter.COLUMN_NAME_NEGATIVE)));
			    	   if (mMatchNeg.contains(mDestroyerShooter.negative.getText().toString()))
			    	   {
	    					mDestroyerShooter.match = true;
			    	   }
			       }
			       mDestroyerShooter.touched = true;
			       mDestroyerShooter.positive = positive;
			       mDestroyerShooter.move_to_x = x;
			       mDestroyerShooter.move_to_y = y;
			       return true;
			       
			}
	    	
	    });



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
}
