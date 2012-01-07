package com.feigdev.simplelocations;

public interface LocationUpdateListener {
	public void onUpdate();
	public void onStatusUpdate();
	public void onAvailable();
	public void onUnAvailable();
}
