package com.drjcfitz.android.quacktracker.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.drjcfitz.android.quacktracker.R;

public class LocationHouseDialog extends DialogFragment {
	public static final String EXTRA_LOCATION = "com.drjcfitz.android.quacktracker.dialog.location";
	public static final String EXTRA_HOUSE = "com.drjcfitz.android.quacktracker.dialog.house";
	private EditText locationInput, houseInput;
	private String mLocation, mHouse;
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		View v = getActivity().getLayoutInflater()
				.inflate(R.layout.location_house_dialog, null);
		
		locationInput = (EditText)v.findViewById(R.id.input_LocName);
		locationInput.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				mLocation = locationInput.getText().toString();
				//getArguments().putString(EXTRA_LOCATION, mLocation);
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		
		houseInput = (EditText)v.findViewById(R.id.input_HouseName);
		houseInput.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				mHouse = houseInput.getText().toString();
				//getArguments().putString(EXTRA_LOCATION, mLocation);
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		
		return new AlertDialog.Builder(getActivity())
		.setView(v)
		.setTitle(R.string.add_location)
		.setPositiveButton(android.R.string.ok,
			new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					sendResult(Activity.RESULT_OK);
				}
			})
		.create();
	}
	
	private void sendResult(int resultCode) {
		if (getTargetFragment() == null)
			return;
		
		Intent i = new Intent();
		i.putExtra(EXTRA_LOCATION, mLocation);
		i.putExtra(EXTRA_HOUSE, mHouse);
		
		getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, i);
	}

}
