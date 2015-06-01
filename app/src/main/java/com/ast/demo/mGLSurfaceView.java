package com.ast.demo;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

class mGLSurfaceView extends GLSurfaceView
{
	protected OpenGLRenderer mRenderer;

	public void onTouchEv(final float deltaX, final float deltaY) {
		queueEvent(new Runnable() { public void run() {
			mRenderer.onscroll(deltaX, deltaY);
		}});	 
	}
	public void onTouchEv(final float zf) {
		queueEvent(new Runnable() { public void run() {
			mRenderer.onzoom(zf);
		}});	 
	}
	public void onGestEv(final float x, final float y) {
		queueEvent(new Runnable() { public void run() {
			mRenderer.onlongpress(x, y);
		}});	 
	}
	public mGLSurfaceView(Context context)
	{
		super(context);
		Handler handler = new mHandler((MainActivity) context);
		mRenderer = new OpenGLRenderer(context, handler);
		setRenderer(mRenderer);
	}

	static class mHandler extends Handler
	{
		private final WeakReference<MainActivity> ui;
		mHandler(MainActivity ref) {
			this.ui = new WeakReference<>(ref);
		}
		@Override
		public void handleMessage (Message msg) {
			ui.get().tv.setText((String) msg.obj);
		}
	}
}
