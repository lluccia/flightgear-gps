package org.flightgear.fggps.gps;

import java.util.Observable;

import org.mapsforge.core.GeoPoint;

public class GPS extends Observable {

	private double latitude;
	
	private double longitude;

	private int groundspeed;
	
	private int altitude;
	
	private float heading;

	/** Aircraft path along the ground (corrected for wind, deviation etc.) */
	private float track;
	
	/** Course line to your next waypoint */
	private float desiredTrack;
	
	private float bearing;
	
	private float distance;
	
	private GPSMode mode;

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public int getGroundspeed() {
		return groundspeed;
	}

	public void setGroundspeed(int groundspeed) {
		this.groundspeed = groundspeed;
	}

	public int getAltitude() {
		return altitude;
	}

	public void setAltitude(int altitude) {
		this.altitude = altitude;
	}

	public float getHeading() {
		return heading;
	}

	public void setHeading(float heading) {
		this.heading = heading;
	}

	public float getTrack() {
		return track;
	}

	public void setTrack(float track) {
		this.track = track;
	}

	public float getDesiredTrack() {
		return desiredTrack;
	}

	public void setDesiredTrack(float desiredTrack) {
		this.desiredTrack = desiredTrack;
	}

	public float getBearing() {
		return bearing;
	}

	public void setBearing(float bearing) {
		this.bearing = bearing;
	}

	public float getDistance() {
		return distance;
	}

	public void setDistance(float distance) {
		this.distance = distance;
	}

	public GPSMode getMode() {
		return mode;
	}

	public void setMode(GPSMode mode) {
		this.mode = mode;
	}
	
	public GeoPoint getGeoPointPosition() {
		return new GeoPoint(
				((int) (this.latitude*1e6)),
				((int) (this.longitude*1e6)));
	}

	public String getFormattedGroundspeed() {
		return String.valueOf(groundspeed) + " kt";
	}

	public CharSequence getFormattedAltitude() {
		return String.valueOf(altitude) + " ft";
	}
}
