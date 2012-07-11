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

import net.iubris.kusor._roboguice.provider.LocatorSettingsProvider;
import net.iubris.kusor._roboguice.provider.annotations.LocationUpdateActionAnnotation;
import net.iubris.kusor._roboguice.provider.annotations.PackageNameAnnotation;
import net.iubris.kusor._roboguice.provider.annotations.UpdatesDistanceAnnotation;
import net.iubris.kusor._roboguice.provider.annotations.UpdatesIntervalAnnotation;
import net.iubris.kusor.locator.KLocator;
import com.google.inject.AbstractModule;
import com.novoda.location.LocatorSettings;

public class KusorModule extends AbstractModule {

	private final String packageName;
	private final String updateAction;
	private final int updatesInterval;
	private final int updatesDistance;
	//private final boolean internal;
	
	public KusorModule() {
		this.packageName = "";
		this.updateAction = "";
		this.updatesInterval = 0;
		this.updatesDistance = 0;
		//internal = false;
	}
	
	public KusorModule(String packageName, String updateAction,
			int updatesInterval, int updatesDistance) {
		this.packageName = packageName;
		this.updateAction = updateAction;
		this.updatesInterval = updatesInterval;
		this.updatesDistance = updatesDistance;
		//this.internal = true;
	}
	
	@Override
	protected void configure() {		
		
		//if (internal) {
		bindConstant().annotatedWith(PackageNameAnnotation.class).to(packageName);
		bindConstant().annotatedWith(LocationUpdateActionAnnotation.class).to(updateAction);
		bindConstant().annotatedWith(UpdatesIntervalAnnotation.class).to(updatesInterval);
		bindConstant().annotatedWith(UpdatesDistanceAnnotation.class).to(updatesDistance);
		//	Ln.d("internal "+internal);
		//}
		bind(LocatorSettings.class).toProvider(LocatorSettingsProvider.class);
		bind(KLocator.class).asEagerSingleton();
		//bind(LocationObservableProvider.class).to(KLocator.class);		
	}

}
