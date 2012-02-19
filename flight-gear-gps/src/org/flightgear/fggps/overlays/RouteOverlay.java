package org.flightgear.fggps.overlays;

import java.util.ArrayList;
import java.util.List;

import org.flightgear.fggps.gps.Waypoint;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;

import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;

public class RouteOverlay extends Overlay {

	Waypoint startPoint;
	
	List<Waypoint> waypoints = new ArrayList<Waypoint>();

	private static final int markerRadius = 6;
	
	@Override
	public boolean draw(Canvas canvas, MapView mapView, boolean shadow,
			long when) {

		Projection projection = mapView.getProjection();

		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setColor(Color.MAGENTA);
		paint.setStrokeWidth(5); 
		paint.setAlpha(120); 
		
		Point lastPoint = new Point();
		
		projection.toPixels(startPoint.getGeoPoint(), lastPoint);
		
		RectF marker = new RectF(lastPoint.x - markerRadius, lastPoint.y - markerRadius,
								lastPoint.x + markerRadius, lastPoint.y + markerRadius);
		canvas.drawOval(marker, paint);		
		
		for (Waypoint waypoint : waypoints) {
			Point currentPoint = new Point();
			projection.toPixels(waypoint.getGeoPoint(), currentPoint); 
			
			canvas.drawLine(lastPoint.x, lastPoint.y, currentPoint.x,currentPoint.y, paint);

			marker = new RectF(currentPoint.x - markerRadius, currentPoint.y - markerRadius,
								currentPoint.x + markerRadius, currentPoint.y + markerRadius);
			canvas.drawOval(marker, paint);
			
			lastPoint = currentPoint;
		}
		
		return super.draw(canvas, mapView, shadow, when);
	}

	/**
	 * Method that returns a dummy route for testing
	 * @return
	 *//*
	public static RouteOverlay getDummyRoute() {
		RouteOverlay routeOverlay = new RouteOverlay();
		
		routeOverlay.setStartPoint(new GeoPoint((int) (-23.5 * 1E6), (int) (-46.5 * 1E6)));
		
		routeOverlay.getWaypoints().add(new GeoPoint((int) (-24.5 * 1E6), (int) (-47.5 * 1E6)));
		routeOverlay.getWaypoints().add(new GeoPoint((int) (-25.5 * 1E6), (int) (-50.5 * 1E6)));
		routeOverlay.getWaypoints().add(new GeoPoint((int) (-27.5 * 1E6), (int) (-52.5 * 1E6)));
		
		return routeOverlay;
	}*/

	public Waypoint getStartPoint() {
		return startPoint;
	}

	public void setStartPoint(Waypoint startPoint) {
		this.startPoint = startPoint;
	}

	public List<Waypoint> getWaypoints() {
		return waypoints;
	}

	public void setWaypoints(List<Waypoint> waypoints) {
		this.waypoints = waypoints;
	}

}
