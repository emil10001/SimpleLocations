package com.feigdev.simplelocations;

import java.util.ArrayList;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

/**
 * This is a location controller capable of handling a single location
 * provider at a time. It keeps an array of previous locations in memory.
 * If multiple providers are needed, use multiple instances of this class,
 * and manage them externally. If you use this with multiple providers, it 
 * will not function properly.
 * 
 * @author emil10001
 *
 */
public class LocationController implements LocationListener {
	private ArrayList<Location> locations;
	private Handler handle;
	private boolean enabled;
	private String provider;
	public static final int UPDATE = 10011;
	public static final int STATUS = 10012;
	public static final int UNAVAILABLE = 10013;
	public static final int AVAILABLE = 10014;
	
	public LocationController(Handler handle){
		locations = new ArrayList<Location>();
		this.handle = handle;
		enabled = true;
		provider = "";
	}
	
	public String getProvider(){
		return provider;
	}

	@Override
	public void onLocationChanged(Location location) {
		locations.add(location);
		if (handle == null){
			return;
		}
		provider = location.getProvider();
		Message msg = new Message();
		msg.what = UPDATE;
		msg.obj = provider;
		handle.sendMessage(msg);
	}

	@Override
	public void onProviderDisabled(String provider) {
		enabled = false;
		if (handle == null){
			return;
		}
		Message msg = new Message();
		msg.what = STATUS;
		handle.sendMessage(msg);
	}

	@Override
	public void onProviderEnabled(String provider) {
		enabled = true;
		if (handle == null){
			return;
		}
		Message msg = new Message();
		msg.what = STATUS;
		handle.sendMessage(msg);
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		if (handle == null){
			return;
		}
		Message msg;
		switch (status) {
			case LocationProvider.OUT_OF_SERVICE:
			case LocationProvider.TEMPORARILY_UNAVAILABLE:
				msg = new Message();
				msg.what = UNAVAILABLE;
				handle.sendMessage(msg);
				break;
			case LocationProvider.AVAILABLE:
				msg = new Message();
				msg.what = AVAILABLE;
				handle.sendMessage(msg);
				break;
		}
	}
	
	public boolean isEnabled(){
		return enabled;
	}
	
	/**
	 * 
	 * @return all the previous locations stored in memory
	 */
	public ArrayList<Location> getLocations(){
		return locations;
	}
}
