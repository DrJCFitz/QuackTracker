package com.drjcfitz.android.quacktracker;

import android.support.v4.app.Fragment;


public class DuckListActivity extends SingleFragmentActivity {

	@Override
	protected Fragment createFragment() {
		return new QuackListFragment();
	}
}
