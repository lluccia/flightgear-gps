package org.flightgear.fggps.gps;

import java.util.ArrayList;
import java.util.List;

import org.mapsforge.core.GeoPoint;

/**
 * Class representing a waypoint
 * @author leandro
 *
 */
public class Waypoint {
	
	private String ident;
	
	private String name;
	
	private WaypointType type;
	
	private GeoPoint geoPoint;

	public String getIdent() {
		return ident;
	}

	public void setIdent(String id) {
		this.ident = id;
	}

	public String getName() {
		return name;
	}

	public WaypointType getType() {
		return type;
	}

	public void setType(WaypointType type) {
		this.type = type;
	}

	public void setName(String name) {
		this.name = name;
	}

	public GeoPoint getGeoPoint() {
		return geoPoint;
	}

	public void setGeoPoint(GeoPoint geoPoint) {
		this.geoPoint = geoPoint;
	}

	public static List<Waypoint> getDummyList() {
		List<Waypoint> waypoints = new ArrayList<Waypoint>();
		Waypoint wp = new Waypoint();
		
		wp = new Waypoint();
		wp.setIdent("SPSP");
		wp.setName("Congonhas");
		waypoints.add(wp);
		
		wp = new Waypoint();
		wp.setIdent("SPJD");
		wp.setName("Jundiaí");
		waypoints.add(wp);
		
		wp = new Waypoint();
		wp.setIdent("SPKP");
		wp.setName("Viracopos");
		waypoints.add(wp);
		
		return waypoints;
	}

	@Override
	public String toString() {
		return ident + ": " + name;
	}
	
}
