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
package net.iubris.kusor._roboguice.provider;

import javax.inject.Inject;
import javax.inject.Provider;

import net.iubris.kusor._roboguice.provider.annotations.LocationUpdateAction;
import com.novoda.location.LocatorSettings;

public class LocatorSettingsProvider implements Provider<LocatorSettings> {

	private final String locationUpdateAction;
//	private final int updatesInterval;
//	private final int updatesDistance;
		
	@Inject
	protected LocatorSettingsProvider(
			@LocationUpdateAction String locationUpdateAction
//			@PackageNameAnnotation  String packageName, 
			/*@,UpdatesInterval int updatesInterval, 
			@UpdatesDistance int updatesDistance*/) {
		this.locationUpdateAction = locationUpdateAction;
//		this.updatesInterval = updatesInterval;
//		this.updatesDistance = updatesDistance;
	}

	@Override
	public LocatorSettings get() {
		final LocatorSettings locationSettings = 
				new LocatorSettings(locationUpdateAction);
//		locationSettings.setUpdatesInterval(updatesInterval);
//		locationSettings.setUpdatesDistance(updatesDistance);
		return locationSettings;
	}

}
