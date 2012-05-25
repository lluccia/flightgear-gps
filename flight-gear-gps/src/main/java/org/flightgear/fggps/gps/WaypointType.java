package org.flightgear.fggps.gps;

public enum WaypointType {
	
	AIRPORT("airport"),
	VOR("vor"),
	NDB("ndb"),
	FIX("fix"),
	WPT("wpt");
	
	private String queryString;

	WaypointType(String queryString) {
		this.queryString=queryString;
	}
	
	public String getQueryString() {
		return queryString;
	}
	
}
