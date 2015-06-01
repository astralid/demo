package com.ast.demo;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.os.Handler;
import android.os.Message;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

class OpenGLRenderer implements GLSurfaceView.Renderer
{
	private Cube mCube = new Cube();
	private Context context;
	public int sW = 0;
	public int sH = 0;
	private Camera cam = new Camera();
	private Handler handler;

	public OpenGLRenderer (Context c, Handler h)
	{
		context = c;
		handler = h;
	}

	public void onzoom(float d) {
		cam.zoom(10f*(d-1f));
	}

	public void onlongpress(float x, float y) {
		float unitY = 2f*y/sH - 1f;
		float unitX = 2f*x/sW - 1f;
		printCoords(cam.getNearFarRay(unitX,unitY));

		double[] angs = new double[2];
		double yrm = mUtil.deg2rad*cam.fovy/2;
		double xrm = yrm*sW/sH;
		angs[0] = Math.atan(unitX) * xrm;
		angs[1] = Math.atan(unitY) * yrm;
		float thX = (float)angs[0];
		float thY = (float)angs[1];
		cam.look(thX, thY);
	}

	public void onscroll(float dX, float dY)
	{
		// unitize (from -1 to +1) ?
		float dx = dX/sW;
		float dy = dY/sH;
		cam.orbit(-dx, -dy);
	}

	private void printCoords (float[] p) {
		Message m = handler.obtainMessage();
		String s = "";
		for (int n=0; n<p.length; n++) {
			s += String.valueOf(p[n]);
			if (n < p.length-1)
				s += ", ";
		}
		m.obj = s;
		handler.sendMessage(m);
	}

	@Override
	public void onSurfaceCreated (GL10 gl, EGLConfig config)
	{
		gl.glClearColor(0.0f,0.0f,0.0f,0.5f);
		gl.glClearDepthf(1.0f);
		gl.glEnable(GL10.GL_DEPTH_TEST);
		gl.glDepthFunc(GL10.GL_LEQUAL);
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
		gl.glPixelStorei(GL10.GL_UNPACK_ALIGNMENT, 1);
		mCube.loadTexture(gl, context); // Load image into Texture
		printCoords(new float[]{});
	}

	@Override
	public void onDrawFrame (GL10 gl)
	{
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		gl.glLoadIdentity();
		cam.applyView(gl);
		dCube(gl);
		gl.glLoadIdentity();
	}
	protected void dCube(GL10 gl) {
		gl.glPushMatrix();
		gl.glTranslatef(0f, 0f, 0f);
		mCube.draw(gl);
		gl.glPopMatrix();
	}

	@Override
	public void onSurfaceChanged (GL10 gl,int width,int height)
	{
		float fovy = 45.0f;
		float zNear = 10.0f;
		float zFar = 500.0f;
		gl.glViewport(0,0,width,height);
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		GLU.gluPerspective(gl, fovy, (float) width / (float) height, zNear, zFar);
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		cam.setProjection(width,height,fovy,zNear,zFar);
		this.sW = width;
		this.sH = height;
	}
}
