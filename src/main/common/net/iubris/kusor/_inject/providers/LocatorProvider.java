/*******************************************************************************
 * Copyleft 2013 Massimiliano Leone - massimiliano.leone@iubris.net .
 * 
 * LocatorProvider.java is part of 'Kusor'.
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

import android.content.Context;

import com.novoda.location.Locator;
import com.novoda.location.LocatorFactory;
import com.novoda.location.LocatorSettings;

//@ContextSingleton
public class LocatorProvider implements Provider<Locator> {
	
	private final LocatorSettings locatorSettings;
	private Context context;
	
	@Inject
	public LocatorProvider(LocatorSettings locatorSettings, Context context) {
		this.locatorSettings = locatorSettings;
		this.context = context;
	}

	@Override
	public Locator get() {
		Locator novodaLocator = LocatorFactory.getInstance();
		novodaLocator.prepare(context, locatorSettings);
		return novodaLocator;
	}

}
