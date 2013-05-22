package com.nullin.hardestgame.sprit;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import android.graphics.Canvas;
import android.graphics.Color;

/**
 * @author zhaosheng x,y is the center coordinates of the sprite
 * 
 */
public abstract class Sprite {

	protected int color;
	protected float x;
	protected float y;
	protected float startX, startY;

	public Sprite() {
		this(-1);
	}

	public Sprite(int color) {
		this.color = color;
	}

	public void move(float x, float y) {
		this.x += x;
		this.y += y;
	}

	public void moveTo(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public void recordPosition(float x, float y) {
		startX = x;
		startY = y;
	}

	/**
	 * get the value if sprite hit a point
	 * 
	 * @param x
	 * @param y
	 * 
	 * 
	 */
	public abstract boolean isHit(float x, float y);

	/**
	 * draw sprite
	 * 
	 * @param canvas
	 */
	public abstract void draw(Canvas canvas);

	/**
	 * update the position by locus
	 */
	public abstract void updatePosition();
}
