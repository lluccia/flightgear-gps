package org.flightgear.fggps.updaters;

import java.util.List;

import org.flightgear.fggps.PlaneItemizedOverlay;
import org.flightgear.fggps.PlaneOverlay;
import org.flightgear.fggps.domain.GPS;

import android.graphics.Bitmap;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;


/**
 * This class is responsible for updating the map with the current GPS position
 * @author leandro
 *
 */
public class MapUpdater {
	
	private MapView mapView;
	
	private PlaneOverlay planeOverlay;
	
	private GPS gps;
	
	public MapUpdater(MapView mapView, GPS gps, Bitmap planeIcon) {
		super();
		this.mapView = mapView; 
		this.gps = gps;
		this.planeOverlay = new PlaneOverlay(planeIcon, 0);
	}

	public void update() {
		GeoPoint position = 
				new GeoPoint(gps.getLatitude1E6(), gps.getLongitude1E6());
		
		planeOverlay.setHeading(gps.getHeading());
		
		PlaneItemizedOverlay planeItemizedOverlay = 
				new PlaneItemizedOverlay(
						planeOverlay.getRotatedDrawable(), 
						mapView.getMapCenter());
		
		List<Overlay> overlays = mapView.getOverlays();
		overlays.clear();
		overlays.add(planeItemizedOverlay);
		
		mapView.getController().animateTo(position);
	}
}