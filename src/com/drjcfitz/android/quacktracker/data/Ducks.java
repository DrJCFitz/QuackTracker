package com.drjcfitz.android.quacktracker.data;

import java.util.Date;

public class Ducks {
	private long id;
	private Date mDate;
	private int mLive, mDead;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public Ducks() {
		mDate = new Date();
	}
	public Date getDate() {
		return mDate;
	}
	public void setDate(Date date) {
		mDate = date;
	}
	public int getLive() {
		return mLive;
	}
	public void setLive(int live) {
		mLive = live;
	}
	public int getDead() {
		return mDead;
	}
	public void setDead(int dead) {
		mDead = dead;
	}
	
	
}
