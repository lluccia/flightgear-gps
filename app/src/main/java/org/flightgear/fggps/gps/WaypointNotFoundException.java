package org.flightgear.fggps.gps;

public class WaypointNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -2600570587641557472L;

	public WaypointNotFoundException(String message) {
		super(message);
	}
}
