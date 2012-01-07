package com.feigdev.simplelocations;

import java.util.ArrayList;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationProvider;
import android.os.Bundle;

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
	private boolean enabled;
	private String provider;
	private LocationUpdateListener locListen;
	public static final int UPDATE = 10011;
	public static final int STATUS = 10012;
	public static final int UNAVAILABLE = 10013;
	public static final int AVAILABLE = 10014;
	
	public LocationController(LocationUpdateListener locListen){
		locations = new ArrayList<Location>();
		this.locListen = locListen;
		enabled = true;
		provider = "";
	}
	
	public String getProvider(){
		return provider;
	}

	@Override
	public void onLocationChanged(Location location) {
		locations.add(location);
		if (locListen == null){
			return;
		}
		provider = location.getProvider();
		locListen.onUpdate();
	}

	@Override
	public void onProviderDisabled(String provider) {
		enabled = false;
		if (locListen == null){
			return;
		}
		locListen.onStatusUpdate();
	}

	@Override
	public void onProviderEnabled(String provider) {
		enabled = true;
		if (locListen == null){
			return;
		}
		locListen.onStatusUpdate();
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		if (locListen == null){
			return;
		}
		switch (status) {
			case LocationProvider.OUT_OF_SERVICE:
			case LocationProvider.TEMPORARILY_UNAVAILABLE:
				locListen.onUnAvailable();
				break;
			case LocationProvider.AVAILABLE:
				locListen.onAvailable();
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
