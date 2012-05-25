package org.flightgear.fggps;

import java.util.ArrayList;
import java.util.List;

import org.flightgear.fggps.gps.GPS;
import org.flightgear.fggps.gps.GPSScratch;
import org.flightgear.fggps.gps.Waypoint;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

public class SearchActivity extends ListActivity implements OnClickListener {

	private FlightGearGPSContext flightGearGPSContext = FlightGearGPSContext.getContext();
	
	private GPSScratch gpsScratch = flightGearGPSContext.getGpsScratch();
	
	private GPS gps = flightGearGPSContext.getGps();
	
	private List<Waypoint> queryResults = new ArrayList<Waypoint>();

	private ArrayAdapter<Waypoint> waypointsListAdapter;

	private static Integer MAX_RESULTS = 5;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.search_layout);
		
		Button button = (Button) findViewById(R.id.buttonNearest);
		button.setOnClickListener(this);

		button = (Button) findViewById(R.id.buttonSearch);
		button.setOnClickListener(this);
		
		button = (Button) findViewById(R.id.buttonDTO);
		button.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		final Spinner spinnerType = (Spinner) findViewById(R.id.spinnerType);
		final ProgressDialog dialog;

		switch (v.getId()) {
		case R.id.buttonNearest:

			dialog = ProgressDialog.show(SearchActivity.this, "",
					"Searching. Please wait...", true);

			Thread searchThread = new Thread() {
				public void run() {
					try {
						gpsScratch.setPosition(gps.getGeoPointPosition());

						List<Waypoint> nearest = gpsScratch.nearest(
								(String) spinnerType.getSelectedItem(),
								MAX_RESULTS);

						queryResults.clear();
						queryResults.addAll(nearest);
						
						FlightGearGPSContext.getContext().setQueryResults(queryResults);
						
						//waypointsListAdapter.notifyDataSetChanged();
					} catch (Exception e) {
						Log.e("SearchActivity", e.getMessage());
					}
					handler.sendEmptyMessage(0);
				}

				private Handler handler = new Handler() {

					@Override
					public void handleMessage(Message msg) {
						dialog.dismiss();
					}
				};

			};
			searchThread.start();
			
			while (searchThread.isAlive()) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			this.finish();
			
			break;

		case R.id.buttonSearch:

			dialog = ProgressDialog.show(SearchActivity.this, "",
					"Searching. Please wait...", true);

			new Thread() {
				public void run() {
					try {
						EditText inputQuery = (EditText) findViewById(R.id.inputQuery);
						CheckBox searchByName = (CheckBox) findViewById(R.id.searchByName);

						gpsScratch.setPosition(gps.getGeoPointPosition());

						List<Waypoint> results;

						if (searchByName.isChecked()) {
							results = gpsScratch.searchByName(
									(String) spinnerType.getSelectedItem(),
									inputQuery.getText().toString(), false);
						} else {
							results = gpsScratch.search(
									(String) spinnerType.getSelectedItem(),
									inputQuery.getText().toString(), true);
						}

						queryResults.clear();
						queryResults.addAll(results);

					} catch (Exception e) {
						Log.e("SearchActivity", e.getMessage());
					}
					handler.sendEmptyMessage(0);
				}

				private Handler handler = new Handler() {

					@Override
					public void handleMessage(Message msg) {
						waypointsListAdapter.notifyDataSetChanged();
						dialog.dismiss();
					}
				};

			}.start();
			
			this.finish();
			
			break;

	/*	case R.id.buttonDTO:
			
			ListView list = getListView();
			//Waypoint selectedItem = (Waypoint) list.getSelectedItem();
			Waypoint selectedItem = (Waypoint) list.getItemAtPosition(1); //TODO - remove. just for test
			
			if (selectedItem != null) {
				gpsScratch.direct(selectedItem);
			}
			this.finish();
			break;*/
		default:
			break;
		}

	}
}
