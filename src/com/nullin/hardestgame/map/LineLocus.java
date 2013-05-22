package com.nullin.hardestgame.map;

import android.graphics.Rect;

import com.nullin.hardestgame.C;

public class LineLocus implements ILocus {
	private float x, y;
	private Rect rect;
	private int directionX = 1;
	private int directionY = 1;
	private float[] result = new float[2];
	private float velocityX = 0;
	private float velocityY = 0;

	public LineLocus(int x, int y, Rect rect, int cycle) {
		super();
		this.x = x;
		this.y = y;
		this.rect = rect;
		if (!rect.contains(x, y)) {
			if (rect.left == rect.right) {
				if (rect.bottom < y) {
					this.y = rect.bottom;
				} else if (rect.top > y) {
					this.y = rect.top;
				}
				this.x = rect.left;
			} else if (rect.top == rect.bottom) {
				if (rect.left > x) {
					this.x = rect.left;
				} else if (rect.right < x) {
					this.x = rect.right;
				}
				this.y = rect.top;
			} else {
				this.x = rect.left;
				this.y = rect.top;
			}

		} else {
			if (y / x != (rect.bottom - rect.top) / (rect.right - rect.left)) {
				this.x = x - rect.left < rect.right - x ? rect.left : rect.right;
				this.y = y - rect.top < rect.bottom - y ? rect.top : rect.bottom;
			}
		}

		velocityX = (2.0f * (rect.right - rect.left) / (int) (cycle / C.SPF)) / C.T_RATE;
		velocityY = (2.0f * (rect.bottom - rect.top) / (int) (cycle / C.SPF)) / C.T_RATE;
	}

	@Override
	public float[] getCoordinates() {
		x += directionX * velocityX;
		y += directionY * velocityY;
		if (x > rect.right) {
			x = rect.right;
			directionX = -1;
		} else if (x < rect.left) {
			x = rect.left;
			directionX = 1;
		}
		if (y > rect.bottom) {
			y = rect.bottom;
			directionY = -1;
		} else if (y < rect.top) {
			y = rect.top;
			directionY = 1;
		}
		result[0] = x;
		result[1] = y;
		return result;
	}

}
