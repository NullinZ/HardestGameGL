package com.nullin.hardestgame.demo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class multiBitmapView extends SurfaceView implements SurfaceHolder.Callback, Runnable {
	SurfaceHolder mHolder;
	Bitmap circle;
	Bitmap multiCircle;

	public multiBitmapView(Context context) {
		super(context);
		mHolder = getHolder();
		mHolder.addCallback(this);
		initCircle();
	}

	Paint p = new Paint();

	public void initCircle() {
		p.setAntiAlias(true);
		p.setColor(Color.WHITE);
		circle = Bitmap.createBitmap(6, 6, Config.ARGB_8888);
		multiCircle = Bitmap.createBitmap(480, 320, Config.ARGB_8888);
		Canvas c = new Canvas(circle);
		c.drawCircle(3, 3, 3, p);
		c = new Canvas(multiCircle);
		for (int i = 0; i < 1600; i++) {
			c.drawBitmap(circle, i * 15 % 800, i * 15 / 40, p);
		}

	}

	public void draw(Canvas canvas) {
		for (int i = 0; i < 1600; i++) {
			canvas.drawBitmap(circle, i * 15 % 800, i * 15 / 40, p);
		}
	}

	public void draw2(Canvas c) {
		c.drawBitmap(multiCircle, 0, 0, p);
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

	}

	public void surfaceCreated(SurfaceHolder holder) {
		new Thread(this).start();
	}

	public void surfaceDestroyed(SurfaceHolder holder) {

	}

	public void run() {
		Canvas canvas;
		long a = System.currentTimeMillis();
		int i = 0;
		while (true) {
			if (i > 1000)
				break;
			try {
				Thread.sleep(30);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			canvas = null;
			try {
				canvas = mHolder.lockCanvas();
				synchronized (this.mHolder) {
					draw(canvas);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (canvas != null)
					mHolder.unlockCanvasAndPost(canvas);
			}
			i++;
		}
		System.out.println(System.currentTimeMillis() - a);
		a = System.currentTimeMillis();
		i = 0;
		while (true) {
			if (i > 1000)
				break;
			try {
				Thread.sleep(30);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			canvas = null;
			try {
				canvas = mHolder.lockCanvas();
				synchronized (this.mHolder) {
					draw2(canvas);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (canvas != null)
					mHolder.unlockCanvasAndPost(canvas);
			}
			i++;
		}
		System.out.println(System.currentTimeMillis() - a);

	}
}
