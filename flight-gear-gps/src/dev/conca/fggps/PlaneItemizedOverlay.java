package dev.conca.fggps;

import android.graphics.drawable.Drawable;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

public class PlaneItemizedOverlay extends ItemizedOverlay<OverlayItem> {

	private OverlayItem overlayItem;
	
	public PlaneItemizedOverlay(Drawable defaultMarker) {
		super(boundCenter(defaultMarker));
	}
	
	public PlaneItemizedOverlay(Drawable defaultMarker, GeoPoint position) {
		this(defaultMarker);
		
		overlayItem = new OverlayItem(position, "", "");
		
		this.populate();
	}

	@Override
	protected OverlayItem createItem(int i) {
		return overlayItem;
	}

	@Override
	public int size() {
		return 1;
	}
	
	public OverlayItem getOverlayItem() {
		return this.overlayItem;
	}
	
}
