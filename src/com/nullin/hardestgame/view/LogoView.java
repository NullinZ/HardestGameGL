package com.nullin.hardestgame.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Message;
import android.view.View;

import com.nullin.hardestgame.C;
import com.nullin.hardestgame.HGActivity;
import com.nullin.hardestgame.R;
import com.nullin.hardestgame.engine.ResourceManager;

public class LogoView extends View implements Runnable {

	private Matrix tMatrix;
	private Matrix sMatrix;
	private Matrix mMatrix;
	private Bitmap bitmapBall;
	private Bitmap bitmapSpring;
	private Bitmap bitmapRight;
	private HGActivity mHG;
	final private float[] skew = new float[] { -0.2f, -0.15f, -0.1f, -0.5f, 0.1f, 0.2f, 0.15f, 0.1f };
	final private float[] ballY = new float[] { 20f, 15f, 10f, 5f, -5f, -10f, -15f, -20f };
	final private float[] scale = new float[] { 0.9f, 0.9f, 0.9f, 0.9f, 1.12f, 1.12f, 1.12f, 1.12f };
	final private float[] scale2 = new float[] { 1.02f, 1.02f, 1.02f, 1.02f, 0.98f, 0.98f, 0.98f, 0.98f };

	public LogoView(Context context) {
		super(context);
		setKeepScreenOn(true);
		bitmapBall = ResourceManager.getBitmap(R.drawable.logo_ball);
		bitmapSpring = ResourceManager.getBitmap(R.drawable.logo_spring);
		bitmapRight = ResourceManager.getBitmap(R.drawable.logo_right);
		mHG = (HGActivity) context;
		new Thread(this).start();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawColor(Color.WHITE);
		if (mMatrix == null || sMatrix == null || tMatrix == null) {
			return;
		}
		canvas.drawBitmap(bitmapRight, mMatrix, null);
		canvas.drawBitmap(bitmapSpring, sMatrix, null);
		canvas.drawBitmap(bitmapBall, tMatrix, null);
	}

	@Override
	public void run() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		float v = 30;
		mMatrix = new Matrix();
		sMatrix = new Matrix();
		tMatrix = new Matrix();
		mMatrix.setSkew(0.4f, 0, 0, 350);
		mMatrix.postTranslate(-90, 150);
		sMatrix.setSkew(0.4f, 0, 0, 350);
		sMatrix.postTranslate(-180, 150);
		tMatrix.setTranslate(180, -100);

		for (int i = 0; i < 20; i++) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			mMatrix.postTranslate(v, 0);
			sMatrix.postTranslate(v, 0);
			v -= 1;
			postInvalidate();
		}
		for (int i = 0; i < skew.length; i++) {
			mMatrix.postSkew(skew[i], 0, 0, 350);
			sMatrix.postSkew(skew[i], 0, 0, 350);
			postInvalidate();
			try {
				Thread.sleep(30);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		v = 10;
		for (int i = 0; i < 10; i++) {
			tMatrix.postTranslate(0, v);
			v += 2;
			postInvalidate();
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		for (int i = 0; i < skew.length; i++) {
			sMatrix.postScale(1, scale[i], 0, 400);
			mMatrix.postScale(1, scale2[i], 0, 400);
			tMatrix.postTranslate(0, ballY[i]);
			postInvalidate();
			try {
				Thread.sleep(30);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Message msg = Message.obtain();
		msg.what = C.ACTION_SHOW_LOGO_GAME;
		mHG.handler.sendMessage(msg);
	}
}
