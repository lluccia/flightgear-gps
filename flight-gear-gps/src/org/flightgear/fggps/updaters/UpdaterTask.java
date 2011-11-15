package org.flightgear.fggps.updaters;

import java.io.IOException;
import java.util.TimerTask;

import android.util.Log;


public class UpdaterTask extends TimerTask {
	
	private MapUpdater mapUpdater;
	
	private GPSUpdater gpsUpdater;
	
	public UpdaterTask(MapUpdater mapUpdater, GPSUpdater gpsUpdater) {
		super();
		this.mapUpdater = mapUpdater;
		this.gpsUpdater = gpsUpdater;
	}

	@Override
	public void run() {
		try {
			gpsUpdater.update();
			mapUpdater.update();
		} catch (IOException e) {
			Log.e("ConnectionError", "Error connecting to FlightGear server.", e);
		}
	}
}
