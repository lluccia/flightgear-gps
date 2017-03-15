package org.flightgear.fggps;

import org.flightgear.fggps.gps.GPSScratch;
import org.flightgear.fggps.gps.Waypoint;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class SearchResultsActivity extends ListActivity implements Runnable {

	private static final String TAG = "SearchResultsActivity";

	private FlightGearGPSContext flightGearGPSContext = FlightGearGPSContext
			.getContext();

	private GPSScratch gpsScratch = flightGearGPSContext.getGpsScratch();

	private LayoutInflater layoutInflater;

	private Waypoint selectedWaypoint;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_results);

		this.layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		ListAdapter adapter = new ArrayAdapter<Waypoint>(
				this, // Context.
				R.layout.waypoint_lists_item, R.id.textWaypointName,
				flightGearGPSContext.getQueryResults()) {

			@Override
			public View getView(int position, View convertView,
					android.view.ViewGroup parent) {
				View row;
				if (null == convertView) {
					row = layoutInflater.inflate(R.layout.waypoint_lists_item,
							null);
				} else {
					row = convertView;
				}

				TextView tvIdent = (TextView) row
						.findViewById(R.id.textWaypointIdent);
				tvIdent.setText(getItem(position).getIdent());

				TextView tvName = (TextView) row
						.findViewById(R.id.textWaypointName);
				tvName.setText(getItem(position).getName());
				
				TextView tvDistance = (TextView) row
						.findViewById(R.id.textWaypointDistance);
				long roundedDistance = Math.round(getItem(position).getDistanceNM());
				tvDistance.setText(roundedDistance + " NM");

				return row;
			}

		};

		setListAdapter(adapter);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		selectedWaypoint = (Waypoint) l.getItemAtPosition(position);
		Log.d(TAG, "item selected:" + selectedWaypoint.getIdent());
		Thread threadDirectToWaypoint = new Thread(this);
		threadDirectToWaypoint.start();

		Intent intent = new Intent(this, FlightGearGPSActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);

		this.finish();
	}

	@Override
	public void run() {
		gpsScratch.directTo(selectedWaypoint);
	}
}
