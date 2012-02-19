package org.flightgear.fggps.updaters;

import java.util.List;
import java.util.TimerTask;

import org.flightgear.fggps.connection.FlightGearConnector;
import org.flightgear.fggps.extractor.GPSExtractor;
import org.flightgear.fggps.gps.GPS;
import org.flightgear.fggps.gps.GPSScratch;
import org.flightgear.fggps.gps.WaypointType;
import org.flightgear.fggps.overlays.PlaneOverlay;
import org.flightgear.fggps.overlays.RouteOverlay;

import android.content.res.Resources;

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
	
	private GPSExtractor gpsExtractor;
	
	private boolean online = false;
	
	public MapUpdater(MapView mapView, 
			FlightGearConnector fgConnector, Resources resources, GPS gps) {
		super();
		this.mapView = mapView; 
		
		this.planeOverlay = new PlaneOverlay(resources, gps);
//		this.routeOverlay = RouteOverlay.getDummyRoute();
		
		GPSScratch gpsScratch = new GPSScratch(fgConnector);
		
		RouteOverlay rtOverlay = new RouteOverlay();
			rtOverlay.setStartPoint(gpsScratch.getCurrentWaypoint());
			rtOverlay.setWaypoints(gpsScratch.nearest(WaypointType.AIRPORT, 10));
		this.routeOverlay = rtOverlay;
		
		GeoPoint homePosition = new GeoPoint((int) (20*1E6), (int) (20*1E6));
		
		mapView.getController().animateTo(homePosition);
		
		this.gpsExtractor = new GPSExtractor(fgConnector, gps);
		setOnlineOverlays();
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
	
	public void update() {
		gpsExtractor.extractData();
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
		this.update();
	}
	
}
