package org.flightgear.fggps;

import com.google.android.maps.GeoPoint;

public class GPS {

	private GeoPoint currentPosition;

	private float heading;

	private GPSMode mode;

	public GeoPoint getCurrentPosition() {
		return currentPosition;
	}

	public void setCurrentPosition(GeoPoint currentPosition) {
		this.currentPosition = currentPosition;
	}

	public float getHeading() {
		return heading;
	}

	public void setHeading(float heading) {
		this.heading = heading;
	}

	public GPSMode getMode() {
		return mode;
	}

	public void setMode(GPSMode mode) {
		this.mode = mode;
	}
}
