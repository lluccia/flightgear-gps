package org.flightgear.fggps.connection;

import java.util.TimerTask;

import android.util.Log;

/**
 * This class is responsible for trying to connect including current position,
 * routes etc
 * 
 * @author Leandro Conca
 * 
 */
public class ConnectionTask extends TimerTask {

	/** Time between task executions */
	public final static long INTERVAL_MS = 5000;

	private FGFSConnectionManager fgfsConnectionManager;
	
	public ConnectionTask(FGFSConnectionManager fgfsConnectionManager) {
		this.fgfsConnectionManager = fgfsConnectionManager;
	}
	
	@Override
	public void run() {
		if (!fgfsConnectionManager.isConnected()) {
			fgfsConnectionManager.connect();
		}
	}

}
