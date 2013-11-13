package com.drjcfitz.android.quacktracker;

import java.util.ArrayList;
import java.util.Date;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.drjcfitz.android.quacktracker.data.Ducks;
import com.drjcfitz.android.quacktracker.data.QuackDataProvider;
import com.drjcfitz.android.quacktracker.data.QuackDatabaseHelper;
import com.drjcfitz.android.quacktracker.dialog.DatePickerFragment;
import com.drjcfitz.android.quacktracker.dialog.LocationDialog;
import com.drjcfitz.android.quacktracker.dialog.LocationHouseDialog;
import com.drjcfitz.android.quacktracker.dialog.TimePickerFragment;

public class QuackEntryFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
	private static final int INIT_SPINNER_LOCATION = 0;
	private static final int INIT_SPINNER_HOUSE = 1;
	
	public static final String TAG = "QuackEntryFragment";
	public static final String DIALOG_DATE = "date";
	public static final String DIALOG_TIME = "time";
	public static final String DIALOG_LOCATION = "location";
	public static final String DIALOG_LOCATION_HOUSE = "location_house";
	public static final int REQUEST_DATE = 0;
	public static final int REQUEST_TIME = 1;
	public static final int REQUEST_LOCATION = 2;
	public static final int REQUEST_HOUSE = 3;
	public static final int REQUEST_LOCATION_HOUSE = 4;

	private Button mDateButton, mTimeButton;
	private Spinner mLocSpinner, mHouseSpinner;
	private EditText mEditLive, mEditDead;
	private Ducks mDuck;
	private Long spinnerLocId, spinnerHouseId;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		Log.i(TAG, "QuackEntryFragment onCreate()");
		
		Intent i = getActivity().getIntent();
		if (i.hasExtra(QuackListFragment.EXTRA_NAV_LOCATION)) {
			spinnerLocId = i.getLongExtra(QuackListFragment.EXTRA_NAV_LOCATION, 0);
			spinnerHouseId = null;
		} else if (i.hasExtra(QuackListFragment.NEW_LOCATION)) {
			spinnerLocId = i.getLongExtra(QuackListFragment.NEW_LOCATION, 0);
			spinnerHouseId = i.getLongExtra(QuackListFragment.NEW_HOUSE, 0);
		}
		
		mDuck = new Ducks();
		
		getLoaderManager().initLoader(INIT_SPINNER_LOCATION, null, this);
		getLoaderManager().initLoader(INIT_SPINNER_HOUSE, null, this);
	}
	
	@TargetApi(11)
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_entry, null);
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			if (NavUtils.getParentActivityName(getActivity()) != null) {
				getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
			}
		}
			
		mLocSpinner = (Spinner)v.findViewById(R.id.entryLocationSpinner);
		mLocSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {		
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {
					int count = 0;
					if (count > 0) {
						switch (pos) {
						case 0:
							Log.i(TAG, "Create a new Location table");
							FragmentManager fm = getActivity().getSupportFragmentManager();
							LocationDialog locDialog = new LocationDialog();
							locDialog.setTargetFragment(QuackEntryFragment.this, REQUEST_LOCATION);
							locDialog.show(fm, DIALOG_LOCATION);
							break;
						default:
							Log.i(TAG, "No Location selected");
							break;
						}
					}
					count++;
			};

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}				
		});
		
		mHouseSpinner = (Spinner)v.findViewById(R.id.entryHouseSpinner);
		mHouseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			int count = 0;
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {
				if (count > 0) {
					switch (pos) {
					default:
						break;
					}
				}
				count++;
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}				
		});
			
		mDateButton = (Button)v.findViewById(R.id.entryDateButton);
		mDateButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				FragmentManager fm = getActivity().getSupportFragmentManager();
				//DatePickerFragment dialog = new DatePickerFragment();
				DatePickerFragment dialog = DatePickerFragment.newInstance(mDuck.getDate());
				dialog.setTargetFragment(QuackEntryFragment.this, REQUEST_DATE);
				dialog.show(fm, DIALOG_DATE);
			}
		});
		
		mTimeButton = (Button)v.findViewById(R.id.entryTimeButton);
		mTimeButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				FragmentManager fm = getActivity().getSupportFragmentManager();
				TimePickerFragment timeDialog = TimePickerFragment.newInstance(mDuck.getDate());
				timeDialog.setTargetFragment(QuackEntryFragment.this, REQUEST_TIME);
				timeDialog.show(fm, DIALOG_TIME);
			}
		});
		
		mEditLive = (EditText)v.findViewById(R.id.entryLiveCount);
		mEditLive.addTextChangedListener(new TextWatcher() {
			public void onTextChanged(CharSequence c, int start, int before, int count) {
				if (c.toString().trim().length() > 0) {
					mDuck.setLive(Integer.parseInt(c.toString()));
				}					
			}
			
			public void beforeTextChanged(CharSequence c, int start, int count, int after) {
				//
			}
			
			public void afterTextChanged(Editable c) {
			}
		});

		mEditDead = (EditText)v.findViewById(R.id.entryDeadCount);
		mEditDead.addTextChangedListener(new TextWatcher() {
			public void onTextChanged(CharSequence c, int start, int before, int count) {
				if (c.toString().trim().length() > 0) {
					mDuck.setDead(Integer.parseInt(c.toString()));
				}
			}
			
			public void beforeTextChanged(CharSequence c, int start, int count, int after) {
				//
			}
			
			public void afterTextChanged(Editable c) {
			}
		});
		
		updateDate();

		return v;
	}
		
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.fragment_quack_entry_menu, menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.entry_menu_save:
				Toast.makeText(getActivity(), "Changes saved to database", Toast.LENGTH_LONG).show();
				return true;
			case android.R.id.home:
				if (NavUtils.getParentActivityName(getActivity()) != null) {
					NavUtils.navigateUpFromSameTask(getActivity());
				}
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	
	public void updateDate() {
		mDateButton.setText(DateFormat.format("EEEE, MMMM dd, yyyy", mDuck.getDate()).toString());
		mTimeButton.setText(DateFormat.format("h:mm a", mDuck.getDate()).toString());
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != Activity.RESULT_OK) return;
		
		if (requestCode == REQUEST_DATE) {
			Date date = (Date)data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
			mDuck.setDate(date);
		} else if (requestCode == REQUEST_TIME) {
			Date date = (Date)data.getSerializableExtra(TimePickerFragment.EXTRA_TIME);
			mDuck.setDate(date);
		} else if (requestCode == REQUEST_LOCATION ) {
			String location = data.getStringExtra(LocationHouseDialog.EXTRA_LOCATION);
			ContentValues contentValues = new ContentValues();
			contentValues.put(QuackDatabaseHelper.COLUMN_LOCATION_ADDRESS, location);
			Uri rowId = getActivity().getContentResolver().insert(QuackDataProvider.CONTENT_URI_LOCATION, contentValues);
			Toast.makeText(getActivity(), "The location " + location + " added as " + rowId.toString(), Toast.LENGTH_SHORT).show();
		} else if (requestCode == REQUEST_HOUSE ) {
			String house = data.getStringExtra(LocationHouseDialog.EXTRA_HOUSE);
			ContentValues contentValues = new ContentValues();
			contentValues.put(QuackDatabaseHelper.COLUMN_HOUSE_IDENTIFIER, house);
			Uri rowId = getActivity().getContentResolver().insert(QuackDataProvider.CONTENT_URI_HOUSE, contentValues);
			Toast.makeText(getActivity(), "The house " + house + " added as " + rowId.toString(), Toast.LENGTH_SHORT).show();
		} 
		updateDate();
	}
	
	public void setFieldsToDefaultValues() {
		mEditLive.setText(null);
		mEditDead.setText(null);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int loaderId, Bundle args) {
		if (loaderId == INIT_SPINNER_LOCATION) {
			return new CursorLoader(getActivity(),
					QuackDataProvider.CONTENT_URI_LOCATION,
					new String[] { QuackDatabaseHelper.COLUMN_LOCATION_ID, QuackDatabaseHelper.COLUMN_LOCATION_ADDRESS }, 
							null, null, null);
		} else {
			return new CursorLoader(getActivity(),
					QuackDataProvider.CONTENT_URI_HOUSE,
					new String[] { "rowId _id", QuackDatabaseHelper.COLUMN_HOUSE_IDENTIFIER}, 
							null, null, null);
		}
	}

	@Override
	public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
		Log.i(TAG, "QuackEntryFragment onLoadFinished()");
		int cursorId = cursorLoader.getId();
		if (cursorId == INIT_SPINNER_LOCATION) {
			SimpleCursorAdapter simpleAdapter = new SimpleCursorAdapter(getActivity().getBaseContext(),
					android.R.layout.simple_spinner_dropdown_item,
					cursor,
					new String[] { QuackDatabaseHelper.COLUMN_LOCATION_ADDRESS },
					new int[] { android.R.id.text1 },
					SimpleCursorAdapter.NO_SELECTION);
			mLocSpinner.setAdapter(simpleAdapter);
			mLocSpinner.setSelection(matchId(mLocSpinner, spinnerLocId));	
		} else {
			SimpleCursorAdapter simpleAdapter = new SimpleCursorAdapter(getActivity().getBaseContext(),
					android.R.layout.simple_spinner_dropdown_item,
					cursor,
					new String[] { QuackDatabaseHelper.COLUMN_HOUSE_IDENTIFIER },
					new int[] { android.R.id.text1 },
					SimpleCursorAdapter.NO_SELECTION);
			mHouseSpinner.setAdapter(simpleAdapter);
			if (spinnerHouseId != null) {
				mHouseSpinner.setSelection(matchId(mHouseSpinner, spinnerHouseId));
			}
		}
	}

	@Override
	public void onLoaderReset(Loader<Cursor> cursorLoader) {
		if (cursorLoader.getId() == INIT_SPINNER_LOCATION) {
			mLocSpinner.setAdapter(null);
		} else {
			mHouseSpinner.setAdapter(null);			
		}
	}
	
	
	private ArrayList<String> populateSpinner(int id, Cursor cursor) {
		ArrayList<String> spinnerArrayList = new ArrayList<String>();
		if (cursor.moveToFirst()) {
			while (!cursor.isAfterLast()) {
				if (id == INIT_SPINNER_LOCATION) {
					spinnerArrayList.add(cursor.getString(cursor.getColumnIndex(QuackDatabaseHelper.COLUMN_LOCATION_ADDRESS)));
					Log.i(TAG, "Adding to location spinner: " + String.valueOf(spinnerArrayList.size()));
				} else {
					spinnerArrayList.add(cursor.getString(cursor.getColumnIndex(QuackDatabaseHelper.COLUMN_HOUSE_IDENTIFIER)));
					Log.i(TAG, "Adding to house spinner: " + String.valueOf(spinnerArrayList.size()));
					}
				cursor.moveToNext();
			}
		}
		return spinnerArrayList;
	}
	
	private int matchId(Spinner spinner, Long id) {
		int inc;
		for (inc = 0;  inc < spinner.getCount() - 1; inc++) {
			Log.i(TAG, spinner.getCount() + " total items in spinner.\n" + 
					" current spinner item is " + spinner.getItemAtPosition(inc).toString() + "\n" + 
					" spinner item id is " + spinner.getItemIdAtPosition(inc) + "\n" + 
					" adapter item id is " + spinner.getAdapter().getItemId(inc) + "\n" +
					" and adapter object is " + spinner.getAdapter().getItem(inc) + "\n" +
					" and target item id is " + String.valueOf(id));
			// need to account for discrepancy of 1 
			if (spinner.getAdapter().getItemId(inc) == id) { 
				break;
			}
		}
		return inc;
	}
}
