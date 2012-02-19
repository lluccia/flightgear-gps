package org.flightgear.fggps.gps;

/**
 * Class representing a route Leg (way between two waypoints)
 * @author leandro
 *
 */
public class Leg {
	
	private Waypoint startWaypoint;
	
	private Waypoint endWaypoint;
	
	public Leg(Waypoint startWaypoint, Waypoint endWaypoint) {
		super();
		this.startWaypoint = startWaypoint;
		this.endWaypoint = endWaypoint;
	}

	public Waypoint getStartWaypoint() {
		return startWaypoint;
	}

	public void setStartWaypoint(Waypoint startWaypoint) {
		this.startWaypoint = startWaypoint;
	}

	public Waypoint getEndWaypoint() {
		return endWaypoint;
	}

	public void setEndWaypoint(Waypoint endWaypoint) {
		this.endWaypoint = endWaypoint;
	}
	
	
}
