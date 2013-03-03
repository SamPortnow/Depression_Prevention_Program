package com.example.bato;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.Fragment;
import android.content.Context;
import android.content.res.Resources;
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

public class AnimatedNegative extends View
{
	
    private Context mContext;
    Resources mRes;
    private Bitmap[] mExplosions = new Bitmap[4];
    private Bitmap cloud;
    private Bitmap gray_cloud;
    private Bitmap positive_text;
    private Bitmap dark_clouds;
    private Bitmap sun;
    private Bitmap thunder;
	int x = 0;
	int y = 0;
	int posx = -1;
	int posy = -1;
	private Handler h;
	private final int FRAME_RATE = 1000;
    private CalendarDbAdapter mCalendarDbHelper;
    String positive_word;
    TextPaint paint = new TextPaint();
    TextPaint positive_paint = new TextPaint();
    TextPaint game_over = new TextPaint();
    TextPaint score = new TextPaint();
    boolean add = false;
    boolean destroyed = false;
    int count = 0;
    int move = 0;
    int cloud_marker;
    int thunder_struck;
    ArrayList<String> negative_thoughts = new ArrayList<String>();
    ArrayList<String> positive_thoughts = new ArrayList<String>();
    boolean replaced = false;
    int stored_x = 0;
    int stored_y = 0;
    boolean positive_draw = false;
    boolean first = true;
    TextView positive;
    TextView negative;
    final int array_size;
    boolean new_negative = true;
    String word;
    boolean moved_forward;
    boolean starting = true;
    boolean moved_back;
    private MediaPlayer thunderPlayer;
    boolean typing;
    int thunder_time = 0;
    long start_mills;
    long current_mills;
    long rt;
    boolean start = true; 
    long scorer;
    long tracker;
    String write_tracker;
    TextView scorekeep;
    int width;
    int height;
    Paint score_background = new Paint();
    int bonus_x = -1;
    int bonus_y = -1;
    boolean bonus;
    TextPaint bonus_paint = new TextPaint();

    //word bank of positive words to check against 

    
    public AnimatedNegative(Context context, AttributeSet attrs)  
    
