package org.flightgear.fggps;

import java.util.Timer;

import org.flightgear.fggps.connection.FlightGearConnector;
import org.flightgear.fggps.gps.GPS;
import org.flightgear.fggps.updaters.MapUpdater;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;

public class FlightGearGPSActivity extends MapActivity {

	private SharedPreferences preferences;

	private MapView mapView;

	/** Timer responsible for the running the updater tasks */
	private Timer timer;
	
	/** Task responsible for updating the map drawings */
	private MapUpdater mapUpdater;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gps);

		preferences = PreferenceManager.getDefaultSharedPreferences(this);

		String flightGearIP = preferences.getString("flightgear_ip", "");
		String flightGearPort = preferences.getString("flightgear_port", "9000");

		mapView = (MapView) findViewById(R.id.mapview);
		mapView.setBuiltInZoomControls(true);
		mapView.setStreetView(false);
		mapView.setSatellite(true);
		mapView.setKeepScreenOn(true);
		
		
		FlightGearConnector flightGearConnector = new FlightGearConnector(
				flightGearIP, Integer.valueOf(flightGearPort));
		
		flightGearConnector.connect();
		
		this.mapUpdater = new MapUpdater(mapView, flightGearConnector,
				this.getResources(), new GPS());

		// set timer task to update map periodically
		this.timer = new Timer("update-timer");
		timer.scheduleAtFixedRate(mapUpdater, 0, MapUpdater.UPDATE_INTERVAL_MS);
		
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.preferences:
			Intent settingsActivity = new Intent(getBaseContext(),
					PreferencesActivity.class);
			startActivity(settingsActivity);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}