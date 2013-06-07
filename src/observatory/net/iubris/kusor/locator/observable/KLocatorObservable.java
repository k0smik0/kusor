/*******************************************************************************
 * Copyleft 2012 Massimiliano Leone - massimiliano.leone@iubris.net .
 * 
 * KLocatorObservable.java is part of 'Kusor'.
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
package net.iubris.kusor.locator.observable;


import javax.inject.Inject;

import net.iubris.kusor._inject.locator.annotations.UpdatesDistance;
import net.iubris.kusor._inject.locator.annotations.UpdatesInterval;
import net.iubris.kusor.locator.KLocator;
import net.iubris.polaris.observatory.action.LocationNotificationAction;
import net.iubris.polaris.observatory.observable.LocationObservable;
import net.iubris.polaris.observatory.observable.LocatorObservableSyncDelegate;
import net.iubris.polaris.observatory.observer.LocationObserver;
import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.novoda.location.Locator;

public class KLocatorObservable extends KLocator implements LocationObservable {

	private final LocatorObservableSyncDelegate syncObservableDelegate;

	@Inject	
	public KLocatorObservable(Context context, Locator novodaLocator,
			@UpdatesInterval int updatesInterval,
			@UpdatesDistance int updatesDistance,
			LocatorObservableSyncDelegate syncObservableDelegate) {
		super(context, novodaLocator, updatesInterval, updatesDistance);
		this.syncObservableDelegate = syncObservableDelegate;
	}
	@Override
	public void attachObserver(LocationObserver observer) {
		syncObservableDelegate.attachObserver(observer);
	}
	@Override
	public void detachObserver(LocationObserver observer) {
		syncObservableDelegate.detachObserver(observer);	
	}	
	@Override
	public void notifyObserver(LocationNotificationAction action) {
		syncObservableDelegate.notifyObserver(action);
	}	
	@Override
	protected void onNewLocation(final Location location) {
		notifyObserver( new LocationNotificationAction() {
			@Override
			public void execute(LocationObserver observer) {
Log.d("KLocatorObservable:77","notifying location: "+location);				
				observer.onLocationUpdate(location);
			}
		});		
	}
}
