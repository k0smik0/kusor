package net.iubris.kusor2;

import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import io.nlopez.smartlocation.SmartLocation.LocationControl;
import io.nlopez.smartlocation.location.config.LocationParams;
import io.nlopez.smartlocation.location.providers.LocationGooglePlayServicesWithFallbackProvider;
import net.iubris.polaris.locator.core.Locator;
import net.iubris.polaris.locator.core.exceptions.LocationNullException;
import net.iubris.polaris.locator.core.updater.OnLocationUpdatedCallback;
import net.iubris.polaris.locator.core.updater.OnNoNewLocationTimeoutCallback;
import net.iubris.polaris.locator.utils.LocationStrategiesUtils;
import android.content.Context;
import android.location.Location;
//import android.util.Log;
import android.util.Log;

public class KLocatorSLL implements Locator {
	
	private static final int MAX_RETRY = 3;
	private final LocationControl locationController;
	private final OnLocationUpdatedListener onLocationUpdatedListener = new OnLocationUpdatedListener() {
		@Override
		public void onLocationUpdated(Location location) {
//			Log.d("KLocatorSLL:19","new location found: "+location);
			KLocatorSLL.this.locationCurrent = location;
		}
	};
	
	
	
	protected Location locationCurrent;
	private int minimumTimeThreshold;
	private int minimumDistanceThreshold;

	public KLocatorSLL(Context context, int minimumTimeThreshold, int minimumDistanceThreshold) {
		this.minimumTimeThreshold = minimumTimeThreshold;
		this.minimumDistanceThreshold = minimumDistanceThreshold;
		locationController = SmartLocation.with(context).location()
				.config(LocationParams.NAVIGATION)
				.provider( new LocationGooglePlayServicesWithFallbackProvider(context) );
	}

	@Override
	public synchronized Location getLocation() throws LocationNullException {
		Location locationLast = locationController.getLastLocation();
		if (locationLast==null && locationCurrent==null) {
			locationController.stop();
			locationController.start(onLocationUpdatedListener);
			
			throw new LocationNullException();
		}
		
		if (locationLast!=null && locationCurrent==null) {
//			Log.d("KLocatorSLL:40","current location is null, returning lastLocation: "+lastLocation);
			
			locationCurrent = locationLast;
			return locationCurrent;
		}
		
		if (locationLast==null && locationCurrent!=null) {
			return locationCurrent;
		}
		
		if (!isLocationBetter(locationLast)) {
//			Log.d("KLocatorSLL:60", "current location is worst, returning lastLocation: "+location);
			Log.d("KLocatorSLL:60", "new location ("+locationLast+") is worst, retrying/blocking...");
			
			locationLast = locationController.getLastLocation();
			int sleepCount = 0;
			while(!isLocationBetter(locationLast) && sleepCount<MAX_RETRY) {
				try { Thread.sleep(500); } catch (InterruptedException e) {}
				locationLast = locationController.getLastLocation();
				Log.d("KLocatorSLL", "trying new location: "+locationLast);
				sleepCount++;
			}
			if (!isLocationBetter(locationLast) && sleepCount<MAX_RETRY) {
				Log.d("KLocatorSLL:71", "new location ("+locationLast+") is however worst, but we return it anyway...");
			}
		} else {
			Log.d("KLocatorSLL:74", System.currentTimeMillis()+": current location is better, returning: "+locationCurrent);
		}
		locationCurrent = locationLast;
		return locationCurrent;
	}
	
	private boolean isLocationBetter(Location locationLast) {
		return (LocationStrategiesUtils.isLocationBetter(locationLast, this.locationCurrent, minimumTimeThreshold, minimumDistanceThreshold));
	}

	@Override
	public Integer getMinimumDistanceThreshold() {
		return minimumDistanceThreshold;
	}

	@Override
	public Integer getMinimumTimeThreshold() {
		return minimumTimeThreshold;
	}

	@Override
	public void startLocationUpdates() {
//		Log.d("KLocatorSLL:68","starting location updates");
		locationController.start( new OnLocationUpdatedListener() {
			@Override
			public void onLocationUpdated(Location location) {
//				Log.d("KLocatorSLL:73","new location found: "+location);
				KLocatorSLL.this.locationCurrent = location;
			}
		} );
//		Location location = locationController.getLastLocation();
//		Log.d("KLocatorSLL:81","location: "+location);
	}
	
	@Override
	public void startLocationUpdates(final OnLocationUpdatedCallback onLocationUpdatedCallback) {
		locationController.start( new OnLocationUpdatedListener() {
			@Override
			public void onLocationUpdated(Location location) {
//				Log.d("KLocatorSLL:73","new location found: "+location);
				KLocatorSLL.this.locationCurrent = location;
				onLocationUpdatedCallback.onLocationUpdated(location);
			}
		});
	}
	@Override
	public void startLocationUpdates(OnLocationUpdatedCallback onLocationUpdatedCallback,
			OnNoNewLocationTimeoutCallback onNoNewLocationTimeoutCallback) {
		startLocationUpdates(onLocationUpdatedCallback);
		onNoNewLocationTimeoutCallback.onNoNewLocation(locationCurrent);
	}

	@Override
	public void stopLocationUpdates() {
		locationController.stop();
	}
}
