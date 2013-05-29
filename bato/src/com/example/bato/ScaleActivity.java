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
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnClickListener;
import android.view.View.OnDragListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;



public class ScaleActivity extends Activity
{	
	Context mContext;
	ScaleView mScale;
	AutoCompleteTextView positive_thought;
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
	ScaleArrayAdapter arrayAdapter;
	ListView listView;
	int width;
	int j;
	String negative;
	String positive;
	int believe;
	int help;
	LayoutInflater inflater;
	View view;
	int times;
	Typeface sans;
	SharedPreferences preferences;
	ArrayList <String> positive_thoughts = new ArrayList<String>();

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
	    inflater = LayoutInflater.from(this);
   	 	sans = Typeface.create("sans-serif-condensed", Typeface.BOLD);
	    mContext = this;
	    mDbHelper=new ScaleDbAdapter(mContext);
	    mDbHelper.open();
	    Cursor positives = mDbHelper.fetchPositives();
	    while (positives.moveToNext())
	    {	
	    	String pos_thought = positives.getString(positives.getColumnIndexOrThrow(ScaleDbAdapter.COLUMN_NAME_POSITIVE));
			if (! positive.contains(pos_thought))
			{
				positive_thoughts.add(pos_thought);
			}
		}
	    mCalHelper = new CalendarDbAdapter(mContext);
	    mCalHelper.open();
	    Cursor thoughts = mCalHelper.fetchNegs();
	    while (thoughts.moveToNext())
	    	{
	    			String thought = thoughts.getString(thoughts.getColumnIndexOrThrow(CalendarDbAdapter.COLUMN_NAME_THOUGHT));
	    			if (! negative_thoughts.contains(thought))
	    			{
	    				negative_thoughts.add(thought);
	    			}

	    	
	    	}
	    thoughts.close();
	    mCalHelper.close();
	    populatePositiveWords(mContext);
	    setContentView(R.layout.activity_scale);
	    mScale = (ScaleView) findViewById(R.id.scale_view);
	    arrayAdapter =  new ScaleArrayAdapter(this, R.layout.negatives, android.R.id.text1, negative_thoughts);
	    listView = (ListView) findViewById(R.id.listview);
	    listView.setAdapter(arrayAdapter);
	    mBag = new ImageView (mContext);
	    mBag.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.bag));
	    mGreenBag = new ImageView(mContext);
	    mGreenBag.setImageBitmap(mScale.mGreenBag);
	    layout = (RelativeLayout) findViewById(R.id.game_view);
	    ArrayAdapter<String> adapter = new ArrayAdapter<String> (this, android.R.layout.simple_dropdown_item_1line, positive_thoughts);
	    positive_thought = (AutoCompleteTextView) findViewById(R.id.thoughts);
	    positive_thought.setAdapter(adapter);
	    fire = (Button) findViewById(R.id.scale_it);
	    fire.setClickable(false);
	    question = (Button) new Button(mContext);
	    question.setBackgroundResource(R.drawable.question);
	    InputFilter[] FilterArray = new InputFilter[1];
	    FilterArray[0] = new InputFilter.LengthFilter(60);
	    preferences = this.getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
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
				fire.setClickable(false);
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
		    	pos.setTypeface(sans);
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
    			
    			if (count == 4)
    				{
        			layout.removeView(question);
    				}
				}
        		positive_thought.setText(null);
        		
        	}
	    });
		preferences = this.getSharedPreferences(this.getPackageName(), Context.MODE_PRIVATE);
		if (preferences.getString("list instructions", null) == null)
				{
					
				AlertDialog.Builder builder = new Builder(mContext);
				builder.setTitle("Instructions");
				builder.setMessage("Drag a negative thought onto the scale to begin");
				builder.setPositiveButton(android.R.string.ok, null);
				builder.create().show();				
				preferences.edit().putString("list instructions", "Yes").commit();
				}
	    
		
		
		mScale.setOnDragListener(new OnDragListener()
		{

			@Override
			public boolean onDrag(View arg0, DragEvent arg1) 
			{
				float x = arg1.getX();
				float y = arg1.getY();
				
				switch (arg1.getAction())
				{
					case DragEvent.ACTION_DRAG_LOCATION:
						if (x >= mScale.getWidth()/6 && x <= mScale.getWidth()/3 
						&& y <= ((mScale.getHeight()/2 + mScale.getHeight()/6) + j) && y >= (mScale.getHeight()/2) + j)
						{
							mScale.move = true;
							mScale.i -=1;
							j += (mScale.getHeight()/200);
						}
						
						else
						{
							mScale.move = false;
						}
						break;
					
					case DragEvent.ACTION_DRAG_STARTED:
						break;
						
					case DragEvent.ACTION_DROP:

					
					case DragEvent.ACTION_DRAG_ENDED:
						mScale.move = false;
						
						if (x >= mScale.getWidth()/6 && x <= mScale.getWidth()/3 
						&& y <= ((mScale.getHeight()/2 + mScale.getHeight()/5) + j) && y >= (mScale.getHeight()/2) + j)
						{
							View view = (View) arg1.getLocalState();
							TextView negative = (TextView) view.findViewById(android.R.id.text1);
							mScale.width = layout.getWidth();
							mScale.update();
							mScale.negative.setText(negative.getText().toString());
							mScale.i = 0;
							mScale.reset = true;
							mScale.start = true;
							layout.removeView(listView);
						    fire.setClickable(true);
							RelativeLayout.LayoutParams sQuestionParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,  LayoutParams.WRAP_CONTENT); 
							sQuestionParams.leftMargin = width/2 - width/8;
							sQuestionParams.topMargin = mScale.getHeight()/2;
							layout.addView(question, sQuestionParams);
							if (preferences.getString("scale instructions", null) == null)
							{
								
							AlertDialog.Builder builder = new Builder(mContext);
							builder.setTitle("Instructions");
							builder.setMessage("Outweight the negative thought by coming up with thoughts that CHALLENGE it. Touch the question mark for help");
							builder.setPositiveButton(android.R.string.ok, null);
							builder.create().show();				
							preferences.edit().putString("scale instructions", "Yes").commit();
							}
						}
						else
						{
							mScale.reposition = true;
						}
						break;
				}
				

				return true;
			}
			
		});
	
	}
	
	@Override
	public void onWindowFocusChanged(boolean focus)
	{
		super.onWindowFocusChanged(focus);
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		width = size.x;
	}
	

	protected void clear(Context context)
	{
		layout.removeView(fire);
		layout.removeView(mScale);
		layout.removeView(mNegs);
		layout.removeView(positive_thought);
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
				builder.setMessage("Drag and drop a thought into the bank!");
				builder.setPositiveButton(android.R.string.ok, null);
				builder.create().show();				
				preferences.edit().putString("bank instructions", "Yes").commit();
				}
		for (int i = 0; i <4; i++)
		{
			mScale.mPositive.get(i).setOnTouchListener(new MyListener(i)
				{
					int i = getPosition();
					
					@Override
					public boolean onTouch(View v, MotionEvent event) 
					{ 
					    ClipData data = ClipData.newPlainText(mScale.mPositive.get(i).getText().toString(), null);
					    DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
					    v.startDrag(data, shadowBuilder, v, 0);
					    return true;
					    }
				
				
				});
			
			mBag.setOnDragListener(new MyDragListener(i)
			{

				@Override
				public boolean onDrag(View arg0, DragEvent arg1) 
				{
            		RelativeLayout.LayoutParams paramsBag = new RelativeLayout.LayoutParams(layout.getWidth()/2, layout.getHeight()/2);
            		paramsBag.leftMargin = mScale.width/4;
            		paramsBag.topMargin =  mScale.height/4;
            		
					switch(arg1.getAction())
					{
					case DragEvent.ACTION_DROP:
						
					    times++;
						mBag.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.bag));
						ClipData data = arg1.getClipData();
						AlertDialog.Builder build_believe = new AlertDialog.Builder(mContext);	
						view =  inflater.inflate(R.layout.believe_dialog, null);
						negative = mScale.negative.getText().toString();
						positive = data.getDescription().getLabel().toString();
						build_believe.setView(view);
						build_believe.setTitle("Rate your thought");
						build_believe.setPositiveButton("Next", new DialogInterface.OnClickListener()
						{

							@Override
							public void onClick(DialogInterface dialog,
									int which) 
							{

								believe = ((SeekBar) view.findViewById(R.id.believe)).getProgress();
								help = ((SeekBar) view.findViewById(R.id.help)).getProgress();
								mDbHelper.createRelation(negative, positive, believe, help);
			            		AlertDialog.Builder builder = new Builder(mContext);
			            		builder.setTitle("Great Job!");
			            		if (times != 4)
			            		{
			            		builder.setNeutralButton("Drop another thought in the bank", null);
			            		}
			            		
			            		builder.setNegativeButton("Go Home", new DialogInterface.OnClickListener()
			            		{

									@Override
									public void onClick(DialogInterface dialog,int which)
									{
										{
											finish();
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
			            				Intent i = new Intent(mContext, ScaleActivity.class);				
			            				mContext.startActivity(i);
			            			}
			            			
			            		});			
			            		
			            		builder.create().show();

							}		
						});
						AlertDialog dialog = build_believe.create();
		            	dialog.show();
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
