package org.flightgear.fggps.gps;

import java.util.ArrayList;
import java.util.List;

import org.flightgear.fggps.connection.FlightGearConnector;

import com.google.android.maps.GeoPoint;

/**
 * GPS controller class that interacts with FlightGear GPS internals
 * 
 * @author leandro
 * 
 */
public class GPSScratch {

	private FlightGearConnector fgConnector;

	private static final String PROPERTY_PATH_SCRATCH = "instrumentation/gps/scratch/";
	private static final String PROPERTY_PATH_COMMAND = "instrumentation/gps/command/";

	private static final String COMMAND_LEG = "leg";
	private static final String COMMAND_OBS = "obs";
	private static final String COMMAND_DIRECT = "direct";
	private static final String COMMAND_NEAREST = "nearest";
	private static final String COMMAND_SEARCH = "search";
	private static final String COMMAND_SEARCH_NAME = "search-name";
	private static final String COMMAND_LOAD_ROUTE_WPT = "load-route-wpt";
	private static final String COMMAND_NEXT = "next";
	private static final String COMMAND_PREVIOUS = "previous";
	private static final String COMMAND_DEFINE_USER_WAYPOINT = "define-user-waypoint";
	private static final String COMMAND_ROUTE_INSERT_BEFORE = "route-insert-before";
	private static final String COMMAND_ROUTE_INSERT_AFTER = "route-insert-after";
	private static final String COMMAND_ROUTE_DELETE = "route-delete";

	public GPSScratch(FlightGearConnector fgConnector) {
		super();
		this.fgConnector = fgConnector;
	}

	public void leg() {
		execute(COMMAND_LEG);
	}

	public void obs() {
		execute(COMMAND_OBS);
	}

	public void direct() {
		execute(COMMAND_DIRECT);
	}

	/**
	 * Search waypoints nearest to the current waypoint in scratch
	 * 
	 * @param type
	 *            of waypoint
	 * @param maxResults
	 *            the query will return (Integer)
	 * @return @
	 */
	public List<Waypoint> nearest(WaypointType type, Integer maxResults) {

		List<Waypoint> waypoints = new ArrayList<Waypoint>();

		setProperty("type", type.toString());
		setPropertyInt("max-results", maxResults);

		execute(COMMAND_NEAREST);

		for (int i = 0; i < getPropertyInt("result-count"); i++) {
			waypoints.add(getCurrentWaypoint());
			next();
		}

		return waypoints;
	}

	/**
	 * Search waypoints nearest to the current waypoint set in scratch
	 * 
	 * @param type
	 *            NavigationAidType
	 * @param maxResults
	 * @return @
	 */
	public List<Waypoint> search(WaypointType type, String query,
			Boolean exactMatch) {

		List<Waypoint> waypoints = new ArrayList<Waypoint>();

		setProperty("type", type.toString());
		setProperty("query", query);
		setPropertyBoolean("exactMatch", exactMatch);

		execute(COMMAND_SEARCH);

		for (int i = 0; i < getPropertyInt("result-count"); i++) {
			waypoints.add(getCurrentWaypoint());
			next();
		}

		return waypoints;
	}

	public void searchByName(WaypointType type, String query, Boolean exactMatch) {
		setProperty("type", type.toString());
		setProperty("query", query);
		setPropertyBoolean("exactMatch", exactMatch);

		execute(COMMAND_SEARCH_NAME);
	}

	private void previous() {
		execute(COMMAND_PREVIOUS);
	}

	private void next() {
		execute(COMMAND_NEXT);
	}

	/**
	 * Gets the current waypoint in the scratch
	 * 
	 * @return the Waypoint in the scratch
	 */
	public Waypoint getCurrentWaypoint() {
		Waypoint waypoint = new Waypoint();

		waypoint.setIdent(getProperty("ident"));
		waypoint.setName(getProperty("name"));
		waypoint.setGeoPoint(new GeoPoint(
				(int) (getPropertyDouble("latitude-deg") * 1e6),
				(int) (getPropertyDouble("longitude-deg") * 1e6)));

		return waypoint;
	}

	private void setProperty(String property, String value) {
		fgConnector.set(PROPERTY_PATH_SCRATCH + property, value);
	}

	private void setPropertyInt(String property, Integer value) {
		fgConnector.setInt(PROPERTY_PATH_SCRATCH + property, value);
	}

	private void setPropertyBoolean(String property, Boolean value) {
		fgConnector.setBoolean(PROPERTY_PATH_SCRATCH + property, value);
	}

	private String getProperty(String property) {
		return fgConnector.get(PROPERTY_PATH_SCRATCH + property);
	}

	private int getPropertyInt(String property) {
		return fgConnector.getInt(PROPERTY_PATH_SCRATCH + property);
	}

	private Boolean getPropertyBoolean(String property) {
		return fgConnector.getBoolean(PROPERTY_PATH_SCRATCH + property);
	}

	private Double getPropertyDouble(String property) {
		return fgConnector.getDouble(PROPERTY_PATH_SCRATCH + property);
	}

	private void execute(String command) {
		fgConnector.set(PROPERTY_PATH_COMMAND, command);
	}
}
