package com.samportnow.bato.database;

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
public class ActivityDbAdapter {
	//FIRST STEP CREATE THE VARS YOU NEED FOR THE DATABASE
    private static final String DATABASE_NAME = "data"; //my database name
    private static final String DATABASE_TABLE = "activities"; //this particular table is the activities table. I might make a separate table for a ranking system. We will see. 
    private static final int DATABASE_VERSION = 2;
    public static final String COLUMN_NAME_VALUE1_RELATIONSHIP = "Value_1_Relationship";
    public static final String COLUMN_NAME_VALUE1_EDUCATION = "Value_1_Education";
    public static final String COLUMN_NAME_VALUE1_RECREATION = "Value_1_Recreation";
    public static final String COLUMN_NAME_VALUE1_MIND = "Value_1_Mind";
    public static final String COLUMN_NAME_VALUE1_DAILY = "Value_1_Daily";
    public static final String COLUMN_NAME_ACTIVITY1_RELATIONSHIP="Activity_1_Relationship";
    public static final String COLUMN_NAME_ACTIVITY2_RELATIONSHIP="Activity_2_Relationship";
    public static final String COLUMN_NAME_ACTIVITY3_RELATIONSHIP="Activity_3_Relationship";
    public static final String COLUMN_NAME_ACTIVITY4_RELATIONSHIP="Activity_4_Relationship";
    public static final String COLUMN_NAME_ACTIVITY5_RELATIONSHIP="Activity_5_Relationship";
    public static final String COLUMN_NAME_ACTIVITY1_EDUCATION="Activity_1_Education";
    public static final String COLUMN_NAME_ACTIVITY2_EDUCATION="Activity_2_Education";
    public static final String COLUMN_NAME_ACTIVITY3_EDUCATION="Activity_3_Education";
    public static final String COLUMN_NAME_ACTIVITY4_EDUCATION="Activity_4_Education";
    public static final String COLUMN_NAME_ACTIVITY5_EDUCATION="Activity_5_Education";
    public static final String COLUMN_NAME_ACTIVITY1_RECREATION="Activity_1_Recreation";
    public static final String COLUMN_NAME_ACTIVITY2_RECREATION="Activity_2_Recreation";
    public static final String COLUMN_NAME_ACTIVITY3_RECREATION="Activity_3_Recreation";
    public static final String COLUMN_NAME_ACTIVITY4_RECREATION="Activity_4_Recreation";
    public static final String COLUMN_NAME_ACTIVITY5_RECREATION="Activity_5_Recreation";
    public static final String COLUMN_NAME_ACTIVITY1_MIND="Activity_1_Mind";
    public static final String COLUMN_NAME_ACTIVITY2_MIND="Activity_2_Mind";
    public static final String COLUMN_NAME_ACTIVITY3_MIND="Activity_3_Mind";
    public static final String COLUMN_NAME_ACTIVITY4_MIND="Activity_4_Mind";
    public static final String COLUMN_NAME_ACTIVITY5_MIND="Activity_5_Mind";
    public static final String COLUMN_NAME_ACTIVITY1_DAILY="Activity_1_Daily";
    public static final String COLUMN_NAME_ACTIVITY2_DAILY="Activity_2_Daily";
    public static final String COLUMN_NAME_ACTIVITY3_DAILY="Activity_3_Daily";
    public static final String COLUMN_NAME_ACTIVITY4_DAILY="Activity_4_Daily";
    public static final String COLUMN_NAME_ACTIVITY5_DAILY="Activity_5_Daily";
    public static final String COLUMN_NAME_MORNING_MINUTE="Morning_Minute";
    public static final String COLUMN_NAME_MORNING_HOUR="Morning_Hour";
    public static final String COLUMN_NAME_EVENING_MINUTE="Evening_Minute";
    public static final String COLUMN_NAME_EVENING_HOUR="Evening_Hour";
    public static final String COLUMN_NAME_CONTACT1="Contact1";
    public static final String COLUMN_NAME_CONTACT2="Contact2";
    public static final String COLUMN_NAME_CONTACT3="Contact3";
    public static final String COLUMN_NAME_CONTACT4="Contact4";
    public static final String COLUMN_NAME_CONTACT5="Contact5";

    

    public static final String KEY_ROWID = "_id"; //all my vars are now declared 

    private static final String TAG = "ActivityDbAdapter";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;
    
