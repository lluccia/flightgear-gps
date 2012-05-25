package org.flightgear.fggps.view.overlay;

import java.util.List;

import org.flightgear.fggps.R;
import org.flightgear.fggps.gps.Waypoint;
import org.mapsforge.android.maps.overlay.ArrayItemizedOverlay;
import org.mapsforge.android.maps.overlay.OverlayItem;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;

public class SearchResultsOverlay {
	
	Drawable airportMarker;

    ArrayItemizedOverlay itemizedOverlay;

	List<Waypoint> searchResults;
    
	public SearchResultsOverlay(Resources resources) {
		airportMarker = resources.getDrawable(R.drawable.airport_placemark);
		itemizedOverlay = new ArrayItemizedOverlay(airportMarker);
	}
	
	public void updateResults(List<Waypoint> searchResults) {
		itemizedOverlay.clear();
		for (Waypoint waypoint: searchResults) {
			 OverlayItem item = new OverlayItem(waypoint.getGeoPoint(),
					 waypoint.getIdent(), waypoint.getName());
			itemizedOverlay.addItem(item);
		}
	}
	
	public ArrayItemizedOverlay getItemizedOverlay() {
		return itemizedOverlay;
	}

}
