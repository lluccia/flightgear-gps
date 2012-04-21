package org.flightgear.fggps.view.overlay;

import org.flightgear.fggps.gps.Route;
import org.flightgear.fggps.gps.Waypoint;
import org.mapsforge.android.maps.Projection;
import org.mapsforge.android.maps.overlay.Overlay;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;

public class RouteOverlay extends Overlay {

	private Route route;

	private static final int markerRadius = 6;

	public RouteOverlay(Route route) {
		super();
		this.route = route;
	}

	@Override
	protected void drawOverlayBitmap(Canvas canvas, Point drawPosition,
			Projection projection, byte drawZoomLevel) {
		
		if (route.getStartWaypoint() != null) {
			Paint paint = new Paint();
			paint.setAntiAlias(true);
			paint.setColor(Color.MAGENTA);
			paint.setStrokeWidth(5);
			paint.setAlpha(120);

			Point lastPoint = new Point();

			projection.toPixels(route.getStartWaypoint().getGeoPoint(),
					lastPoint);

			RectF marker = new RectF(lastPoint.x - markerRadius, lastPoint.y
					- markerRadius, lastPoint.x + markerRadius, lastPoint.y
					+ markerRadius);
			canvas.drawOval(marker, paint);

			for (Waypoint waypoint : route.getWaypoints()) {
				Point currentPoint = new Point();
				projection.toPixels(waypoint.getGeoPoint(), currentPoint);

				canvas.drawLine(lastPoint.x, lastPoint.y, currentPoint.x,
						currentPoint.y, paint);

				marker = new RectF(currentPoint.x - markerRadius,
						currentPoint.y - markerRadius, currentPoint.x
								+ markerRadius, currentPoint.y + markerRadius);
				canvas.drawOval(marker, paint);

				lastPoint = currentPoint;
			}
		}
	}

}