    private static final String DATABASE_CREATE =  //create the database! you already know!! // modified android code of text . I want to allow for null text! 
        "create table activities (_id integer primary key, " +
        "Value_1_Relationship text,"+
        "Value_1_Education text ," +
        "Value_1_Recreation text ," +
        "Value_1_Mind text ," +
        "Value_1_Daily text ," +
        "Activity_1_Relationship text , Activity_2_Relationship text , Activity_3_Relationship text , Activity_4_Relationship text , Activity_5_Relationship text ," +
        "Activity_1_Education text , Activity_2_Education text , Activity_3_Education text , Activity_4_Education text , Activity_5_Education text ," +
        "Activity_1_Recreation text , Activity_2_Recreation text , Activity_3_Recreation text , Activity_4_Recreation text , Activity_5_Recreation text ," +
        "Activity_1_Mind text , Activity_2_Mind text , Activity_3_Mind text , Activity_4_Mind text , Activity_5_Mind text ," +
        "Activity_1_Daily text , Activity_2_Daily text , Activity_3_Daily text , Activity_4_Daily text , Activity_5_Daily text," +
        "Morning_Minute integer, Morning_Hour integer, Evening_Minute integer, Evening_Hour integer," +
        "Contact1 text, Contact2 text, Contact3 text, Contact4 text, Contact5 text);";
    //and now we have a database. it's a big one! 	
    
