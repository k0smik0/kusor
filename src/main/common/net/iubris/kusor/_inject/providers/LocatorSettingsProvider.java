/*******************************************************************************
 * Copyleft 2012 Massimiliano Leone - massimiliano.leone@iubris.net .
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
package net.iubris.kusor._inject.providers;

import javax.inject.Inject;
import javax.inject.Provider;

import net.iubris.kusor._inject.locator.annotations.UpdatesDistance;
import net.iubris.kusor._inject.locator.annotations.UpdatesInterval;
import net.iubris.kusor._inject.providers.annotations.LocationUpdateAction;
import android.content.Context;
import android.util.Log;

import com.novoda.location.LocatorSettings;

//@ContextSingleton
public class LocatorSettingsProvider implements Provider<LocatorSettings> {

	/*private final String locationUpdateAction;
//	private final String packageName;
	private final int updatesInterval;
	private final int updatesDistance;
	private final Context context;
	*/
	private final LocatorSettings locationSettings;
		
	@Inject
	protected LocatorSettingsProvider(
			@LocationUpdateAction String locationUpdateAction,
//			@LocationUpdatePackageName String packageName,
			@UpdatesInterval int updatesInterval, 
			@UpdatesDistance int updatesDistance
			,Context context) {
		/*this.locationUpdateAction = locationUpdateAction;
//		this.packageName = packageName;
		this.updatesInterval = updatesInterval;
		this.updatesDistance = updatesDistance;
		this.context = context;*/
		
Log.d("LocatorSettingsProvider:54", locationUpdateAction+" "+/*packageName+*/" "+context.getPackageName());

		Log.d("LocatorSettingsProvider:56",context.getPackageName());
		locationSettings =
		//		new LocatorSettings(packageName, locationUpdateAction);
				new LocatorSettings(context.getPackageName(), locationUpdateAction); // 1.0.6, 1.0.8
		//		new LocatorSettings(locationUpdateAction); // 2.0-alpha//
		//		new LocatorSettings("com.novoda.location", "ACTIVE_LOCATION_UPDATE_ACTION");
		locationSettings.setUpdatesInterval(updatesInterval);
		locationSettings.setUpdatesDistance(updatesDistance);
	}

	@Override
	public LocatorSettings get() {
		return locationSettings;
	}
}
