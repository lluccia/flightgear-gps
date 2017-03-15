package org.flightgear.fggps;

import java.util.ArrayList;
import java.util.List;

import org.flightgear.fggps.gps.GPS;
import org.flightgear.fggps.gps.GPSScratch;
import org.flightgear.fggps.gps.Waypoint;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

public class SearchActivity extends Activity implements Runnable {

	protected static final String TAG = "SearchActivity";

	private FlightGearGPSContext flightGearGPSContext = FlightGearGPSContext
			.getContext();

	private GPSScratch gpsScratch = flightGearGPSContext.getGpsScratch();

	private GPS gps = flightGearGPSContext.getGps();

	private List<Waypoint> queryResults = new ArrayList<Waypoint>();

	private static Integer MAX_RESULTS = 5;

	private ProgressDialog dialog;

	private Activity searchActivity;
	private Spinner spinnerType;

	private boolean isNearest;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.search_layout);

		searchActivity = this;
		spinnerType = (Spinner) findViewById(R.id.spinnerType);
	}

	public void onSearchClick(View v) {
		isNearest = false;
		startSearchThread();
	}

	public void onNearestClick(View v) {
		isNearest = true;
		startSearchThread();
	}

	private void startSearchThread() {
		dialog = ProgressDialog.show(this, "", "Searching. Please wait...",
				true);

		Thread searchThread = new Thread(this);
		searchThread.start();
	}

	@Override
	public void run() {
		try {
			Log.d(TAG, "Running search thread...");
			gpsScratch.setPosition(gps.getGeoPointPosition());

			List<Waypoint> searchResults;
			if (isNearest) {
				searchResults = gpsScratch.nearest(
						(String) spinnerType.getSelectedItem(), MAX_RESULTS);
			} else {
				EditText inputQuery = (EditText) findViewById(R.id.inputQuery);
				CheckBox searchByName = (CheckBox) findViewById(R.id.searchByName);

				if (searchByName.isChecked()) {
					searchResults = gpsScratch.searchByName(
							(String) spinnerType.getSelectedItem(), inputQuery
									.getText().toString(), false);
				} else {
					searchResults = gpsScratch.search((String) spinnerType
							.getSelectedItem(),
							inputQuery.getText().toString(), true);
				}
			}

			queryResults.clear();
			queryResults.addAll(searchResults);

			FlightGearGPSContext.getContext().setQueryResults(queryResults);

		} catch (Exception e) {
			Log.e(TAG, "Error ocurred during search", e);
		}
		handler.sendEmptyMessage(0);
	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			Log.d(TAG, "Search thread finished...");

			dialog.dismiss();

			Intent intent = new Intent(searchActivity,
					SearchResultsActivity.class);
			startActivity(intent);
		}
	};
}
