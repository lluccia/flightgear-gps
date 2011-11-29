package org.flightgear.fggps.overlays;

import org.flightgear.fggps.R;

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
	
	private GeoPoint position;
	
	private float heading;
	
	public PlaneOverlay(Resources resources) {
		this(resources, new GeoPoint((int) (20*1E6), (int) (20*1E6)), 10);
	}
	
	public PlaneOverlay(Resources resources, GeoPoint position, float heading) {
		this.resources = resources;
		this.position = position;
		this.planeIcon = BitmapFactory.decodeResource(resources, R.drawable.plane_icon);
		this.heading = heading;
		this.rotatedPlaneIcon = rotateDrawable(heading);
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
	
	public void updatePosition(GeoPoint position) {
		this.position = position;
	}
	
	public void updateHeading(float heading) {
		if (heading != this.heading) {
			this.heading = heading;
			this.rotatedPlaneIcon = rotateDrawable(heading);
		}
	}
	
	@Override
	public boolean draw(Canvas canvas, MapView mapView, boolean shadow,
			long when) {
		
		Projection projection = mapView.getProjection();
		
		Point point = new Point();
				
		projection.toPixels(position, point);
		
		canvas.drawBitmap(rotatedPlaneIcon, 
				point.x - rotatedPlaneIcon.getWidth() / 2,
				point.y - rotatedPlaneIcon.getHeight() / 2,
				null);
		
		return super.draw(canvas, mapView, shadow, when);
	}
}