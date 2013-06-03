package com.example.bato;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Handler;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class DestroyerShooter extends View
{
	Context mContext; 
	Handler h;
    private ScaleDbAdapter mScaleDbHelper;
    private Bitmap[] mExplosions = new Bitmap[4];
    ArrayList <TextView> positives = new ArrayList<TextView>();
    private Bitmap cloud;
    private Bitmap gray_cloud;
    private Bitmap dark_clouds;
    private Bitmap sun;
    private Bitmap thunder;
    private MediaPlayer thunderPlayer;
    ArrayList<String> negative_thoughts = new ArrayList<String>();
    ArrayList<String> positive_thoughts = new ArrayList<String>();
    boolean first = true;
    boolean move;
    int width;
    int height;
    int array_size;
    int x;
    int FRAME_RATE = 30;
    TextPaint paint = new TextPaint();
    TextPaint positive_paint = new TextPaint();
    TextPaint game_over = new TextPaint();
    TextPaint score = new TextPaint();
    Paint score_background = new Paint();
    TextPaint bonus_paint = new TextPaint();
    TextView positive;
    boolean touched;
    float move_to_x;
    float move_to_y;
    float move_x;
    float move_y;
    int thunder_time;
    int dark_coord_x;
    int dark_coord_y;
    boolean new_negative = true;
    TextView negative;
    String word;
    boolean add;
    int count = 1;
    boolean left_bound;
    boolean right_bound;
    boolean redraw = true; 
    boolean destroyed;
    boolean match;
    float going_x;
    float going_y;
    int place;
    DestroyerShooterView mDestroyer;
    Score mScore;
    boolean clear;
    boolean moveIt;
    int stored_x;
    int stored_y;
    int mTrack = 1;
    boolean hit;
    int speed;
    int index;
    Typeface sans;




	public DestroyerShooter(Context context, AttributeSet attrs) 
	{
		super(context, attrs);
        mContext = this.getContext();
		mDestroyer = (DestroyerShooterView) context;
   	 	sans = Typeface.create("sans-serif-condensed", Typeface.BOLD);
        h = new Handler();
        mScaleDbHelper=new ScaleDbAdapter(mContext);
	    mScaleDbHelper.open();
	    //declare the explosions images as well as the cloud images 
	    mExplosions[0] = BitmapFactory.decodeResource(getResources(), R.drawable.asteroid_explode1);
	    mExplosions[1] = BitmapFactory.decodeResource(getResources(), R.drawable.asteroid_explode2);
	    mExplosions[2] = BitmapFactory.decodeResource(getResources(), R.drawable.asteroid_explode3);
	    mExplosions[3] = BitmapFactory.decodeResource(getResources(), R.drawable.asteroid_explode4);
	    cloud = BitmapFactory.decodeResource(getResources(), R.drawable.cloud);
	    gray_cloud = BitmapFactory.decodeResource(getResources(), R.drawable.graycloud);
	    dark_clouds = BitmapFactory.decodeResource(getResources(), R.drawable.dark_clouds);
	    sun = BitmapFactory.decodeResource(getResources(), R.drawable.sun);
	    thunder = BitmapFactory.decodeResource(getResources(), R.drawable.thunder);
		thunderPlayer = MediaPlayer.create(mContext, R.raw.thunder);
	    Cursor thoughts = mScaleDbHelper.fetchNegs();
	    	    //create a string array of negative thoughts from the db
	    	while (thoughts.moveToNext())
	    	{
	    		if (thoughts.getString(thoughts.getColumnIndexOrThrow(ScaleDbAdapter.COLUMN_NAME_NEGATIVE)).length() > 0)
	    		{
	    			negative_thoughts.add(thoughts.getString(thoughts.getColumnIndexOrThrow(ScaleDbAdapter.COLUMN_NAME_NEGATIVE)));
	    		}
	   
	    	}
	     thoughts.close();
	     array_size = negative_thoughts.size();
	    	 //default positive thougts after a certain amount of time... 
	    	
	    
	    
}    

	private Runnable r= new Runnable() 
	{

		@Override
		public void run() {
			invalidate();
			
		}

	};
	
	
	@Override
	 protected void onSizeChanged(int xNew, int yNew, int xOld, int yOld)
	{
	     super.onSizeChanged(xNew, yNew, xOld, yOld);
	     width = xNew;
	     height = yNew;
	}

	protected void onDraw (Canvas canvas)
    {		
		if (negative_thoughts.size() > 0)
		{
		if (first == true)
    	{ 
    		mExplosions[0] = Bitmap.createScaledBitmap(mExplosions[0], width/2, height/2, true);
    	    mExplosions[1] = Bitmap.createScaledBitmap(mExplosions[1], width/2, height/2, true);
    	    mExplosions[2] = Bitmap.createScaledBitmap(mExplosions[2], width/2, height/2, true);
    	    mExplosions[3] = Bitmap.createScaledBitmap(mExplosions[3], width/2, height/2, true);
    	    cloud = Bitmap.createScaledBitmap(cloud, width/3, height/4, true);
    	    gray_cloud = Bitmap.createScaledBitmap(gray_cloud, width/3, height/4, true);
    	    dark_clouds = Bitmap.createScaledBitmap(dark_clouds, width, height + (height/4), true);
    	    sun = Bitmap.createScaledBitmap(sun, width, height + (height/4), true);
        	//setting my background color...
        	paint.setColor(Color.BLACK); 
        	paint.setTypeface(sans);
        	paint.setTextSize(25); 
        	score.setAntiAlias(true);
        	score.setTextSize(height/25);
    	    score.setColor(Color.CYAN);
    	    score.setShadowLayer(1, 1, 1, Color.RED);
        	score_background.setStyle(TextPaint.Style.FILL);
        	score_background.setColor(Color.WHITE);
        	bonus_paint.setAntiAlias(true);
        	bonus_paint.setTypeface(Typeface.DEFAULT_BOLD);
        	bonus_paint.setTextSize(30);
        	bonus_paint.setColor(Color.WHITE);
        	speed = this.getWidth()/25;
    		canvas.save();
    		first = false;
    	}
		if (redraw == true)
		{
        	move_x = width/3;
        	move_y = height + (height/25);
        	going_x = width/3;
        	going_y = height + (height/25);
        	redraw = false;
		}
    	canvas.drawBitmap(dark_clouds, 0, 0, null);
    	if (thunder_time > 30)
    	{
    		thunder_time = 0;
    	}
    
		if (dark_coord_x < 0)
		{
			speed = speed * -1;
		}
	
	
		if (dark_coord_x > width - (width/3))
		{
			speed = speed * -1;
		}
    
	dark_coord_x += speed;
	
	if (positives.size() > 2)
	{
    	canvas.drawBitmap(sun, 0, dark_coord_y - height, null);

	}
    placeDarkClouds(canvas, dark_coord_x, dark_coord_y, thunder_time);
	
	thunder_time += 1;
	


    
	if (touched == true)
    {
			
			going_x += (move_to_x - move_x)/FRAME_RATE;
	    	going_y -= (move_y - move_to_y)/FRAME_RATE;
			fireCloud(canvas, going_x, going_y);
    }
	
	if (positives.size() > 0)
	{	
	for (int i = 1; i < positives.size() + 1; i++)
	{             				
		canvas.drawBitmap(positives.get(i-1).getDrawingCache(), stored_x, stored_y, null);
		stored_x += width/3;
    	if (i % 3 == 0)
 		{
    		 stored_y += height/4;
 			 stored_x = 0;
 		}

	}
	}
	stored_x = 0;
	stored_y = 0;	
	h.postDelayed(r, FRAME_RATE);
	    
	}
		
	else
	{
		game_over(canvas);
	}

    }
	
	private void placeDarkClouds(Canvas canvas, int dark_coord_x, int dark_coord_y, int thunder_time)
	 {
		if (new_negative == true)
		{
		 negative = new TextView(mContext);
		 Log.e("index is", "" + index);
		 index = (int) (Math.random() * negative_thoughts.size());
		 word = negative_thoughts.get(index);
		 negative.setText(word);
		 negative.layout(0, 0, width/3, height/4);
		 negative.setGravity(Gravity.CENTER);
		 negative.setTextSize(15);
		 negative.setTextColor(Color.BLACK);
    	 negative.setTypeface(Typeface.DEFAULT_BOLD);
    	 negative.setShadowLayer(5, 2, 2, Color.WHITE);
    	 negative.setDrawingCacheEnabled(true);
    	 negative.setBackgroundResource(R.drawable.graycloud);
		}
	 
		 canvas.drawBitmap(negative.getDrawingCache(), dark_coord_x, dark_coord_y, null);
		 if (thunder_time == 0)
		 {
			 canvas.drawBitmap(thunder, dark_coord_x + ((width/3)/4), dark_coord_y + height/4, null);
		 }
		 if(!thunderPlayer.isPlaying() && add == false)
		 {
			 thunderPlayer.start();
		 }
		 new_negative = false;
	 }
	
	private void fireCloud(Canvas canvas, float going_x, float going_y)
	{
    	canvas.drawBitmap(positive.getDrawingCache(), going_x, going_y, null);
    	if (going_y <= height - mDestroyer.cannon.getHeight() && clear == true)
    	{
    		mDestroyer.clear(mContext);
    		clear = false;
    	}
    	if ( going_x >= dark_coord_x - negative.getWidth()/2 && going_x <= dark_coord_x + negative.getWidth()/2 && going_y <= dark_coord_y + negative.getHeight()/2 && going_y >= dark_coord_y - negative.getHeight()/2)
    	{
    	 	if (match == true)
    	 	{
    	 		hit = true;
    	 		for (int i = 0; i < 100; i ++)
    	 		{
    	 			if (i >= 0 && i  < 25)
    	 			{
    	 			canvas.drawBitmap(mExplosions[0], dark_coord_x, dark_coord_y, paint);	
    	 			}
    	 			
    	 			if (i >= 25 && i  < 50)
    	 			{
    	 			canvas.drawBitmap(mExplosions[1], dark_coord_x, dark_coord_y, paint);	
    	 			}
    	 			
    	 			
    	 			if (i >= 50 && i  < 75)
    	 			{
    	 			canvas.drawBitmap(mExplosions[2], dark_coord_x, dark_coord_y, paint);	
    	 			}
    	 			
    	 			
    	 			if (i >= 75 && i  < 100)
    	 			{
    	 			canvas.drawBitmap(mExplosions[3], dark_coord_x, dark_coord_y, paint);	
    	 			}
    	 			
    	 			
    	 		}
    	 		mDestroyer.update(mContext); //update the score!
    	 		//mDestroyer.mSwitchSuccess(); //
    	 		negative_thoughts.remove(index);
    	 		new_negative = true;
    	 		
    	 		positives.add(positive);
    	 		if (positives.size() % 3 == 0)
    	 		{
    	 			dark_coord_y = dark_coord_y + height/4;
    	 		}
    	 		
    	 		if (positives.size() == 9)
    	 		{
    	 			Toast.makeText(mContext, "GREAT JOB! KEEP GETTING THOSE POINTS!!!", Toast.LENGTH_LONG).show();
    	 			dark_coord_y = 0;
    	 			for (int i = 0; i < 9; i++)
    	 			{
    	 				positives.remove(0);
    	 			}
    	 		}
    	 		array_size = negative_thoughts.size();
        	 	dark_coord_x = 0;
        		redraw = true;
        		touched = false;
        		destroyed = true;
        		match = false;
    	 	}
    	 	

    	}
    	
    	
    	if ( going_y <= 0 - negative.getHeight())
    	{
    		match = false;
    		touched = false;
    		redraw = true;
    	}
	}
	
	private void game_over(Canvas canvas)
	{
        	canvas.drawBitmap(sun, 0, 0 , null);
        	game_over.setColor(Color.WHITE);
	    	game_over.setTextSize(50);
	    	canvas.translate(0, height/2);
        	StaticLayout game_over_layout = new StaticLayout("Good Job!", game_over, width, Layout.Alignment.ALIGN_NORMAL,1f,0f,true);
        	game_over_layout.draw(canvas);
        	if (thunderPlayer.isPlaying())
        	{
        		thunderPlayer.stop();
        	
      }
	
	}


}


