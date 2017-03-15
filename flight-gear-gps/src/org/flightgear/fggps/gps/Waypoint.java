package org.flightgear.fggps.gps;

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

	private Double distanceNM;
	
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

	@Override
	public String toString() {
		return ident + ": " + name;
	}

	public Double getDistanceNM() {
		return distanceNM;
	}

	public void setDistanceNM(Double distanceNM) {
		this.distanceNM = distanceNM;
	}
	
}
