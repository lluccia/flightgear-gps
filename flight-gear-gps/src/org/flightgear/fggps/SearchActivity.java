package org.flightgear.fggps;

import java.util.ArrayList;
import java.util.List;

import org.flightgear.fggps.gps.GPS;
import org.flightgear.fggps.gps.GPSScratch;
import org.flightgear.fggps.gps.Waypoint;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

public class SearchActivity extends Activity implements OnClickListener {

	private GPS gps = FlightGearGPSActivity.gps;

	private GPSScratch gpsScratch = FlightGearGPSActivity.gpsScratch;

	private List<Waypoint> queryResults = new ArrayList<Waypoint>();

	private ArrayAdapter<Waypoint> waypointsListAdapter;

	private static Integer MAX_RESULTS = 5;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.setContentView(R.layout.search_layout);

		Button button = (Button) findViewById(R.id.buttonNearest);
		button.setOnClickListener(this);

		button = (Button) findViewById(R.id.buttonSearch);
		button.setOnClickListener(this);

		waypointsListAdapter = new ArrayAdapter<Waypoint>(
				this.getApplicationContext(), R.layout.waypoint_lists_item,
				R.id.textWaypoint, queryResults);

		waypointsListAdapter.setNotifyOnChange(true);

		ListView listResults = (ListView) findViewById(R.id.listResults);
		listResults.setAdapter(waypointsListAdapter);
	}

	@Override
	public void onClick(View v) {
		Spinner spinnerType = (Spinner) findViewById(R.id.spinnerType);
		
		switch (v.getId()) {
		case R.id.buttonNearest:

			gpsScratch.setPosition(gps.getGeoPointPosition());

			List<Waypoint> nearest = gpsScratch.nearest(
					(String) spinnerType.getSelectedItem(), MAX_RESULTS);

			queryResults.clear();
			queryResults.addAll(nearest);

			waypointsListAdapter.notifyDataSetChanged();

			break;

		case R.id.buttonSearch:
			
			EditText inputQuery = (EditText) findViewById(R.id.inputQuery);
			CheckBox searchByName = (CheckBox) findViewById(R.id.searchByName);

			gpsScratch.setPosition(gps.getGeoPointPosition());

			List<Waypoint> results;

			if (searchByName.isChecked()) {
				results = gpsScratch.searchByName((String) spinnerType
						.getSelectedItem(), inputQuery.getText().toString(),
						true);
			} else {
				results = gpsScratch.search((String) spinnerType
						.getSelectedItem(), inputQuery.getText().toString(),
						true);
			}

			queryResults.clear();
			queryResults.addAll(results);
			waypointsListAdapter.notifyDataSetChanged();
		default:
			break;
		}

	}
}
