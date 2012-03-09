package org.flightgear.fggps.view.overlay;

import org.flightgear.fggps.R;
import org.flightgear.fggps.gps.GPS;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;

public class PlaneOverlay extends Overlay {

	private Bitmap planeIcon;
	
	private Bitmap rotatedPlaneIcon;

	private Resources resources;
	
	private GPS gps;

	private float lastHeading;
	
	public PlaneOverlay(Resources resources, GPS gps) {
		this.resources = resources;
		this.planeIcon = BitmapFactory.decodeResource(resources, R.drawable.plane_icon);
		this.gps = gps;
		this.rotatedPlaneIcon = rotateDrawable(gps.getHeading());
	}
	
	private Bitmap rotateDrawable(float angle) {
		Bitmap canvasBitmap = planeIcon.copy(Bitmap.Config.ARGB_8888, true);
		canvasBitmap.eraseColor(0x00000000);
		
		Canvas canvas = new Canvas(canvasBitmap);
		
		Matrix rotateMatrix = new Matrix();
		rotateMatrix.setRotate(angle, canvas.getWidth() / 2,
				canvas.getHeight() / 2);
		
		canvas.drawBitmap(planeIcon, rotateMatrix, null);
		
		return new BitmapDrawable(resources, canvasBitmap).getBitmap();
	}
	
	private void updateHeading(float heading) {
		if (heading != this.lastHeading) {
			this.lastHeading = heading;
			this.rotatedPlaneIcon = rotateDrawable(heading);
		}
	}
	
	@Override
	public boolean draw(Canvas canvas, MapView mapView, boolean shadow,
			long when) {
		
		updateHeading(gps.getHeading());
		
		Projection projection = mapView.getProjection();
		
		Point point = new Point();
				
		projection.toPixels(gps.getGeoPointPosition(), point);
		
		canvas.drawBitmap(rotatedPlaneIcon, 
				point.x - rotatedPlaneIcon.getWidth() / 2,
				point.y - rotatedPlaneIcon.getHeight() / 2,
				null);
		
		return super.draw(canvas, mapView, shadow, when);
	}
	
	public GeoPoint getGeoPointPosition() {
		return this.gps.getGeoPointPosition();
	}
}