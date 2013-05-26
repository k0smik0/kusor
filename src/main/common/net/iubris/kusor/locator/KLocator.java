/*******************************************************************************
 * Copyleft 2012 Massimiliano Leone - massimiliano.leone@iubris.net .
 * 
 * KLocator.java is part of 'Kusor'.
 * 
 * 'Kusor' is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 * 
 * 'Kusor' is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with 'Kusor' ; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301 USA
 ******************************************************************************/
package net.iubris.kusor.locator;

import javax.inject.Inject;

import net.iubris.kusor._roboguice.provider.annotations.UpdatesDistance;
import net.iubris.kusor._roboguice.provider.annotations.UpdatesInterval;
import net.iubris.polaris.locator.Locator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.util.Log;
import com.novoda.location.LocatorFactory;
import com.novoda.location.LocatorSettings;
import com.novoda.location.exception.NoProviderAvailable;

public class KLocator implements Locator {
	
	private final LocatorSettings novodaLocatorSettings;
	private final com.novoda.location.Locator novodaLocator;
	private final Context context;	
	private Location location;
	
	private final int updatesInterval;
	private final int updatesDistance;

	@Inject
	public KLocator(Context context, LocatorSettings novodaLocatorSettings,
			@UpdatesInterval int updatesInterval, 
			@UpdatesDistance int updatesDistance) {
		this.context = context;
		this.novodaLocatorSettings = novodaLocatorSettings;
		novodaLocatorSettings.setUpdatesInterval(updatesInterval);
		novodaLocatorSettings.setUpdatesDistance(updatesDistance);
		this.updatesDistance = updatesDistance;
		this.updatesInterval = updatesInterval;
	
		novodaLocator = LocatorFactory.getInstance();
	    novodaLocator.prepare(context, novodaLocatorSettings);
Log.d("KLocator"," "+hashCode());
	}
	
	public Location getLocation() {
		return location;
	}
	protected void onNewLocation(final Location location) {		
		this.location = location;
	}	
	
	public void startLocationUpdates(){
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(novodaLocatorSettings.getUpdateAction());
//Ln.d("starting updates");		
		context.registerReceiver(freshLocationReceiver, intentFilter);
		try {
			novodaLocator.startLocationUpdates();
		} catch(NoProviderAvailable np) {
			np.printStackTrace();
		}
	}
	
	public void stopLocationUpdates() {
//Ln.d("stopping updates");		
		context.unregisterReceiver(freshLocationReceiver);
		novodaLocator.stopLocationUpdates();		
	}
	
	public BroadcastReceiver freshLocationReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			final Location location = novodaLocator.getLocation();
//			if (isDebuggable(context)) {
//			if (BuildConfig.DEBUG) {
			Log.d("KLocator:85","Kusor's freshLocationReceiver, with Context: "+context+" - new location fix is "+location);
//				Toast.makeText(context, "Kusor's freshLocationReceiver, with Context: "+context+" - new location fix is "+location, Toast.LENGTH_SHORT).show();
//			}
			KLocator.this.onNewLocation( location );
		}
	};

	@Override
	public Integer getMinimumDistanceThreshold() {
		return updatesDistance;
	}

	@Override
	public Integer getMinimumTimeThreshold() {
		return updatesInterval;
	}
	
	/*
	private boolean isDebuggable(Context context) {
	    boolean debuggable = false;
	    PackageManager pm = context.getPackageManager();
	    try {
	        ApplicationInfo appinfo = pm.getApplicationInfo(context.getPackageName(), 0);
	        debuggable = (0 != (appinfo.flags &= ApplicationInfo.FLAG_DEBUGGABLE));
	    }
	    catch(NameNotFoundException e) {
//	        debuggable variable will remain false
	    }
	    return debuggable;
	}
	*/
}
