package com.example.bato;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
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

	private static Set<String> mPositiveWords = null;
	
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
	    birdPlayer = MediaPlayer.create(mContext, R.raw.bird);
	    View view = inflater.inflate(R.layout.activity_destroyer, container, false);
	    Button fire = (Button) view.findViewById(R.id.destroy);
	    final EditText positive_thought = (EditText) view.findViewById(R.id.destroyer);
	    PositiveAnimatedNegative = (AnimatedNegative) view.findViewById(R.id.anim_view);
	    fire.setOnClickListener(new OnClickListener()
	    {
	    	@Override
	    	public void onClick(View view) 
	    	{
	    		PositiveAnimatedNegative.add = false;
	    		
	    		//if the button is clicked invalidate the ondraw method and pass in the text of the positive word 
				inputLine = positive_thought.getText().toString();
				inputTokens = inputLine.split(" ");
				
				if (inputLine.isEmpty())
				{
					Toast.makeText(getActivity(), "You have to write something!", Toast.LENGTH_SHORT).show();
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
					
					if (mPositiveWords.contains(token.toLowerCase(Locale.US)))
					{
						matchesToken = true;
						break;
					}
				}
				
				if (matchesToken == false)
				{
					Toast.makeText(mContext, "Use positive words!", Toast.LENGTH_SHORT).show();
					return;
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