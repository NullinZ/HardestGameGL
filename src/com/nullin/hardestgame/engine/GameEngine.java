package com.nullin.hardestgame.engine;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Bitmap.Config;
import android.os.Message;
import android.view.MotionEvent;

import com.nullin.hardestgame.C;
import com.nullin.hardestgame.HGActivity;
import com.nullin.hardestgame.MediaPlayerAdapter;
import com.nullin.hardestgame.map.Map;
import com.nullin.hardestgame.map.data.BallGenerator;
import com.nullin.hardestgame.map.data.CoinGenerator;
import com.nullin.hardestgame.map.data.Generator;
import com.nullin.hardestgame.sprit.Ball;
import com.nullin.hardestgame.sprit.Hero;
import com.nullin.hardestgame.utilites.TextureLoader;
import com.nullin.hardestgame.view.GameView;

public class GameEngine implements Runnable, ILevelController {

	public static int gameState = C.GAME_STATE_LOADING;
	private static GameEngine mGameEngine;
	public int mLevel = 1;
	private Generator mBallGenerator;
	private Generator mCoinGenerator;
	private HGActivity mHG;
	private ResourceManager mRM = ResourceManager.getManager();

	private Ball[] balls;
	private Ball[] coins;
	private ControlPad mPad;
	private Hero mHero;
	private Map map;
	private int coinsLeaved = 0;

	private GameEngine() {
		map = new Map();
		mBallGenerator = BallGenerator.getGenerator();
		mCoinGenerator = CoinGenerator.getGenerator();
		mPad = new ControlPad(150, C.getHeight() - 120);
		map.registerLevelController(this);
		mHero = new Hero(map.start.centerX() + C.GV_MARGIN_LEFT, map.start.centerY() + C.GV_MARGIN_TOP, C.HERO_RADIUS);
	}

	public static GameEngine getEngine() {
		if (mGameEngine == null) {
			mGameEngine = new GameEngine();
		}
		return mGameEngine;
	}

	Matrix m = new Matrix();

	@Override
	public void run() {
		while (gameState != C.GAME_STATE_OVER) {
			try {
				Thread.sleep(C.SPF);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (gameState != C.GAME_STATE_RUNNING) {
				continue;
			}
			for (int i = 0; i < balls.length; i++) {
				if (balls[i] == null)
					continue;
				balls[i].updatePosition();
				if (balls[i].isHit(mHero.getBorder())) {
					MediaPlayerAdapter.die();
					mHero.die();
					coins = mCoinGenerator.getLevelData(mLevel);
					coinsLeaved = coins != null ? coins.length : 0;
					sendMessage(C.ACTION_DIE, mHero.dieTimes());
				}
			}
			for (int i = 0; i < coins.length; i++) {
				if (coins[i] == null)
					continue;
				coins[i].updatePosition();
				if (coins[i].isHit(mHero.getBorder())) {
					MediaPlayerAdapter.getCoin();
					coinsLeaved--;
					coins[i] = null;
				}
			}
		}
	}

	public void doKeyDown(int direction) {
		mHero.move(map, direction);
	}

	public void start() {
		// gameState = C.GAME_STATE_RUNNING;
		new Thread(this).start();
		new KeyThread(this).start();
		sendMessage(C.ACTION_PLAY_MUSIC, null);
	}

	public void stop() {
		sendMessage(C.ACTION_STOP_MUSIC, null);
		gameState = C.GAME_STATE_OVER;

	}

	public void start(int level) {
		this.mLevel = level;
		balls = mBallGenerator.getLevelData(mLevel);
		coins = mCoinGenerator.getLevelData(mLevel);
		coinsLeaved = coins != null ? coins.length : 0;
		map.toGate(mLevel);
		mHero.moveTo(map.start.centerX() + C.GV_MARGIN_LEFT, map.start.centerY() + C.GV_MARGIN_TOP);
		gameState = C.GAME_STATE_RUNNING;
		new Thread(this).start();
		new KeyThread(this).start();
		sendMessage(C.ACTION_PLAY_MUSIC, null);
	}

	public ControlPad getPad() {
		return mPad;
	}

	public Ball[] getBalls() {
		return balls;
	}

	public Ball[] getCoins() {
		return coins;
	}

	public Hero getHero() {
		return mHero;
	}

	public Map getMap() {
		return map;
	}

	public boolean doTouchEvent(android.view.MotionEvent event) {
		float x = event.getX();
		float y = event.getY();
		// proB = false;
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if (x < 0 || x > 350 || y > C.getHeight() || y < C.getHeight() - 350)
				return true;
			break;
		case MotionEvent.ACTION_MOVE:

			break;
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP:
			break;

		default:
			break;
		}
		mPad.doTouchEvent(event);
		return true;

	}

	@Override
	public void levelComplete() {
		if (coinsLeaved == 0 && mLevel <= 10) {
			gameState = C.GAME_STATE_LOADING;
			mLevel++;
			balls = mBallGenerator.getLevelData(mLevel);
			coins = mCoinGenerator.getLevelData(mLevel);
			coinsLeaved = coins != null ? coins.length : 0;
			map.toGate(mLevel);

			mHero.recordPosition(map.start.centerX() + C.GV_MARGIN_LEFT, map.start.centerY() + C.GV_MARGIN_TOP);
			mHero.moveTo(map.start.centerX() + C.GV_MARGIN_LEFT, map.start.centerY() + C.GV_MARGIN_TOP);
			sendMessage(C.ACTION_SHOW_TIP, mRM.rStatusText[mLevel - 1]);
			sendMessage(C.ACTION_LEVEL_CHANGE, mLevel);
			KeyThread.forward(C.DIRECTION_NULL);
			mPad.reset();
		} else if (mLevel > 10) {
			gameState = C.GAME_STATE_OVER;
			sendMessage(C.ACTION_SUCCESS, mRM.rStatusText[mLevel - 1]);
		}
		GameView.nextGate = true;
	}

	private void sendMessage(int what, Object content) {
		Message msg = Message.obtain();
		msg.what = what;
		msg.obj = content;
		mHG.handler.sendMessage(msg);
	}

	public void setHG(HGActivity mHG) {
		this.mHG = mHG;
	}
}
