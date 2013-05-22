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
import com.nullin.hardestgame.sprit.Ball;
import com.nullin.hardestgame.sprit.Hero;

public class GameLogoView extends View implements Runnable {

	private HGActivity mHG;
	private Hero mHero;
	private Ball[] balls = new Ball[3];
	private Bitmap bitmapRight;
	private Matrix mMatrix;

	public GameLogoView(Context context) {
		super(context);
		setKeepScreenOn(true);
		mHG = (HGActivity) context;
		mHero = new Hero(-50, -50, 20);
		mHero.setBlurW(10);
		mHero.setAlpha(180);
		bitmapRight = ResourceManager.getBitmap(R.drawable.logo_game);

		for (int i = 0; i < balls.length; i++) {
			balls[i] = new Ball(-50, -50, 15, Color.BLUE);
			balls[i].setBlurW(15);
			balls[i].setAlpha(190);
		}
		mMatrix = new Matrix();
		mMatrix.setScale(0.0003f, 1, 400, 240);

		new Thread(this).start();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawColor(Color.BLACK);
		mHero.draw(canvas);
		for (int i = 0; i < balls.length; i++) {
			balls[i].draw(canvas);
		}
		canvas.drawBitmap(bitmapRight, mMatrix, null);
	}

	@Override
	public void run() {
		try {
			Thread.sleep(1500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		float scaleRate = 2.45f;
		for (int i = 0; i < 60; i++) {
			if (i > 29 && i < 50) {
				mMatrix.postScale(scaleRate, 1, 400, 240);
				scaleRate *= 0.95;
			}
			mHero.moveTo(i * 15, (float) (C.getHeight() / 2 + 100 * Math.sin(Math.PI * i / 20)));
			for (int j = 0; j < balls.length; j++) {
				balls[j].moveTo((57 - i) * 15, (float) (C.getHeight() / 2 + 100 * Math.sin(Math.PI * i / 25 + j * 15)));
			}
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
		msg.what = C.ACTION_SHOW_MENU;
		mHG.handler.sendMessage(msg);
	}
}
