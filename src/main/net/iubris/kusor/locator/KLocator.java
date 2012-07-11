/*******************************************************************************
 * Copyleft 2012 Massimiliano Leone - massimiliano.leone@iubris.net .
 * 
 * KLocator.java is part of kusor.
 * 
 * kusor is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 * 
 * kusor is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with kusor ; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301 USA
 ******************************************************************************/
package net.iubris.kusor.locator;

import roboguice.util.Ln;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;

import com.google.inject.Inject;
import com.novoda.location.Locator;
import com.novoda.location.LocatorFactory;
import com.novoda.location.LocatorSettings;
import com.novoda.location.exception.NoProviderAvailable;

public class KLocator /* implements LocationObservable, LocationProvider, LocationUpdater */{
	
	private final LocatorSettings novodaLocatorSettings;
	private final Locator novodaLocator;
	private final Context context;	
	//private final KlocatorObservableSyncObservableDelegate syncObservableDelegate;// = new KlocatorSyncObservableDelegate();
	private Location location;
	//private final Collection<LocationObserver> locationObservers = new ArrayList<LocationObserver>();
		
	@Inject
//	public KLocator(Context context, LocatorSettings locationSettings, KlocatorObservableSyncObservableDelegate syncObservableDelegate) {
	public KLocator(Application context, LocatorSettings novodaLocatorSettings/*, KlocatorObservableSyncObservableDelegate syncObservableDelegate*/) {
		this.context = context;
		this.novodaLocatorSettings = novodaLocatorSettings;
	
		novodaLocator = LocatorFactory.getInstance();
	    novodaLocator.prepare(context, novodaLocatorSettings);
	    
	  //  this.syncObservableDelegate = syncObservableDelegate;
	    
Ln.d("constructor");
//Verboser.reflectiveToString(  novodaLocator.getSettings() );
	}
	
	/*private Thread dialogThread = new Thread(){
		@Override
		public void run() {	
			
		}
	};*/
	//private CountDownLatch countDownLatch = new CountDownLatch(1);
	
	//@Override
	public Location getLocation() {
		/*if (location == null ) {
			final Location novodaLocation = novodaLocator.getLocation();			
			if (novodaLocation == null) {
				location = novodaLocator.getLocation();
			}
		}*/
			/*final LocationListener list = new LocationListener() {				
				@Override
				public void onStatusChanged(String provider, int status, Bundle extras) {}				
				@Override
				public void onProviderEnabled(String provider) {}				
				@Override
				public void onProviderDisabled(String provider) {}				
				@Override
				public void onLocationChanged(Location location) {
					KLocator.this.location = location;					
				}
			};*/
				/*countDownLatch = new CountDownLatch(1);
				try {					
					countDownLatch.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}*/
			//}
			//
		Ln.d(location);
		return location;
	}
	protected void onNewLocation(final Location location) {		
		this.location = location;
		/*
		notifyObserver( new INotificationAction<LocationObserver>() {
			@Override
			public void execute(LocationObserver observer) {
Ln.d("notifying location: "+location);				
				observer.onLocationUpdate(location);
			}
		});
		*/
	}	
	
	//@Override
	public void startLocationUpdates(){
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(novodaLocatorSettings.getUpdateAction());
Ln.d("starting updates");		
		context.registerReceiver(freshLocationReceiver, intentFilter);
		try {
			novodaLocator.startLocationUpdates();
		} catch(NoProviderAvailable np) {
			np.printStackTrace();
		}
	}
	
	//@Override
	public void stopLocationUpdates() {
Ln.d("stopping updates");		
		context.unregisterReceiver(freshLocationReceiver);
		novodaLocator.stopLocationUpdates();		
	}
	
	public BroadcastReceiver freshLocationReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			final Location location = novodaLocator.getLocation();
Ln.d("NEW location: "+location);
			KLocator.this.onNewLocation( location );
			//location = novodaLocator.getLocation();
			/*try {
				//countDownLatch.countDown();
			} catch (NullPointerException e) {
				Ln.d("npe");
			}*/

		}
	};
	
	//OBSERVER SECTION
	/*
	@Override
	public void attachObserver(LocationObserver observer) {
		//locationObservers.add(observer);
		Ln.d("syncObservableDelegate: "+syncObservableDelegate.toString());
		Ln.d("observer: " +observer.toString());
		syncObservableDelegate.attachObserver(observer);
	}
	@Override
	public void detachObserver(LocationObserver observer) {
		//locationObservers.remove(observer);
		syncObservableDelegate.detachObserver(observer);	
	}
	@Override
	public void notifyObserver(INotificationAction<LocationObserver> action) {
//		for (LocationObserver locationObserver: locationObservers) {
//			//locationObserver.
//			action.execute(locationObserver);
//		}
		syncObservableDelegate.notifyObserver(action);
	}
*/	
	
	
	
	
	/*
	public static boolean isLocationOlder(Location location, long timeMinimumThreshold ){
Ln.d("current time: "+System.currentTimeMillis());
Ln.d("location time: "+location.getTime());
Ln.d("timeMinimumThreshold: "+timeMinimumThreshold);
Ln.d("difference: "+ (System.currentTimeMillis() - location.getTime()) );
		if(System.currentTimeMillis() - location.getTime() > timeMinimumThreshold) return true;
		return false;
	}
	public static boolean isLocationFarer(Location actualLocation, Location oldLocation, float distanceMinimumThreshold){
		if (actualLocation.distanceTo(oldLocation) > distanceMinimumThreshold) return true;
		return false;
	}*/
}
