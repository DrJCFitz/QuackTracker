package com.drjcfitz.android.quacktracker.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class QuackDatabaseHelper extends SQLiteOpenHelper {
	private static final String TAG = "QuackDatabaseHelper";
    private static final String DB_NAME = "duck_data";
    private static final int DB_VERSION = 1;
    
	public static final String TABLE_LOCATION = "location";
	public static final String COLUMN_LOCATION_ID = "_id";
	public static final String COLUMN_LOCATION_ADDRESS = "address";

	public static final String TABLE_HOUSE = "house";
	public static final String COLUMN_HOUSE_IDENTIFIER = "identifier";
	public static final String COLUMN_HOUSE_LOCATION_ID = "location_id";
	
	public static final String TABLE_DUCK = "duck";
	public static final String COLUMN_DUCK_HOUSE = "house_id";
	public static final String COLUMN_DUCK_DATE = "date";
	public static final String COLUMN_DUCK_TIME = "time";
	public static final String COLUMN_DUCK_LIVE = "live";
	public static final String COLUMN_DUCK_DEAD = "dead";
	public static final String COLUMN_DUCK_COMMENT = "comment";

    public QuackDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        //quackContentResolver = context.getContentResolver();
    }
    
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table location (" + 
				" _id integer primary key autoincrement," +
				" address varchar(30))"); 
		Log.i(TAG, "SQLite Database onCreate(); " + TABLE_LOCATION + " created.");
		db.execSQL("create table house (" + 
				" identifier varchar(15)," +
				" loc_id references location(_id))"); 
		Log.i(TAG, "SQLite Database onCreate(); " + TABLE_HOUSE + " created.");
		db.execSQL("create table duck (" + 
				" _id integer primary key autoincrement," +
				" house_id references house(identifier)," + 
				" date varchar(30), time varchar(10), "+
				" live int, dead int, comment varchar(200))"); 
		Log.i(TAG, "SQLite Database onCreate(); " + TABLE_DUCK + " created.");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
