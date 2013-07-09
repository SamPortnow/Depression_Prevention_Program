package com.samportnow.bato;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class GameDbAdapter
{
	//FIRST STEP CREATE THE VARS YOU NEED FOR THE DATABASE
    private static final String DATABASE_NAME = "game_data"; //my database name
    private static final String DATABASE_TABLE = "game"; //this particular table is the activities table. I might make a separate table for a ranking system. We will see. 
    private static final int DATABASE_VERSION = 2;
    public static final String COLUMN_NAME_SCORE = "Score";
    public static final String COLUMN_NAME_PUSHED = "Pushed";
    public static final String KEY_ROWID = "_id"; //all my vars are now declared 

    private static final String TAG = "GameDbAdapter";
    private DatabaseHelper mGameDbHelper;
    private SQLiteDatabase mGameDb;
    
    private static final String DATABASE_CREATE =  //create the database! you already know!! // modified android code of text . I want to allow for null text! 
	        "create table game (_id integer primary key autoincrement, " +
	        "Score int, pushed text)";
 
    
    private final Context mGameCtx; //declare a context. activity extends from context. it's a basic part of android app. need to research this more. 

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL(DATABASE_CREATE); //and here is where the database is created. because we declared the string DATA_BASE_CREATE above
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { //if the db gets upgraded. android docs say that the memory from the db is wiped. I need a workaround here!! 
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS activities");
            onCreate(db);
        }
    }

    /**
     * Constructor - takes the context to allow the database to be
     * opened/created
     * 
     * @param ctx the Context within which to work
     */
    public GameDbAdapter(Context ctx) {  //ActivitiyDbAdapter? Look into this one sahn. 
        this.mGameCtx = ctx;
    }

    /**
     * Open the notes database. If it cannot be opened, try to create a new
     * instance of the database. If it cannot be created, throw an exception to
     * signal the failure
     * 
     * @return this (self reference, allowing this to be chained in an
     *         initialization call)
     * @throws SQLException if the database could be neither opened or created
     */
    public GameDbAdapter open() throws SQLException {
        mGameDbHelper = new DatabaseHelper(mGameCtx);
        mGameDb = mGameDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mGameDbHelper.close();
    }

    /**
     * Create a new note using the title and body provided. If the note is
     * successfully created return the new rowId for that note, otherwise return
     * a -1 to indicate failure.
     * 
     * @param title the title of the note
     * @param body the body of the note
     * @return rowId or -1 if failed
     */
    
    //now do a create, update, and fetch functions!!!
    
    public long createGame(int score) 
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(COLUMN_NAME_SCORE, score);
        return mGameDb.insert(DATABASE_TABLE, null, initialValues);
    }
    
    public boolean updatePush(long Id)
    {
    	String filter = "_id=" + Id;
    	ContentValues args = new ContentValues();
    	args.put(COLUMN_NAME_PUSHED, "Yes");
    	return mGameDb.update(DATABASE_TABLE, args, filter, null) > 0;	    	
    }
    
    public Cursor fetchScores()
    {
        return mGameDb.query(DATABASE_TABLE, new String[] {KEY_ROWID,
        		COLUMN_NAME_SCORE}, null, null, null, null, COLUMN_NAME_SCORE + " DESC");

    }
   
    

}






