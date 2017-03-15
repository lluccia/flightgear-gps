package org.flightgear.fggps.connection;

import java.io.IOException;

import org.flightgear.data.PropertyTree;

import android.util.Log;

/**
 * This class is responsible for trying to connect to flightgear server
 * 
 * @author Leandro Conca
 * 
 */
public class ConnectionTask implements Runnable {

	private static final String TAG = "ConnectionTask";

	/** Time between task executions */
	public static final long INTERVAL_MS = 5000L;

	private PropertyTree propertyTree;

	public ConnectionTask(PropertyTree propertyTree) {
		this.propertyTree = propertyTree;
	}

	@Override
	public void run() {
		if (!propertyTree.isConnected()) {
			try {
				Log.d(TAG, "Trying to connect...");
				propertyTree.connect();
				Log.d(TAG, "Connection estabilished!");
			} catch (IOException e) {
				Log.w(TAG, "Unable to connect", e);
			}
		}
	}

}
