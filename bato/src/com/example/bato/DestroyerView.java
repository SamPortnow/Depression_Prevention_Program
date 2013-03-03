package com.example.bato;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class DestroyerView extends Fragment
{
	private Context mContext;
	Paint paint = new Paint();
	private AnimatedNegative PositiveAnimatedNegative;
	private String[] inputTokens;
	boolean play_again;
	boolean go_ahead = false;
	String inputLine;
	Toast fix_it;
	private Pattern four_letter_words = Pattern.compile("not|cant|cnt|can't"); 
	private MediaPlayer birdPlayer;
	private int trial;
	private int game;
	private long rt;
	private long current_mills;
	private static Set<String> mPositiveWords = null;
	private GameDbAdapter mDbHelper;
	int count;
	int score_tracker;
	int mean_rt;
	private int trial_check;
	String yes = "Yes";

	
	public static boolean populatePositiveWords(Context context)
	{
		mPositiveWords = new HashSet<String>();
		
		try
		{
			BufferedReader reader = new BufferedReader(new InputStreamReader(context.getAssets().open("positive_words.txt")));			
			String line = reader.readLine();
			
			while (line != null)
			{
				mPositiveWords.add(line.toLowerCase(Locale.US));
				line = reader.readLine();
			}
			
			reader.close();
		}
		catch (IOException exception)
		{
			return false;
		}
		
		return true;
	}
	
	@Override
	public void onAttach(Activity activity)
	{
		super.onAttach(activity);
		
		DestroyerView.populatePositiveWords(activity);
		
		activity.getActionBar().hide();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	        Bundle savedInstanceState) 
	{
	    super.onCreate(savedInstanceState);
	    mContext = this.getActivity();
	    mDbHelper=new GameDbAdapter(mContext);
	    mDbHelper.open();
	    Cursor activity = mDbHelper.fetchGame(game);
	    Cursor mean_rt_cursor = mDbHelper.fetchRT();

	    if (mean_rt_cursor.moveToFirst())
	    {
	    	while (mean_rt_cursor.moveToNext())
	    	{
	    		if (mean_rt_cursor.getString(mean_rt_cursor.getColumnIndexOrThrow(GameDbAdapter.COLUMN_NAME_SUCCESS)).contains("Yes"))
	    		{

	    			mean_rt += mean_rt_cursor.getInt(mean_rt_cursor.getColumnIndexOrThrow(GameDbAdapter.COLUMN_NAME_RT));
	    			trial++;
	    		}
	    	}
	    }
	    
	    if (activity.moveToLast())
	    {
	    	game = activity.getInt(
			activity.getColumnIndexOrThrow(GameDbAdapter.COLUMN_NAME_GAME_NUMBER));
	    	game++;
	    	
	    }
	    
	    if (trial_check > 0)
	    {
	    	mean_rt = mean_rt/trial_check;
	    }
	    
	    birdPlayer = MediaPlayer.create(mContext, R.raw.bird);
	    
	    View view = inflater.inflate(R.layout.activity_destroyer, container, false);
	    Button fire = (Button) view.findViewById(R.id.destroy);
	    final EditText positive_thought = (EditText) view.findViewById(R.id.destroyer);
	    PositiveAnimatedNegative = (AnimatedNegative) view.findViewById(R.id.anim_view);
	    positive_thought.setOnFocusChangeListener(new OnFocusChangeListener()
	    {

			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				PositiveAnimatedNegative.typing = true;
				
			}
	    	
	    });
	    
	    fire.setOnClickListener(new OnClickListener()
	    {
	    	@Override
	    	public void onClick(View view) 
	    	{
	    		trial++;
	    		PositiveAnimatedNegative.add = false;
	    		Calendar right_now = Calendar.getInstance();
      			current_mills = right_now.getTimeInMillis();
      			rt = current_mills - PositiveAnimatedNegative.start_mills;
	    		//if the button is clicked invalidate the ondraw method and pass in the text of the positive word 
				inputLine = positive_thought.getText().toString();
				inputTokens = inputLine.split(" ");
				
				if (inputLine.isEmpty())
				{
					Toast.makeText(getActivity(), "You have to write something!", Toast.LENGTH_SHORT).show();
					mDbHelper.createGame(current_mills, rt, 0, game, "No", trial, positive_thought.getText().toString(), PositiveAnimatedNegative.negative.getText().toString(), "No");
					return;					
				}
				
				if (inputTokens.length < 3)
				{
					Toast.makeText(mContext, "At least three words are required.", Toast.LENGTH_SHORT).show();
					mDbHelper.createGame(current_mills, rt, 0, game, "No", trial, positive_thought.getText().toString(), PositiveAnimatedNegative.negative.getText().toString(), "No");
					return;
				}				
				
				if (four_letter_words.matcher(inputLine).find() == true)
				{
					Toast.makeText(mContext, "Make an affirmative statement!", Toast.LENGTH_SHORT).show();
					mDbHelper.createGame(current_mills, rt, 0, game, "No", trial, positive_thought.getText().toString(), PositiveAnimatedNegative.negative.getText().toString(), "No");
					return;
				}
				
				boolean matchesToken = false;
				
				for (int i = 0; i < inputTokens.length; i++)
				{
					String token = inputTokens[i];
					
					if (mPositiveWords.contains(token.toLowerCase(Locale.US)))
					{
						matchesToken = true;
						break;
					}
				}
				
				if (matchesToken == false)
				{
					Toast.makeText(mContext, "Use positive words!", Toast.LENGTH_SHORT).show();
					mDbHelper.createGame(current_mills, rt, 0, game, "No", trial, positive_thought.getText().toString(), PositiveAnimatedNegative.negative.getText().toString(), "No");
					return;
				}
	    		count++;
	    		
				if (rt < mean_rt)
				{
					score_tracker += 25 + (mean_rt - rt)/100;
					PositiveAnimatedNegative.bonus = true;
					
				}
				
				else
				{
					score_tracker += 25;
				}
				if (count < 11)
					
				{
					mDbHelper.createGame(current_mills, rt, score_tracker, game, yes, trial, positive_thought.getText().toString(), PositiveAnimatedNegative.negative.getText().toString(), "No");
				}
				else
				{
					mDbHelper.createGame(current_mills, rt, score_tracker, game, yes, trial, positive_thought.getText().toString(), PositiveAnimatedNegative.negative.getText().toString(), yes);
	
				}
				
	

					InputMethodManager imm = (InputMethodManager)mContext.getSystemService(
					      Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(positive_thought.getWindowToken(), 0);			
	    		PositiveAnimatedNegative.invalidate();
        		PositiveAnimatedNegative.add = true;
        		PositiveAnimatedNegative.positive_word = positive_thought.getText().toString();
    			if(!birdPlayer.isPlaying())
				{
					birdPlayer.start();
				}
    			PositiveAnimatedNegative.typing = false;
    			PositiveAnimatedNegative.scorer = score_tracker;
        		positive_thought.setText(null);
        	}
	    });

	    return view;	   
	}

	@Override
	public void onDestroyView()
	{
		super.onDestroyView();
		
		getActivity().getActionBar().show();		
	}
}