package org.flightgear.fggps.connection;

import java.io.IOException;

public class FlightGearConnector {

	private String serverIP;
	
	private int serverPort;
	
	private FGFSConnection fgfs;
	
	private boolean connected = false;
	
	public FlightGearConnector(String serverIP, int serverPort) {
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
	
	public String get(String path) {
		try {
			return fgfs.get(path);
		} catch (IOException e) {
			this.connected=false;
			return null;
		}
	}
	
	public Boolean getBoolean(String path) {
		try {
			return fgfs.getBoolean(path);
		} catch (IOException e) {
			this.connected=false;
			return null;
		}
	}
	
	public Double getDouble(String path) {
		try {
			return fgfs.getDouble(path);
		} catch (IOException e) {
			this.connected=false;
			return null;
		}
	}
	
	public Float getFloat(String path) {
		try {
			return fgfs.getFloat(path);
		} catch (IOException e) {
			this.connected=false;
			return null;
		}
	}
	
	public Integer getInt(String path)  {
		try {
			return fgfs.getInt(path);
		} catch (IOException e) {
			this.connected=false;
			return null;
		}
	}
	
	public String getString(String path)  {
		try {
			return fgfs.get(path);
		} catch (IOException e) {
			this.connected=false;
			return null;
		}
	}
	
	public void set(String path, String value) {
		try {
			fgfs.set(path, value);
		} catch (IOException e) {
			this.connected=false;			
		}
	}
	
	public void setInt(String path, Integer value) {
		try {
			fgfs.setInt(path, value);
		} catch (IOException e) {
			this.connected=false;			
		}
	}
	
	public void setBoolean(String path, Boolean value) {
		try {
			fgfs.setBoolean(path, value);
		} catch (IOException e) {
			this.connected=false;			
		}
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
