package com.drjcfitz.android.quacktracker;

import android.support.v4.app.Fragment;

public class QuackEntryActivity extends SingleFragmentActivity {

	public Fragment createFragment() {
		return new QuackEntryFragment();
	}

}