    private final Context mCtx; //declare a context. activity extends from context. it's a basic part of android app. need to research this more. 

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
    public ActivityDbAdapter(Context ctx) {  //ActivitiyDbAdapter? Look into this one sahn. 
        this.mCtx = ctx;
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
    public ActivityDbAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mDbHelper.close();
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
    
    //put in the values, activities, importance, enjoyment ratings! whoof! 
    //or do I need to do different createNotes for each view?? YES
    
    
    public long createRelationship(long rowId, String value_relationship,  //insert relationship value and activities
    		String activity1_relationship, String activity2_relationship, String activity3_relationship,
    		String activity4_relationship, String activity5_relationship) {
        ContentValues initialValues = new ContentValues();
        Log.e("Create","I am creating");
        initialValues.put(KEY_ROWID, rowId);
        initialValues.put(COLUMN_NAME_VALUE1_RELATIONSHIP, value_relationship);
        initialValues.put(COLUMN_NAME_ACTIVITY1_RELATIONSHIP, activity1_relationship);
        initialValues.put(COLUMN_NAME_ACTIVITY2_RELATIONSHIP, activity2_relationship);
        initialValues.put(COLUMN_NAME_ACTIVITY3_RELATIONSHIP, activity3_relationship);
        initialValues.put(COLUMN_NAME_ACTIVITY4_RELATIONSHIP, activity4_relationship);
        initialValues.put(COLUMN_NAME_ACTIVITY5_RELATIONSHIP, activity5_relationship);
        return mDb.insert(DATABASE_TABLE, null, initialValues);
    }
    
    public long createEducation(long rowId, String value_education,String activity1_education, String activity2_education, String activity3_education,
    		String activity4_education, String activity5_education) //insert relationship value and activities
    {
        ContentValues initialValues = new ContentValues();
        Log.e("Create","I am creating Education");
        initialValues.put(KEY_ROWID, rowId);
        initialValues.put(COLUMN_NAME_VALUE1_EDUCATION, value_education);
        initialValues.put(COLUMN_NAME_ACTIVITY1_EDUCATION, activity1_education);
        initialValues.put(COLUMN_NAME_ACTIVITY2_EDUCATION, activity2_education);
        initialValues.put(COLUMN_NAME_ACTIVITY3_EDUCATION, activity3_education);
        initialValues.put(COLUMN_NAME_ACTIVITY4_EDUCATION, activity4_education);
        initialValues.put(COLUMN_NAME_ACTIVITY5_EDUCATION, activity5_education);
        return mDb.insert(DATABASE_TABLE, null, initialValues);	
    }
    
    public long createRecreation(long rowId,String value_recreation,String activity1_recreation, String activity2_recreation, String activity3_recreation,
    		String activity4_recreation, String activity5_recreation) //insert relationship value and activities
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_ROWID, rowId);
        initialValues.put(COLUMN_NAME_VALUE1_RECREATION, value_recreation);
        initialValues.put(COLUMN_NAME_ACTIVITY1_RECREATION, activity1_recreation);
        initialValues.put(COLUMN_NAME_ACTIVITY2_RECREATION, activity2_recreation);
        initialValues.put(COLUMN_NAME_ACTIVITY3_RECREATION, activity3_recreation);
        initialValues.put(COLUMN_NAME_ACTIVITY4_RECREATION, activity4_recreation);
        initialValues.put(COLUMN_NAME_ACTIVITY5_RECREATION, activity5_recreation);
        return mDb.insert(DATABASE_TABLE, null, initialValues);	
    }
    
    public long createMind(long rowId, String value_mind,String activity1_mind, String activity2_mind, String activity3_mind,
    		String activity4_mind, String activity5_mind)
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_ROWID, rowId);
        initialValues.put(COLUMN_NAME_VALUE1_MIND, value_mind);
        initialValues.put(COLUMN_NAME_ACTIVITY1_MIND, activity1_mind);
        initialValues.put(COLUMN_NAME_ACTIVITY2_MIND, activity2_mind);
        initialValues.put(COLUMN_NAME_ACTIVITY3_MIND, activity3_mind);
        initialValues.put(COLUMN_NAME_ACTIVITY4_MIND, activity4_mind);
        initialValues.put(COLUMN_NAME_ACTIVITY5_MIND, activity5_mind);
        return mDb.insert(DATABASE_TABLE, null, initialValues);
    }
    
    public long createDaily(long rowId, String value_daily,String activity1_daily, String activity2_daily, String activity3_daily,
    		String activity4_daily, String activity5_daily)
    
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_ROWID, rowId);
        initialValues.put(COLUMN_NAME_VALUE1_DAILY, value_daily);
        initialValues.put(COLUMN_NAME_ACTIVITY1_DAILY, activity1_daily);
        initialValues.put(COLUMN_NAME_ACTIVITY2_DAILY, activity2_daily);
        initialValues.put(COLUMN_NAME_ACTIVITY3_DAILY, activity3_daily);
        initialValues.put(COLUMN_NAME_ACTIVITY4_DAILY, activity4_daily);
        initialValues.put(COLUMN_NAME_ACTIVITY5_DAILY, activity5_daily);
        return mDb.insert(DATABASE_TABLE, null, initialValues);
    	
    }
    
    
    public long createMorningTime(long rowId, long morning_minute, long morning_hour)
    {
    	ContentValues initialValues = new ContentValues();
    	initialValues.put(KEY_ROWID, rowId);
    	initialValues.put(COLUMN_NAME_MORNING_MINUTE, morning_minute);
    	initialValues.put(COLUMN_NAME_MORNING_HOUR, morning_hour);
    	return mDb.insert(DATABASE_TABLE, null, initialValues);
    	
    }
    
    public long createEveningTime(long rowId,long evening_minute, long evening_hour)
    {
    	ContentValues initialValues = new ContentValues();
    	initialValues.put(KEY_ROWID, rowId);
    	initialValues.put(COLUMN_NAME_EVENING_MINUTE, evening_minute);
    	initialValues.put(COLUMN_NAME_EVENING_HOUR, evening_hour);
    	return mDb.insert(DATABASE_TABLE, null, initialValues);
    }
    
    public long createContacts(long rowId, String contact1, String contact2, String contact3, String contact4, String contact5)
    {
    	ContentValues initialValues = new ContentValues();
    	initialValues.put(KEY_ROWID, rowId);
    	initialValues.put(COLUMN_NAME_CONTACT1, contact1);
    	initialValues.put(COLUMN_NAME_CONTACT2, contact2);
    	initialValues.put(COLUMN_NAME_CONTACT3, contact3);
    	initialValues.put(COLUMN_NAME_CONTACT4, contact4);
    	initialValues.put(COLUMN_NAME_CONTACT5, contact5);
    	return mDb.insert(DATABASE_TABLE, null, initialValues);    	
    }
 // deleted delete note method. not relevant for my purposes.    
 //update relationship
    
    public boolean updateRelationship(long rowId, String value_relationship, String activity1_relationship,
    		String activity2_relationship, String activity3_relationship, String activity4_relationship,
    		String activity5_relationship) {
        Log.e("I","AM updating");
    	ContentValues args = new ContentValues();
        args.put(COLUMN_NAME_VALUE1_RELATIONSHIP, value_relationship);
        args.put(COLUMN_NAME_ACTIVITY1_RELATIONSHIP, activity1_relationship);
        args.put(COLUMN_NAME_ACTIVITY2_RELATIONSHIP, activity2_relationship);
        args.put(COLUMN_NAME_ACTIVITY3_RELATIONSHIP, activity3_relationship);
        args.put(COLUMN_NAME_ACTIVITY4_RELATIONSHIP, activity4_relationship);
        args.put(COLUMN_NAME_ACTIVITY5_RELATIONSHIP, activity5_relationship);

        return mDb.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
    }
    
    
    public boolean updateEducation(long rowId, String value_education, String activity1_education,
    		String activity2_education, String activity3_education, String activity4_education,
    		String activity5_education) {
        ContentValues args = new ContentValues();
        args.put(KEY_ROWID, rowId);
        args.put(COLUMN_NAME_VALUE1_EDUCATION, value_education);
        args.put(COLUMN_NAME_ACTIVITY1_EDUCATION, activity1_education);
        args.put(COLUMN_NAME_ACTIVITY2_EDUCATION, activity2_education);
        args.put(COLUMN_NAME_ACTIVITY3_EDUCATION, activity3_education);
        args.put(COLUMN_NAME_ACTIVITY4_EDUCATION, activity4_education);
        args.put(COLUMN_NAME_ACTIVITY5_EDUCATION, activity5_education);

        return mDb.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
    }
    
    public boolean updateRecreation(long rowId, String value_recreation, String activity1_recreation,
    		String activity2_recreation, String activity3_recreation, String activity4_recreation,
    		String activity5_recreation) {
        ContentValues args = new ContentValues();
        args.put(KEY_ROWID, rowId);
        args.put(COLUMN_NAME_VALUE1_RECREATION, value_recreation);
        args.put(COLUMN_NAME_ACTIVITY1_RECREATION, activity1_recreation);
        args.put(COLUMN_NAME_ACTIVITY2_RECREATION, activity2_recreation);
        args.put(COLUMN_NAME_ACTIVITY3_RECREATION, activity3_recreation);
        args.put(COLUMN_NAME_ACTIVITY4_RECREATION, activity4_recreation);
        args.put(COLUMN_NAME_ACTIVITY5_RECREATION, activity5_recreation);

        return mDb.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
    }
    
    public boolean updateMind(long rowId, String value_mind, String activity1_mind,
    		String activity2_mind, String activity3_mind, String activity4_mind,
    		String activity5_mind) {
        ContentValues args = new ContentValues();
        args.put(KEY_ROWID, rowId);
        args.put(COLUMN_NAME_VALUE1_MIND, value_mind);
        args.put(COLUMN_NAME_ACTIVITY1_MIND, activity1_mind);
        args.put(COLUMN_NAME_ACTIVITY2_MIND, activity2_mind);
        args.put(COLUMN_NAME_ACTIVITY3_MIND, activity3_mind);
        args.put(COLUMN_NAME_ACTIVITY4_MIND, activity4_mind);
        args.put(COLUMN_NAME_ACTIVITY5_MIND, activity5_mind);

        return mDb.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
    }
    
    public boolean updateDaily(long rowId, String value_daily, String activity1_daily,
    		String activity2_daily, String activity3_daily, String activity4_daily,
    		String activity5_daily) {
        ContentValues args = new ContentValues();
        args.put(KEY_ROWID, rowId);
        args.put(COLUMN_NAME_VALUE1_DAILY, value_daily);
        args.put(COLUMN_NAME_ACTIVITY1_DAILY, activity1_daily);
        args.put(COLUMN_NAME_ACTIVITY2_DAILY, activity2_daily);
        args.put(COLUMN_NAME_ACTIVITY3_DAILY, activity3_daily);
        args.put(COLUMN_NAME_ACTIVITY4_DAILY, activity4_daily);
        args.put(COLUMN_NAME_ACTIVITY5_DAILY, activity5_daily);

        return mDb.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
    }
    
    public boolean updateTimeMorning(long rowId, long morning_minute, long morning_hour)
    {
    	ContentValues args = new ContentValues();
    	args.put(KEY_ROWID, rowId);
    	args.put(COLUMN_NAME_MORNING_MINUTE, morning_minute);
    	args.put(COLUMN_NAME_MORNING_HOUR, morning_hour);   
    	Log.e("morning",""+morning_minute);
    	return mDb.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
    	
    }
    
    public boolean updateTimeEvening(long rowId, long evening_minute, long evening_hour)
    {
    	ContentValues args = new ContentValues();
    	args.put(KEY_ROWID, rowId);
    	args.put(COLUMN_NAME_EVENING_MINUTE, evening_minute);
    	args.put(COLUMN_NAME_EVENING_HOUR, evening_hour);
    	return mDb.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;

    }
    
    public boolean updateContacts(long rowId, String contact1, String contact2, String contact3, String contact4, String contact5)
    {
    	ContentValues args = new ContentValues();
    	args.put(KEY_ROWID, rowId);
    	args.put(COLUMN_NAME_CONTACT1, contact1);
    	args.put(COLUMN_NAME_CONTACT2, contact2);
    	args.put(COLUMN_NAME_CONTACT3, contact3);
    	args.put(COLUMN_NAME_CONTACT4, contact4);
    	args.put(COLUMN_NAME_CONTACT5, contact5);
    	return mDb.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;

    }
