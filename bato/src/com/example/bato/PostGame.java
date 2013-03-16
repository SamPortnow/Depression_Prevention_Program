package com.example.bato;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.IntentService;
import android.content.Intent;
import android.database.Cursor;
import android.os.IBinder;
import android.util.Log;

public class PostGame extends IntentService
{
	GameDbAdapter mScoresHelper;
	Cursor game;
	int game_count;
	int cal_count;
	int current; 
	GamePushDbAdapter mPushDbHelper = new GamePushDbAdapter(this);

	
	  public PostGame() 
	  {
		    super("PostGame");
		  
	  }

	  @Override
	  protected void onHandleIntent(Intent intent) 
	  {
		mScoresHelper = new GameDbAdapter(this);
		JSONArray jArrayGames = new JSONArray();
		mScoresHelper.open();
		mPushDbHelper.open();
		
		Cursor database = mPushDbHelper.fetchPush();
		if (database.moveToFirst())
		{
			current = database.getInt(database.getColumnIndexOrThrow(CalendarPushDbAdapter.KEY_ROWID));
		}
		
		game = mScoresHelper.fetchLatest(current);

		while (game.moveToNext())
		{
			try 
			{
				JSONObject jObjectGame = new JSONObject();
				jObjectGame.put("Time", game.getLong(game.getColumnIndexOrThrow(GameDbAdapter.COLUMN_NAME_TIME)));
				jObjectGame.put("RT", game.getLong(game.getColumnIndexOrThrow(GameDbAdapter.COLUMN_NAME_RT)));
				jObjectGame.put("Score", game.getInt(game.getColumnIndexOrThrow(GameDbAdapter.COLUMN_NAME_SCORE)));
				jObjectGame.put("Game Number", game.getInt(game.getColumnIndexOrThrow(GameDbAdapter.COLUMN_NAME_GAME_NUMBER)));
				jObjectGame.put("Game Complete", game.getString(game.getColumnIndexOrThrow(GameDbAdapter.COLUMN_NAME_GAME_COMPLETE)));
				jObjectGame.put("Trial", game.getInt(game.getColumnIndexOrThrow(GameDbAdapter.COLUMN_NAME_TRIAL)));
				jObjectGame.put("Negative Thought", game.getString(game.getColumnIndexOrThrow(GameDbAdapter.COLUMN_NAME_NEGATIVE_THOUGHT)));
				jObjectGame.put("Positive Thought", game.getString(game.getColumnIndexOrThrow(GameDbAdapter.COLUMN_NAME_POSITIVE_THOUGHT)));
				jObjectGame.put("Success", game.getString(game.getColumnIndexOrThrow(GameDbAdapter.COLUMN_NAME_SUCCESS)));
				jArrayGames.put(jObjectGame);
				current++;

			} 
			catch (IllegalArgumentException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		game.close();
		mScoresHelper.close();
		database.close();
		try 
		{
			PostData(jArrayGames, current);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
		
		private void PostData(JSONArray jArrayGames, int current) throws ClientProtocolException, IOException
		{
		HttpClient httpclientgame = new DefaultHttpClient();
		HttpPost httpostgame = new HttpPost("http://10.0.2.2:5000/game");
		StringEntity sGames;
		try 
		{
			sGames = new StringEntity(jArrayGames.toString());
			httpostgame.setEntity(sGames);
			httpostgame.setHeader("Accept", "application/json");
			httpostgame.setHeader("Content-type", "application/json");
			HttpResponse postResponse = httpclientgame.execute(httpostgame);
			if (postResponse.getStatusLine().getStatusCode() == 200)
			{
					current++;
					mPushDbHelper.createPush(current);
			}
			mPushDbHelper.close();
		} 
		catch (UnsupportedEncodingException e) 
		{
			e.printStackTrace();
		}
			
			
		}
	



@Override
public IBinder onBind(Intent arg0) {
	// TODO Auto-generated method stub
	return null;
}




}
