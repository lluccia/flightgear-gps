package test.org.flightgear.fggps.gps;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.List;

import junit.framework.Assert;

import org.flightgear.data.PropertyTreeTelnet;
import org.flightgear.fggps.gps.GPSScratch;
import org.flightgear.fggps.gps.Waypoint;
import org.flightgear.fggps.gps.WaypointNotFoundException;
import org.flightgear.fggps.gps.WaypointType;
import org.junit.Before;
import org.junit.Test;
import org.mapsforge.core.GeoPoint;

@Ignore
public class GPSScratchIT {

	private GPSScratch gpsScratch;

	private PropertyTreeTelnet propertyTree;

	private GeoPoint sbspGeoPoint = new GeoPoint(-23628000, -46656000); // SP -
																		// Congonhas
	
	@Before
	public void setUp() throws UnknownHostException, IOException {
		this.propertyTree = new PropertyTreeTelnet("localhost", 9000);
		this.gpsScratch = new GPSScratch(propertyTree);
	}

	@Test
	public void testNearestAirports() {
		this.gpsScratch.setPosition(sbspGeoPoint);
		List<Waypoint> nearest = this.gpsScratch.nearest("airport", 5);

		Assert.assertEquals(5, nearest.size());

		for (Waypoint airport : nearest) {
			System.out.print(airport.getIdent());
			System.out.print(":");
			System.out.print(airport.getName());
			System.out.print(":");
			System.out.println(airport.getGeoPoint());
		}
	}

	@Test
	public void testNearestVORs() {
		gpsScratch.setPosition(sbspGeoPoint);
		List<Waypoint> nearest = this.gpsScratch.nearest("vor", 5);

		Assert.assertEquals(5, nearest.size());

		for (Waypoint airport : nearest) {
			System.out.print(airport.getIdent());
			System.out.print(":");
			System.out.print(airport.getName());
			System.out.print(":");
			System.out.println(airport.getGeoPoint());
		}
	}

	@Test(expected = WaypointNotFoundException.class)
	public void testDirectToInexistent() {
		gpsScratch.directTo(getInexistentWaypoint());
	}
	
	private Waypoint getInexistentWaypoint() {
		Waypoint waypoint = new Waypoint();
		waypoint.setType(WaypointType.AIRPORT);
		waypoint.setIdent("NOTEX");
		return waypoint;
	}
	
	@Test
	public void testDirectToExistent() {
		gpsScratch.directTo(getExistentWaypoint());
	}
	
	private Waypoint getExistentWaypoint() {
		Waypoint waypoint = new Waypoint();
		waypoint.setType(WaypointType.AIRPORT);
		waypoint.setIdent("KHAF");
		return waypoint;
	}
}
