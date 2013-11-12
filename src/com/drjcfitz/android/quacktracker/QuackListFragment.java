package com.drjcfitz.android.quacktracker;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;



public class QuackListFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {
	private SimpleCursorAdapter locAdapter;
//	implements ActionBar.OnNavigationListener  {
//	private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";

	private static final String TAG = "DuckListFragment";
	private static final String DIALOG_LOCATION = "com.drjcfitz.android.duckcount.location";
	public static final String INSERT_LOCATION = "com.drjcfitz.android.duckcount.insert_location";
//	private static final String DIALOG_LOCATION_HOUSE = "com.drjcfitz.android.duckcount.location_house";
	private static final int LISTVIEW_LOCATION = 0;
	//private static final int SPINNER_LOCATION = 1; 
	private static final int REQUEST_LOCATION_HOUSE = 3;
	
	//private SimpleCursorAdapter locationCursorAdapter;
	//private ActionBar actionBar; 
	//private ActionBar.OnNavigationListener mNavCallback;
	
	/*@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.list_fragment_menu, menu);
	}
	
	@TargetApi(11)
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.list_menu_delete_table:
				/*DuckDatabaseHelper duckDbDeleteHelper = new DuckDatabaseHelper(getActivity());
				duckDbDeleteHelper.deleteTable();
				return true;
			case R.id.list_menu_add:
				Intent i = new Intent(getActivity(), EntryActivity.class);
				startActivity(i);
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}*/
	
	@TargetApi(11)
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setHasOptionsMenu(true);
		setRetainInstance(true);
		//getActivity().setTitle(null);
		getLoaderManager().initLoader(LISTVIEW_LOCATION, null, this);

		/*// Set up the action bar to show a dropdown list.
		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

		// Set up the dropdown list navigation in the action bar.
		actionBar.setListNavigationCallbacks(
		// Specify a SpinnerAdapter to populate the dropdown list.
				new ArrayAdapter<String>(getActionBarThemedContextCompat(),
						android.R.layout.simple_list_item_1,
						android.R.id.text1, new String[] {
								getString(R.string.title_section1),
								getString(R.string.title_section2),
								getString(R.string.title_section3), }), this);

		actionBar = getActivity().getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);	
	
		getLoaderManager().initLoader(SPINNER_LOCATION, null, this); */
		locAdapter = new SimpleCursorAdapter(getActivity().getApplicationContext(),
				android.R.layout.simple_list_item_1,
				null,
				new String[] { QuackDatabaseHelper.COLUMN_LOCATION_ADDRESS},
				new int[] { android.R.id.text1 }, 
				CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
		
		setListAdapter(locAdapter);
	}
	
	@TargetApi(11)
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_quack_list, parent, false);

		ListView listView = (ListView)v.findViewById(android.R.id.list);
		listView.setEmptyView(v.findViewById(android.R.id.empty));
		//duckAdapter = new DuckListCursorAdapter(getActivity(), null); 
		
		ImageButton addNewEntry = (ImageButton)v.findViewById(R.id.empty_addImageButton);
		addNewEntry.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				FragmentManager fm = getActivity().getSupportFragmentManager();
				//LocationHouseDialog locHouseDialog = new LocationHouseDialog();
				LocationDialog locHouseDialog = new LocationDialog();
				locHouseDialog.setTargetFragment(QuackListFragment.this, REQUEST_LOCATION_HOUSE);
				locHouseDialog.show(fm, DIALOG_LOCATION);
			}
		});
		
		/*locationCursorAdapter = new SimpleCursorAdapter(getActivity().getBaseContext(), 
				android.R.layout.simple_spinner_dropdown_item,
				null,
				new String[]{ DuckDatabaseHelper.COLUMN_LOCATION_ADDRESS }, 
				new int[]{android.R.id.text1},1);
		
		actionBar.setListNavigationCallbacks(locationCursorAdapter, mNavCallback);

		mNavCallback = new OnNavigationListener() {
			@Override
			public boolean onNavigationItemSelected(int position, long id){
				return true;
			}
		};*/
		//duckDbHelper.close();
		
		//duckLocationSpinner = (Spinner)v.findViewById(R.id.duck_loc_spinner);
		//duckLocationSpinner.setAdapter(duckLocAdapter);*/
		
		/*if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
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
					inflater.inflate(R.menu.duck_list_item_context, menu);
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
						//DuckDatabaseHelper duckDbHelper = new DuckDatabaseHelper(getActivity());
						/* for (int i = duckAdapter.getCount() - 1; i >= 0; i--) {
							if (getListView().isItemChecked(i)) {
								duckDbHelper.deleteDuck(duckAdapter.getItemId(i));
								Log.i(TAG, "List Item # " + String.valueOf(i) + " selected");
							}
						}
						mode.finish(); */
	/*					return true;
					default:
						return false;
					}
				}
				@Override
				public void onDestroyActionMode(ActionMode mode) {
				}
			});
		} */
		return v;			
	}
	
	/*@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Crime c = ((CrimeAdapter)getListAdapter()).getItem(position);
		//Log.d(TAG, c.getTitle() + " was clicked.");	
		
		//Intent i = new Intent(getActivity(), CrimeActivity.class);
		Intent i = new Intent(getActivity(), CrimePagerActivity.class);
		i.putExtra(CrimeFragment.EXTRA_CRIME_ID, c.getId());
		startActivity(i);
		//startActivityForResult(i, REQUEST_CRIME);
	}*/
