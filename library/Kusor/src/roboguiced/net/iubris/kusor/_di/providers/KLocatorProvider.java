package net.iubris.kusor._di.providers;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

import com.novoda.location.Locator;

import android.app.Application;
import net.iubris.kusor._di.locator.annotations.UpdatesDistance;
import net.iubris.kusor._di.locator.annotations.UpdatesInterval;
import net.iubris.kusor._di.providers.annotations.LocationUpdaterPackageName;
import net.iubris.kusor.locator.KLocator;

@Singleton
public class KLocatorProvider implements Provider<KLocator> {

	private final KLocator kLocator;

	@Inject
	public KLocatorProvider(Application application, Locator locator, @LocationUpdaterPackageName String locationUpdaterPackageName, @UpdatesInterval int updatesInterval, @UpdatesDistance int updatesDistance) {
		kLocator = new KLocator(application, locator, locationUpdaterPackageName, updatesInterval, updatesDistance);
	}
	
	@Override
	public KLocator get() {
		return kLocator;
	}

}
