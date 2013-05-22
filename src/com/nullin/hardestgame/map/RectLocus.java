package com.nullin.hardestgame.map;

import com.nullin.hardestgame.C;

import android.graphics.Rect;

public class RectLocus implements ILocus {
	private int x, y;
	private Rect rect;
	private int direction = 0;
	private float[] result = new float[2];
	private boolean clockwise = true;
	private int velocity = 0;
	final static int UP = -1;
	final static int DOWN = 1;
	final static int LEFT = -2;
	final static int RIGHT = 2;

	public RectLocus(int startX, int startY, Rect rect, int cycle) {
		super();
		this.x = startX;
		this.y = startY;
		this.rect = rect;
		// if (!rect.contains(startX, startY)) {
		// this.x = rect.left;
		// this.y = rect.top;
		// }

		// this.x = x - rect.left < rect.right - x ? rect.left : rect.right;
		// this.y = y - rect.top < rect.bottom - y ? rect.top : rect.bottom;
		if (rect.top == y) {
			direction = UP;
		}
		if (rect.right == x) {
			direction = RIGHT;
		}
		if (rect.bottom == y) {
			direction = DOWN;
		}
		if (rect.left == x) {
			direction = LEFT;
		}
		velocity = (int) ((rect.bottom - rect.top + rect.right - rect.left) * 2 / (int) (cycle / C.SPF) / C.T_RATE);

	}

	public RectLocus exClickWise(boolean clockwise) {
		this.clockwise = clockwise;
		return this;
	}

	@Override
	public float[] getCoordinates() {
		switch (direction) {
		case UP:
			x += clockwise ? velocity : -velocity;
			if (x > rect.right) {
				x = rect.right;
				direction = RIGHT;
			} else if (x < rect.left) {
				x = rect.left;
				direction = LEFT;
			}
			break;
		case RIGHT:
			y += clockwise ? velocity : -velocity;
			if (y > rect.bottom) {
				y = rect.bottom;
				direction = DOWN;
			} else if (y < rect.top) {
				y = rect.top;
				direction = UP;
			}
			break;
		case DOWN:
			x -= clockwise ? velocity : -velocity;
			if (x > rect.right) {
				x = rect.right;
				direction = RIGHT;
			} else if (x < rect.left) {
				x = rect.left;
				direction = LEFT;
			}
			break;
		case LEFT:
			y -= clockwise ? velocity : -velocity;
			if (y > rect.bottom) {
				y = rect.bottom;
				direction = DOWN;
			} else if (y < rect.top) {
				y = rect.top;
				direction = UP;
			}
			break;
		}
		result[0] = x;
		result[1] = y;
		return result;
	}
}
