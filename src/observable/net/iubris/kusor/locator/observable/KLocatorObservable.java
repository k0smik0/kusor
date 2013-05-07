/*******************************************************************************
 * Copyleft 2012 Massimiliano Leone - massimiliano.leone@iubris.net .
 * 
 * KLocator4Diane.java is part of 'Kusor'.
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


import net.iubris.kusor.locator.KLocator;
import net.iubris.polaris.locator.Locator;
import android.app.Application;
import android.location.Location;

import com.novoda.location.LocatorSettings;

public class KLocatorObservable extends KLocator implements Locator {

	private final KlocatorObservableSyncObservableDelegate syncObservableDelegate;

	public KLocatorObservable(Application context, LocatorSettings novodaLocatorSettings,
			KlocatorObservableSyncObservableDelegate syncObservableDelegate) {
		super(context, novodaLocatorSettings);
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
//		super.onNewLocation(location);		
		
		notifyObserver( new LocationNotificationAction() {
			@Override
			public void execute(LocationObserver observer) {
//Ln.d("notifying location: "+location);				
				observer.onLocationUpdate(location);
			}
		});		
	}
}
