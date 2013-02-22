package com.example.bato;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint.Style;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class AnimatedNegative extends View
{
	
    private Context mContext;
    Resources mRes;
    private Bitmap[] mExplosions = new Bitmap[4];
    private Bitmap cloud;
    private Bitmap gray_cloud;
	int x = 0;
	int y = 0;
	int posx = -1;
	int posy = -1;
	private int xVelocity = 10;
	private int yVelocity = 5;
	private Handler h;
	private final int FRAME_RATE = 30;
    private CalendarDbAdapter mCalendarDbHelper;
    String positive_word;
    TextPaint paint = new TextPaint();
    TextPaint positive_paint = new TextPaint();
    TextPaint game_over = new TextPaint();
    boolean add = false;
    boolean destroyed = false;
    int count = 0;
    ArrayList<String> negative_thoughts = new ArrayList<String>();
    ArrayList<String> positive_thoughts = new ArrayList<String>();
    boolean replaced = false;
    int stored_x = 0;
    int stored_y = 0;



    
    public AnimatedNegative(Context context, AttributeSet attrs)  
    
    {

    		super(context, attrs);
            mContext = this.getContext();
            h = new Handler();
            mCalendarDbHelper=new CalendarDbAdapter(mContext);
    	    mCalendarDbHelper.open();
    	    mExplosions[0] = BitmapFactory.decodeResource(getResources(), R.drawable.asteroid_explode1);
    	    mExplosions[1] = BitmapFactory.decodeResource(getResources(), R.drawable.asteroid_explode2);
    	    mExplosions[2] = BitmapFactory.decodeResource(getResources(), R.drawable.asteroid_explode3);
    	    mExplosions[3] = BitmapFactory.decodeResource(getResources(), R.drawable.asteroid_explode4);
    	    cloud = BitmapFactory.decodeResource(getResources(), R.drawable.cloud);
    	    cloud = cloud.createScaledBitmap(cloud, 150, 150, true);
    	    gray_cloud = BitmapFactory.decodeResource(getResources(), R.drawable.graycloud);
    	    gray_cloud = gray_cloud.createScaledBitmap(gray_cloud, 150, 150, true);

    	    Cursor thoughts = mCalendarDbHelper.fetchThoughts();

    	    	while (thoughts.moveToNext())
    	    	{
    	    		if (thoughts.getString(thoughts.getColumnIndexOrThrow(CalendarDbAdapter.COLUMN_NAME_THOUGHT)).length() > 0 && thoughts.getString(thoughts.getColumnIndexOrThrow(CalendarDbAdapter.COLUMN_NAME_THOUGHT)).charAt(0) == '-')
    	    		{
    	    			negative_thoughts.add(thoughts.getString(thoughts.getColumnIndexOrThrow(CalendarDbAdapter.COLUMN_NAME_THOUGHT)));
    	    		}
    	    	// you win when there is no more space left!!!!
    	   int array_size = negative_thoughts.size();
    	   
    	    	}
    	    
    	  
    	    
    	    
    }    
	
    	private Runnable r= new Runnable() 
    	{
		@Override
		public void run() {
			invalidate(); 
		}
		};
		

            
            protected void onDraw (Canvas canvas)
            {
				if (positive_thoughts.size() < 12)
				{
				

				canvas.drawBitmap(cloud, 0, 0, null);
					
				String word = negative_thoughts.get(0);
            	paint.setColor(Color.parseColor("#1E90FF")); 
            	paint.setStyle(Style.FILL); 
            	canvas.drawPaint(paint); 
            	paint.setColor(Color.BLACK); 
            	paint.setTextSize(25); 
    	    	positive_paint.setColor(Color.parseColor("#FF4444"));
    	    	positive_paint.setTextSize(25); 
            		canvas.drawColor(Color.parseColor("#1E90FF"));
            		canvas.drawBitmap(gray_cloud, x, y, null);
            		StaticLayout layout = new StaticLayout(word, paint, 150, Layout.Alignment.ALIGN_NORMAL,1f,0f,true);
            		canvas.translate(x, y + 5);
            		layout.draw(canvas);
            	
        	    
        	    if (add == true)
        	    {
        	    	StaticLayout positive_layout = new StaticLayout(positive_word, positive_paint, 150, Layout.Alignment.ALIGN_NORMAL,1f,0f,true);
        	    		if (posx <0 && posy < 0)
        	    			{
        	    				posx = this.getWidth();
        	    				posy = this.getBottom() - positive_layout.getHeight();
        	    			}
        	    		
        	    		else if (posx != x && posy != y)
        	    			{
        	    				posx -= (posx/xVelocity);
        	    				posy -= (posy/yVelocity);

        	    				if (posx/xVelocity <= x && posy/yVelocity <= y )
        	    					{
        	    						posx = x;
        	    						posy = y;
        	    						explode(canvas, posx, posy, paint);
        	    						x = posx;
        	    						y = posy;
        	    					}

        	    			}
        	    
        	    		
        	    		destroy_and_replace(canvas, posx, posy, positive_layout);
        	    		
        	    		if (posx == x && posy == y )
        	    		{

    	    				positive_thoughts.add(positive_word);
    	    								// rule is increment 150 for every 3 x increments, then increase y by 150
    	    								// and set x to 0
    	    				count++;
    	    				if (count % 3 == 0)
    	    				{
    	    					y = y + 150;
    	    					x = 0;
    	    				}
    	    				else
    	    				{
    	    					x = x + 150;
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
         
         private void explode(Canvas canvas, int posx, int posy, TextPaint paint)
         {
				for (int i = 0; i < 4; i ++)
				{
					canvas.drawBitmap(mExplosions[i], posx, posy, paint);
					h.postDelayed(r, FRAME_RATE);
				}
         }
       
        private void destroy_and_replace(Canvas canvas, int posx, int posy, Layout positive_layout)
        {
    		canvas.restore();
    		canvas.drawBitmap(cloud, posx, posy, null);
	    	canvas.translate(posx,posy + 10);
	    	positive_layout.draw(canvas);
	    	canvas.restore();
	    	h.postDelayed(r, FRAME_RATE);	
        }
        
        
        private Fragment game_over(Canvas canvas)
        {
	    	game_over.setColor(Color.WHITE);
	    	game_over.setTextSize(50);
	    	canvas.translate(this.getWidth()/2, this.getHeight()/2);
        	StaticLayout game_over_layout = new StaticLayout("Good Job!", game_over, 150, Layout.Alignment.ALIGN_NORMAL,1f,0f,true);
        	game_over_layout.draw(canvas);
        	return new DestroyerView();

        }

}


