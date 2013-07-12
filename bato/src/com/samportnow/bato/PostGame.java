package com.samportnow.bato;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

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

public class PostGame extends IntentService
{
	GameDbAdapter mScoresHelper;
	Cursor update;
	int game_count;
	int cal_count;
	int current; 
    ArrayList<Long> mRowId = new ArrayList<Long>();

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
		update = mScoresHelper.fetchScores();
		while (update.moveToNext())
		{
			try 
			{
				JSONObject jObjectGame = new JSONObject();
				if (update.getString(update.getColumnIndexOrThrow(GameDbAdapter.COLUMN_NAME_SCORE)) != "Yes")
				{
					jObjectGame.put("Score", update.getInt(update.getColumnIndexOrThrow(GameDbAdapter.COLUMN_NAME_SCORE)));
					mRowId.add(update.getLong(update.getColumnIndexOrThrow(GameDbAdapter.KEY_ROWID)));
				}
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
		update.close();
		mScoresHelper.close();
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
					for (int i = 0; i < mRowId.size(); i++)
					mScoresHelper.updatePush(mRowId.get(i));
			}
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
