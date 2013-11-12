package com.drjcfitz.android.quacktracker;


import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class QuackLocationProvider extends ContentProvider {
	private static final String AUTHORITY = "com.drjcfitz.android.quacktracker.quacklocationprovider";
	private static final String BASE_LOC = "location";
	public static final Uri CONTENT_URI = Uri.parse("content://" + 
			AUTHORITY + "/" + BASE_LOC);
	private static final int LOCATION = 1;
	private static final int LOCATION_ID = 2;
	
	private QuackDatabaseHelper quackDb;
	
	

	public QuackLocationProvider() {
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
		SQLiteDatabase writeDb = quackDb.getWritableDatabase();
		long rowId = 0;
		switch (uriType) {
		case LOCATION:
			rowId = writeDb.insert(QuackDatabaseHelper.TABLE_LOCATION, null, values);
			break;
		case LOCATION_ID:
			break;
		default:
			throw new UnsupportedOperationException("Not yet implemented");
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return Uri.parse(BASE_LOC + "/" + rowId);
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
		queryBuilder.setTables(QuackDatabaseHelper.TABLE_LOCATION);
		int uriType = sURIMatcher.match(uri);
		switch (uriType) {
		case LOCATION:
			//queryBuilder.appendWhere(QuackDatabaseHelper.COLUMN_LOCATION_ID + " = ?");
			break;
		case LOCATION_ID:
			queryBuilder.appendWhere(QuackDatabaseHelper.COLUMN_LOCATION_ID + " = ?");
			break;
		default:
			throw new UnsupportedOperationException("Not yet implemented");
		}
		Cursor cursor = queryBuilder.query(quackDb.getReadableDatabase(), 
				null, null, null, null, null, null);
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
		sURIMatcher.addURI(AUTHORITY, BASE_LOC, LOCATION);
		sURIMatcher.addURI(AUTHORITY, BASE_LOC + "/#", LOCATION_ID);
	}
}
