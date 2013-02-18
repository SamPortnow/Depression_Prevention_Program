package com.example.bato;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Simple notes database access helper class. Defines the basic CRUD operations
 * for the notepad example, and gives the ability to list all notes as well as
 * retrieve or modify a specific note.
 * Modified to include ALL activities and values. Will be returned later to the user 
 * so they can rank activities that they WANT to do. User must rank the activities; however. 
 */
public class CalendarDbAdapter {
	//FIRST STEP CREATE THE VARS YOU NEED FOR THE DATABASE
    private static final String DATABASE_NAME = "calendar_data"; //my database name
    private static final String DATABASE_TABLE = "calendar"; //this particular table is the activities table. I might make a separate table for a ranking system. We will see. 
    private static final int DATABASE_VERSION = 2;
    public static final String COLUMN_NAME_WITHIN="Within";
    public static final String COLUMN_NAME_HOUR="Hour";
    public static final String COLUMN_NAME_DAY="Day";
    public static final String COLUMN_NAME_ACTIVITY="Activity";
    public static final String COLUMN_NAME_FEELING = "Feeling";
    public static final String COLUMN_NAME_THOUGHT = "Thought";
    public static final String KEY_ROWID = "_id"; //all my vars are now declared 

    private static final String TAG = "CalendarDbAdapter";
    private DatabaseHelper mCalendarDbHelper;
    private SQLiteDatabase mCalendarDb;
    
    private static final String DATABASE_CREATE =  //create the database! you already know!! // modified android code of text . I want to allow for null text! 
        "create table calendar  (_id integer primary key autoincrement, " +
        "Day integer, Hour integer, Within integer, Activity text, Feeling integer, Thought text)";
    
    private final Context mCalendarCtx; //declare a context. activity extends from context. it's a basic part of android app. need to research this more. 

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
    public CalendarDbAdapter(Context ctx) {  //ActivitiyDbAdapter? Look into this one sahn. 
        this.mCalendarCtx = ctx;
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
    public CalendarDbAdapter open() throws SQLException {
        mCalendarDbHelper = new DatabaseHelper(mCalendarCtx);
        mCalendarDb = mCalendarDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mCalendarDbHelper.close();
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
    
    public long createCalendar(long Day, long Hour, long Within, String Activity, int Feeling, String Thought) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(COLUMN_NAME_DAY, Day);
        initialValues.put(COLUMN_NAME_HOUR, Hour);
        initialValues.put(COLUMN_NAME_WITHIN, Within);
        initialValues.put(COLUMN_NAME_ACTIVITY,Activity);
        initialValues.put(COLUMN_NAME_FEELING, Feeling);
        initialValues.put(COLUMN_NAME_THOUGHT, Thought);
        return mCalendarDb.insert(DATABASE_TABLE, null, initialValues);
    }
    
    public boolean updateCalendar(long Day, long Hour, long Within, String Activity, int Feeling, String Thought)
    	{
    	String day=String.valueOf(Day);
    	String shour=String.valueOf(Hour);
    	String swithin = String.valueOf(Within);
    	ContentValues args = new ContentValues();
        args.put(COLUMN_NAME_DAY, Day);
        args.put(COLUMN_NAME_HOUR, Hour);
        args.put(COLUMN_NAME_WITHIN, Within);
        args.put(COLUMN_NAME_ACTIVITY, Activity);
        args.put(COLUMN_NAME_FEELING, Feeling);
        args.put(COLUMN_NAME_THOUGHT, Thought);
        return mCalendarDb.update(DATABASE_TABLE, args, COLUMN_NAME_DAY+" = ? AND "+COLUMN_NAME_HOUR+" = ? AND "+COLUMN_NAME_WITHIN+" =?", new String[] {day,shour,swithin}) > 0;
        
    }
    
    public Cursor fetchWholeCalendar(long Day, long hour)
    {
    	String day=String.valueOf(Day);
    	String shour=String.valueOf(hour);
        return mCalendarDb.query(DATABASE_TABLE, new String[] {KEY_ROWID,COLUMN_NAME_ACTIVITY, COLUMN_NAME_FEELING, COLUMN_NAME_THOUGHT}, COLUMN_NAME_DAY+" = ? AND "+COLUMN_NAME_HOUR+" = ?", new String[] {day,shour}, null, null, null);
        
    }


    public Cursor fetchCalendar(long Day, long hour, long Within)
    {
    	String day=String.valueOf(Day);
    	String shour=String.valueOf(hour);
    	String swithin = String.valueOf(Within);
        return mCalendarDb.query(DATABASE_TABLE, new String[] {KEY_ROWID,COLUMN_NAME_ACTIVITY, COLUMN_NAME_FEELING, COLUMN_NAME_THOUGHT}, COLUMN_NAME_DAY+" = ? AND "+COLUMN_NAME_HOUR+" = ? AND "+COLUMN_NAME_WITHIN+" =?" , new String[] {day,shour,swithin}, null, null, null);
        
    }
    
    
    public Cursor fetchThoughts()
    {
        return mCalendarDb.query(DATABASE_TABLE, new String[] {KEY_ROWID, COLUMN_NAME_THOUGHT}, null , null, null, null, null); 
    }

   

}
