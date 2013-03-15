package com.example.bato;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

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

public class PostGame extends IntentService
{
	GameDbAdapter mScoresHelper;
	Cursor game;
	int game_count;
	int cal_count;
	
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
		game = mScoresHelper.fetchAll();
		
		
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
		
		try 
		{
			PostData(jArrayGames);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
		
		private void PostData(JSONArray jArrayGames) throws ClientProtocolException, IOException
		{
		HttpClient httpclientgame = new DefaultHttpClient();
		HttpPost httpostgame = new HttpPost("http://10.0.2.2:5000/");
		StringEntity sGames;
		try 
		{
			sGames = new StringEntity(jArrayGames.toString());
			httpostgame.setEntity(sGames);
			httpostgame.setHeader("Accept", "application/json");
			httpostgame.setHeader("Content-type", "application/json");
			httpclientgame.execute(httpostgame);
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
