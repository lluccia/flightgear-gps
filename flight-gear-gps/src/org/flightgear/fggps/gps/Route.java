package org.flightgear.fggps.gps;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class responsible for representing a GPS route.
 * @author leandro
 *
 */
public class Route {
	
	Waypoint currentWaypoint;
	
	List<Waypoint> waypoints;
	
	public void addWaypoint(Waypoint waypoint) {
		waypoints.add(waypoint);
	}
	
	public void removeWaypoint(Waypoint waypoint) {
		waypoints.remove(waypoint);
	}
	
	public void setActiveWaypoint(Waypoint waypoint) {
		currentWaypoint = waypoint;
	}
	
	public List<Waypoint> getWaypoints() {
		return Collections.unmodifiableList(waypoints);
	}
	
	public List<Leg> getLegs() {
		List<Leg> legs = new ArrayList<Leg>(waypoints.size()-1);
		
		if (waypoints.size() > 1) {
			for (int wpIndex = 1; wpIndex < waypoints.size(); wpIndex++) {
				Leg leg = new Leg(
						waypoints.get(wpIndex-1), 
						waypoints.get(wpIndex));
				legs.add(leg);
			}
		}
		return legs;
	}
	
	public Leg getActiveLeg() {
		Leg activeLeg = null;
		
		int currentWpIndex = waypoints.indexOf(currentWaypoint);
		if (currentWpIndex > 0) {
			activeLeg = new Leg(waypoints.get(currentWpIndex-1), currentWaypoint);
		}
		
		return activeLeg;
	}
	
}
