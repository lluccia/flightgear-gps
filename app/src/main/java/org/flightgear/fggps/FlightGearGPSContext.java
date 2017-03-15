package org.flightgear.fggps;

import java.util.List;

import org.flightgear.fggps.gps.GPS;
import org.flightgear.fggps.gps.GPSScratch;
import org.flightgear.fggps.gps.Route;
import org.flightgear.fggps.gps.Waypoint;

public class FlightGearGPSContext {

	private static FlightGearGPSContext flightGearGPSContext;
	
	private GPS gps =  new GPS();
	
	private GPSScratch gpsScratch;
	
	private Route route = new Route();
	
	private List<Waypoint> queryResults;

	private boolean connected = false;
	
	private boolean routeDefined = false;
	
	private FlightGearGPSContext() {
	}

	public static FlightGearGPSContext getContext() {
		if (flightGearGPSContext == null) {
			flightGearGPSContext = new FlightGearGPSContext();
		}
		return flightGearGPSContext;
	}

	public GPS getGps() {
		return gps;
	}

	public void setGps(GPS gps) {
		this.gps = gps;
	}

	public GPSScratch getGpsScratch() {
		return gpsScratch;
	}

	public void setGpsScratch(GPSScratch gpsScratch) {
		this.gpsScratch = gpsScratch;
	}

	public Route getRoute() {
		return route;
	}

	public void setRoute(Route route) {
		this.route = route;
	}

	public List<Waypoint> getQueryResults() {
		return queryResults;
	}

	public void setQueryResults(List<Waypoint> queryResults) {
		this.queryResults = queryResults;
	}

	public boolean isConnected() {
		return connected;
	}

	public void setConnected(boolean connected) {
		this.connected = connected;
	}

	public boolean isRouteDefined() {
		return routeDefined;
	}

	public void setRouteDefined(boolean routeDefined) {
		this.routeDefined = routeDefined;
	}
}
