package net.iubris.kusor_sample.activity;

import net.iubris.kusor.locator.KLocator;
import net.iubris.kusor_sample.service.KusorService;
import roboguice.RoboGuice;
import roboguice.activity.RoboActivity;
import roboguice.util.RoboAsyncTask;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class KusorActivity extends RoboActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		startService( new Intent(this, KusorService.class) );
		
		// use receiver to get location asynchronously
		String action = getPackageName()+"."+KLocator.ACTION_UPDATED;
		registerReceiver(receiver, new IntentFilter(action));
	}
	
	private BroadcastReceiver receiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
//			if (intent.getAction().equalsIgnoreCase("net.iubris.kusor.ACTION_LOCATION_UPDATED")){
				KLocator kLocator = RoboGuice.getInjector(context).getInstance(KLocator.class);
				Location location = kLocator.getLocation();
				showLocation("onReceive[KusorActivity:35]:\n",location);
//			}
		}
	};
	
	private void showLocation(String padding, Location location) {
		Log.d("KusorActivity:41",	padding+location );
		Toast.makeText(this, padding+location, Toast.LENGTH_SHORT).show();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		// or you can wait for a location not null, using a while, but this blocks the ui, so use asynctask!
		new RoboAsyncTask<Location>(this) {
			@Override
			public Location call() throws Exception {
				KLocator kLocator = RoboGuice.getInjector(context).getInstance(KLocator.class);
				while (kLocator.getLocation()==null) {
					Thread.sleep(10);
				}
				return kLocator.getLocation();
			}
			protected void onSuccess(Location location) throws Exception {
				showLocation("onResume:\n",location);
			};
		}.execute();
	};
	
	@Override
	protected void onDestroy() {
		unregisterReceiver(receiver);
		super.onDestroy();
	}
}
