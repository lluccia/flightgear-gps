package dev.conca.fggps.domain;

public class GPS {
	
	/** latitude in degrees*1E6 */
	public int latitude1E6;
	
	/** latitude in degrees*1E6 */
	public int longitude1E6;
	
	/** altitude in feets */
	public float altitude;
	
	/** heading in degrees */
	public float heading;

	public int getLatitude1E6() {
		return latitude1E6;
	}

	public void setLatitude1E6(int latitude1e6) {
		latitude1E6 = latitude1e6;
	}

	public int getLongitude1E6() {
		return longitude1E6;
	}

	public void setLongitude1E6(int longitude1e6) {
		longitude1E6 = longitude1e6;
	}

	public float getAltitude() {
		return altitude;
	}

	public void setAltitude(float altitude) {
		this.altitude = altitude;
	}

	public float getHeading() {
		return heading;
	}

	public void setHeading(float heading) {
		this.heading = heading;
	}
	
}
