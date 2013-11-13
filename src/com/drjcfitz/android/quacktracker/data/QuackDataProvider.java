package com.drjcfitz.android.quacktracker.data;


import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

public class QuackDataProvider extends ContentProvider {
	private static final String TAG = "QuackDataProvider";
	private static final String AUTHORITY = "com.drjcfitz.android.quacktracker.data.quackdataprovider";
	private static final String BASE_LOCATION = "location";
	private static final String BASE_HOUSE = "house";
	private static final String BASE_DUCK = "duck";
	
	public static final Uri CONTENT_URI_LOCATION = Uri.parse("content://" + 
			AUTHORITY + "/" + BASE_LOCATION);
	public static final Uri CONTENT_URI_HOUSE = Uri.parse("content://" + 
			AUTHORITY + "/" + BASE_HOUSE);
	public static final Uri CONTENT_URI_DUCK = Uri.parse("content://" + 
			AUTHORITY + "/" + BASE_DUCK);
	
	private static final int LOCATION = 1;
	private static final int LOCATION_ID = 2;
	private static final int HOUSE = 3;
	private static final int HOUSE_ID = 4;
	private static final int DUCK = 5;
	private static final int DUCK_ID = 6;
	
	private QuackDatabaseHelper quackDb;	

	public QuackDataProvider() {
	}
	
	@Override
	public boolean onCreate() {
		quackDb = new QuackDatabaseHelper(getContext());
		return true;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// Implement this to handle requests to delete one or more rows.
		throw new UnsupportedOperationException("Not yet implemented");
	}

	@Override
	public String getType(Uri uri) {
		// TODO: Implement this to handle requests for the MIME type of the data
		// at the given URI.
		throw new UnsupportedOperationException("Not yet implemented");
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		int uriType = sURIMatcher.match(uri);
		long rowId = 0;
		String uriString = "";
		switch (uriType) {
		case LOCATION:
			Log.i(TAG, "Within LOCATION");
			uriString = BASE_LOCATION;
			rowId = quackDb.getWritableDatabase().insert(QuackDatabaseHelper.TABLE_LOCATION, null, values);
			break;
		case LOCATION_ID:
			Log.i(TAG, "Within LOCATION_ID");
			uriString = BASE_LOCATION + "/#";
			rowId = quackDb.getWritableDatabase().insert(QuackDatabaseHelper.TABLE_LOCATION, null, values);
			break;
		case HOUSE:
			Log.i(TAG, "Within HOUSE");
			uriString = BASE_HOUSE;
			rowId = quackDb.getWritableDatabase().insert(QuackDatabaseHelper.TABLE_HOUSE, null, values);
			break;
		case HOUSE_ID:
			Log.i(TAG, "Within HOUSE_ID");
			uriString = BASE_HOUSE + "/#";
			rowId = quackDb.getWritableDatabase().insert(QuackDatabaseHelper.TABLE_HOUSE, null, values);
			break;
		case DUCK:
			Log.i(TAG, "Within DUCK");
			uriString = BASE_DUCK;
			rowId = quackDb.getWritableDatabase().insert(QuackDatabaseHelper.TABLE_DUCK, null, values);
			break;
		case DUCK_ID:
			Log.i(TAG, "Within DUCK_ID");
			uriString = BASE_DUCK + "/#";
			rowId = quackDb.getWritableDatabase().insert(QuackDatabaseHelper.TABLE_DUCK, null, values);
			break;
		default:
			throw new UnsupportedOperationException("Not yet implemented");
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return Uri.parse(uriString + "/" + rowId);
	}

	@Override
	public Cursor query(Uri uri, String[] projectionIn, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
		int uriType = sURIMatcher.match(uri);
		switch (uriType) {
		case LOCATION:
			queryBuilder.setTables(QuackDatabaseHelper.TABLE_LOCATION);
			//queryBuilder.appendWhere(QuackDatabaseHelper.COLUMN_LOCATION_ID + " = ?");
			break;
		case LOCATION_ID:
			queryBuilder.setTables(QuackDatabaseHelper.TABLE_LOCATION);
			//queryBuilder.appendWhere(QuackDatabaseHelper.COLUMN_LOCATION_ID + " = ?");
			break;
		case HOUSE:
			queryBuilder.setTables(QuackDatabaseHelper.TABLE_HOUSE);
			//queryBuilder.appendWhere(QuackDatabaseHelper.COLUMN_LOCATION_ID + " = ?");
			break;
		case HOUSE_ID:
			queryBuilder.setTables(QuackDatabaseHelper.TABLE_HOUSE);
			//queryBuilder.appendWhere(QuackDatabaseHelper.COLUMN_LOCATION_ID + " = ?");
			break;
		case DUCK:
			queryBuilder.setTables(QuackDatabaseHelper.TABLE_DUCK);
			//queryBuilder.appendWhere(QuackDatabaseHelper.COLUMN_LOCATION_ID + " = ?");
			break;
		case DUCK_ID:
			queryBuilder.setTables(QuackDatabaseHelper.TABLE_DUCK);
			//queryBuilder.appendWhere(QuackDatabaseHelper.COLUMN_LOCATION_ID + " = ?");
			break;
		default:
			throw new UnsupportedOperationException("Not yet implemented");
		}
		Cursor cursor = queryBuilder.query(quackDb.getReadableDatabase(), 
				projectionIn, selection, selectionArgs, null, null, sortOrder);
		cursor.setNotificationUri(getContext().getContentResolver(), uri);
		return cursor;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO: Implement this to handle requests to update one or more rows.
		throw new UnsupportedOperationException("Not yet implemented");
	}
	
	private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	static {
		sURIMatcher.addURI(AUTHORITY, BASE_LOCATION, LOCATION);
		sURIMatcher.addURI(AUTHORITY, BASE_LOCATION + "/#", LOCATION_ID);
		sURIMatcher.addURI(AUTHORITY, BASE_HOUSE, HOUSE);
		sURIMatcher.addURI(AUTHORITY, BASE_HOUSE + "/#", HOUSE_ID);
		sURIMatcher.addURI(AUTHORITY, BASE_DUCK, DUCK);
		sURIMatcher.addURI(AUTHORITY, BASE_DUCK + "/#", DUCK_ID);
	}
}
