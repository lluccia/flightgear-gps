package org.flightgear.fggps.gps;

public enum WaypointType {
	
	AIRPORT("airport"),
	VOR("vor"),
	NDB("ndb"),
	FIX("fix"),
	WPT("wpt");
	
	private String type;

	WaypointType(String type) {
		this.type=type;
	}
	
	@Override
	public String toString() {
		return type;
	}
	
}
