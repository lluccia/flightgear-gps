package org.flightgear.fggps.extractor;

import org.flightgear.fggps.connection.FlightGearConnector;

/**
 * Abstract class representing data that can be extracted from game properties
 * @author leandro
 *
 * @param <T>
 */
public abstract class Extractor<T> {
	
	private FlightGearConnector fgConnector;
	
	private T dataObject;
	
	public Extractor(FlightGearConnector fgConnector, T dataObject) {
		this.fgConnector = fgConnector;
		this.dataObject = dataObject;
	}
	
	public void extractData() {
		if (fgConnector.isConnected()) {
			extractData(fgConnector, dataObject);
		}
	}
	
	public abstract void extractData(FlightGearConnector fgConnector, T dataObject);
}
