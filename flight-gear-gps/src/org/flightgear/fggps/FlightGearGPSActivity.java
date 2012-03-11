package org.flightgear.fggps;

import java.io.IOException;
import java.util.Timer;

import org.flightgear.fggps.connection.ConnectionTask;
import org.flightgear.fggps.connection.FGFSConnector;
import org.flightgear.fggps.gps.GPS;
import org.flightgear.fggps.gps.GPSScratch;
import org.flightgear.fggps.gps.Route;
import org.flightgear.fggps.updaters.MapUpdater;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;

public class FlightGearGPSActivity extends MapActivity {

	private FGFSConnector fgfsConnectionManager;

	public static GPS gps;

	public static GPSScratch gpsScratch;

	private SharedPreferences preferences;

	// private RotateView rotateView;

	private MapView mapView;

	/** Timer responsible for the running the updater tasks */
	private Timer timer;

	/** Task responsible for updating the map drawings */
	private MapUpdater mapUpdater;

	private Handler updateHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			updateTextView();
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.gps_layout);

		preferences = PreferenceManager.getDefaultSharedPreferences(this);

		String flightGearIP = preferences.getString("flightgear_ip",
				"192.168.1.100");
		String flightGearPort = preferences
				.getString("flightgear_port", "9000");

		mapView = (MapView) findViewById(R.id.mapview);
		mapView.setBuiltInZoomControls(true);
		mapView.setStreetView(false);
		mapView.setSatellite(true);
		mapView.setKeepScreenOn(true);
		mapView.setClickable(true);
		new FGFSConnector(flightGearIP, Integer.valueOf(flightGearPort));

		fgfsConnectionManager = new FGFSConnector(flightGearIP,
				Integer.valueOf(flightGearPort));

		gpsScratch = new GPSScratch(fgfsConnectionManager);

		gps = new GPS();

		this.mapUpdater = new MapUpdater(mapView, fgfsConnectionManager,
				this.getResources(), gps, new Route(), updateHandler);

		ConnectionTask connectionTask = new ConnectionTask(
				fgfsConnectionManager);

		// set timer task to update map periodically
		this.timer = new Timer("update-timer");
		timer.scheduleAtFixedRate(connectionTask, 0, ConnectionTask.INTERVAL_MS);
		timer.scheduleAtFixedRate(mapUpdater, 0, MapUpdater.UPDATE_INTERVAL_MS);

	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		try {
			fgfsConnectionManager.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
		case R.id.menu_preferences:
			Intent settingsActivity = new Intent(getBaseContext(),
					PreferencesActivity.class);
			startActivity(settingsActivity);
			return true;
		case R.id.menu_search:
			Intent searchActivity = new Intent(getBaseContext(),
					SearchActivity.class);

			startActivity(searchActivity);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void updateTextView() {
		TextView textgroundspeed = (TextView) findViewById(R.id.groundspeedKt);
		TextView textaltitudeFt = (TextView) findViewById(R.id.altitudeFt);

		textgroundspeed.setText(gps.getFormattedGroundspeed());
		textaltitudeFt.setText(gps.getFormattedAltitude());

	}

}