package com.example.bato;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.Display;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.mobeta.android.dslv.DragSortListView;
import com.mobeta.android.dslv.DragSortListView.DropListener;



public class ScaleActivity extends Activity
{	
	Context mContext;
	ScaleView mScale;
	EditText positive_thought;
	Button fire;
	TextView pos;
	private static Set<String> mNegativeWords;
	int count;
	private Pattern four_letter_words = Pattern.compile("not|cant|cnt|can't"); 
	String inputLine;
	private String[] inputTokens;
	Button question;
	Button skip;
	RelativeLayout layout;
	RelativeLayout.LayoutParams [] params = new RelativeLayout.LayoutParams[4];
	ImageView mBag;
	ImageView mGreenBag;
	float[] mCurrentX;
	float[] mCurrentY;
	float[] mStartX;
	float[] mStartY;
	float[] moveByX;
	float[] moveByY;
	int x_coord;
	int y_coord;
	OnTouchListener mTouchListener;
	RelativeLayout.LayoutParams [] mMoveParams= new RelativeLayout.LayoutParams[4];
	boolean mRemoved;
	String word;
	TextView mNegative;
	boolean mStart;
	private ScaleDbAdapter mDbHelper;
	ScrollView mScroll;
	TextView mNegs;
	ArrayList<String> negative_thoughts = new ArrayList<String>();
	CalendarDbAdapter mCalHelper;
	ListAdapter mAdapter;
	ArrayAdapter<String> arrayAdapter;
	

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
	    mDbHelper=new ScaleDbAdapter(mContext);
	    mDbHelper.open();
	    mCalHelper = new CalendarDbAdapter(mContext);
	    mCalHelper.open();
	    Cursor thoughts = mCalHelper.fetchThoughts();
	    while (thoughts.moveToNext())
	    	{
	    		if (thoughts.getString(thoughts.getColumnIndexOrThrow(CalendarDbAdapter.COLUMN_NAME_THOUGHT)).length() > 0 && thoughts.getString(thoughts.getColumnIndexOrThrow(CalendarDbAdapter.COLUMN_NAME_THOUGHT)).charAt(0) == '-')
	    		{
	    			negative_thoughts.add(thoughts.getString(thoughts.getColumnIndexOrThrow(CalendarDbAdapter.COLUMN_NAME_THOUGHT)));
	    		}
	   
	    	}
	    thoughts.close();
	    mCalHelper.close();
	    populatePositiveWords(mContext);
	    setContentView(R.layout.activity_scale);
	    mScale = (ScaleView) findViewById(R.id.scale_view);
	    mScale.negative.setText(negative_thoughts.get(0));
	    arrayAdapter =  new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, negative_thoughts);
	    DragSortListView listView = (DragSortListView) findViewById(R.id.listview);
	    listView.setDropListener(new DropListener()
	    {

			@Override
			public void drop(int from, int to) 
			{
				String item=arrayAdapter.getItem(from);
				arrayAdapter.remove(item);
				arrayAdapter.insert(item, to);
				if (to == 0)
				{
					mScale.mSwitch(item);
				}
			}


	    	
	    });
	    listView.setAdapter(arrayAdapter);
	    mBag = new ImageView (mContext);
	    mBag.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.bag));
	    mGreenBag = new ImageView(mContext);
	    mGreenBag.setImageBitmap(mScale.mGreenBag);
	    layout = (RelativeLayout) findViewById(R.id.game_view);
	    positive_thought = (EditText) findViewById(R.id.thoughts);
	    fire = (Button) findViewById(R.id.scale_it);
	    question = (Button) new Button(mContext);
	    question.setBackgroundResource(R.drawable.question);
	    InputFilter[] FilterArray = new InputFilter[1];
	    FilterArray[0] = new InputFilter.LengthFilter(60);
	    positive_thought.setFilters(FilterArray);
	    question.setOnClickListener(new OnClickListener()
	    {

			@Override
			public void onClick(View arg0) {
					AlertDialog.Builder builder = new Builder(mContext);
					
					builder.setTitle("HELP: Strategies for Challenging Negative Thoughts");
					builder.setView(getLayoutInflater().inflate(R.layout.dialog_help, null));
					builder.setPositiveButton(android.R.string.ok, null);
					builder.create().show();				
				}
		
			
	    	
	    });
	    
	    fire.setOnClickListener(new OnClickListener()
	    {
	    	@Override
	    	public void onClick(View view) 
	    	{
	    		//if the button is clicked invalidate the ondraw method and pass in the text of the positive word 
				inputLine = positive_thought.getText().toString();
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
				
				InputMethodManager imm = (InputMethodManager)mContext.getSystemService(
					      Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(positive_thought.getWindowToken(), 0);
		    	pos = new TextView (mContext);
    			pos.layout(0, 0, mScale.width/3, mScale.height/4);
		    	pos.setGravity(Gravity.CENTER);
		    	pos.setTextSize(15);
		    	pos.setTextColor(Color.RED);
		    	pos.setTypeface(Typeface.DEFAULT_BOLD);
		    	pos.setShadowLayer(5, 2, 2, Color.YELLOW);
				pos.setText(positive_thought.getText().toString());
		    	pos.setDrawingCacheEnabled(true);
		    	pos.setBackgroundResource(R.drawable.whitecloud);
			    pos.setFocusableInTouchMode(true);
				mScale.mPositive.add(pos);
				mScale.scale_it = true;
    			count++;
    			mScale.sStop = false;
    			mScale.tracker = count;
    			mStart = true;
				}
        		positive_thought.setText(null);
        		
        	}
	    });
		SharedPreferences preferences = this.getSharedPreferences(this.getPackageName(), Context.MODE_PRIVATE);
		if (preferences.getString("scale instructions", null) == null)
		{
			
		AlertDialog.Builder builder = new Builder(mContext);
		builder.setTitle("Instructions");
		builder.setMessage("Outweight the negative thought by coming up with thoughts that CHALLENGE the truth of it");
		builder.setPositiveButton(android.R.string.ok, null);
		builder.create().show();				
		preferences.edit().putString("scale instructions", "Yes").commit();
		}
	
	}
	
	@Override
	public void onWindowFocusChanged(boolean focus)
	{
		super.onWindowFocusChanged(focus);
		RelativeLayout.LayoutParams sQuestionParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,  LayoutParams.WRAP_CONTENT); 
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int width = size.x;
		sQuestionParams.leftMargin = width/5 + mScale.getWidth()/2;
		sQuestionParams.topMargin = mScale.getHeight()/2;
		layout.addView(question, sQuestionParams);
	}

	
	protected void clear(Context context)
	{
		layout.removeView(fire);
		layout.removeView(positive_thought);
		layout.removeView(skip);
		layout.removeView(question);
		layout.removeView(mScale);
		layout.removeView(mNegs);
		layout.removeView(mScroll);
		for (int i = 0; i < 4; i++)
		{
		params[i] = new RelativeLayout.LayoutParams(mScale.width/3, mScale.height/4); //changed this from layout.getheight()/4
		}
		params[0].leftMargin = 0;
		params[0].topMargin = 0;
		params[1].leftMargin = layout.getWidth() - mScale.mPositive.get(1).getWidth();
		params[1].topMargin = 0;
		params[2].leftMargin = 0;
		params[2].topMargin = mScale.height - mScale.mPositive.get(2).getHeight();
		params[3].leftMargin = layout.getWidth() - mScale.mPositive.get(1).getWidth();
		params[3].topMargin = mScale.height - mScale.mPositive.get(2).getHeight();
		
		for (int i = 0; i < 4; i++)
		{
			layout.addView(mScale.mPositive.get(i), params[i]);
		}
		
		RelativeLayout.LayoutParams paramsBag = new RelativeLayout.LayoutParams(layout.getWidth()/2, layout.getHeight()/2);
		paramsBag.addRule(RelativeLayout.CENTER_HORIZONTAL);
		paramsBag.addRule(RelativeLayout.CENTER_VERTICAL);
		layout.addView(mBag, paramsBag);
		
		SharedPreferences preferences = this.getSharedPreferences(this.getPackageName(), Context.MODE_PRIVATE);
		if (preferences.getString("bank instructions", null) == null)
				{
					
				AlertDialog.Builder builder = new Builder(mContext);
				builder.setTitle("Instructions");
				builder.setMessage("Drag and drop the thought you BELIEVE the most into the bank!");
				builder.setPositiveButton(android.R.string.ok, null);
				builder.create().show();				
				preferences.edit().putString("bank instructions", "Yes").commit();
				}
		for (int i = 0; i <4; i++)
		{
			mScale.mPositive.get(i).setOnTouchListener(new MyListener(i)
				{
					 
					
					@Override
					public boolean onTouch(View v, MotionEvent event) 
					{ 
						int i = getPosition();
						mMoveParams[i] = (RelativeLayout.LayoutParams) mScale.mPositive.get(i).getLayoutParams();
					    ClipData data = ClipData.newPlainText("", "");
					    DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
					    v.startDrag(data, shadowBuilder, v, 0);
					        switch(event.getActionMasked())
					        {
					            case MotionEvent.ACTION_DOWN:
					                break;
					            case MotionEvent.ACTION_MOVE:
					                y_coord = (int) event.getRawY();
					                x_coord = (int) event.getRawX();
					                if (x_coord > mScale.width) 
					                {
					                    x_coord = mScale.width;
					                }
					                if (y_coord > mScale.height) {
					                    y_coord= mScale.height;
					                }
					                mMoveParams[i].leftMargin = x_coord - 25;
					                mMoveParams[i].topMargin = y_coord - 75;
					                mScale.mPositive.get(i).setLayoutParams(mMoveParams[i]);
					                break;

					            case MotionEvent.ACTION_UP:
					            	break;
		
					            default:
					                break;
					        }
					        return true;
					    }
				
				
				});
			
			mBag.setOnDragListener(new MyDragListener(i)
			{

				@Override
				public boolean onDrag(View arg0, DragEvent arg1) 
				{
					int i = getPosition();
            		RelativeLayout.LayoutParams paramsBag = new RelativeLayout.LayoutParams(layout.getWidth()/2, layout.getHeight()/2);
            		paramsBag.leftMargin = mScale.width/4;
            		paramsBag.topMargin =  mScale.height/4;
            		
					switch(arg1.getAction())
					{
					case DragEvent.ACTION_DROP:
		        	    mDbHelper.createRelation(mScale.negative.getText().toString(), mScale.mPositive.get(i).getText().toString());
	            		AlertDialog.Builder builder = new Builder(mContext);
	            		builder.setTitle("Great Job!");		
	            		builder.setNegativeButton("Go Home", new DialogInterface.OnClickListener()
	            		{

							@Override
							public void onClick(DialogInterface dialog,int which)
							{
								{
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
	            				Intent i = new Intent(mContext, MainActivity.class);				
	            				mContext.startActivity(i);
	            			}
	            			
	            		});			
	            		
	            		builder.create().show();
		            	
	            		break;
					case DragEvent.ACTION_DRAG_ENTERED:
					    mBag.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.green_bag));
					    break;
					case DragEvent.ACTION_DRAG_EXITED:
					    	mBag.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.bag));
							break;
						}
						
					
					return true;
				}
				
			});
			
			

		}
	}
	

		
	@Override
	public void onDestroy()
	{
		super.onDestroy();
		mDbHelper.close();
	}


}
