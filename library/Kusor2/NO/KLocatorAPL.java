package net.iubris.kusor2;

import java.util.ArrayList;
import java.util.List;

import net.iubris.polaris.locator.Locator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

/**
 * from https://github.com/googlesamples/android-play-location/blob/master/LocationUpdates/app/src/main/java/com/google/android/gms/location/sample/locationupdates/MainActivity.java
 */
public class KLocatorAPL implements Locator {
	
	protected static final String TAG = "location-updates-sample";
    /**
     *  Keys for storing activity state in the Bundle.
     */
    protected final static String LOCATION_KEY = "location-key";
	
    /**
     * The default desired interval for location updates. Inexact. Updates may be more or less frequent.
     */
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;

    /**
     * The default fastest rate for active location updates. Exact. Updates will never be more frequent
     * than this value.
     */
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = UPDATE_INTERVAL_IN_MILLISECONDS / 2;
	
	private final List<LocationListener> locationListeners = new ArrayList<LocationListener>();
	
	private final LocationListener internalLocationListener = new LocationListener() {
		@Override
		public void onLocationChanged(Location location) {
			KLocatorAPL.this.location = location;
			// notify to externals
			for (LocationListener ll: locationListeners)
				ll.onLocationChanged(location);
		}
	};
	private final GoogleApiClient.ConnectionCallbacks connectionCallback = new GoogleApiClient.ConnectionCallbacks() {
        @Override public void onConnected(Bundle bundle) {
            startLocationUpdatesUsingFuse();
        }

        @Override public void onConnectionSuspended(int cause) {
            // The connection to Google Play services was lost for some reason. We call connect() to
            // attempt to re-establish the connection.
            Log.i(TAG, "Connection suspended");
            googleApiClient.connect();
        }
    };
    private final GoogleApiClient.OnConnectionFailedListener connectionFailedListener = new GoogleApiClient.OnConnectionFailedListener() {
        @Override public void onConnectionFailed(ConnectionResult connectionResult) {
            // Refer to the javadoc for ConnectionResult to see what error codes might be returned in
            // onConnectionFailed.
            Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + connectionResult.getErrorCode());
        }
    };
    /**
     * Provides the entry point to Google Play services.
     */
    protected final GoogleApiClient googleApiClient;

    /**
     * Stores parameters for requests to the FusedLocationProviderApi.
     */
    protected LocationRequest locationRequest;
    
    private final Context context;

    private Location location;


    public KLocatorAPL(Context context) {
        this.context = context;
        // Kick off the process of building a GoogleApiClient and requesting the LocationServices API
        googleApiClient = buildGoogleApiClient();
    }
    

    private void startUpdate() {
        if (googleApiClient.isConnected()) {
            startLocationUpdatesUsingFuse();
        } else {
            googleApiClient.connect();
        }
    }
    
    private void startUpdate(LocationListener locationListener) {
    	this.locationListeners.add(locationListener);
    	startUpdate();
    }
    

    private void stopUpdate() {
        stopLocationUpdatesUsingFuse();
        if (googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
    }

    @SuppressWarnings("deprecation")
	@SuppressLint("InlinedApi")
	public boolean isLocationEnabled() {
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);

            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
            }
            return locationMode != Settings.Secure.LOCATION_MODE_OFF;
        } else{
            locationProviders = Settings.Secure.getString(context.getContentResolver(), 
//            		Settings.Secure.LOCATION_MODE
            		Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }
    }

    public void openSettingsScreen() {
        Intent viewIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        context.startActivity(viewIntent);
    }

    public void release() {
//        context = null;
    }

    /**
     * Builds a GoogleApiClient. Uses the {@code #addApi} method to request the
     * LocationServices API.
     * @return 
     */
    private synchronized GoogleApiClient buildGoogleApiClient() {
        Log.i(TAG, "Building GoogleApiClient");
        return new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(connectionCallback)
                .addOnConnectionFailedListener(connectionFailedListener)
                .addApi(LocationServices.API)
                .build();
    }

    /**
     * Sets up the location request. Android has two location request settings:
     * {@code ACCESS_COARSE_LOCATION} and {@code ACCESS_FINE_LOCATION}. These settings control
     * the accuracy of the current location. This sample uses ACCESS_FINE_LOCATION, as defined in
     * the AndroidManifest.xml.
     * <p/>
     * When the ACCESS_FINE_LOCATION setting is specified, combined with a fast update
     * interval (5 seconds), the Fused Location Provider API returns location updates that are
     * accurate to within a few feet.
     * <p/>
     * These settings are appropriate for mapping applications that show real-time location
     * updates.
     */
    public void config(long interval, long fastestInterval, int priority) {
        createLocationRequest(interval, fastestInterval, priority);
    }
    public void config() {
    	// Create default location request.
        createLocationRequest(UPDATE_INTERVAL_IN_MILLISECONDS, FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS, LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
    }

    /**
     * Sets up the location request. Android has two location request settings:
     * {@code ACCESS_COARSE_LOCATION} and {@code ACCESS_FINE_LOCATION}. These settings control
     * the accuracy of the current location. This sample uses ACCESS_FINE_LOCATION, as defined in
     * the AndroidManifest.xml.
     * <p/>
     * When the ACCESS_FINE_LOCATION setting is specified, combined with a fast update
     * interval (5 seconds), the Fused Location Provider API returns location updates that are
     * accurate to within a few feet.
     * <p/>
     * These settings are appropriate for mapping applications that show real-time location
     * updates.
     */
    private void createLocationRequest(long interval, long fastestInterval, int priority) {
        locationRequest = new LocationRequest();

        // Sets the desired interval for active location updates. This interval is
        // inexact. You may not receive updates at all if no location sources are available, or
        // you may receive them slower than requested. You may also receive updates faster than
        // requested if other applications are requesting location at a faster interval.
        locationRequest.setInterval(interval);

        // Sets the fastest rate for active location updates. This interval is exact, and your
        // application will never receive updates faster than this value.
        locationRequest.setFastestInterval(fastestInterval);

        locationRequest.setPriority(priority);
        
    }

    /**
     * Requests location updates from the FusedLocationApi.
     */
    private void startLocationUpdatesUsingFuse() {
        // The final argument to {@code requestLocationUpdates()} is a LocationListener
        // (http://developer.android.com/reference/com/google/android/gms/location/LocationListener.html).
        LocationServices.FusedLocationApi.requestLocationUpdates(
                googleApiClient, locationRequest, internalLocationListener
        );
    }

    /**
     * Removes location updates from the FusedLocationApi.
     */
    private void stopLocationUpdatesUsingFuse() {
        // It is a good practice to remove location requests when the activity is in a paused or
        // stopped state. Doing so helps battery performance and is especially
        // recommended in applications that request frequent location updates.

        // The final argument to {@code requestLocationUpdates()} is a LocationListener
        // (http://developer.android.com/reference/com/google/android/gms/location/LocationListener.html).
        if (googleApiClient.isConnected()) {
            LocationServices.FusedLocationApi
            .removeLocationUpdates(googleApiClient, internalLocationListener);
        }
    }

	@Override
	public Location getLocation() {
		return location;
	}

	@Override
	public Integer getMinimumDistanceThreshold() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer getMinimumTimeThreshold() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void startLocationUpdates() {
		startUpdate();		
	}
	
	public void startLocationUpdates(LocationListener locationListener) {
		startUpdate(locationListener);		
	}

	@Override
	public void stopLocationUpdates() {
		stopUpdate();		
	}
}