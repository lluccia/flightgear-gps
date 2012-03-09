package org.flightgear.fggps.extractor;

import org.flightgear.fggps.connection.FGFSConnectionManager;

/**
 * Abstract class representing a data object that can be extracted from game properties
 * @author leandro
 *
 * @param <T> The type of data object
 */
public abstract class Extractor<T> {
	
	private FGFSConnectionManager fgConnector;
	
	private T dataObject;
	
	public Extractor(FGFSConnectionManager fgConnector, T dataObject) {
		this.fgConnector = fgConnector;
		this.dataObject = dataObject;
	}
	
	public void extractData() {
		if (fgConnector.isConnected()) {
			extractData(fgConnector, dataObject);
		}
	}
	
	public abstract void extractData(FGFSConnectionManager fgConnector, T dataObject);
}
