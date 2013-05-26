/*******************************************************************************
 * Copyleft 2012 Massimiliano Leone - massimiliano.leone@iubris.net .
 * 
 * KusorModule.java is part of 'Kusor'.
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
package net.iubris.kusor._roboguice.module;

import java.util.Locale;

import net.iubris.kusor._roboguice.provider.LocatorSettingsProvider;
import net.iubris.kusor._roboguice.provider.annotations.LocationUpdateAction;
import net.iubris.kusor._roboguice.provider.annotations.UpdatesDistance;
import net.iubris.kusor._roboguice.provider.annotations.UpdatesInterval;
import net.iubris.kusor.locator.KLocator;

import com.google.inject.AbstractModule;
import com.novoda.location.LocatorSettings;

public class KusorModule extends AbstractModule {

//	private final String packageName;
	private final String updateAction;
	private final int updatesInterval;
	private final int updatesDistance;
	//private final boolean internal;
	
	public KusorModule() {
//		this.packageName = "net.iubris.kusor";
		this.updateAction = "net.iubris.location.action.ACTION_FRESH_LOCATION";
		this.updatesInterval = 5*60*1000; // second
		this.updatesDistance = 50; // meters
		//internal = false;
	}
	/**
	 * @param updateAction 		something as SOMETHING_AS_ACTION_FRESH_LOCATION
	 * @param updatesInterval 	an integer (milliseconds)
	 * @param updatesDistance 	an integer (meters)
	 */
	public KusorModule(String updateAction, int updatesInterval, int updatesDistance) {
		this.updateAction = updateAction.toUpperCase(Locale.getDefault());
		this.updatesInterval = updatesInterval;
		this.updatesDistance = updatesDistance;
	}
	
	@Override
	protected void configure() {		
		
		bindConstant().annotatedWith(LocationUpdateAction.class).to(updateAction);
		bindConstant().annotatedWith(UpdatesInterval.class).to(updatesInterval);
		bindConstant().annotatedWith(UpdatesDistance.class).to(updatesDistance);

		bind(LocatorSettings.class).toProvider(LocatorSettingsProvider.class);
		bind(KLocator.class).asEagerSingleton();
		//bind(LocationObservableProvider.class).to(KLocator.class);	
	}
}
