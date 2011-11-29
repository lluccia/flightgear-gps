package org.flightgear.fggps.overlays;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;

public class RouteOverlay extends Overlay {

	GeoPoint startPoint;
	
	List<GeoPoint> waypoints = new ArrayList<GeoPoint>();

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
		
		projection.toPixels(startPoint, lastPoint);
		
		RectF marker = new RectF(lastPoint.x - markerRadius, lastPoint.y - markerRadius,
								lastPoint.x + markerRadius, lastPoint.y + markerRadius);
		canvas.drawOval(marker, paint);		
		
		for (GeoPoint waypoint : waypoints) {
			Point currentPoint = new Point();
			projection.toPixels(waypoint, currentPoint); 
			
			canvas.drawLine(lastPoint.x, lastPoint.y, currentPoint.x,currentPoint.y, paint);

			marker = new RectF(currentPoint.x - markerRadius, currentPoint.y - markerRadius,
								currentPoint.x + markerRadius, currentPoint.y + markerRadius);
			canvas.drawOval(marker, paint);
			
			lastPoint = currentPoint;
		}
		
		return super.draw(canvas, mapView, shadow, when);
	}

	public static RouteOverlay getDummyRoute() {
		RouteOverlay routeOverlay = new RouteOverlay();
		
		routeOverlay.setStartPoint(new GeoPoint((int) (-23.5 * 1E6), (int) (-46.5 * 1E6)));
		
		routeOverlay.getWaypoints().add(new GeoPoint((int) (-24.5 * 1E6), (int) (-47.5 * 1E6)));
		routeOverlay.getWaypoints().add(new GeoPoint((int) (-25.5 * 1E6), (int) (-50.5 * 1E6)));
		routeOverlay.getWaypoints().add(new GeoPoint((int) (-27.5 * 1E6), (int) (-52.5 * 1E6)));
		
		return routeOverlay;
	}

	public GeoPoint getStartPoint() {
		return startPoint;
	}

	public void setStartPoint(GeoPoint startPoint) {
		this.startPoint = startPoint;
	}

	public List<GeoPoint> getWaypoints() {
		return waypoints;
	}

	public void setWaypoints(List<GeoPoint> waypoints) {
		this.waypoints = waypoints;
	}

}