//the following methods return a cursor!     
    
//fetch relationship value method     
    public Cursor fetchRelationshipValue(Long mRowId) {

        return mDb.query(DATABASE_TABLE, new String[] {COLUMN_NAME_VALUE1_RELATIONSHIP}, null, null, null, null, null);
    }

//fetch education value method    
    
    public Cursor fetchEducationValue(Long mRowId) {

        return mDb.query(DATABASE_TABLE, new String[] {COLUMN_NAME_VALUE1_EDUCATION}, null, null, null, null, null);
    }

//fetch recreation value method
    public Cursor fetchRecreationValue(Long mRowId) {

        return mDb.query(DATABASE_TABLE, new String[] {COLUMN_NAME_VALUE1_RECREATION}, null, null, null, null, null);
    }

//fetch mind value method     
    public Cursor fetchMindValue(Long mRowId) {

        return mDb.query(DATABASE_TABLE, new String[] {COLUMN_NAME_VALUE1_MIND}, null, null, null, null, null);
    }
//fetch daily value method
    public Cursor fetchDailyValue(Long mRowId) {

        return mDb.query(DATABASE_TABLE, new String[] {COLUMN_NAME_VALUE1_DAILY}, null, null, null, null, null);
    }
    
//fetch relationship activities method
    public Cursor fetchRelationshipActivities(Long mRowId) {

        return mDb.query(DATABASE_TABLE, new String[] {COLUMN_NAME_VALUE1_RELATIONSHIP,
        		COLUMN_NAME_ACTIVITY1_RELATIONSHIP,COLUMN_NAME_ACTIVITY2_RELATIONSHIP,
        		COLUMN_NAME_ACTIVITY3_RELATIONSHIP,COLUMN_NAME_ACTIVITY4_RELATIONSHIP,
        		COLUMN_NAME_ACTIVITY5_RELATIONSHIP}, null, null, null, null, null);
    }

   
 //fetch education activities method
    public Cursor fetchEducationActivities(Long mRowId) {

        return mDb.query(DATABASE_TABLE, new String[] {COLUMN_NAME_VALUE1_EDUCATION,
        		COLUMN_NAME_ACTIVITY1_EDUCATION,COLUMN_NAME_ACTIVITY2_EDUCATION,
        		COLUMN_NAME_ACTIVITY3_EDUCATION,COLUMN_NAME_ACTIVITY4_EDUCATION,
        		COLUMN_NAME_ACTIVITY5_EDUCATION}, null, null, null, null, null);
    }
    
 //fetch recreation activities method
    public Cursor fetchRecreationActivities(Long mRowId) {

        return mDb.query(DATABASE_TABLE, new String[] {COLUMN_NAME_VALUE1_RECREATION,
        		COLUMN_NAME_ACTIVITY1_RECREATION,COLUMN_NAME_ACTIVITY2_RECREATION,
        		COLUMN_NAME_ACTIVITY3_RECREATION,COLUMN_NAME_ACTIVITY4_RECREATION,
        		COLUMN_NAME_ACTIVITY5_RECREATION}, null, null, null, null, null);
    }

