/*******************************************************************************
 * Copyleft 2013 Massimiliano Leone - massimiliano.leone@iubris.net .
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
package net.iubris.kusor._di._roboguice.module;

import net.iubris.kusor._di.locator.annotations.UpdatesDistance;
import net.iubris.kusor._di.locator.annotations.UpdatesInterval;
import net.iubris.kusor._di.providers.KLocatorProvider;
import net.iubris.kusor._di.providers.NovodaLocatorProvider;
import net.iubris.kusor._di.providers.NovodaLocatorSettingsProvider;
import net.iubris.kusor._di.providers.annotations.LocationUpdateAction;
import net.iubris.kusor._di.providers.annotations.LocationUpdaterPackageName;
import net.iubris.kusor.locator.KLocator;

import com.google.inject.AbstractModule;
import com.novoda.location.Locator;
import com.novoda.location.LocatorSettings;

public class KusorModule extends AbstractModule {

	private final String packageName;
	private final String updateAction; 
//			"ACTION_LOCATION_FRESH";
//			"ACTION_LOCATION_UPDATED";
			
	
	private final int updatesInterval;
	private final int updatesDistance;
	//private final boolean internal;
	
	public KusorModule() {
		this.packageName = "net.iubris.kusor";
		this.updateAction = packageName+""+net.iubris.polaris.locator.core.Locator.ACTION_LOCATION_UPDATED_SUFFIX;
		this.updatesInterval = 5*60*1000; // 5 second
		this.updatesDistance = 50; // 50 meters
		//internal = false;
	}
	/**
	 * @param packageName		your app package name
	 * @param updateAction 		something as SOMETHING_AS_ACTION_FRESH_LOCATION
	 * @param updatesInterval 	an integer (milliseconds)
	 * @param updatesDistance 	an integer (meters)
	 */
	public KusorModule(String packageName,/* String updateAction,*/ int updatesInterval, int updatesDistance) {
		this.packageName = packageName;
//		this.updateAction = updateAction.toUpperCase(Locale.getDefault());
		this.updateAction = packageName+""+net.iubris.polaris.locator.core.Locator.ACTION_LOCATION_UPDATED_SUFFIX;
		this.updatesInterval = updatesInterval;
		this.updatesDistance = updatesDistance;
	}
	
	@Override
	protected void configure() {
		
		bindConstant().annotatedWith(LocationUpdaterPackageName.class).to(packageName);
//Log.d("KusorModule:66","packageName: "+packageName);
		bindConstant().annotatedWith(LocationUpdateAction.class).to(updateAction);
		bindConstant().annotatedWith(UpdatesInterval.class).to(updatesInterval);
		bindConstant().annotatedWith(UpdatesDistance.class).to(updatesDistance);

		bind(LocatorSettings.class).toProvider(NovodaLocatorSettingsProvider.class).asEagerSingleton();
		bind(Locator.class).toProvider(NovodaLocatorProvider.class).asEagerSingleton();
		bind(KLocator.class).toProvider(KLocatorProvider.class).asEagerSingleton();
		//bind(LocationObservableProvider.class).to(KLocator.class);
		
//		bindLocationNullAllWrongString(); // provides an english message
//		bindLocationNullEnableGPSString(); // provides an english message
		
//		bind(GetFreshLocationTask.class).toProvider(GetFreshLocationTaskProvider.class);
	}
	
//	/**
//	 * binding needed for {@link GetFreshLocationTask}<br/>
//	 * default: in english
//	 */
//	protected void bindLocationNullAllWrongString() {
//		bind(String.class).annotatedWith(LocationNullAllWrongString.class).toInstance("location is null, something was wrong.");
//	}
//	/**
//	 * binding needed for {@link GetFreshLocationTask}<br/>
//	 * default: do nothing 
//	 */
//	protected void bindLocationNullEnableGPSString() {
//		bind(String.class).annotatedWith(LocationNullEnableGPSString.class).toInstance("location is null, you could enable your GPS.");
//	}
	
	
}
