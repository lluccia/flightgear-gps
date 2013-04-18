package org.flightgear.fggps.connection;

import java.io.IOException;
import java.util.TimerTask;

import org.flightgear.data.PropertyTree;

import android.util.Log;

/**
 * This class is responsible for trying to connect to flightgear server 
 * @author Leandro Conca
 * 
 */
public class ConnectionTask extends TimerTask {

	/** Time between task executions */
	public final static long INTERVAL_MS = 5000;

	private PropertyTree propertyTree;
	
	public ConnectionTask(PropertyTree propertyTree) {
		this.propertyTree = propertyTree;
	}
	
	@Override
	public void run() {
		if (!propertyTree.isConnected()) {
			try {
				propertyTree.connect();
			} catch (IOException e) {
				Log.w(ConnectionTask.class.getSimpleName(), "Unable to connect" , e);
			}
		}
	}

}
