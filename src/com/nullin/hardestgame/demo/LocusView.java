//package com.nullin.hardestgame.demo;
//
//import java.util.Random;
//
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.graphics.Bitmap.Config;
//import android.graphics.Canvas;
//import android.graphics.Color;
//import android.graphics.Matrix;
//import android.graphics.Paint;
//import android.graphics.Paint.Align;
//import android.graphics.Rect;
//import android.graphics.drawable.BitmapDrawable;
//import android.view.KeyEvent;
//import android.view.MotionEvent;
//import android.view.SurfaceHolder;
//
//import com.nullin.hardestgame.C;
//import com.nullin.hardestgame.R;
//import com.nullin.hardestgame.engine.ControlPad;
//import com.nullin.hardestgame.engine.KeyThread;
//import com.nullin.hardestgame.map.LineExLocus;
//import com.nullin.hardestgame.map.Map;
//import com.nullin.hardestgame.map.RectLocus;
//import com.nullin.hardestgame.map.RingLocus;
//import com.nullin.hardestgame.sprit.Ball;
//import com.nullin.hardestgame.sprit.Hero;
//import com.nullin.hardestgame.view.GameView;
//
//public class LocusView extends GameView implements SurfaceHolder.Callback, Runnable {
//	SurfaceHolder mHolder;
//	Ball[] balls;
//	Bitmap deadPoint;
//	Bitmap windMill;
//	final int height = C.getHeight(), width = C.getWidth();
//	Random r = new Random();
//	final public static float RADIUS = 7.8f;
//	Paint paint;
//	ControlPad mPad;
//	Hero mHero;
//	Map map;
//	private int death = 0;
//	private int overFPS, allFPS;
//	private int level = 4;
//
//	public LocusView(Context context) {
//		super(context);
//		deadPoint = ((BitmapDrawable) getResources().getDrawable(R.drawable.c)).getBitmap();
//		initPaint();
//		mHolder = getHolder();
//		mHolder.addCallback(this);
//		map = new Map();
//		map.toGate(5);
//		mHero = new Hero(240, 120, 10.8f);
//		mPad = new ControlPad(this, 80, height - 80);
//		initBalls2_4_7();
//		initBalls8_9();
//	}
//
//	public void initPaint() {
//		paint = new Paint();
//		paint.setColor(Color.WHITE);
//		paint.setAntiAlias(true);
//		paint.setTextAlign(Align.CENTER);
//	}
//
//	public void initBalls2_4_7() {
//		Ball[] balls = new Ball[56];
//		float x = 21 * RADIUS;
//		float y = 21 * RADIUS;
//		for (int i = 0; i < balls.length; i++) {
//			float r = (i % 7 + 1) * 3 * RADIUS + RADIUS;
//			float d = (i % 4) * C.PI / 2;
//			if (i < balls.length >> 1) {
//				d = (float) (d + Math.atan((RADIUS + 3) / r));
//			} else {
//				d = (float) (d - Math.atan((RADIUS + 3) / r));
//			}
//			r = (int) Math.sqrt((2 * 2 * RADIUS * RADIUS) + r * r);
//			balls[i] = new Ball(0, 0, RADIUS, new RingLocus(x, y, r, d, 2000));
//		}
//		for (int i = 0; i < balls.length; i++) {
//			balls[i].updatePosition();
//		}
//		windMill = Bitmap.createBitmap((int) x * 2, (int) x * 2, Config.ARGB_8888);
//		Canvas canvas = new Canvas(windMill);
//		for (int i = 0; i < balls.length; i++) {
//			balls[i].draw(canvas);
//		}
//		canvas = null;
//
//	}
//
//	public void initBalls8_9() {
//		balls = new Ball[74];
//		for (int i = 0; i < balls.length; i++) {
//			float x = (i / 9) % 4 * ((width - C.GV_MARGIN_LEFT - 6 * 30) / 4) + (3 * 2 * RADIUS) + 2 * RADIUS
//					+ C.GV_MARGIN_LEFT + 4 * 30;
//			float y = (i / 9) / 4 * (5 * 2 * RADIUS + (4 + 1) * RADIUS + 2 * 30) + (3 * 2 * RADIUS) + 2 * RADIUS
//					+ C.GV_MARGIN_TOP + 60;
//
//			if (i % 9 == 0) {
//				balls[i] = new Ball(x, y, RADIUS, null);
//			} else {
//				int r = (int) ((i % 9 - 1 + 4) / 4 * (2 * RADIUS + 9.6));// +
//				// RADIUS;
//				float d = ((i % 9 - 1 + 4) % 4) * C.PI / 2;
//				balls[i] = new Ball(0, 0, RADIUS, new RingLocus(x, y, r, d, 2800));
//			}
//		}
//		int x = 6 * 30 + C.GV_MARGIN_LEFT + 15;
//		int y = 5 * 30 + C.GV_MARGIN_TOP + 15;
//		balls[72] = new Ball(x, y, RADIUS, new RectLocus(x, y, new Rect(x, y, x + 300 - 15, y + 60 - 15), 4000));
//		balls[73] = new Ball(x, y, RADIUS, new LineExLocus(100, 100,
//				new Rect(x, y, x + 330 - 2 * 15, y + 180 - 2 * 15), 2000, 1000));
//
//	}
//
//	Matrix m = new Matrix();
//
//	public void draw(Canvas canvas) {
//
//		canvas.drawColor(0xffCCCCCC);
//		map.drawMap(canvas);
//		canvas.drawBitmap(windMill, m, null);
//		for (int i = 0; i < balls.length; i++) {
//			balls[i].draw(canvas);
//		}
//		canvas.drawText("DEATH: " + death, width - 100, 20, paint);
//		mHero.draw(canvas);
//		mPad.draw(canvas);
//	}
//
//	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
//
//	}
//
//	public void surfaceCreated(SurfaceHolder holder) {
//		new Thread(this).start();
//		new ProcessData().start();
//		new KeyThread(this).start();
//	}
//
//	public void surfaceDestroyed(SurfaceHolder holder) {
//		proB = false;
//	}
//
//	public void run() {
//		Canvas canvas;
//		long dt = 0;
//
//		while (proB) {
//			allFPS++;
//			if (dt < 33) {
//				dt = 33 - dt;
//			} else {
//				overFPS++;
//				System.out.println("OVER:" + dt + " \tRATE:" + overFPS + "/" + allFPS);
//				dt = 0;
//			}
//			try {
//				Thread.sleep(dt);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//			canvas = null;
//			dt = System.currentTimeMillis();
//			try {
//				canvas = mHolder.lockCanvas();
//				synchronized (this.mHolder) {
//					draw(canvas);
//				}
//			} catch (Exception e) {
//				// e.printStackTrace();
//			} finally {
//				if (canvas != null)
//					mHolder.unlockCanvasAndPost(canvas);
//			}
//			dt = System.currentTimeMillis() - dt;
//		}
//	}
//
//	boolean proB = true;
//
//	class ProcessData extends Thread {
//
//		@Override
//		public void run() {
//			m.setRotate(0, width >> 1, height >> 1);
//			float d = 0;
//			m.setTranslate((map.getWidth() - windMill.getWidth() >> 1) + C.GV_MARGIN_LEFT, (map.getHeight()
//					- windMill.getHeight() >> 1)
//					+ C.GV_MARGIN_TOP);
//			while (proB) {
//				try {
//					Thread.sleep(C.SPF);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//				d += 5;
//				m.postRotate(5, (map.getWidth() >> 1) + C.GV_MARGIN_LEFT, (map.getHeight() >> 1) + C.GV_MARGIN_TOP);
//
//				for (int i = 0; i < balls.length; i++) {
//					balls[i].updatePosition();
//					if (balls[i].isHit(mHero.getBorder())) {
//						mHero.die();
//					}
//				}
//			}
//		}
//	}
//
//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		if (KeyEvent.KEYCODE_DPAD_UP == keyCode) {
//			if (level > 0)
//				level--;
//		}
//		if (KeyEvent.KEYCODE_DPAD_DOWN == keyCode) {
//			level++;
//		}
//		map.toGate(level % 30);
//		return false;
//	}
//
//	@Override
//	public void doKeyDown() {
//		mHero.move(map, direction);
//	}
//
//	public boolean onTouchEvent(android.view.MotionEvent event) {
//		float x = event.getX();
//		float y = event.getY();
//		// proB = false;
//		switch (event.getAction()) {
//		case MotionEvent.ACTION_DOWN:
//			if (x < 0 || x > 200 || y > height || y < height - 200)
//				return false;
//			break;
//		case MotionEvent.ACTION_MOVE:
//
//			break;
//		case MotionEvent.ACTION_CANCEL:
//		case MotionEvent.ACTION_UP:
//			direction = C.DIRECTION_NULL;
//			break;
//
//		default:
//			break;
//		}
//		mPad.onTouchEvent(event);
//		return true;
//
//	}
//}
