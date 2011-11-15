package dev.conca.fggps;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class PlaneOverlay {

	private Bitmap planeIcon;
	
	private float heading;
	
	public PlaneOverlay(Bitmap planeIcon, float heading) {
		this.planeIcon = planeIcon;
		this.heading = heading;
	}

	public Drawable getDrawable() {
		return new BitmapDrawable(planeIcon);
	}
	
	public Drawable getRotatedDrawable() {
		return rotateDrawable(heading);
	}

	private BitmapDrawable rotateDrawable(float angle) {
		Bitmap arrowBitmap = planeIcon;
		
		Bitmap canvasBitmap = arrowBitmap.copy(Bitmap.Config.ARGB_8888, true);
		canvasBitmap.eraseColor(0x00000000);
		
		Canvas canvas = new Canvas(canvasBitmap);
		
		Matrix rotateMatrix = new Matrix();
		rotateMatrix.setRotate(angle, canvas.getWidth() / 2,
				canvas.getHeight() / 2);
		
		canvas.drawBitmap(arrowBitmap, rotateMatrix, null);
		
		return new BitmapDrawable(canvasBitmap);
	}
	
	public float getHeading() {
		return this.heading;
	}
	
	public void setHeading(float heading) {
		this.heading = heading;
	}

}