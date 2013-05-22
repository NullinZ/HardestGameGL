package com.nullin.hardestgame.map;

import com.nullin.hardestgame.C;

import android.graphics.Rect;

public class LineExLocus implements ILocus {
	private int x, y;
	private Rect rect;
	private int direction = 1;
	private float[] result = new float[2];
	private float velocity = 0;
	private int startType = 0;
	private boolean init = true;
	final public static int UP = 1;
	final public static int RIGHT = 2;
	final public static int DOWN = 3;
	final public static int LEFT = 4;

	public LineExLocus(Rect rect, int startType, int cycle) {
		super();
		this.rect = rect;
		this.startType = startType;
		switch (startType) {
		case UP:
			x = direction > 0 ? rect.left : rect.right;
			y = rect.top;
			break;
		case LEFT:
			x = rect.left;
			y = direction > 0 ? rect.bottom : rect.top;
			break;
		case RIGHT:
			x = rect.right;
			y = direction > 0 ? rect.top : rect.bottom;
			break;
		case DOWN:
			x = direction > 0 ? rect.right : rect.left;
			y = rect.bottom;
			break;
		}

		velocity = ((rect.right - rect.left) + (rect.bottom - rect.top)) * 2.0f / (int) (cycle / C.SPF) / C.T_RATE;

	}

	@Override
	public float[] getCoordinates() {
		int start = init ? startType : getNextLine();
		switch (start) {
		case UP:
			x += direction * velocity;
			if (x > rect.right) {
				x = rect.right;
				if ((init && direction >= 0) || (!init && direction < 0)) {
					init = !init;
				} else {
					direction = -direction;
				}
			} else if (x < rect.left) {
				x = rect.left;
				if ((init && direction >= 0) || (!init && direction < 0)) {
					init = !init;
				} else {
					direction = -direction;
				}
			}
			break;
		case LEFT:
			y -= direction * velocity;
			if (y > rect.bottom) {
				y = rect.bottom;
				if ((init && direction >= 0) || (!init && direction < 0)) {
					init = !init;
				} else {
					direction = -direction;
				}
			} else if (y < rect.top) {
				y = rect.top;
				if ((init && direction >= 0) || (!init && direction < 0)) {
					init = !init;
				} else {
					direction = -direction;
				}
			}
			break;
		case RIGHT:
			y += direction * velocity;
			if (y > rect.bottom) {
				y = rect.bottom;
				if ((init && direction >= 0) || (!init && direction < 0)) {
					init = !init;
				} else {
					direction = -direction;
				}
			} else if (y < rect.top) {
				y = rect.top;
				if ((init && direction >= 0) || (!init && direction < 0)) {
					init = !init;
				} else {
					direction = -direction;
				}
			}
			break;
		case DOWN:
			x -= direction * velocity;
			if (x > rect.right) {
				x = rect.right;
				if ((init && direction >= 0) || (!init && direction < 0)) {
					init = !init;
				} else {
					direction = -direction;
				}
			} else if (x < rect.left) {
				x = rect.left;
				if ((init && direction >= 0) || (!init && direction < 0)) {
					init = !init;
				} else {
					direction = -direction;
				}
			}
			break;
		}
		result[0] = x;
		result[1] = y;
		return result;
	}

	private int getNextLine() {
		if (!init || direction > 0)
			if (startType == LEFT) {
				return UP;
			} else {
				return startType + 1;
			}
		else if (startType == UP) {
			return LEFT;
		} else {
			return startType - 1;
		}
	}

}
