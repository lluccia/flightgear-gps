package org.flightgear.fggps;

import java.io.IOException;
import java.util.Timer;

import org.flightgear.fggps.R;
import org.flightgear.fggps.updaters.GPSUpdater;
import org.flightgear.fggps.updaters.MapUpdater;
import org.flightgear.fggps.updaters.UpdaterTask;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;

public class FlightGearGPSActivity extends MapActivity {
	
	/** FG Server IP - to be parameterized */
	private static final String IP="192.168.1.103";
	
	/** FG Server PORT - to be parameterized */
	private static final int PORT = 9000;
	
	/** Time between position updates */
	private long UPDATE_RATE_MS = 100;
	
	private MapView mapView;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mapView = (MapView) findViewById(R.id.mapview);
		mapView.setBuiltInZoomControls(true);
		mapView.setStreetView(false);
		mapView.setSatellite(true);
		
		GPSUpdater gpsUpdater=null;
		try {
			gpsUpdater = new GPSUpdater(IP, PORT);
		} catch (IOException e) {
			Log.e("ConnectionError", "Error connecting to FlightGear server.", e);
			this.finish();
		}
		
		MapUpdater mapUpdater = 
				new MapUpdater(mapView, 
						gpsUpdater.getGps(),
						BitmapFactory.decodeResource(this.getResources(), 
								R.drawable.plane_icon));
		
		//set timer task to update coordinates
		UpdaterTask updater = new UpdaterTask(mapUpdater, gpsUpdater);
		Timer timer = new Timer("update-timer");
		timer.scheduleAtFixedRate(updater, 0, UPDATE_RATE_MS );
    }

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
	
}