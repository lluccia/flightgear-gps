package org.flightgear.fggps.extractor;

import org.flightgear.fggps.connection.FlightGearConnector;
import org.flightgear.fggps.gps.GPS;
import org.flightgear.fggps.gps.GPSMode;

public class GPSExtractor extends Extractor<GPS> {

	public GPSExtractor(FlightGearConnector fgConnector, GPS gps) {
		super(fgConnector, gps);
	}

	@Override
	public void extractData(FlightGearConnector fgConnector, GPS gps) {
		gps.setLatitude(fgConnector.getDouble("/instrumentation/gps/indicated-latitude-deg"));
		gps.setLongitude(fgConnector.getDouble("/instrumentation/gps/indicated-longitude-deg"));
		
		gps.setAltitude(fgConnector.getFloat("/instrumentation/gps/indicated-altitude-ft"));
		gps.setHeading(fgConnector.getFloat("/instrumentation/gps/indicated-track-true-deg"));
		gps.setGroundspeed(fgConnector.getFloat("/instrumentation/gps/indicated-ground-speed-kt"));
		
		//gps.setBearing(fgConnector.getFloat("/instrumentation/gps/"));
		gps.setTrack(fgConnector.getFloat("/instrumentation/gps/indicated-track-true-deg"));
		gps.setDesiredTrack(fgConnector.getFloat("/instrumentation/gps/desired-course-deg"));
		
		//gps.setDistance(fgConnector.getFloat(""));
		
		gps.setMode(GPSMode.valueOf(fgConnector.getString("/instrumentation/gps/mode").toUpperCase()));
	}

}
