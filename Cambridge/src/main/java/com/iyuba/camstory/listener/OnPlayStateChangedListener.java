package com.iyuba.camstory.listener;

public interface OnPlayStateChangedListener {
	void onPrepared();
	
	void playCompletion();

	void playFaild();
}
