/*******************************************************************************
 * Copyleft 2012 Massimiliano Leone - massimiliano.leone@iubris.net .
 * 
 * LocatorSettingsProvider.java is part of kusor.
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
package net.iubris.kusor._roboguice.provider;

import net.iubris.kusor._roboguice.provider.annotations.LocationUpdateActionAnnotation;
import net.iubris.kusor._roboguice.provider.annotations.PackageNameAnnotation;
import net.iubris.kusor._roboguice.provider.annotations.UpdatesDistanceAnnotation;
import net.iubris.kusor._roboguice.provider.annotations.UpdatesIntervalAnnotation;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.novoda.location.LocatorSettings;

public class LocatorSettingsProvider implements Provider<LocatorSettings> {

	private String locationUpdateAction;
	private String packageName;
	
	private int updatesInterval;
	private int updatesDistance;
		
	@Inject
	protected LocatorSettingsProvider(
			@LocationUpdateActionAnnotation String locationUpdateAction,
			@PackageNameAnnotation  String packageName, 
			@UpdatesIntervalAnnotation int updatesInterval, 
			@UpdatesDistanceAnnotation int updatesDistance) {
		this.locationUpdateAction = locationUpdateAction;
		this.packageName = packageName;
		this.updatesInterval = updatesInterval;
		this.updatesDistance = updatesDistance;
	}

	@Override
	public LocatorSettings get() {
		final LocatorSettings locationSettings = new LocatorSettings(packageName, locationUpdateAction);
		locationSettings.setUpdatesInterval(updatesInterval);
		locationSettings.setUpdatesDistance(updatesDistance);
		return locationSettings;
	}

}
