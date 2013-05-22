package com.nullin.hardestgame.demo;

import java.util.Random;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class PhysicsView extends SurfaceView implements SurfaceHolder.Callback,
		Runnable {
	SurfaceHolder mHolder;
	Ball[] balls = new Ball[1000];
	Bitmap bitmap;
	int height = 480, width = 800, velocity = 30;
	Random r = new Random();
	final public static int RADIUS = 2;
	final public static float G = 1f;
	Paint paint;
	int time=0;

	public PhysicsView(Context context) {
		super(context);
		// bitmap = ((BitmapDrawable) getResources().getDrawable(R.drawable.bg))
		// .getBitmap();
		initPaint();
		mHolder = getHolder();
		mHolder.addCallback(this);
		for (int i = 0; i < balls.length; i++) {
			balls[i] = new Ball(i % (width / (RADIUS * 1.5f)) * (RADIUS * 1.5f)
					+ RADIUS, r.nextInt(height), RADIUS);
		}
	}

	public void initPaint() {
		paint = new Paint();
		paint.setColor(0xff000000);
		paint.setAntiAlias(true);
		paint.setTextSize(RADIUS << 1);
		paint.setTextAlign(Align.CENTER);
	}

	public void draw(Canvas canvas) {
		// canvas.drawBitmap(bitmap, 0, 0, null);
		canvas.drawColor(Color.WHITE);
		for (int i = 0; i < balls.length; i++) {
			balls[i].draw(canvas);
		}
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	public void surfaceCreated(SurfaceHolder holder) {
		new Thread(this).start();
		new ProcessData().start();
	}

	public void surfaceDestroyed(SurfaceHolder holder) {

	}

	public void run() {
		Canvas canvas;

		while (true) {
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
		}
	}

	class ProcessData extends Thread {
		@Override
		public void run() {
			while (true) {
				try {
					Thread.sleep(33);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				int sx=0,sy=0;
				for (int i = 0; i < balls.length; i++) {
					sx=(int) distance(0, acceleration((int) gravity(100, balls[i].weight, (int) (-balls[i].x+downX)), balls[i].weight), time);
					sy=(int) distance(0, acceleration((int) gravity(100, balls[i].weight, (int) (-balls[i].y+downY)), balls[i].weight), time);
					balls[i].move(sx, sy);
				}
				time++;
			}
		}
	}

	float[][] cache = new float[balls.length][2];
	float downX;
	float downY;

	public boolean onTouchEvent(android.view.MotionEvent event) {
		downX = event.getX();
		downY = event.getY();
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:

			break;
		case MotionEvent.ACTION_MOVE:

			break;
		case MotionEvent.ACTION_UP:
			break;

		default:
			break;
		}
		return true;

	}

	class Ball {
		float x, y, r;
		int weight = 1;
		int direction = velocity;

		public Ball(float x, float y, float r) {
			super();
			this.x = x;
			this.y = y;
			this.r = r;

			// paint.setStyle(Style.STROKE);
		}

		public void moveTo(float x, float y) {
			if (x != 0)
				this.x = x;
			if (y != 0)
				this.y = y;
		}

		public void move(float x, float y) {
			if (x != 0)
				this.x += x;
			if (y != 0)
				this.y += y;
		}

		public void draw(Canvas canvas) {
			canvas.drawCircle(x, y, r, paint);
			// canvas.drawRect(x - r, y - r, x + r, y + r, paint);
			// canvas.drawText(alphabet[Math.abs(direction % alphabet.length)],
			// x, y,
			// paint);

		}
	}

	public static float gravity(int m1, int m2, int r) {
		return m1 * m2 * G / (r * r);// G＝6.67×10-11N·m2/kg2
	}

	public static float distance(float velocity0, float acceleration, long time) {
		return velocity0 * time + acceleration * acceleration * time / 2;
	}

	public static float distance(float velocity0, long time) {
		return velocity0 * time;
	}

	public static float velocityT(float velocity0, float acceleration, long time) {
		return velocity0 + acceleration * time;
	}

	public static float acceleration(int force, int weight) {
		return force / weight;
	}
}
