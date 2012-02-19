package org.flightgear.fggps.gps;

import com.google.android.maps.GeoPoint;

/**
 * Class representing a waypoint
 * @author leandro
 *
 */
public class Waypoint {
	
	private String ident;
	
	private String name;
	
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

	public void setName(String name) {
		this.name = name;
	}

	public GeoPoint getGeoPoint() {
		return geoPoint;
	}

	public void setGeoPoint(GeoPoint geoPoint) {
		this.geoPoint = geoPoint;
	}
	
}
