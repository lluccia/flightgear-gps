package org.flightgear.fggps;

import java.io.IOException;

import org.flightgear.fgfsclient.FGFSConnection;

import com.google.android.maps.GeoPoint;

public class FlightGearConnector {

	private String serverIP;
	
	private int serverPort;
	
	private FGFSConnection fgfs;
	
	private boolean connected = false;
	
	public FlightGearConnector(String serverIP, int serverPort) {
		super();
		this.serverIP = serverIP;
		this.serverPort = serverPort;
	}
	
	public void connect() {
		new ConnectorThread().start();
	}
	
	public void updateConfiguration(String serverIP, int serverPort) throws IOException {
		this.fgfs = new FGFSConnection(serverIP, serverPort);
	}

	public boolean isConnected() {
		return this.connected;
	}
	
	public GeoPoint getCurrentPosition() throws IOException {
		int latitude = (int) (fgfs.getDouble("/instrumentation/gps/indicated-latitude-deg") * 1E6);
		int longitude = (int) (fgfs.getDouble("/instrumentation/gps/indicated-longitude-deg") * 1E6);;
		
		return new GeoPoint(latitude, longitude);
	}
	
	public float getCurrentIndicatedAltitudeFt() throws IOException {
		return fgfs.getFloat("/instrumentation/gps/indicated-altitude-ft");
	}
	
	public float getCurrentHeadingTrueDeg() throws IOException {
		return fgfs.getFloat("/instrumentation/gps/indicated-track-true-deg");
	}
	
	public class ConnectorThread extends Thread {
		
		@Override
		public void run() {
			try {
				fgfs = new FGFSConnection(serverIP, serverPort);
				connected = true;
			} catch (IOException e) {
				connected = false;
			}
		}
	}
}
