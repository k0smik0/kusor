/*******************************************************************************
 * Copyleft 2013 Massimiliano Leone - massimiliano.leone@iubris.net .
 * 
 * KusorActivity.java is part of 'Kusor'.
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
package net.iubris.kusor_sample.activity;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;

import net.iubris.kusor.locator.KLocator;
import net.iubris.kusor_sample.R;
import net.iubris.kusor_sample.service.KusorService;
import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;
import roboguice.util.RoboAsyncTask;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.TextView;

@ContentView(R.layout.sample)
public class KusorActivity extends RoboActivity {
	
	private final String action = "net.iubris.kusor_sample."+KLocator.ACTION_UPDATED;
	
	@InjectView(R.id.text_field_locations) TextView textViewLocations;
	@InjectView(R.id.text_field_providers) TextView textViewProviders;
	
	@Inject KLocator kLocator;

	private Intent serviceIntent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		serviceIntent = new Intent(this, KusorService.class);
		startService( serviceIntent );
		
		// use receiver to get location asynchronously		
		registerReceiver(receiver, new IntentFilter(action));
		
		textViewLocations.setMovementMethod(new ScrollingMovementMethod());
	}
	
	private BroadcastReceiver receiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
//			if (intent.getAction().equalsIgnoreCase("net.iubris.kusor.ACTION_LOCATION_UPDATED")){
//				KLocator kLocator = RoboGuice.getInjector(context).getInstance(KLocator.class);
				Location location = kLocator.getLocation();
//				showLocation("onReceive[KusorActivity:35]:\n",location);
				showLocation("onReceive:\n",location);
//			}
		}
	};
	
//	@SuppressWarnings("deprecation")
	private void showLocation(String from, Location location) {
		Log.d("KusorActivity:41",	from+location );
		DecimalFormat df = new DecimalFormat("##.######");
		long locationTime = location.getTime();
//		Date date = new Date(locationTime);
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss.SSS",Locale.getDefault());
		String formattedDate = sdf.format( new Date(locationTime));
//		.toLocaleString();
		textViewLocations.setText(textViewLocations.getText()
				+from
				+"latitude:"+ df.format( location.getLatitude() )+", longitude:"+df.format( location.getLongitude() )+"\n"
				+"at: "+formattedDate+"\n"
				+"by: "+location.getProvider()+"\n"
				+"accuracy: "+location.getAccuracy()+"\n"
				+"\n");
//		Toast.makeText(this, padding+location, Toast.LENGTH_SHORT).show();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		textViewProviders.setText(
			"is gps enabled: "+ kLocator.isGpsProviderEnabled()+"\n"
			+"is network enabled: "+ kLocator.isNetworkProviderEnabled()
		);
		
		// or you can wait for a location not null, using a while, but this blocks the ui, so use asynctask!
		new RoboAsyncTask<Location>(this) {
			@Override
			public Location call() throws Exception {
				
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
		stopService( serviceIntent  );
		super.onDestroy();
	}
}
