package org.flightgear.fggps.updaters;

import java.io.IOException;
import java.util.List;
import java.util.TimerTask;

import org.flightgear.fggps.FlightGearConnector;
import org.flightgear.fggps.overlays.PlaneOverlay;
import org.flightgear.fggps.overlays.RouteOverlay;

import android.content.res.Resources;
import android.util.Log;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;


/**
 * This class is responsible for updating the map data, 
 * including current position, routes etc
 * @author Leandro Conca
 *
 */
public class MapUpdater extends TimerTask {

	/** Time between position updates */
	public final static long UPDATE_INTERVAL_MS = 300;
	
	private MapView mapView;
	
	private PlaneOverlay planeOverlay;
	
	private RouteOverlay routeOverlay;
	
	private FlightGearConnector flightGearConnector;
	
	private boolean online = false;
	
	public MapUpdater(MapView mapView, 
			FlightGearConnector flightGearConnector, Resources resources) {
		super();
		this.mapView = mapView; 
		this.flightGearConnector = flightGearConnector;
		
		this.planeOverlay = new PlaneOverlay(resources);
		this.routeOverlay = RouteOverlay.getDummyRoute();
		
		GeoPoint homePosition = new GeoPoint((int) (20*1E6), (int) (20*1E6));
		
		mapView.getController().animateTo(homePosition);
		
		changeOnlineStatus(false);
	}
	
	public void setOnlineOverlays() {
		List<Overlay> overlays = mapView.getOverlays();
		overlays.clear();
		overlays.add(routeOverlay);
		overlays.add(planeOverlay);
	}
	
	public void setOfflineOverlays() {
		List<Overlay> overlays = mapView.getOverlays();
		overlays.clear();
	}
	
	public void update() throws IOException {
		GeoPoint position = flightGearConnector.getCurrentPosition();
		planeOverlay.updatePosition(position);
		planeOverlay.updateHeading(flightGearConnector.getCurrentHeadingTrueDeg());
		
		
		Log.d("MapUpdater","Updating plane position");
		if (position != null) {
			Log.d("MapUpdater","LAT: " + position.getLatitudeE6());
			Log.d("MapUpdater","LONG: " + position.getLongitudeE6());
			
			mapView.getController().animateTo(position);
		} else {
			Log.d("MapUpdater","Position is null!");
		}
		
	}

	private void changeOnlineStatus(boolean connected) {
		if (online) {
			if (!connected) {
				setOfflineOverlays();
				online = false;
			}
		} else {
			if (connected) {
				setOnlineOverlays();
				online = true;
			}
		}
	}
	
	@Override
	public void run() {
		try {
			if (flightGearConnector.isConnected()) {
				this.update();
				changeOnlineStatus(true);
			} else {
				flightGearConnector.connect();
			}
		} catch (IOException e) {
			changeOnlineStatus(false);
		}
	}
	
}
