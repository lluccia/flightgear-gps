package org.flightgear.fggps.updaters;

import java.io.IOException;

import org.flightgear.fgfsclient.FGFSConnection;
import org.flightgear.fggps.domain.GPS;


/**
 * Class responsible for getting gps information from simulation
 * and loading data into GPS domain object
 * 
 * @author Leandro Conca
 */
public class GPSUpdater {
	
	private FGFSConnection fgfs;
	
	private GPS gps;
	
	public GPSUpdater(String serverIP, int serverPort) throws IOException {
		super();
		this.fgfs = new FGFSConnection(serverIP, serverPort);
		this.gps = new GPS();
	}

	public void setFGFSConnection(FGFSConnection fgfs) {
		this.fgfs = fgfs;
	}

	public void setGPS(GPS gps) {
		this.gps = gps;
	}
	
	public GPS getGps() {
		return gps;
	}

	public void update() throws IOException {
		//updatePosition;
		int lat = (int) (fgfs.getDouble("/instrumentation/gps/indicated-latitude-deg") * 1E6);
		int lng = (int) (fgfs.getDouble("/instrumentation/gps/indicated-longitude-deg") * 1E6);
		
		this.gps.setLatitude1E6(lat);
		this.gps.setLongitude1E6(lng);
		
		//update altitude
		float alt = fgfs.getFloat("/instrumentation/gps/indicated-altitude-ft");
		this.gps.setAltitude(alt);
		
		//updateHeading
		float hdg = fgfs.getFloat("/instrumentation/gps/indicated-track-true-deg");
		this.gps.setHeading(hdg);
	}
}
