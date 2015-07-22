package net.iubris.kusor2;

import java.util.List;

import net.iubris.polaris.locator.core.Locator;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;
import android.widget.Toast;
import fr.quentinklein.slt.LocationTracker;
import fr.quentinklein.slt.TrackerSettings;

public class KLocatorSLT implements Locator {
	
	private final int timeBetweenUpdatesInMilliseconds;
	private final int distanceBetweenUpdates;
	private final LocationTracker locationTracker;
	
	private Location location;
	private boolean started;
	private final LocationManager locationManager;
	
	public KLocatorSLT(Context context, LocationManager locationManager, 
			int locationSearchingTimeout, String noLocationTimeoutErrorMessage,  
			boolean useGPS, boolean useNetwork, boolean usePassive, 
			int timeBetweenUpdatesInMilliseconds, int distanceBetweenUpdates) {
		this.locationManager = locationManager;
		this.timeBetweenUpdatesInMilliseconds = timeBetweenUpdatesInMilliseconds;
		this.distanceBetweenUpdates = distanceBetweenUpdates;
		TrackerSettings settings = new TrackerSettings()
						.setTimeout(locationSearchingTimeout)
			            .setUseGPS(useGPS)
			            .setUseNetwork(useNetwork)
			            .setUsePassive(usePassive)
			            .setTimeBetweenUpdates(timeBetweenUpdatesInMilliseconds)
			            .setMetersBetweenUpdates(distanceBetweenUpdates);
		
		final Context c = context;
		final String nltem = noLocationTimeoutErrorMessage;
		locationTracker = new LocationTracker(context, settings) {
			@Override
			public void onLocationFound(Location location) {
				Log.d("KLocatorSLT:26", "new location found: "+location);
				setLocation(location);
			}
			@Override
			public void onTimeout() {
				Toast.makeText(c, nltem, Toast.LENGTH_LONG).show();
			}
		};
		if (locationTracker!=null)
			started = true;
	}
	private void setLocation(Location location) {
		this.location = location;
	}
	
	public KLocatorSLT(Context context, LocationManager locationManager, String noLocationTimeoutErrorMessage) {
		this(context, locationManager, TrackerSettings.DEFAULT_TIMEOUT, noLocationTimeoutErrorMessage, true, true, true, 5*60*1000, 50);
	}

	@Override
	public Location getLocation() {
		Log.d("KLocatorSLT:55","returning location: "+location);
		if (location==null) {
			Location lastKnowLocation = getLastKnowLocation();
			if (location==null)
				return lastKnowLocation;
		}
		return location;
	}
	private Location getLastKnowLocation() {
		List<String> allProviders = locationManager.getAllProviders();
		for (String provider : allProviders) {
			Log.d("KLocatorSLT","provider: "+provider);
			Location lastKnownLocation = locationManager.getLastKnownLocation(provider);
			if (lastKnownLocation!=null)
				return lastKnownLocation;
		}
		return null;
	}

	@Override
	public Integer getMinimumDistanceThreshold() {
		return distanceBetweenUpdates;
	}

	@Override
	public Integer getMinimumTimeThreshold() {
		return timeBetweenUpdatesInMilliseconds;
	}

	@Override
	public void startLocationUpdates() {
		if(locationTracker != null && started == false) {
			locationTracker.startListen();
			started = true;
		}
	}

	@Override
	public void stopLocationUpdates() {
		if(locationTracker != null && started == true) {
			locationTracker.stopListen();
			started = false;
		}
	}
}
