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
    	    Cursor thoughts = mCalendarDbHelper.fetchThoughts();

    	    	while (thoughts.moveToNext())
    	    	{
    	    		if (thoughts.getString(thoughts.getColumnIndexOrThrow(CalendarDbAdapter.COLUMN_NAME_THOUGHT)).length() > 0 && thoughts.getString(thoughts.getColumnIndexOrThrow(CalendarDbAdapter.COLUMN_NAME_THOUGHT)).charAt(0) == '-')
    	    		{
    	    			negative_thoughts.add(thoughts.getString(thoughts.getColumnIndexOrThrow(CalendarDbAdapter.COLUMN_NAME_THOUGHT)));
    	    			Log.e("negative thought is", "" + negative_thoughts.get(count).toString());
    	    			Log.e("At least", "it is working");
    	    			count++;
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
				if (negative_thoughts.isEmpty() != true)
				{
            	String word = negative_thoughts.get(0);
            	paint.setColor(Color.parseColor("#1E90FF")); 
            	paint.setStyle(Style.FILL); 
            	canvas.drawPaint(paint); 
            	paint.setColor(Color.BLACK); 
            	paint.setTextSize(25); 
            	if (replaced != true)
            	{
            		StaticLayout layout = new StaticLayout(word, paint, 150, Layout.Alignment.ALIGN_NORMAL,1f,0f,true);
            		canvas.translate(x, y);
            		layout.draw(canvas);
            	}
            	else
            	{
            		
            		// didn't set this up yet but here is where new words and such would come from. as it stands now,
            		// the negative words keep getting replaced
            		word = positive_thoughts.get(0);
            		StaticLayout positive_replace_layout = new StaticLayout(word, paint, 150, Layout.Alignment.ALIGN_NORMAL,1f,0f,true);
            		canvas.translate(x, y);
            		positive_replace_layout.draw(canvas);
            	}
        	    
        	    if (add == true)
        	    {
        	    	positive_paint.setColor(Color.WHITE);
        	    	positive_paint.setTextSize(25); 
        	    	StaticLayout positive_layout = new StaticLayout(positive_word, positive_paint, 150, Layout.Alignment.ALIGN_NORMAL,1f,0f,true);

        	    		if (posx <0 && posy < 0)
        	    			{
        	    				posx = this.getWidth();
        	    				posy = this.getBottom() - positive_layout.getHeight();
        	    			}
        	    		
        	    		else if (posx != x && posy != x)
        	    			{
        	    				posx -= (posx/xVelocity);
        	    				posy -= (posy/yVelocity);
        	    		
        	    				if (posx/xVelocity == x && posy/yVelocity == x )
        	    					{
        	    						posx = 0;
        	    						posy = 0;
        	    						x = -100;
        	    						y = -100;
        	    						explode(canvas, posx, posy, paint);
        	    						x = posx;
        	    						y = posy;
        	    					}

        	    			}
        	    
        	    		
        	    		destroy_and_replace(canvas, posx, posy, positive_layout);
        	    		
        	    		if (posx == x && posy == y )
        	    		{
    	    				Log.e("I am here","OK");
        	    			posx = -1;
    	    				posy = -1;
    	    				positive_thoughts.add(positive_word);
    	    				negative_thoughts.remove(0);
    	    				add = false;
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
	    	canvas.translate(posx,posy);
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


