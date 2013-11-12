package com.drjcfitz.android.quacktracker;

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
	
	//private ContentResolver quackContentResolver;

    public QuackDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        //quackContentResolver = context.getContentResolver();
    }
    
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table location (" + 
				" _id integer primary key autoincrement," +
				" address varchar(30))"); 
		Log.i(TAG, "SQLite Database onCreate(); " + TABLE_LOCATION + "created.");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
