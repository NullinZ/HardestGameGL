package com.nullin.hardestgame.demo;

import java.util.Random;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class BallsView extends SurfaceView implements SurfaceHolder.Callback, Runnable {
	SurfaceHolder mHolder;
	Ball[] balls = new Ball[1000];
	int height = 480, width = 800, velocity = 30;
	Random r = new Random();
	final public static int RADIUS = 2;
	final public static String[] alphabet = { "a", "b", "c", "d", "e", "f", "g", "h", "i", "g", "k", "l", "m", "n",
			"o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z" };

	public BallsView(Context context) {
		super(context);
		mHolder = getHolder();
		mHolder.addCallback(this);
		for (int i = 0; i < balls.length; i++) {
			balls[i] = new Ball(i % (width / (RADIUS * 1.5f)) * (RADIUS * 1.5f) + RADIUS, r.nextInt(height), RADIUS);
		}
	}

	public void draw(Canvas canvas) {
		// canvas.drawBitmap(bitmap, 0, 0, null);
		canvas.drawColor(Color.WHITE);
		for (int i = 0; i < balls.length; i++) {
			balls[i].draw(canvas);
		}
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

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
			// for (int i = 0; i < balls.length; i++) {
			// if (balls[i].y >= height-RADIUS) {
			// balls[i].direction = -r.nextInt(velocity);
			// } else if (balls[i].y <= 0+RADIUS) {
			// balls[i].direction = r.nextInt(velocity);
			// }
			// balls[i].move(-1, balls[i].y + balls[i].direction);
			// }
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
				if (!down) {
					for (int i = 0; i < balls.length; i++) {

						if (balls[i].y >= height - RADIUS) {
							balls[i].direction = -r.nextInt(velocity);
						} else if (balls[i].y <= 0 + RADIUS) {
							balls[i].direction = r.nextInt(velocity);
						}
						balls[i].move(-1, balls[i].y + balls[i].direction);
					}
					Log.i("zhaosheng", "hello");
				}
			}
		}
	}

	float[][] cache = new float[balls.length][2];
	boolean up = true, down = false;
	float downX;
	float downY;
	Boom b;

	public boolean onTouchEvent(android.view.MotionEvent event) {
		downX = event.getX();
		downY = event.getY();
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			for (int i = 0; i < balls.length; i++) {
				cache[i][0] = balls[i].x;
				cache[i][1] = balls[i].y;
			}
			down = true;
			up = false;
			b = new Boom();
			b.start();
			break;
		case MotionEvent.ACTION_MOVE:

			break;
		case MotionEvent.ACTION_UP:
			up = true;
			break;

		default:
			down = false;
			break;
		}
		return true;

	}

	private class Boom extends Thread {

		Boom() {
		}

		int boom = 0;

		@Override
		public void run() {
			boom = 0;
			while (true) {
				try {
					Thread.sleep(33);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if (up) {
					boom++;
				}
				for (int i = 0; i < balls.length; i++) {
					if (up) {
						balls[i].move(balls[i].x + balls[i].direction % 10 * Math.abs(cache[i][0] - balls[i].x)
								/ (cache[i][0] - balls[i].x), balls[i].y + balls[i].direction % 10
								* Math.abs(cache[i][1] - balls[i].y) / (cache[i][1] - balls[i].y));
					} else {
						balls[i].move(balls[i].x - balls[i].direction % 20 * Math.abs(balls[i].x - downX)
								/ (balls[i].x - downX), balls[i].y - balls[i].direction % 10
								* Math.abs(balls[i].y - downY) / (balls[i].y - downY));
					}
				}
				if (boom >= 30) {
					down = false;
					break;

				}
			}
			for (int i = 0; i < balls.length; i++) {
				balls[i].move(cache[i][0], cache[i][1]);
			}
		}
	}

	Paint paint;

	class Ball {
		float x, y, r;
		float topX, topY;
		int direction = velocity;

		public Ball(float x, float y, float r) {
			super();
			this.x = x;
			this.y = y;
			this.r = r;
			paint = new Paint();
			paint.setColor(0xff000000);
			paint.setAntiAlias(true);
			paint.setTextSize(RADIUS << 1);
			paint.setTextAlign(Align.CENTER);
			// paint.setStyle(Style.STROKE);
		}

		public void move(float x, float y) {
			if (x != -1)
				this.x = x;
			// if (y != -1)
			this.y = y;
		}

		public void draw(Canvas canvas) {
			canvas.drawCircle(x, y, r, paint);
			// canvas.drawRect(x - r, y - r, x + r, y + r, paint);
			// canvas.drawText(alphabet[Math.abs(direction % alphabet.length)],
			// x, y,
			// paint);

		}
	}
}
