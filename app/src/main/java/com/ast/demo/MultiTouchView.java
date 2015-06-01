package com.ast.demo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import java.util.HashMap;

class MultiTouchView extends View implements View.OnTouchListener {

	private mGLSurfaceView mglv;
	private GestureDetector gDetector;
	private ScaleGestureDetector scaleDetector;
	
	public MultiTouchView(Context context, mGLSurfaceView s) {
		super(context);
		scaleDetector = new ScaleGestureDetector(context, new ScaleListener());
		gDetector = new GestureDetector(context, new GestureListener());
		mglv = s;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event)
	{
		scaleDetector.onTouchEvent(event);
		gDetector.onTouchEvent(event);

		int action = event.getActionMasked();
		switch (action) {
			case MotionEvent.ACTION_DOWN:
				this.getParent().requestDisallowInterceptTouchEvent(true);
			case MotionEvent.ACTION_POINTER_DOWN:
				captureDown(event);
				break;
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_POINTER_UP:
				captureUp(event);
				break;
			case MotionEvent.ACTION_MOVE:
				capturePointerMoves(event);
				break;
		}
		invalidate();
		return true;
	}

	private HashMap<Integer, PointF> touchPoints = new HashMap<Integer, PointF>();
	private void captureDown(MotionEvent event) {
		int index = event.getActionIndex();
		int id = event.getPointerId(index);
		touchPoints.put(id, new PointF(event.getX(index), event.getY(index)));
	}
	private void captureUp(MotionEvent event) {
		int index = event.getActionIndex();
		int id = event.getPointerId(index);
		touchPoints.remove(id);
	}
	private void capturePointerMoves(MotionEvent event) {
		int length = event.getPointerCount();
		int id;
		for (int i = 0; i < length; i++) {
			id = event.getPointerId(i);
			try {
				touchPoints.get(id).set(event.getX(i), event.getY(i));
			} catch (IndexOutOfBoundsException e) {
			}
		}
	}

	private Rect bounds = new Rect();
	private Paint paint = new Paint();
	@Override
	protected void onDraw(Canvas canvas) {

		if (touchPoints.size() > 0) {
			int radius = 48; // Size of finger indicator
			for (Integer key : touchPoints.keySet()) {
				PointF point = touchPoints.get(key);
				int x = (int) point.x;
				int y = (int) point.y;

				bounds.set(x - radius, y - radius, x + radius, y + radius);
				paint.setStyle(Paint.Style.FILL);
				paint.setColor(Color.WHITE);
				canvas.drawCircle(x, y, radius, paint);

				paint.setColor(Color.BLACK);
				paint.setTextSize(48);
				canvas.drawText(String.valueOf(key), x - 12, y + 12, paint);
			}
		}
		super.onDraw(canvas);
	}

	private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener
	{
		@Override
		public boolean onScale(ScaleGestureDetector detector) {
			mglv.onTouchEv(detector.getScaleFactor());
			return true;
		}
	}

	private class GestureListener extends GestureDetector.SimpleOnGestureListener
	{
		@Override
		public void onLongPress(MotionEvent ev) {
			float h = mglv.getHeight();
			// invert y
			mglv.onGestEv(ev.getX(),h-ev.getY());
		}
		@Override
		public boolean onScroll(MotionEvent ev0, MotionEvent ev, float dx, float dy) {
			// invert x
			mglv.onTouchEv(-dx,dy);
			return true;
		}
	}
}
