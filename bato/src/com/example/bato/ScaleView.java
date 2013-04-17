package com.example.bato;

import java.io.IOException;
import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.text.TextPaint;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class ScaleView extends View
{
	Context mContext;
	private Bitmap[] mScale= new Bitmap[72];
	private Bitmap mBag;
	Bitmap mGreenBag;
    ArrayList<TextView> mPositive = new ArrayList<TextView>();
	TextPaint positive_paint = new TextPaint();
	Handler h;
	int FRAME_RATE = 30;
	int width;
	int height;
	boolean first = true;
	TextView negative;
    CalendarDbAdapter mCalendarDbHelper;
    ArrayList<String> negative_thoughts = new ArrayList<String>();
    int array_size;
    String word;
    boolean scale_it = false;
    int i = 4;
    int j;
    int mMoveNeg;
    int tracker;
    float[] mMoveXPos = new float[4];
    float[] mMoveYPos = new float[4];
    float[] mMoveByXPos = new float[4];
    float[] mMoveByYPos = new float[4];
    float[] mCurrentXPos = new float[4];
    float[] mCurrentYPos = new float[4];
    float moveX;
    float moveY;
    float currentX;
    float currentY;
    float draw_em;
    boolean mMoveScale;
    boolean game_over;
    boolean game_game_over;
    boolean sStop;
    boolean mDrag;
    boolean clear;
    Button fire;
    Button question;
    EditText positive_thoughts;
    Button Skip;
    ScaleActivity mScaleView;
    ScaleActivity nScaleView;
    RelativeLayout layout;
	boolean firstPlace;
	boolean secondPlace;
	boolean thirdPlace;
	boolean fourthPlace;
   
    
    
	public ScaleView(Context context) 
	{
		super(context);
		width = this.getWidth();
		height = this.getHeight();
		mContext = this.getContext();
		mScaleView = (ScaleActivity) context;
    	negative = new TextView(mContext);
        h = new Handler();
        mCalendarDbHelper=new CalendarDbAdapter(mContext);
        mCalendarDbHelper.open();
	    Cursor thoughts = mCalendarDbHelper.fetchThoughts();

	    //create a string array of negative thoughts from the db
	    	while (thoughts.moveToNext())
	    	{
	    		if (thoughts.getString(thoughts.getColumnIndexOrThrow(CalendarDbAdapter.COLUMN_NAME_THOUGHT)).length() > 0 && thoughts.getString(thoughts.getColumnIndexOrThrow(CalendarDbAdapter.COLUMN_NAME_THOUGHT)).charAt(0) == '-')
	    		{
	    			negative_thoughts.add(thoughts.getString(thoughts.getColumnIndexOrThrow(CalendarDbAdapter.COLUMN_NAME_THOUGHT)));
	    		}
	   
	    	}
	     thoughts.close();
	     array_size = negative_thoughts.size();
	     mBag =BitmapFactory.decodeResource(getResources(), R.drawable.bag);
	     mGreenBag = BitmapFactory.decodeResource(getResources(), R.drawable.green_bag);
	     
		for (int i = 0; i < 72; i ++)
		{
			try
			{
			mScale[i] = BitmapFactory.decodeStream(context.getAssets().open("scale_"+i+".gif"));
			}
			catch (IOException e) 
			{
		    
			}
		}
	}
	
	
	private Runnable r= new Runnable() 
	{

		@Override
		public void run() {
			invalidate();
			
		}

	};
	
	@Override
	protected void dispatchDraw (Canvas canvas)
    {
		super.onDraw(canvas);
		if (first == true)
		{
			width = this.getWidth();
			height = this.getHeight();
			mScale[i] = Bitmap.createScaledBitmap(mScale[i], (int) (width * 1.5), height, true);
			mBag = Bitmap.createScaledBitmap(mBag, width/2, height/2, true);
			mGreenBag = Bitmap.createScaledBitmap(mBag, width/4, height/4, true);
    		word = negative_thoughts.get((int) (Math.random() * array_size));
    		negative.setText(word);
        	negative.layout(0, 0, width/3, height/4);
        	negative.setGravity(Gravity.CENTER);
        	negative.setTextSize(15);
        	negative.setTextColor(Color.BLACK);
         	negative.setTypeface(Typeface.DEFAULT_BOLD);
        	negative.setShadowLayer(5, 2, 2, Color.WHITE);
        	negative.setDrawingCacheEnabled(true);
        	negative.setBackgroundResource(R.drawable.graycloud);
	    	positive_paint.setColor(Color.parseColor("#FF4444"));
       	 	positive_paint.setShadowLayer(5, 2, 2, Color.YELLOW);
        	positive_paint.setTypeface(Typeface.DEFAULT_BOLD);
	    	positive_paint.setTextSize(25);
	    	mCurrentXPos[0] = (width/2);
	    	mCurrentYPos[0] = height/4 + 71 * (height/150);
	    	mCurrentXPos[1] = (width/2) + (width/8);
	    	mCurrentYPos[1] = height/6 + 71 * (height/150);
	    	mCurrentXPos[2] = width/2;
	    	mCurrentYPos[2] = height/12 + 71 * (height/150);
	    	mCurrentXPos[3] = (width/2) + (width/8);
	    	mCurrentYPos[3] = height/18 + 71 * (height/150);
	    	mMoveXPos[0] = ((width/2) - width)/FRAME_RATE;
	    	mMoveYPos[0] = ((height/4) - (height + (height/4)))/FRAME_RATE;
	    	mMoveXPos[1] = (((width/2) + (width/8)) - width)/ FRAME_RATE;
	    	mMoveYPos[1] = ((height/6) - (height))/FRAME_RATE;
	    	mMoveXPos[2] = ((width/2) - width)/ FRAME_RATE;
	    	mMoveYPos[2] = ((height/12) - (height))/FRAME_RATE;
	    	mMoveXPos[3] = (((width/2) + (width/8)) - width)/ FRAME_RATE;
	    	mMoveYPos[3] = ((height/18) - (height))/FRAME_RATE;
	    	mMoveByXPos[0] = -(width/2)/ FRAME_RATE;
	    	mMoveByYPos[0] = -((height/4 + 71 * (height/150)))/FRAME_RATE;
	    	mMoveByXPos[1] =  ((width - (width/3)) - (width/2 + (width/8)))/ FRAME_RATE;
	    	mMoveByYPos[1] = -((height/6 + 71 * (height/150)))/FRAME_RATE;
	    	mMoveByXPos[2] = - (width/2)/ FRAME_RATE;
	    	mMoveByYPos[2] =  ((height - (height/4)) - ((height/12) + 71 * (height/150)))/FRAME_RATE;
	    	mMoveByXPos[3] =  ((width - (width/3)) - (width/2 + (width/8)))/ FRAME_RATE;
	    	mMoveByYPos[3] =  ((height - (height/4)) - ((height/18) + 71 * (height/150)))/FRAME_RATE;
	    	currentX = width;
	    	currentY = height + height/4;
			first = false;
		}
		
		if (game_over == false)
		{
			canvas.drawBitmap(mScale[i], 0 - (width/4), 0, null);
			canvas.drawBitmap(negative.getDrawingCache(),(int) (width/12), (int) (height - (height)/2.5) - (j), null);
		}
		
		else
		{
			canvas.drawBitmap(mBag, width/4, height/4, null);
		}
		
		if (mMoveScale == true)
		{
			i++;
			j+= (height/150);
			ScaleIt(canvas, i);
			if (i == 21 || i == 37 || i == 53 || i == 71)
			{
				mMoveScale = false;
			}
			if (i == 71)
			{
				game_over = true;
			}
			
		}
		
		if (tracker > 0)
		{
			if (tracker == 1)
			{
				if (currentX > width/2 && currentY > height/4 && sStop == false)
				{
					currentX += mMoveXPos[0]; 
					currentY += mMoveYPos[0];
					canvas.drawBitmap(mPositive.get(tracker -1 ).getDrawingCache(), currentX, currentY, null);

				}
				
				else 
				{
			    	if (sStop == false)
			    	{
			    		
			    		
			    		mMoveScale = true;
			    		sStop = true;
			    		currentX = width;
			    		currentY = height + height/4;
			    		draw_em++;
			    	}
						
				}
			}
			
			
			if (tracker == 2)
			{
				
				if (currentX > width/2 + (width/8) && currentY > (height/6) && sStop == false)
				{
					currentX += mMoveXPos[1]; 
					currentY += mMoveYPos[1];
					canvas.drawBitmap(mPositive.get(tracker -1 ).getDrawingCache(), currentX, currentY, null);

				}
				
				else
				{
			    	if (sStop == false)
			    	{
			    		mMoveScale = true;
			    		sStop = true;
			    		currentX = width;
			    		currentY = height + height/4;
			    		draw_em++;
			    	}
				}
			}
			
			
			if (tracker == 3)
			{
				
				if (currentX > width/2 && currentY > height/12 && sStop == false)
				{
					currentX += mMoveXPos[2]; 
					currentY += mMoveYPos[2];
					canvas.drawBitmap(mPositive.get(tracker -1 ).getDrawingCache(), currentX, currentY, null);

				}
				
				else 
				{
					if (sStop == false)
					{
				    	mMoveScale = true;
				    	sStop = true;
						currentX = width;
				    	currentY = height + height/4;
				    	draw_em++;
					}
				}
			}
			
			if (tracker == 4)
			{
				
				if (currentX > width/2 + (width/8) && currentY > (height/18) && sStop == false)
				{
					currentX += mMoveXPos[3]; 
					currentY += mMoveYPos[3];
					canvas.drawBitmap(mPositive.get(tracker -1 ).getDrawingCache(), currentX, currentY, null);
				}
				
				else 
				{
						if (sStop == false)
						{
					    mMoveScale = true;
					    sStop = true;
						currentX = width;
				    	currentY = height + height/4;
				    	draw_em++;
		        		
						}
				}
			}
			
			
			if (draw_em > 0 && game_over == false)
			{
				
				for (int i = 0; i < draw_em; i ++)
				{

				if (i == 0)
				{
					canvas.drawBitmap(mPositive.get(i).getDrawingCache(), width/2, height/4 + j, null);
				}
				if (i == 1)
				{
					canvas.drawBitmap(mPositive.get(i).getDrawingCache(), width/2 + (width/8), height/6 + j, null);

				}
				if (i == 2)
				{
					canvas.drawBitmap(mPositive.get(i).getDrawingCache(), width/2, height/12 + j, null);
				
	
				}
				if (i == 3)
				{
					canvas.drawBitmap(mPositive.get(i).getDrawingCache(), width/2 + (width/8), height/18 + j, null);
				}
				
			}
				
			}
			
			else if (game_over == true)
			{
				if (firstPlace == true && secondPlace == true && thirdPlace == true && fourthPlace == true)
				{
					mScaleView.clear(mContext);
				}
				
				for (int i = 0; i < draw_em; i++)
				{
					
					if (i == 0 && mCurrentXPos[0] > 0 && mCurrentYPos[0] >  0)
					{
						mCurrentXPos[0] += mMoveByXPos[0];
						mCurrentYPos[0] += mMoveByYPos[0];
						canvas.drawBitmap(mPositive.get(i).getDrawingCache(), mCurrentXPos[0], mCurrentYPos[0], null);
					}
					
					else if (i == 0 && mCurrentXPos[0] <= 0 || mCurrentYPos[0] <=  0)
					{ 
						canvas.drawBitmap(mPositive.get(0).getDrawingCache(), 0, 0, null);
						firstPlace = true;
					}
					
					
					if (i == 1 && mCurrentXPos[1] < (width - (mPositive.get(i).getWidth())) && mCurrentYPos[1] > 0)
					{
						mCurrentXPos[1] += mMoveByXPos[1];
						mCurrentYPos[1] += mMoveByYPos[1];
						canvas.drawBitmap(mPositive.get(i).getDrawingCache(), mCurrentXPos[1], mCurrentYPos[1], null);

					}
					
					else if (i == 1 && mCurrentXPos[1] >= (width - (mPositive.get(i).getWidth())) || mCurrentYPos[1] <=  0)
					{
						canvas.drawBitmap(mPositive.get(1).getDrawingCache(), width - (width/3), 0, null);
						secondPlace = true;
					}
					
					if (i == 2 && mCurrentXPos[2] > 0 && mCurrentYPos[2] <  (height - mPositive.get(i).getHeight()))
					{
						mCurrentXPos[2] += mMoveByXPos[2];
						mCurrentYPos[2] += mMoveByYPos[2];
						canvas.drawBitmap(mPositive.get(i).getDrawingCache(), mCurrentXPos[2], mCurrentYPos[2], null);
					}
					
					else if (i == 2 && mCurrentXPos[2] <= 0 || mCurrentYPos[2] >=  (height - mPositive.get(i).getHeight()))
					{
						canvas.drawBitmap(mPositive.get(2).getDrawingCache(), 0, height - (height/4), null);
						thirdPlace = true;
					}
					
					if (i == 3 && mCurrentXPos[3] < (width - (mPositive.get(i).getWidth())) && mCurrentYPos[3] < (height - mPositive.get(i).getHeight()))
					{
						mCurrentXPos[3] += mMoveByXPos[3];
						mCurrentYPos[3] += mMoveByYPos[3];
						canvas.drawBitmap(mPositive.get(i).getDrawingCache(), mCurrentXPos[3], mCurrentYPos[3], null);
					}
					
					else if (i == 3 && mCurrentXPos[3] >= (width - (mPositive.get(i).getWidth())) || mCurrentYPos[3] >= (height - mPositive.get(i).getHeight()))
					{
						canvas.drawBitmap(mPositive.get(3).getDrawingCache(), width - (width/3), height - (height/4), null);
						fourthPlace = true;
					}
						
				}
			}
		}
		h.postDelayed(r, FRAME_RATE);
			
    }
	
	protected void moveIt(Canvas canvas, int moveX,int moveY, int i)
	{
		if (i == 0)
		{
			canvas.drawBitmap(mPositive.get(i).getDrawingCache(), moveX, moveY, null);
		}
		
		if (i == 1)
		{
			canvas.drawBitmap(mPositive.get(i).getDrawingCache(), moveX, moveY, null);
		}
		
		if (i == 2)
		{
			canvas.drawBitmap(mPositive.get(i).getDrawingCache(), moveX, moveY, null);
		}
		
		if (i == 3)
		{
			canvas.drawBitmap(mPositive.get(i).getDrawingCache(), moveX, moveY, null);
			
		}
	
	}

	
	protected void ScaleIt(Canvas canvas, int i)
	{
    	mScale[i] = Bitmap.createScaledBitmap(mScale[i], (int) (width * 1.5), height, true);
    	mScale[i-1].recycle();

	}


	

}
