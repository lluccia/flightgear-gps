package org.flightgear.fggps.updaters;

import java.util.List;
import java.util.TimerTask;

import org.flightgear.data.PropertyTree;
import org.flightgear.fggps.FlightGearGPSContext;
import org.flightgear.fggps.extractor.GPSExtractor;
import org.flightgear.fggps.extractor.RouteExtractor;
import org.flightgear.fggps.view.overlay.PlaneOverlay;
import org.flightgear.fggps.view.overlay.RouteOverlay;
import org.flightgear.fggps.view.overlay.SearchResultsOverlay;
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

	private FlightGearGPSContext flightGearGPSContext = FlightGearGPSContext
			.getContext();

	/** Time between position updates */
	public final static long UPDATE_INTERVAL_MS = 1000;

	private MapView mapView;

	private PlaneOverlay planeOverlay;

	private RouteOverlay routeOverlay;

	private SearchResultsOverlay searchResultsOverlay;

	private GPSExtractor gpsExtractor;

	private RouteExtractor routeExtractor;

	private Handler updateHandler;

	private boolean autocenter = true;

	public MapUpdater(MapView mapView, PropertyTree propertyTree,
			Resources resources, Handler updateHandler) {
		super();
		this.mapView = mapView;

		this.planeOverlay = new PlaneOverlay(resources,
				flightGearGPSContext.getGps());
		this.routeOverlay = new RouteOverlay(flightGearGPSContext.getRoute());
		this.searchResultsOverlay = new SearchResultsOverlay(resources);

		GeoPoint homePosition = new GeoPoint(0, 0);

		mapView.getController().setCenter(homePosition);

		this.gpsExtractor = new GPSExtractor(propertyTree,
				flightGearGPSContext.getGps());
		this.routeExtractor = new RouteExtractor(propertyTree,
				flightGearGPSContext.getRoute());

		this.updateHandler = updateHandler;

		setOnlineOverlays();
	}

	public void setOnlineOverlays() {
		List<Overlay> overlays = mapView.getOverlays();
		overlays.clear();

		overlays.add(planeOverlay);

		overlays.add(routeOverlay);

		overlays.add(searchResultsOverlay.getItemizedOverlay());
	}

	public void update() {
		gpsExtractor.extractData();
		//routeExtractor.extractData();

		if (autocenter) {
			centerPlane();
		}
		
		searchResultsOverlay.updateResults(flightGearGPSContext.getQueryResults());
		
		updateHandler.sendEmptyMessage(0);
	}

	@Override
	public void run() {
		Log.d("MapUpdater", "Updating map data...");
		this.update();
	}

	public void centerPlane() {
		mapView.getController().setCenter(planeOverlay.getGeoPointPosition());
	}

	public boolean isAutocenter() {
		return autocenter;
	}

	public void setAutocenter(boolean autocenter) {
		this.autocenter = autocenter;
	}

}
