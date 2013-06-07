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
