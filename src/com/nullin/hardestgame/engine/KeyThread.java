package com.nullin.hardestgame.engine;

import android.view.KeyEvent;

import com.nullin.hardestgame.C;
import com.nullin.hardestgame.utilites.Configuration;

public class KeyThread extends Thread {
	GameEngine mGE;
	private static int direction = C.DIRECTION_NULL;

	public KeyThread(GameEngine ge) {
		super();
		mGE = ge;
	}

	@Override
	public void run() {
		while (C.GAME_STATE_OVER != GameEngine.gameState) {
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (GameEngine.gameState == C.GAME_STATE_RUNNING) {
				mGE.doKeyDown(direction);
			}
		}
	}

	public static void forward(int direction) {
		KeyThread.direction = direction;
	}

	public static boolean doKeyDown(int keyCode, KeyEvent event) {

		if (!Configuration.isKeyConOn) {
			switch (keyCode) {
			case KeyEvent.KEYCODE_W:
			case KeyEvent.KEYCODE_D:
			case KeyEvent.KEYCODE_A:
			case KeyEvent.KEYCODE_S:
				return false;
			}
		}
		if (!Configuration.isTrConOn) {
			switch (keyCode) {
			case KeyEvent.KEYCODE_DPAD_DOWN:
			case KeyEvent.KEYCODE_DPAD_RIGHT:
			case KeyEvent.KEYCODE_DPAD_LEFT:
			case KeyEvent.KEYCODE_DPAD_UP:
				return false;
			}
		}

		switch (keyCode) {
		case KeyEvent.KEYCODE_DPAD_UP:
		case KeyEvent.KEYCODE_W:
			direction  |=  C.TOUCH_UP;
			break;
		case KeyEvent.KEYCODE_DPAD_DOWN:
		case KeyEvent.KEYCODE_S:
			direction  |=  C.TOUCH_DOWN;
			break;
		case KeyEvent.KEYCODE_DPAD_LEFT:
		case KeyEvent.KEYCODE_K:
			direction  |=  C.TOUCH_LEFT;
			break;
		case KeyEvent.KEYCODE_DPAD_RIGHT:
		case KeyEvent.KEYCODE_L:
			direction |= C.TOUCH_RIGHT;
			break;
		}
		return false;
	}

	public static boolean doKeyUp(int keyCode, KeyEvent event) {
		if (!Configuration.isKeyConOn) {
			switch (keyCode) {
			case KeyEvent.KEYCODE_W:
			case KeyEvent.KEYCODE_D:
			case KeyEvent.KEYCODE_A:
			case KeyEvent.KEYCODE_S:
				return false;
			}
		}
		if (!Configuration.isTrConOn) {
			switch (keyCode) {
			case KeyEvent.KEYCODE_DPAD_DOWN:
			case KeyEvent.KEYCODE_DPAD_RIGHT:
			case KeyEvent.KEYCODE_DPAD_LEFT:
			case KeyEvent.KEYCODE_DPAD_UP:
				return false;
			}
		}

		switch (keyCode) {
		case KeyEvent.KEYCODE_DPAD_UP:
		case KeyEvent.KEYCODE_W:
			direction = direction & C.TOUCH_UP_CANCEL;
			break;
		case KeyEvent.KEYCODE_DPAD_DOWN:
		case KeyEvent.KEYCODE_S:
			direction = direction & C.TOUCH_DOWN_CANCEL;
			break;
		case KeyEvent.KEYCODE_DPAD_LEFT:
		case KeyEvent.KEYCODE_K:
			direction = direction & C.TOUCH_LEFT_CANCEL;
			break;
		case KeyEvent.KEYCODE_DPAD_RIGHT:
		case KeyEvent.KEYCODE_L:
			direction = direction & C.TOUCH_RIGHT_CANCEL;
			break;

		}
		return false;
	}

}
