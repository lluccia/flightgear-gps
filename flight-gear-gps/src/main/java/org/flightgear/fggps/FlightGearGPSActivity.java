package org.flightgear.fggps;

import java.io.IOException;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.flightgear.fggps.connection.ConnectionTask;
import org.flightgear.fggps.connection.FGFSConnector;
import org.flightgear.fggps.gps.GPS;
import org.flightgear.fggps.gps.GPSScratch;
import org.flightgear.fggps.gps.Route;
import org.flightgear.fggps.updaters.MapUpdater;
import org.mapsforge.android.maps.MapActivity;
import org.mapsforge.android.maps.MapView;
import org.mapsforge.android.maps.mapgenerator.tiledownloader.OpenCycleMapTileDownloader;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageButton;
import android.widget.TextView;

public class FlightGearGPSActivity extends MapActivity implements
		OnClickListener, OnTouchListener {

	private FlightGearGPSContext flightGearGPSContext = FlightGearGPSContext.getContext();
	
	private FGFSConnector fgfsConnectionManager;

	private SharedPreferences preferences;

	private MapView mapView;

	/** Task responsible for updating the map drawings */
	private MapUpdater mapUpdater;

	/** Task responsible for managing the connection with Flightgear */
	private ConnectionTask connectionTask;

	private ScheduledThreadPoolExecutor scheduler;

	/** Handler to allow external activities to update the view */
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
				"192.168.0.100");
		String flightGearPort = preferences
				.getString("flightgear_port", "9000");

		mapView = (MapView) findViewById(R.id.mapview);
		mapView.setBuiltInZoomControls(true);
		mapView.getMapZoomControls().setZoomControlsGravity(
				Gravity.BOTTOM + Gravity.CENTER_HORIZONTAL);
		mapView.setKeepScreenOn(true);
		mapView.setClickable(true);
		mapView.setMapGenerator(new OpenCycleMapTileDownloader());
		
		mapView.setOnTouchListener(this);
		
		new FGFSConnector(flightGearIP, Integer.valueOf(flightGearPort));

		fgfsConnectionManager = new FGFSConnector(flightGearIP,
				Integer.valueOf(flightGearPort));

		flightGearGPSContext.setGpsScratch(new GPSScratch(fgfsConnectionManager));

		ImageButton buttonLocation = (ImageButton) findViewById(R.id.buttonLocation);
		buttonLocation.setOnClickListener(this);

		flightGearGPSContext.setGps(new GPS());

		this.mapUpdater = new MapUpdater(mapView, fgfsConnectionManager,
				this.getResources(), updateHandler);

		this.connectionTask = new ConnectionTask(fgfsConnectionManager);

	}

	private void scheduleTimers() {
		this.scheduler = new ScheduledThreadPoolExecutor(5);
		scheduler.scheduleAtFixedRate(connectionTask, 0,
				ConnectionTask.INTERVAL_MS, TimeUnit.MILLISECONDS);
		scheduler.scheduleAtFixedRate(mapUpdater, 0,
				MapUpdater.UPDATE_INTERVAL_MS, TimeUnit.MILLISECONDS);
	}

	@Override
	protected void onPause() {
		super.onPause();
		scheduler.shutdown();
	}

	@Override
	protected void onResume() {
		super.onResume();
		scheduleTimers();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		try {
			fgfsConnectionManager.close();
			scheduler.shutdown();
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
		GPS gps = flightGearGPSContext.getGps();
		
		TextView textGroundSpeed = (TextView) findViewById(R.id.groundspeedKt);
		TextView textAltitudeFt = (TextView) findViewById(R.id.altitudeFt);

		textGroundSpeed.setText(gps.getFormattedGroundspeed());
		textAltitudeFt.setText(gps.getFormattedAltitude());

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.buttonLocation:
			mapUpdater.centerPlane();
			mapUpdater.setAutocenter(true);
			break;
		default:
			break;
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (v.getId()) {
		case R.id.mapview:
			if (event.getActionMasked() == MotionEvent.ACTION_MOVE) {
				mapUpdater.setAutocenter(false);
			}
			break;
		default:
			break;
		}
		return false;
	}
}