    {

    		super(context, attrs);
            mContext = this.getContext();
            h = new Handler();
            mCalendarDbHelper=new CalendarDbAdapter(mContext);
    	    mCalendarDbHelper.open();

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
    	    Cursor thoughts = mCalendarDbHelper.fetchThoughts();

    	    //create a string array of negative thoughts from the db
    	    	while (thoughts.moveToNext())
    	    	{
    	    		if (thoughts.getString(thoughts.getColumnIndexOrThrow(CalendarDbAdapter.COLUMN_NAME_THOUGHT)).length() > 0 && thoughts.getString(thoughts.getColumnIndexOrThrow(CalendarDbAdapter.COLUMN_NAME_THOUGHT)).charAt(0) == '-')
    	    		{
    	    			negative_thoughts.add(thoughts.getString(thoughts.getColumnIndexOrThrow(CalendarDbAdapter.COLUMN_NAME_THOUGHT)));
    	    		}
    	   
    	    	}
    	     negative_thoughts.add("I'm confused");
    	     negative_thoughts.add("I am wasting my life.");
    	     negative_thoughts.add("I'll end up living all alone.");
    	     negative_thoughts.add("People don't consider friendship important anymore.");
    	     negative_thoughts.add("That was a dumb thing for me to do (or say)");
    	     negative_thoughts.add("Life has no meaning.");
    	     negative_thoughts.add("I'm ugly.");
    	     negative_thoughts.add("Nobody loves me.");
    	     negative_thoughts.add("I'll never find what I really want.");
    	     negative_thoughts.add("I am worthless.");
    	     negative_thoughts.add("It's all my fault.");
    	     negative_thoughts.add("Why do so many bad things happen to me?");
    	     negative_thoughts.add("I can't think of anything that would be fun.");
    	     negative_thoughts.add("I don't have what it takes to be successful.");
    	     negative_thoughts.add("Things are so messed up that doing anything about them is useless.");
    	     negative_thoughts.add("I don't have enough willpower.");
    	     negative_thoughts.add("I wish I had never been born.");
    	     negative_thoughts.add("Things are just going to get worse and worse.");
    	     negative_thoughts.add("I wonder if they are talking about me.");
    	     negative_thoughts.add("No matter how hard I try, people aren't satisfied.");
    	     negative_thoughts.add("I'll never make any good friends.");
    	     negative_thoughts.add("I can�t do anything right.");
    	     negative_thoughts.add("Things will never work out for me.");
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
		
	 private Runnable s = new Runnable()
	 {

		@Override
		public void run() {
			
			invalidate();
		}
		 
	 };

            
			protected void onDraw (Canvas canvas)
            {
            
            	//save the canvas on the first draw
            	if (first == true)
            	{ 
                	mExplosions[0] = Bitmap.createScaledBitmap(mExplosions[0], this.getWidth()/2, this.getHeight()/2, true);
            	    mExplosions[1] = Bitmap.createScaledBitmap(mExplosions[1], this.getWidth()/2, this.getHeight()/2, true);
            	    mExplosions[2] = Bitmap.createScaledBitmap(mExplosions[2], this.getWidth()/2, this.getHeight()/2, true);
            	    mExplosions[3] = Bitmap.createScaledBitmap(mExplosions[3], this.getWidth()/2, this.getHeight()/2, true);
            	    cloud = Bitmap.createScaledBitmap(cloud, this.getWidth()/3, this.getHeight()/4, true);
            	    gray_cloud = Bitmap.createScaledBitmap(gray_cloud, this.getWidth()/3, this.getHeight()/4, true);
            	    dark_clouds = Bitmap.createScaledBitmap(dark_clouds, this.getWidth(), this.getHeight() * 2, true);
            	    sun = Bitmap.createScaledBitmap(sun, this.getWidth(), this.getHeight() + this.getHeight()/3, true);
                	//setting my background color...
                	paint.setColor(Color.BLACK); 
                	paint.setTextSize(25); 
                	//setting the paint for the positive word, within the positive cloud
        	    	positive_paint.setColor(Color.parseColor("#FF4444"));
               	 	positive_paint.setShadowLayer(5, 2, 2, Color.YELLOW);
        	    	positive_paint.setTextSize(25);
                	score.setAntiAlias(true);
                	score.setTypeface(Typeface.DEFAULT_BOLD);
                	score.setTypeface(Typeface.SANS_SERIF);
                	score.setTextSize(24);
                	score.setColor(Color.BLUE);
                	score_background.setStyle(TextPaint.Style.FILL);
                	score_background.setColor(Color.WHITE);
                	bonus_paint.setAntiAlias(true);
                	bonus_paint.setTypeface(Typeface.DEFAULT_BOLD);
                	bonus_paint.setTypeface(Typeface.SANS_SERIF);
                	bonus_paint.setTextSize(24);
                	bonus_paint.setColor(Color.WHITE);
            		canvas.save();
            		first = false;
            	}
            	canvas.drawBitmap(dark_clouds, 0, 0, null);
            	if (cloud_marker < 3)
            	{
            	canvas.drawBitmap(sun, 0, y - this.getHeight() - this.getHeight()/3, null);
            	}
            	else
            	{
                	canvas.drawBitmap(sun, 0, y - this.getHeight(), null);

            	}

            	if (tracker < scorer)
            	{
            		tracker += 3;
            		write_tracker = Long.toString(tracker);
            		canvas.drawRect(0, 0, this.getWidth(), this.getHeight()/25, score_background);
            		canvas.drawText("SCORE " + write_tracker, this.getWidth()/3, this.getHeight()/25,  score);
          	     }
            	else
            	{
            		tracker = scorer;
            		write_tracker = Long.toString(tracker);
            		canvas.drawRect(0, 0, this.getWidth(), this.getHeight()/25, score_background);
            		canvas.drawText("SCORE " + write_tracker, this.getWidth()/3, this.getHeight()/25,  score);



            	}
            	//when the size of the array of positive thoughts is 12, there is no more room,
            	//and you have won the game!
            	if (positive_thoughts.size() < 9)
				{

            		if (move == 0 || move == 2)
        	    		{
        	    		move +=1; 
        	    		}
        	    	
        	    		if (move == 1)
        	    		{
        	    		move +=1;
        	    		x += (this.getWidth()/10)/(FRAME_RATE/50);
        	    			
        	    		}
        	    	
        	    	
        	    		if (move == 3)
        	    		{
        	    			move = 0;
            	    		x -= (this.getWidth()/10)/(FRAME_RATE/50);
        	    			
        	    		}
        	    		
            	    	if (thunder_time == 0 || thunder_time == 10)
            	    	{
            	    		thunder_struck = x;

            	    	}
            	    	
            	    	else if (thunder_time > 0 && thunder_time < 10 )
            	    	{
            	    		thunder_struck = this.getWidth();

            	    	}
                	    
            	    	else if (thunder_time > 10)
            	    	{
            	    		thunder_time = 0;
            	    	}
            	    	
        	    		thunder_time += 1;

            	    
        			
            	    place_all_clouds(canvas, x, y + this.getHeight()/25, thunder_struck);

        	    	
        	    	

            	//function to place all clouds (negative and positive)


    	    	
        	    if (add == true) // if the button is clicked in DestroyerView.java, then this becomes true, and the 
        	    	//movement occurs
        	    {
        	    		
        	    		if (bonus == true)
        	    		{
        	    			if (bonus_x < 0 || bonus_x >=this.getWidth())
        	    			{
        	    				bonus_x = 0;
        	    				bonus_y = (this.getHeight());
        	    			}
        	    			
        	    			else if (bonus_x  < this.getWidth())
        	    			{
        	    				bonus_x += (this.getWidth())/(FRAME_RATE/100);
        	    				bonus_y -= (this.getHeight())/(FRAME_RATE/100);
        	    				canvas.drawText("BONUS POINTS!", bonus_x, bonus_y, bonus_paint);
        	    			}
        	    			
        	    			else 
        	    			{
        	    				bonus = false;
        	    			}
    	    				Log.e("bonus x is", ""+ bonus_x);
    	    				Log.e("bonus y is", ""+ bonus_y);

        	    		}
        	    		//set positive_draw to true so that the positive clouds can be drawn 
        	    		positive_draw = true;
        	    		//create a static layout for my positive word and cloud
        	    		StaticLayout positive_layout = new StaticLayout(positive_word, positive_paint, this.getWidth()/3, Layout.Alignment.ALIGN_CENTER,1f,0f,true);
        	    		//change the coordinates of the positive cloud
        	    		if (posx <0 && posy < 0)
        	    			{
        	    				posx = this.getWidth();
        	    				posy = (this.getBottom() - 175);
        	    			}
        	    		
        	    		else if (posx != x && posy != y)
        	    			{
        	    				posx -= (this.getWidth() - x)/(FRAME_RATE/100);
        	    				posy -= (this.getHeight() - y)/(FRAME_RATE/100);
        	    				if (posx <= x || posy <= y)
        	    					{
        	    						posx = x;
        	    						posy = y;
        	    						//if we reach the coordinates of the dark cloud, it explodes
											explode(canvas, posx, posy + this.getHeight()/25, paint);

										
        	    						x = posx;
        	    						y = posy;
        	    					}
        	    				

        	    			}
        	    		//this is the function that sets the positive cloud in motion
        	    		destroy_and_replace(canvas, posx, posy + this.getHeight()/25, positive_layout);
        	    		if (posx == x && posy == y )
        	    		{
    	    				//at the end, add to the array
        	    			positive_thoughts.add(positive_word);
        	    			new_negative = true;

    	    								// rule is increment 150 for every 3 x increments, then increase y by 150
    	    								// and set x to 0
    	    				count++;
    	    				cloud_marker++;
    	    				if (count % 3 == 0) //move the position of the dark cloud
    	    				{
    	    					y = y + this.getHeight()/4;
    	    					x = 0;
    	    				}
    	    				else
    	    				{
    	    					x = x + this.getWidth()/3;
    	    				}
    	    				add = false;
    	        			posx = -1;
    	    				posy = -1;
        	    		}

        	    }

				}
				else
				{
					game_over(canvas);
					
				}
        	    
            }	
        	
         
         private void place_all_clouds(Canvas canvas, int x, int y, int thunder_struck)
         {
      		place_dark_clouds(canvas, x, y, thunder_struck);
        	 if (positive_draw == true)
        	 {	       		
        		 //place your positive clouds. done within a foor loop
        		 for (int i = 1; i < positive_thoughts.size() + 1; i++)
         			{
 

             				place_clouds(canvas, stored_x, stored_y + this.getHeight()/25, i);
        					stored_x += this.getWidth()/3;
               		 		if (i % 3 == 0)
            				{
               		 			stored_y += this.getHeight()/4;
            					stored_x = 0;
            				}

         			}
         			stored_x = 0;
         			stored_y = 0;
         			
        	 	}
         }
            
         private void place_dark_clouds(Canvas canvas, int x, int y, int thunder_struck)
         {
     	     //style the dark clouds
        	 if (new_negative == true)
        	 {
        		 word = negative_thoughts.get((int) (Math.random() * array_size));
        	 }
        	 negative = new TextView(mContext);
        	 negative.setText(word);
        	 negative.layout(0, 0, this.getWidth()/3, this.getHeight()/4);
        	 negative.setGravity(Gravity.CENTER);
        	 negative.setTextSize(15);
        	 negative.setTextColor(Color.BLACK);
        	 negative.setShadowLayer(5, 2, 2, Color.WHITE);
        	 negative.setDrawingCacheEnabled(true);
        	 negative.setBackgroundResource(R.drawable.graycloud);
       		 canvas.drawBitmap(negative.getDrawingCache(), x, y, null);
       		 canvas.drawBitmap(thunder, thunder_struck + (negative.getWidth()/4), y + negative.getHeight(), null);
       		 if (start == true)
       		 {
       			Calendar starting = Calendar.getInstance();
       			 start_mills = starting.getTimeInMillis();
       			 start = false;
       		 }
       		 if(!thunderPlayer.isPlaying())
       		 {
 	    		thunderPlayer.start();
       		 }
       		 new_negative = false;
 	    	h.postDelayed(r, FRAME_RATE);

         }
       
         private void place_clouds(Canvas canvas, int stored_x, int stored_y, int i)
         {
     	     //style the positive clouds 
        	 positive = new TextView(mContext);
        	 positive.setText(positive_thoughts.get(i-1));
        	 positive.layout(0, 0, this.getWidth()/3, this.getHeight()/4);
        	 positive.setGravity(Gravity.CENTER);
        	 positive.setTextSize(15);
        	 positive.setTextColor(Color.RED);
        	 positive.setShadowLayer(5, 2, 2, Color.YELLOW);
        	 positive.setDrawingCacheEnabled(true);
        	 positive.setBackgroundResource(R.drawable.cloud);
			 canvas.drawBitmap(positive.getDrawingCache(), stored_x, stored_y, null);

         }
         
         
         private void explode(Canvas canvas, int posx, int posy, TextPaint paint) 
         {
				//destroy the dark cloud
        	 	for (int i = 0; i < 4; i ++)
				{
        	 		canvas.drawBitmap(mExplosions[i], posx, posy, paint);
				}
        	 	
        	 	start = true;
        	 	
         }
       
        
         
        private void destroy_and_replace(Canvas canvas, int posx, int posy, Layout positive_layout)
        {
        	//move the positive cloud until it reaches teh dark cloud
        	canvas.drawBitmap(cloud, posx, posy, null);
	    	canvas.translate(posx,posy);
	    	positive_layout.draw(canvas);
	    	h.postDelayed(r, FRAME_RATE);
	    	canvas.restore();

        }
        
        
        private Fragment game_over(Canvas canvas)
        {
        	canvas.drawBitmap(sun, 0, 0 , null);
        	game_over.setColor(Color.WHITE);
	    	game_over.setTextSize(50);
	    	canvas.translate(0, this.getHeight()/2);
        	StaticLayout game_over_layout = new StaticLayout("Good Job!", game_over, this.getWidth(), Layout.Alignment.ALIGN_NORMAL,1f,0f,true);
        	game_over_layout.draw(canvas);
        	return new DestroyerView();

        }

}


