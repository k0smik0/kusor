/*******************************************************************************
 * Copyleft 2013 Massimiliano Leone - massimiliano.leone@iubris.net .
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

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import net.iubris.kusor._inject.locator.annotations.UpdatesDistance;
import net.iubris.kusor._inject.locator.annotations.UpdatesInterval;
import net.iubris.polaris.locator.Locator;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import com.novoda.location.exception.NoProviderAvailable;

/**
 * KLocator implements Locator (from Polaris package), providing a LocationUpdater and a LocationProvider<br/>
 * Using novocation locator, It retrieves a new location, which will be stored internally. <br/>
 * This new location can be consumed via getLocation(); <br/>
 * Be careful: behind the scene, all is asynchronous and getLocation() could return "null", at the beginning.<br/>
 * You can use a BroadcastReceiver to receive new location, registering it with IntentFilter action as "'appPackageName'.ACTION_LOCATION_UPDATED",<br/>
 * for example "com.example.ACTION_LOCATION_UPDATED" [ you can retrieve your appPackage using context.getPackageName() ]
 *  
 * @author Massimiliano Leone - k0smik0
 */
@Singleton
public class KLocator implements Locator {
	
	public final static String ACTION_LOCATION_UPDATED = "ACTION_LOCATION_UPDATED";
	
	private final com.novoda.location.Locator novodaLocator;
//	private final Context applicationContext;	
	private final Application applicationContext;	
	private final int updatesInterval;
	private final int updatesDistance;

	private Location location;
	private CountDownLatch latch;
	
	protected static int WAIT = 4; 

	@Inject
	public KLocator(Application /*Context*/ application,
			com.novoda.location.Locator novodaLocator,
			@UpdatesInterval int updatesInterval,
			@UpdatesDistance int updatesDistance) {
		this.applicationContext = application;
		this.novodaLocator = novodaLocator;
		this.updatesDistance = updatesDistance;
		this.updatesInterval = updatesInterval;
		this.latch = new CountDownLatch(1);
	}
	
	@Override
	public Location getLocation() {
		if (location == null) {
//Log.d("KLocator:78","location is null, waiting");
			try {
				latch.await(WAIT,TimeUnit.SECONDS);
//Log.d("KLocator:81","releasing latch however");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} else {
//Log.d("KLocator:86","location is not null, just returning "+location);			
		}
		return location;
	}
	protected void onNewLocation(final Location location) {
//Log.d("KLocator:92",""+location);
//		formatLocationDecimalPlaces(location);
//Log.d("KLocator:94",""+location);
		this.location = location;
		latch.countDown();
	}
	
	@Override
	public void startLocationUpdates() {
//Log.d("KLocator:97","starting updates");
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(novodaLocator.getSettings().getUpdateAction());
		applicationContext.registerReceiver(freshLocationReceiver, intentFilter);
		try {			
			novodaLocator.startLocationUpdates(); // 1.0.6-1.0.8
//			novodaLocator.startActiveLocationUpdates(); // 2.0-alpha
		} catch(NoProviderAvailable np) {
			np.printStackTrace();
		}
	}
	
	/*public void startLocationUpdates(Application context) {
//Log.d("KLocator:97","starting updates");
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(novodaLocator.getSettings().getUpdateAction());
		context.registerReceiver(freshLocationReceiver, intentFilter);
		try {			
			novodaLocator.startLocationUpdates();			
		} catch(NoProviderAvailable np) {
			np.printStackTrace();
		}
	}*/
	
	
	@Override
	public void stopLocationUpdates() {
//Log.d("KLocator:95","stopping updates");
		applicationContext.unregisterReceiver(freshLocationReceiver);
		novodaLocator.stopLocationUpdates();
	}
	
	public BroadcastReceiver freshLocationReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			final Location location = novodaLocator.getLocation();
			// store location
			KLocator.this.onNewLocation( location );
			// broadcast again
			String action = context.getPackageName()+"."+ACTION_LOCATION_UPDATED;
			context.sendBroadcast( new Intent(action) );		
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
	
	public boolean isGpsProviderEnabled() {
		return novodaLocator.isGpsProviderEnabled();
	}
	public boolean isNetworkProviderEnabled() {
		return novodaLocator.isNetworkProviderEnabled();
	}

}
