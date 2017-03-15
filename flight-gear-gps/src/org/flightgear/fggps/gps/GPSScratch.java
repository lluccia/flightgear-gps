package org.flightgear.fggps.gps;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.flightgear.data.PropertyTree;
import org.mapsforge.core.GeoPoint;

import android.util.Log;

/**
 * GPS controller class that interacts with FlightGear GPS internals
 * 
 * @author leandro
 * 
 */
public class GPSScratch {

	private static final String TAG = "GPSScratch";

	private PropertyTree propertyTree;

	private static final String PROPERTY_PATH_SCRATCH = "instrumentation/gps/scratch";
	private static final String PROPERTY_PATH_SCRATCH_WITH_SEPARATOR = PROPERTY_PATH_SCRATCH
			+ "/";
	private static final String PROPERTY_PATH_COMMAND_WITH_SEPARATOR = "instrumentation/gps/command/";

	private static final String PROPERTY_PATH_CURRENT_WAYPOINT = "wp/wp/";
	private static final String PROPERTY_PATH_ACTIVE_WAYPOINT = "wp/wp[1]/";

	private static final String COMMAND_LEG = "leg";
	private static final String COMMAND_OBS = "obs";
	private static final String COMMAND_DIRECT = "direct";
	private static final String COMMAND_NEAREST = "nearest";
	private static final String COMMAND_SEARCH = "search";
	private static final String COMMAND_SEARCH_NAMES = "search-names";
	private static final String COMMAND_LOAD_ROUTE_WPT = "load-route-wpt";
	private static final String COMMAND_NEXT = "next";
	private static final String COMMAND_PREVIOUS = "previous";
	private static final String COMMAND_DEFINE_USER_WAYPOINT = "define-user-waypoint";
	private static final String COMMAND_ROUTE_INSERT_BEFORE = "route-insert-before";
	private static final String COMMAND_ROUTE_INSERT_AFTER = "route-insert-after";
	private static final String COMMAND_ROUTE_DELETE = "route-delete";

	public GPSScratch(PropertyTree propertyTree) {
		super();
		this.propertyTree = propertyTree;
	}

	public void leg() {
		execute(COMMAND_LEG);
	}

	public void obs() {
		execute(COMMAND_OBS);
	}

	private void direct() {
		execute(COMMAND_DIRECT);
	}

	public void directTo(Waypoint waypoint) {
		searchByIdent(waypoint.getType().getQueryString(), waypoint.getIdent());
		if (getResultCount() != 1) {
			throw new WaypointNotFoundException("Waypoint "
					+ waypoint.getIdent() + "[" + waypoint.getType()
					+ "] was not found!");
		}
		direct();
	}

	/**
	 * Sets current position in GPS Scratch. This will affect the nearest
	 * search.
	 * 
	 * @param geopoint
	 *            - current position
	 */
	public void setPosition(GeoPoint geopoint) {
		setProperty("latitude-deg", Double.toString(geopoint.getLatitude()));
		setProperty("longitude-deg", Double.toString(geopoint.getLongitude()));
	}

	/**
	 * Gets the current waypoint in the scratch
	 * 
	 * @return the Waypoint in the scratch
	 */
	public Waypoint getWaypointInScratch() {
		Waypoint waypoint = new Waypoint();
		
		waypoint.setIdent(getProperty("ident"));
		waypoint.setName(getProperty("name"));
		waypoint.setType(WaypointType.valueOf(getProperty("type").toUpperCase(Locale.ENGLISH)));
	
		waypoint.setGeoPoint(new GeoPoint(
				(int) (Double.valueOf(getPropertyDouble("latitude-deg")) * 1e6),
				(int) (Double.valueOf(getPropertyDouble("longitude-deg")) * 1e6)));
	
		waypoint.setDistanceNM(getPropertyDouble("distance-nm"));
		
		return waypoint;
	}

	/**
	 * Search waypoints nearest to the current waypoint in scratch
	 * 
	 * @param type
	 *            of waypoint (any, airport, vor, ndb, fix, wpt)
	 * @param maxResults
	 *            the query will return (Integer)
	 */
	public List<Waypoint> nearest(String type, Integer maxResults) {
		setProperty("type", type);
		setPropertyInt("max-results", maxResults);

		execute(COMMAND_NEAREST);

		return getResults();
	}

	public List<Waypoint> searchByIdent(String type, String ident) {
		search(type, ident, true);
		return getResults();
	}

	/**
	 * Search waypoints nearest to the current waypoint set in scratch
	 * 
	 * @param type
	 *            NavigationAidType
	 * @param maxResults
	 */
	public List<Waypoint> search(String type, String query, Boolean exact) {

		setProperty("type", type);
		setProperty("query", query);
		setPropertyBoolean("exact", exact);

		execute(COMMAND_SEARCH);

		return getResults();
	}

	public List<Waypoint> searchByName(String type, String query, Boolean exact) {
		setProperty("type", type);
		setProperty("query", query);
		setPropertyBoolean("exact", exact);

		execute(COMMAND_SEARCH_NAMES);

		return getResults();
	}

	private void previous() {
		execute(COMMAND_PREVIOUS);
	}

	private void next() {
		execute(COMMAND_NEXT);
	}

	private void setProperty(String property, String value) {
		propertyTree
				.set(PROPERTY_PATH_SCRATCH_WITH_SEPARATOR + property, value);
	}

	private void setPropertyInt(String property, Integer value) {
		propertyTree.setInt(PROPERTY_PATH_SCRATCH_WITH_SEPARATOR + property,
				value);
	}

	private void setPropertyBoolean(String property, Boolean value) {
		propertyTree.setBoolean(
				PROPERTY_PATH_SCRATCH_WITH_SEPARATOR + property, value);
	}

	private String getProperty(String property) {
		return propertyTree
				.get(PROPERTY_PATH_SCRATCH_WITH_SEPARATOR + property);
	}

	private int getPropertyInt(String property) {
		return propertyTree.getInt(PROPERTY_PATH_SCRATCH_WITH_SEPARATOR
				+ property);
	}

	private Boolean getPropertyBoolean(String property) {
		return propertyTree.getBoolean(PROPERTY_PATH_SCRATCH_WITH_SEPARATOR
				+ property);
	}

	private Double getPropertyDouble(String property) {
		return propertyTree.getDouble(PROPERTY_PATH_SCRATCH_WITH_SEPARATOR
				+ property);
	}

	private void execute(String command) {
		propertyTree.set(PROPERTY_PATH_COMMAND_WITH_SEPARATOR, command);
	}

	private List<Waypoint> getResults() {
		Log.d(TAG, "Getting waypoint results");
		List<Waypoint> waypoints = new ArrayList<Waypoint>();
		int resultCount = getResultCount();
		for (int i = 0; i < resultCount; i++) {
			Waypoint waypointInScratch = getWaypointInScratch();

			Log.d(TAG, "Got waypoint:" + waypointInScratch.getIdent());

			waypoints.add(waypointInScratch);
			next();
		}
		return waypoints;
	}

	/**
	 * Method that returns the result count to be used after a search command
	 * execution
	 * 
	 * @return integer - number of results
	 */
	private int getResultCount() {
		int resultCount = 0;
		if (getPropertyBoolean("valid")) {
			resultCount = getPropertyInt("result-count");
			Log.d(TAG, "resultCount:" + resultCount);
		}
		return resultCount;
	}
}
