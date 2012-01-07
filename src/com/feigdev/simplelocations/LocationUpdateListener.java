package com.feigdev.simplelocations;

public interface LocationUpdateListener {
	public void onLocUpdate();
	public void onStatusUpdate();
	public void onAvailable();
	public void onUnAvailable();
}
