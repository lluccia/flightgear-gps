package org.flightgear.fggps.extractor;

import java.util.Map;

import org.flightgear.data.PropertyTree;
import org.flightgear.fggps.gps.GPS;

public class GPSExtractor extends Extractor<GPS> {

	public GPSExtractor(PropertyTree propertyTree, GPS gps) {
		super(propertyTree, gps);
	}

	@Override
	public void extractData(PropertyTree propertyTree, GPS gps) {
		Map<String, String> positionData = propertyTree.dump("/position");;
		Map<String, String> orientationData = propertyTree.dump("/orientation");;

		if (dumpIsValid(positionData, orientationData)) {
			gps.setLatitude(Double.valueOf(positionData.get("latitude-deg")));
			gps.setLongitude(Double.valueOf(positionData.get("longitude-deg")));

			gps.setAltitude(Float.valueOf(positionData.get("altitude-ft"))
					.intValue());

			gps.setHeading(Float.valueOf(orientationData.get("heading-deg")));

			gps.setGroundspeed(propertyTree.getFloat(
					"/velocities/groundspeed-kt").intValue());

			// gps.setTrack(Float.valueOf(..));
			// gps.setDesiredTrack(Float.valueOf(..));

			// gps.setMode(GPSMode.valueOf(..));
		}
	}

	private boolean dumpIsValid(Map<String, String> gpsData,
			Map<String, String> orientationData) {
		return (gpsData.size() > 0 && gpsData.get("latitude-deg") != null
				&& gpsData.get("latitude-deg") != ""
				&& gpsData.get("longitude-deg") != null
				&& gpsData.get("longitude-deg") != ""
				&& gpsData.get("altitude-ft") != null && gpsData
				.get("altitude-ft") != "")
				&& (orientationData.size() > 0
						&& orientationData.get("heading-deg") != null && orientationData
						.get("latitude-deg") != "");
	}

}
