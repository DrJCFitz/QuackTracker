package com.drjcfitz.android.quacktracker;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.drjcfitz.android.quacktracker.data.QuackDataProvider;
import com.drjcfitz.android.quacktracker.data.QuackDatabaseHelper;
import com.drjcfitz.android.quacktracker.dialog.LocationHouseDialog;

public class QuackListFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor>, ActionBar.OnNavigationListener {
	private static final String TAG = "QuackListFragment";
	private static final String DIALOG_LOCATION_HOUSE = "com.drjcfitz.android.quacktracker.location_house";
	public static final String NEW_LOCATION = "com.drjcfitz.android.quacktracker.new_location";
	public static final String NEW_HOUSE = "com.drjcfitz.android.quacktracker.new_house";
	public static final String EXTRA_NAV_LOCATION = "com.drjcfitz.android.quacktracker.extra_nav_location";
	private static final int LISTVIEW_LOCATION = 0;
	private static final int SPINNER_LOCATION = 1; 
	public static final int REQUEST_LOCATION_HOUSE = 4;
	private ActionBar actionBar;
	private ActionBar.OnNavigationListener mNavCallback;
	private SimpleCursorAdapter locationCursorAdapter;
	private Cursor locationCursor;
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.fragment_quack_list_menu, menu);
	}
	
	@TargetApi(11)
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.list_menu_delete_table:
				//disable for now
				return true;
			case R.id.list_menu_add:
				Intent i = new Intent(getActivity(), QuackEntryActivity.class);
				i.putExtra(EXTRA_NAV_LOCATION, locationCursorAdapter.getItemId(actionBar.getSelectedNavigationIndex()));
				Log.i(TAG, "locSpinnerId = " + String.valueOf(locationCursorAdapter.getItemId(actionBar.getSelectedNavigationIndex())));
				startActivity(i);
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	
	@TargetApi(11)
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		setRetainInstance(true);
		getActivity().setTitle(null);
		getLoaderManager().initLoader(LISTVIEW_LOCATION, null, this);

		// Set up the action bar to show a dropdown list.
		actionBar = getActivity().getActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

		getLoaderManager().initLoader(SPINNER_LOCATION, null, this); 
		locationCursorAdapter = new SimpleCursorAdapter(getActivity().getApplicationContext(),
				android.R.layout.simple_spinner_dropdown_item,
				null,
				new String[] { QuackDatabaseHelper.COLUMN_LOCATION_ADDRESS },
				new int[] { android.R.id.text1 }, 
				CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
		actionBar.setListNavigationCallbacks(locationCursorAdapter, mNavCallback);
	}
	
	@TargetApi(11)
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_quack_list_empty, parent, false);

		ListView listView = (ListView)v.findViewById(android.R.id.list);
		listView.setEmptyView(v.findViewById(android.R.id.empty));
		setListAdapter(null);	
		
		ImageButton addNewEntry = (ImageButton)v.findViewById(R.id.empty_addImageButton);
		addNewEntry.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				FragmentManager fm = getActivity().getSupportFragmentManager();
				LocationHouseDialog locHouseDialog = new LocationHouseDialog();
				locHouseDialog.setTargetFragment(QuackListFragment.this, REQUEST_LOCATION_HOUSE);
				locHouseDialog.show(fm, DIALOG_LOCATION_HOUSE);
			}
		});
		
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
			registerForContextMenu(listView);
		} else {
			listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);
			listView.setMultiChoiceModeListener(new MultiChoiceModeListener() {
				@Override
				public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
				}
				@Override
				public boolean onCreateActionMode(ActionMode mode, Menu menu) {
					MenuInflater inflater = mode.getMenuInflater();
					inflater.inflate(R.menu.quack_list_item_context, menu);
					return true;
				}
				@Override
				public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
					return false;
				}
				@Override
				public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
					switch (item.getItemId()) {
					case R.id.menu_item_delete_entry:
						 /*for (int i = duckAdapter.getCount() - 1; i >= 0; i--) {
							if (getListView().isItemChecked(i)) {
								duckDbHelper.deleteDuck(duckAdapter.getItemId(i));
								Log.i(TAG, "List Item # " + String.valueOf(i) + " selected");
							}
						}*/
						return true;
					default:
						return false;
					}
				}
				@Override
				public void onDestroyActionMode(ActionMode mode) {
				}
			});
		} 
		return v;			
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
	}
	
	public class DuckListCursorAdapter extends CursorAdapter {
		public DuckListCursorAdapter(Context context, Cursor cursor) {
			super(context, cursor, 0);
		}

		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			Log.i(TAG, cursor.getCount() + " entries");
			TextView duckTime = (TextView)view.findViewById(R.id.list_duckTime);
			duckTime.setText(cursor.getString(cursor.getColumnIndex("time")));

			TextView duckLive = (TextView)view.findViewById(R.id.list_duckLive);
			duckLive.setText(cursor.getString(cursor.getColumnIndex("live")));
			
			TextView duckDead = (TextView)view.findViewById(R.id.list_duckDead);
			duckDead.setText(cursor.getString(cursor.getColumnIndex("dead")));
		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			LayoutInflater inflater = LayoutInflater.from(parent.getContext());
			View v = inflater.inflate(R.layout.fragment_quack_list_empty, null);
		
			return v;
		}

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != Activity.RESULT_OK) return;
		
		if (requestCode == REQUEST_LOCATION_HOUSE ) {
			String location = data.getStringExtra(LocationHouseDialog.EXTRA_LOCATION);
			String house = data.getStringExtra(LocationHouseDialog.EXTRA_HOUSE);
			
			Log.i(TAG, "Received location as " + location); 
			
			ContentValues contentValues = new ContentValues();
			contentValues.put(QuackDatabaseHelper.COLUMN_LOCATION_ADDRESS, location);
			Uri locUri = getActivity().getApplicationContext().getContentResolver().insert(QuackDataProvider.CONTENT_URI_LOCATION, contentValues);
			Toast.makeText(getActivity(), "The location " + location + " added as " + locUri.toString(), Toast.LENGTH_SHORT).show();
			
			contentValues.clear();
			
			contentValues.put(QuackDatabaseHelper.COLUMN_HOUSE_IDENTIFIER, house);
			Uri houseUri = getActivity().getApplicationContext().getContentResolver().insert(QuackDataProvider.CONTENT_URI_HOUSE, contentValues);
			Toast.makeText(getActivity(), "The house " + house + " added as " + houseUri.toString(), Toast.LENGTH_SHORT).show();
			
			Intent goToEntryFrag = new Intent();
			goToEntryFrag.setClass(getActivity(), QuackEntryActivity.class);
			goToEntryFrag.putExtra(NEW_LOCATION, Long.parseLong(locUri.getLastPathSegment()));
			goToEntryFrag.putExtra(NEW_HOUSE, Long.parseLong(houseUri.getLastPathSegment()));
			startActivity(goToEntryFrag);
		} 
	}
	
	@Override
	public Loader<Cursor> onCreateLoader(int loadId, Bundle args) {
		CursorLoader cursorLoader = new CursorLoader(getActivity(),
				QuackDataProvider.CONTENT_URI_LOCATION,
				new String[] { QuackDatabaseHelper.COLUMN_LOCATION_ID, QuackDatabaseHelper.COLUMN_LOCATION_ADDRESS },
				null, null, null);
		Log.i(TAG, "URI:" + QuackDataProvider.CONTENT_URI_LOCATION + "; Projections: " +
				QuackDatabaseHelper.COLUMN_LOCATION_ID + " and " + QuackDatabaseHelper.COLUMN_LOCATION_ADDRESS);

		return cursorLoader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loadCursor, Cursor cursor) {
		Log.i(TAG, "Cursor elements = " + String.valueOf(cursor.getCount()));
		locationCursor = cursor;
		locationCursorAdapter.swapCursor(cursor);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> duckLoadCursor) {
		locationCursorAdapter.swapCursor(null);
	}

	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
		Log.i(TAG, "navigation item selected at position " + String.valueOf(itemPosition) + " and itemId " + String.valueOf(itemId));
		return true;
	}
	
	@Override
	public void onPause() {
		super.onPause();
		Log.i(TAG, "onPause() called");
		if (locationCursor != null) {
			locationCursor.close();
		}
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.i(TAG, "onDestroy() called");
	}

}