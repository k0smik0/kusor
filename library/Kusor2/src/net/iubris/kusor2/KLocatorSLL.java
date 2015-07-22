package net.iubris.kusor2;

import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import io.nlopez.smartlocation.SmartLocation.LocationControl;
import net.iubris.polaris.locator.core.Locator;
import net.iubris.polaris.locator.utils.LocationStrategiesUtils;
import android.content.Context;
import android.location.Location;
import android.util.Log;

public class KLocatorSLL implements Locator {
	
	private final LocationControl locationController;
	private final OnLocationUpdatedListener onLocationUpdatedListener = new OnLocationUpdatedListener() {
		@Override
		public void onLocationUpdated(Location location) {
			KLocatorSLL.this.location = location;
		}
	};
	
	protected Location location;
	private int minimumTimeThreshold;
	private int minimumDistanceThreshold;

	public KLocatorSLL(Context context, int minimumTimeThreshold, int minimumDistanceThreshold) {
		this.minimumTimeThreshold = minimumTimeThreshold;
		this.minimumDistanceThreshold = minimumDistanceThreshold;
		locationController = SmartLocation.with(context).location();
		// sure?
//		locationController.config(LocationParams.NAVIGATION);
	}

	@Override
	public Location getLocation() {
		if (location==null) {
			Location lastLocation = locationController.getLastLocation();
			Log.d("KLocatorSLL","current location is null, returning lastLocation: "+lastLocation);
			location = lastLocation;
			return location;
		}
		
		Location lastLocation = locationController.getLastLocation();
		if (LocationStrategiesUtils.isLocationBetter(location, lastLocation, minimumTimeThreshold, minimumDistanceThreshold)) {
			Log.d("KLocatorSLL", "current location is better, returning: "+location);
			return location;
		} else {
			Log.d("KLocatorSLL", "current location is worst, returning lastLocation: "+location);
			location = lastLocation;
			return location;
		}
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
		locationController.start(onLocationUpdatedListener);
	}

	@Override
	public void stopLocationUpdates() {
		locationController.stop();
	}
}
