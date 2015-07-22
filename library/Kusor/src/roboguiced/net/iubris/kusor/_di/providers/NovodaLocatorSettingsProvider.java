/*******************************************************************************
 * Copyleft 2013 Massimiliano Leone - massimiliano.leone@iubris.net .
 * 
 * LocatorSettingsProvider.java is part of 'Kusor'.
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
package net.iubris.kusor._di.providers;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

import net.iubris.kusor._di.locator.annotations.UpdatesDistance;
import net.iubris.kusor._di.locator.annotations.UpdatesInterval;
import net.iubris.kusor._di.providers.annotations.LocationUpdateAction;
import net.iubris.kusor._di.providers.annotations.LocationUpdaterPackageName;
import android.app.Application;

import com.novoda.location.LocatorSettings;

@Singleton
public class NovodaLocatorSettingsProvider implements Provider<LocatorSettings> {

	/*private final String locationUpdateAction;
//	private final String packageName;
	private final int updatesInterval;
	private final int updatesDistance;
	private final Context context;
	*/
	private final LocatorSettings locationSettings;
		
	@Inject
	protected NovodaLocatorSettingsProvider(
			@LocationUpdateAction String locationUpdateAction,
			@LocationUpdaterPackageName String packageName,
			@UpdatesInterval int updatesInterval, 
			@UpdatesDistance int updatesDistance
			,Application applicationContext) {
		/*this.locationUpdateAction = locationUpdateAction;
		this.packageName = packageName;
		this.updatesInterval = updatesInterval;
		this.updatesDistance = updatesDistance;
		this.context = context;*/
		
//Log.d("LocatorSettingsProvider:57", locationUpdateAction+" "+packageName+" "+context.getPackageName());

//Log.d("LocatorSettingsProvider:59",context.getPackageName());
		locationSettings =
				new LocatorSettings(packageName, locationUpdateAction);
//				new LocatorSettings(context.getPackageName(), locationUpdateAction); // 1.0.6, 1.0.8
//				new LocatorSettings(locationUpdateAction); // 2.0-alpha//

		//		new LocatorSettings("com.novoda.location", "ACTIVE_LOCATION_UPDATE_ACTION");
		locationSettings.setUpdatesInterval(updatesInterval);
		locationSettings.setUpdatesDistance(updatesDistance);
	}

	@Override
	public LocatorSettings get() {
		return locationSettings;
	}
}