//fetch mind activities method
    
    public Cursor fetchMindActivities(Long mRowId) {

        return mDb.query(DATABASE_TABLE, new String[] {COLUMN_NAME_VALUE1_MIND,
        		COLUMN_NAME_ACTIVITY1_MIND,COLUMN_NAME_ACTIVITY2_MIND,
        		COLUMN_NAME_ACTIVITY3_MIND,COLUMN_NAME_ACTIVITY4_MIND,
        		COLUMN_NAME_ACTIVITY5_MIND}, null, null, null, null, null);
    }
    
//fetch daily activities method 
    public Cursor fetchDailyActivities(Long mRowId) {

        return mDb.query(DATABASE_TABLE, new String[] {COLUMN_NAME_VALUE1_DAILY,
        		COLUMN_NAME_ACTIVITY1_DAILY,COLUMN_NAME_ACTIVITY2_DAILY,
        		COLUMN_NAME_ACTIVITY3_DAILY,COLUMN_NAME_ACTIVITY4_DAILY,
        		COLUMN_NAME_ACTIVITY5_DAILY}, null, null, null, null, null);
    }

//fetch times method
    public Cursor fetchTimeMorning(Long mRowId)
    {
    	return mDb.query(DATABASE_TABLE, new String[] {COLUMN_NAME_MORNING_MINUTE,COLUMN_NAME_MORNING_HOUR},
    			null, null, null, null, null);
    }
    
    public Cursor fetchTimeEvening(Long mRowId)
    {
    	return mDb.query(DATABASE_TABLE, new String[] {COLUMN_NAME_EVENING_MINUTE,COLUMN_NAME_EVENING_HOUR},
    			null, null, null, null, null);
    }
    
    public Cursor fetchContacts(Long mRowId)
    {
        	return mDb.query(DATABASE_TABLE, new String[] {KEY_ROWID, COLUMN_NAME_CONTACT1,COLUMN_NAME_CONTACT2,
        			COLUMN_NAME_CONTACT3,COLUMN_NAME_CONTACT4,COLUMN_NAME_CONTACT5},
        			null, null, null, null, null);
  
    }
    
    public Cursor fetchAll(Long mRowId)
    {
    	return mDb.query(DATABASE_TABLE, new String[] {KEY_ROWID, COLUMN_NAME_VALUE1_RELATIONSHIP,
        		COLUMN_NAME_ACTIVITY1_RELATIONSHIP,COLUMN_NAME_ACTIVITY2_RELATIONSHIP,
        		COLUMN_NAME_ACTIVITY3_RELATIONSHIP,COLUMN_NAME_ACTIVITY4_RELATIONSHIP,
        		COLUMN_NAME_ACTIVITY5_RELATIONSHIP,COLUMN_NAME_VALUE1_EDUCATION,
        		COLUMN_NAME_ACTIVITY1_EDUCATION,COLUMN_NAME_ACTIVITY2_EDUCATION,
        		COLUMN_NAME_ACTIVITY3_EDUCATION,COLUMN_NAME_ACTIVITY4_EDUCATION, 
        		COLUMN_NAME_ACTIVITY5_EDUCATION, COLUMN_NAME_VALUE1_RECREATION,
        		COLUMN_NAME_ACTIVITY1_RECREATION,COLUMN_NAME_ACTIVITY2_RECREATION,
        		COLUMN_NAME_ACTIVITY3_RECREATION,COLUMN_NAME_ACTIVITY4_RECREATION,
        		COLUMN_NAME_ACTIVITY5_RECREATION, COLUMN_NAME_VALUE1_MIND,
        		COLUMN_NAME_ACTIVITY1_MIND,COLUMN_NAME_ACTIVITY2_MIND,
        		COLUMN_NAME_ACTIVITY3_MIND,COLUMN_NAME_ACTIVITY4_MIND,
        		COLUMN_NAME_ACTIVITY5_MIND, COLUMN_NAME_VALUE1_DAILY,
        		COLUMN_NAME_ACTIVITY1_DAILY,COLUMN_NAME_ACTIVITY2_DAILY,
        		COLUMN_NAME_ACTIVITY3_DAILY,COLUMN_NAME_ACTIVITY4_DAILY,
        		COLUMN_NAME_ACTIVITY5_DAILY},
    			null, null, null, null, null);	
    }

//fetch all values
    
//fetch all activities method 
    
//fetch EVERYTHING 
 
//fetch all enjoyment

//fetch all importance 

//deleted the fetchNote method. Not relevant! 
    
}    

