package org.flightgear.fggps.extractor;

import org.flightgear.fggps.connection.FGFSConnector;
import org.flightgear.fggps.gps.Route;
import org.flightgear.fggps.gps.Waypoint;

import com.google.android.maps.GeoPoint;

public class RouteExtractor extends Extractor<Route> {

	public RouteExtractor(FGFSConnector fgConnector, Route route) {
		super(fgConnector, route);
	}

	@Override
	public void extractData(FGFSConnector fgConnector, Route route) {
		
		route.clearWaypoints();
		
		int latitudeE6 = (int)(fgConnector.getDouble("/instrumentation/gps/wp/wp/latitude-deg")*1e6);
		int longitudeE6 = (int)(fgConnector.getDouble("/instrumentation/gps/wp/wp/longitude-deg")*1e6);
		
		Waypoint waypoint = new Waypoint();
		waypoint.setGeoPoint(new GeoPoint(latitudeE6, longitudeE6));
		
		route.setStartWaypoint(waypoint);
		
		for (int i=1;;i++) {
			if (fgConnector.getDouble("/instrumentation/gps/wp/wp["+i+"]/latitude-deg") != null) {
				latitudeE6 = (int)(fgConnector.getDouble("/instrumentation/gps/wp/wp["+i+"]/latitude-deg")*1e6);
				longitudeE6 = (int)(fgConnector.getDouble("/instrumentation/gps/wp/wp["+i+"]/longitude-deg")*1e6);
				
				waypoint = new Waypoint();
				waypoint.setGeoPoint(new GeoPoint(latitudeE6, longitudeE6));
				
				route.addWaypoint(waypoint);
				
			} else {
				break;
			}
		}
		
	}

}
