package org.flightgear.fggps.updaters;

import java.util.List;
import java.util.TimerTask;

import org.flightgear.fggps.connection.FGFSConnectionManager;
import org.flightgear.fggps.extractor.GPSExtractor;
import org.flightgear.fggps.extractor.RouteExtractor;
import org.flightgear.fggps.gps.GPS;
import org.flightgear.fggps.gps.GPSScratch;
import org.flightgear.fggps.gps.Route;
import org.flightgear.fggps.view.overlay.PlaneOverlay;
import org.flightgear.fggps.view.overlay.RouteOverlay;

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
	public final static long UPDATE_INTERVAL_MS = 3000;
	
	private MapView mapView;
	
	private PlaneOverlay planeOverlay;
	
	private RouteOverlay routeOverlay;
	
	private GPSExtractor gpsExtractor;
	
	private RouteExtractor routeExtractor;
	
	private boolean online = false;
	
	public MapUpdater(MapView mapView, 
			FGFSConnectionManager fgConnector, Resources resources, GPS gps, Route route) {
		super();
		this.mapView = mapView; 
		
		this.planeOverlay = new PlaneOverlay(resources, gps);
		this.routeOverlay = new RouteOverlay(route);
		
		GPSScratch gpsScratch = new GPSScratch(fgConnector);
		
		GeoPoint homePosition = new GeoPoint(0, 0);
		
		mapView.getController().animateTo(homePosition);
		
		this.gpsExtractor = new GPSExtractor(fgConnector, gps);
		//this.routeExtractor = new RouteExtractor(fgConnector, route);
		
		setOnlineOverlays();
	}
	
	public void setOnlineOverlays() {
		List<Overlay> overlays = mapView.getOverlays();
		overlays.clear();
		//overlays.add(routeOverlay);
		overlays.add(planeOverlay);
	}
	
	public void setOfflineOverlays() {
		List<Overlay> overlays = mapView.getOverlays();
		overlays.clear();
	}
	
	public void update() {
		gpsExtractor.extractData();
		//routeExtractor.extractData();
		mapView.getController().animateTo(planeOverlay.getGeoPointPosition());
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
		Log.d("MapUpdater", "Updating map data...");
		this.update();
	}
	
}