/*	private static class DuckListCursorLoader extends SQLiteCursorLoader {
		public DuckListCursorLoader(Context context) {
			super(context);
		}
		
		@Override
		protected Cursor loadCursor() {
			return DuckManager.get(getContext()).queryDucks();
		}
	}
	
	public class DuckListCursorAdapter extends CursorAdapter {
		public DuckListCursorAdapter(Context context, Cursor cursor) {
			super(context, cursor, 0);
		}

		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			Log.i(TAG, cursor.getCount() + " entries");
			//TextView testField = (TextView)view.findViewById(android.R.id.text1);
			//testField.setText(c.getString(c.getColumnIndex("time")));
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
			View v = inflater.inflate(R.layout.duck_list_fragment, null);
		
			return v;
		}

	}*/
	
	/*@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		// Restore the previously serialized current dropdown position.
		if (savedInstanceState.containsKey(STATE_SELECTED_NAVIGATION_ITEM)) {
			getActionBar().setSelectedNavigationItem(
					savedInstanceState.getInt(STATE_SELECTED_NAVIGATION_ITEM));
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// Serialize the current dropdown position.
		outState.putInt(STATE_SELECTED_NAVIGATION_ITEM, getActionBar()
				.getSelectedNavigationIndex());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.duck_list, menu);
		return true;
	}

	@Override
	public boolean onNavigationItemSelected(int position, long id) {
		// When the given dropdown item is selected, show its contents in the
		// container view.
		Fragment fragment = new DummySectionFragment();
		Bundle args = new Bundle();
		args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
		fragment.setArguments(args);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.container, fragment).commit();
		return true;
	} */

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != Activity.RESULT_OK) return;
		
		if (requestCode == REQUEST_LOCATION_HOUSE ) {
			String location = data.getStringExtra(LocationDialog.EXTRA_LOCATION);
			Log.i(TAG, "Received location as " + location);
			ContentValues contentValues = new ContentValues();
			contentValues.put(QuackDatabaseHelper.COLUMN_LOCATION_ADDRESS, location);
			Uri locUri = getActivity().getApplicationContext().getContentResolver().insert(QuackLocationProvider.CONTENT_URI, contentValues);
			//long locId = mDuckManager.createLocation(location);
			Toast.makeText(getActivity(), "The location " + location + " added as " + locUri.toString(), Toast.LENGTH_SHORT).show();
			locAdapter.notifyDataSetChanged();
		} 
	}
	
	@Override
	public Loader<Cursor> onCreateLoader(int loadId, Bundle args) {
		//if (loadId == LISTVIEW_DUCK) {
		CursorLoader cursorLoader = new CursorLoader(getActivity(),
				QuackLocationProvider.CONTENT_URI,
				new String[] { QuackDatabaseHelper.COLUMN_LOCATION_ID, QuackDatabaseHelper.COLUMN_LOCATION_ADDRESS },
				null, null, null);
		Log.i(TAG, "URI:" + QuackLocationProvider.CONTENT_URI + "; Projections: " +
				QuackDatabaseHelper.COLUMN_LOCATION_ID + " and " + QuackDatabaseHelper.COLUMN_LOCATION_ADDRESS);

		return cursorLoader;
		//} else {
		//	return new LocationSpinnerLoader(getActivity());
		//}
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loadCursor, Cursor cursor) {
		//if (loadCursor.getId() == LISTVIEW_LOCATION) {
		Log.i(TAG, "Cursor elements = " + String.valueOf(cursor.getCount()));
		locAdapter.swapCursor(cursor);
			//DuckCursor located in DuckDatabaseHelper, creates model layer from database
	//		setListAdapter(duckAdapter);
		//} else if (loadCursor.getId() == SPINNER_LOCATION) {
			//locationCursorAdapter.swapCursor(cursor);
		//}
	}

	@Override
	public void onLoaderReset(Loader<Cursor> duckLoadCursor) {
		locAdapter.swapCursor(null);
	}

}
