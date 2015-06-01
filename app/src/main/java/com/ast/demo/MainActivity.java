package com.ast.demo;

import android.app.Activity;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends Activity
{
	private mGLSurfaceView mGLView;
	public TextView tv;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		tv = (TextView) findViewById(R.id.hello);

		mGLView = new mGLSurfaceView(this);
		MultiTouchView multiTouchView = new MultiTouchView(this, mGLView);

		RelativeLayout rl = (RelativeLayout) findViewById(R.id.rl);
		rl.addView(mGLView);
		rl.addView(multiTouchView);
		rl.setOnTouchListener(multiTouchView);
	}

	@Override
	protected void onPause() {
		super.onPause();
		mGLView.onPause();
	}
	@Override
	protected void onResume() {
		super.onResume();
		mGLView.onResume();
	}
}
