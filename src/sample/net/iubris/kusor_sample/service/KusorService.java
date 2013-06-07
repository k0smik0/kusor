package net.iubris.kusor_sample.service;

import javax.inject.Inject;

import net.iubris.kusor.locator.KLocator;

import roboguice.service.RoboService;
import android.content.Intent;
import android.os.IBinder;

public class KusorService extends RoboService {

	@Inject KLocator kLocator;
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		kLocator.startLocationUpdates();
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
//		kLocator.startLocationUpdates();
		return 0;
	}
	
	@Override
	public void onDestroy() {
		kLocator.stopLocationUpdates();
		super.onDestroy();
	}
	
	

}
