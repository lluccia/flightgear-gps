package org.flightgear.fggps.extractor;

import java.io.IOException;
import java.util.Map;

import org.flightgear.fggps.connection.FGFSConnectionManager;
import org.flightgear.fggps.gps.GPS;
import org.flightgear.fggps.gps.GPSMode;

public class GPSExtractor extends Extractor<GPS> {

	public GPSExtractor(FGFSConnectionManager fgConnector, GPS gps) {
		super(fgConnector, gps);
	}

	@Override
	public void extractData(FGFSConnectionManager fgConnector, GPS gps) {
			Map<String, String> gpsData;
			try {
				gpsData = fgConnector.dump("/position");
				
				if (dumpIsValid(gpsData)) {
					gps.setLatitude(Double.valueOf(gpsData.get("latitude-deg")));
					gps.setLongitude(Double.valueOf(gpsData.get("longitude-deg")));
					
					gps.setAltitude(Float.valueOf(gpsData.get("altitude-ft")));
					//gps.setHeading(Float.valueOf(gpsData.get("indicated-track-true-deg")));
					//gps.setGroundspeed(Float.valueOf(gpsData.get("indicated-ground-speed-kt")));
				
					//gps.setTrack(Float.valueOf(gpsData.get("indicated-track-true-deg")));
					//gps.setDesiredTrack(Float.valueOf(gpsData.get("desired-course-deg")));
				
					//gps.setMode(GPSMode.valueOf(gpsData.get("mode").toUpperCase()));
				}
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
			
	}

	private boolean dumpIsValid(Map<String, String> gpsData) {
		return (gpsData.size() > 0 
				&& gpsData.get("latitude-deg") != null && gpsData.get("latitude-deg") != ""
				&& gpsData.get("longitude-deg") != null && gpsData.get("longitude-deg") != ""
				&& gpsData.get("altitude-ft") != null && gpsData.get("altitude-ft") != "");
	}

}
