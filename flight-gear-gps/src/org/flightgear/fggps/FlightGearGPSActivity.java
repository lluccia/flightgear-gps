package org.flightgear.fggps;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.flightgear.data.PropertyTree;
import org.flightgear.data.PropertyTreeTelnet;
import org.flightgear.fggps.connection.ConnectionTask;
import org.flightgear.fggps.gps.GPS;
import org.flightgear.fggps.gps.GPSScratch;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.TextView;

public class FlightGearGPSActivity extends MapActivity implements
		OnTouchListener, Observer {

	private FlightGearGPSContext flightGearGPSContext = FlightGearGPSContext
			.getContext();

	private PropertyTree propertyTree;

	private SharedPreferences preferences;

	private MapView mapView;

	/** Task responsible for updating the map drawings */
	private MapUpdater mapUpdater;

	/** Task responsible for managing the connection with Flightgear */
	private ConnectionTask connectionTask;

	private ScheduledExecutorService scheduler;

	private ScheduledFuture<?> connectionTaskScheduled;
	private ScheduledFuture<?> mapUpdaterScheduled;

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

		requestWindowFeature(Window.FEATURE_ACTION_BAR);

		setContentView(R.layout.gps_layout);

		preferences = PreferenceManager.getDefaultSharedPreferences(this);

		String flightGearIP = preferences.getString("flightgear_ip",
				"192.168.0.200");
		String flightGearPort = preferences
				.getString("flightgear_port", "9000");

		mapView = (MapView) findViewById(R.id.mapview);

		mapView.setKeepScreenOn(true);
		mapView.setClickable(true);
		mapView.setMapGenerator(new OpenCycleMapTileDownloader());
		mapView.setOnTouchListener(this);

		try {
			propertyTree = new PropertyTreeTelnet(flightGearIP,
					Integer.valueOf(flightGearPort));
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		flightGearGPSContext.setGpsScratch(new GPSScratch(propertyTree));
		flightGearGPSContext.setGps(new GPS());

		this.mapUpdater = new MapUpdater(mapView, propertyTree,
				this.getResources(), updateHandler);

		this.connectionTask = new ConnectionTask(propertyTree);
	}

	@Override
	protected void onStart() {
		super.onStart();
		this.scheduler = Executors.newScheduledThreadPool(2);
		this.connectionTaskScheduled = scheduler.scheduleWithFixedDelay(
				connectionTask, 0, ConnectionTask.INTERVAL_MS,
				TimeUnit.MILLISECONDS);
	}

	@Override
	protected void onResume() {
		super.onResume();
		this.mapUpdaterScheduled = scheduler.scheduleWithFixedDelay(mapUpdater,
				0, MapUpdater.UPDATE_INTERVAL_MS, TimeUnit.MILLISECONDS);
	}

	@Override
	protected void onPause() {
		super.onPause();
		mapUpdaterScheduled.cancel(true);
	}

	@Override
	protected void onStop() {
		super.onStop();
		connectionTaskScheduled.cancel(true);
		scheduler.shutdownNow();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (propertyTree.isConnected()) {
			propertyTree.close();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}

	public void updateTextView() {
		GPS gps = flightGearGPSContext.getGps();

		TextView textGroundSpeed = (TextView) findViewById(R.id.groundspeedKt);
		TextView textAltitudeFt = (TextView) findViewById(R.id.altitudeFt);

		textGroundSpeed.setText(gps.getFormattedGroundspeed());
		textAltitudeFt.setText(gps.getFormattedAltitude());
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		boolean ret;

		switch (item.getItemId()) {
		case R.id.menu_settings:
			Intent settingsIntent = new Intent(this, SettingsActivity.class);
			startActivity(settingsIntent);
			ret = true;
			break;
		case R.id.menu_search:
			search();
			ret = true;
			break;
		case R.id.menu_location:
			location();
			ret = true;
			break;
		default:
			ret = super.onOptionsItemSelected(item);
			break;
		}
		return ret;
	}

	public void location() {
		mapUpdater.centerPlane();
		mapUpdater.setAutocenter(true);
	}

	public void search() {
		Intent intent = new Intent(this, SearchActivity.class);
		startActivity(intent);
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

	@Override
	public void update(Observable observable, Object data) {
		
	}
}