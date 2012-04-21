package org.flightgear.fggps.updaters;

import java.util.List;
import java.util.TimerTask;

import org.flightgear.fggps.connection.FGFSConnector;
import org.flightgear.fggps.extractor.GPSExtractor;
import org.flightgear.fggps.extractor.RouteExtractor;
import org.flightgear.fggps.gps.GPS;
import org.flightgear.fggps.gps.Route;
import org.flightgear.fggps.view.overlay.PlaneOverlay;
import org.flightgear.fggps.view.overlay.RouteOverlay;
import org.mapsforge.android.maps.MapView;
import org.mapsforge.android.maps.overlay.Overlay;
import org.mapsforge.core.GeoPoint;

import android.content.res.Resources;
import android.os.Handler;
import android.util.Log;



/**
 * This class is responsible for updating the map data, including current
 * position, routes etc
 * 
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

	private Handler updateHandler;

	public MapUpdater(MapView mapView, FGFSConnector fgConnector,
			Resources resources, GPS gps, Route route, Handler updateHandler) {
		super();
		this.mapView = mapView;

		this.planeOverlay = new PlaneOverlay(resources, gps);
		this.routeOverlay = new RouteOverlay(route);

		GeoPoint homePosition = new GeoPoint(0, 0);

		mapView.getController().setCenter(homePosition);

		this.gpsExtractor = new GPSExtractor(fgConnector, gps);
		this.routeExtractor = new RouteExtractor(fgConnector, route);
		
		this.updateHandler = updateHandler;
		
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
		routeExtractor.extractData();
		
		updateHandler.sendEmptyMessage(0);
		
		mapView.getController().setCenter(planeOverlay.getGeoPointPosition());
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
