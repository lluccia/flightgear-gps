package org.flightgear.fggps.extractor;

import org.flightgear.data.PropertyTree;
import org.flightgear.fggps.gps.Route;
import org.flightgear.fggps.gps.Waypoint;
import org.mapsforge.core.GeoPoint;

public class RouteExtractor extends Extractor<Route> {

	public RouteExtractor(PropertyTree propertyTree, Route route) {
		super(propertyTree, route);
	}

	@Override
	public void extractData(PropertyTree propertyTree, Route route) {
		
		route.clearWaypoints();
		
		int latitudeE6 = (int)(propertyTree.getDouble("/instrumentation/gps/wp/wp/latitude-deg") * 1e6);
		int longitudeE6 = (int)(propertyTree.getDouble("/instrumentation/gps/wp/wp/longitude-deg") * 1e6);
		
		Waypoint waypoint = new Waypoint();
		waypoint.setGeoPoint(new GeoPoint(latitudeE6, longitudeE6));
		
		route.setStartWaypoint(waypoint);

		//TODO - Identify how many route waypoints exist
		int waypointCount = 1;
		
		for (int i=1; i <= waypointCount; i++) {
			latitudeE6 = (int)(propertyTree.getDouble("/instrumentation/gps/wp/wp["+i+"]/latitude-deg") * 1e6);
			longitudeE6 = (int)(propertyTree.getDouble("/instrumentation/gps/wp/wp["+i+"]/longitude-deg") * 1e6);
			
			waypoint = new Waypoint();
			waypoint.setGeoPoint(new GeoPoint(latitudeE6, longitudeE6));
			
			route.addWaypoint(waypoint);
		}
	}
}
