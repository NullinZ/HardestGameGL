package com.nullin.hardestgame.engine;

import static android.opengl.GLES10.glFrontFace;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Bitmap.Config;
import android.view.MotionEvent;

import com.nullin.hardestgame.C;
import com.nullin.hardestgame.utilites.TextureLoader;

public class ControlPad {

	public final int MAX_RANG = 50;
	public final float SENSITIVITY = 3f;

	private Bitmap padBorder;
	private Bitmap padBorderN;
	private Bitmap padBorderA;
	private Bitmap padNormal;
	private Bitmap padActive;
	private Bitmap pad;
	private float padCenterX, padCenterY, mPadX, mPadY;
	private int padBorderWHalf, padBorderHHalf;
	private int padWidth, padHeight;
	private int direction = C.DIRECTION_NULL;
	private static boolean touchDown = false;

	private ResourceManager mRM;

	public ControlPad(float x, float y) {
		super();
		this.padCenterX = x;
		this.padCenterY = y;

		mRM = ResourceManager.getManager();
		mRM.initPadRes();
		initRes();
		initBgPane();
	}

	private void initRes() {
		padBorder = padBorderN = mRM.rPadBorder;
		padBorderA = mRM.rPadBorderA;

		padBorderWHalf = padBorderA.getWidth() >> 1;
		padBorderHHalf = padBorderA.getHeight() >> 1;

		pad = padNormal = mRM.rPadNormal;
		padActive = mRM.rPadActive;

		padWidth = padNormal.getWidth() >> 1;
		padHeight = padNormal.getHeight() >> 1;
	}

	public boolean doTouchEvent(MotionEvent e) {
		float x = e.getX();
		float y = e.getY();
		int padR = (int) Math.sqrt(((padCenterX - x) * (padCenterX - x) + (padCenterY - y) * (padCenterY - y)));
		switch (e.getAction()) {
		case MotionEvent.ACTION_DOWN:
			pad = padActive;
			padBorder = padBorderA;
			touchDown = true;
			break;
		case MotionEvent.ACTION_MOVE:
			if (!touchDown)
				return false;
			if (padR > MAX_RANG) {
				mPadX = MAX_RANG * (x - padCenterX) / padR;
				mPadY = MAX_RANG * (y - padCenterY) / padR;
			} else {
				mPadX = x;
				mPadY = y;
			}
			direction = C.DIRECTION_NULL;
			if (mPadX > 0 && mPadX > MAX_RANG / SENSITIVITY) {
				direction = direction | C.TOUCH_RIGHT;
				direction = direction & C.TOUCH_LEFT_CANCEL;
			} else if (mPadX < 0 && mPadX < -MAX_RANG / SENSITIVITY) {
				direction = direction | C.TOUCH_LEFT;
				direction = direction & C.TOUCH_RIGHT_CANCEL;
			} else {
				direction = direction & C.TOUCH_RIGHT_CANCEL;
				direction = direction & C.TOUCH_LEFT_CANCEL;
			}
			if (mPadY > 0 && mPadY > MAX_RANG / SENSITIVITY) {
				direction = direction | C.TOUCH_DOWN;
				direction = direction & C.TOUCH_UP_CANCEL;
			} else if (mPadY < 0 && mPadY < -MAX_RANG / SENSITIVITY) {
				direction = direction | C.TOUCH_UP;
				direction = direction & C.TOUCH_DOWN_CANCEL;
			} else {
				direction = direction & C.TOUCH_DOWN_CANCEL;
				direction = direction & C.TOUCH_UP_CANCEL;
			}

			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			touchDown = false;
			reset();
			break;

		default:
			break;
		}
		KeyThread.forward(direction);
		return true;
	}

	public void reset() {
		mPadX = 0;
		mPadY = 0;
		direction = C.DIRECTION_NULL;
		pad = padNormal;
		padBorder = padBorderN;
	}

	public void draw(Canvas canvas) {
		if (canvas != null) {
			canvas.drawBitmap(padBorder, padCenterX - padBorderWHalf, padCenterY - padBorderHHalf, null);
			canvas.drawBitmap(pad, mPadX - padWidth, mPadY - padHeight, null);
		}
	}

	public void draw(GL10 gl) {
		gl.glPushMatrix();
		glFrontFace(GL10.GL_CCW);

		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		if (touchDown)
			TextureLoader.activeTexture(2);
		else
			TextureLoader.activeTexture(1);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, texBuffer);
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, bgVertexBuffer);
		gl.glDrawArrays(GL10.GL_TRIANGLE_FAN, 0, 4);
		if (touchDown)
			TextureLoader.activeTexture(4);
		else
			TextureLoader.activeTexture(3);
		gl.glTranslatef(.1f + mPadX / MAX_RANG, .1f - mPadY / MAX_RANG, .1f);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, texBuffer);
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, bgVertexBuffer);
		gl.glDrawArrays(GL10.GL_TRIANGLE_FAN, 0, 4);
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glPopMatrix();
	}

	private FloatBuffer bgVertexBuffer;
	private FloatBuffer texBuffer;

	public void initBgPane() {
		float[] vertex = new float[] { //
		-11f, 0f, -1f,//
				-11f, -7f, -1f, //
				-4f, -7f, -1f, //
				-4f, 0f, -1f, };
		ByteBuffer vbb = ByteBuffer.allocateDirect(vertex.length * 4);
		vbb.order(ByteOrder.nativeOrder());
		bgVertexBuffer = vbb.asFloatBuffer();
		bgVertexBuffer.put(vertex);
		bgVertexBuffer.position(0);

		float[] texture = new float[] { 0f, 0f, 0f, 1f, 1f, 1f, 1f, 0f, };
		ByteBuffer tbb = ByteBuffer.allocateDirect(texture.length * 4);
		tbb.order(ByteOrder.nativeOrder());
		texBuffer = tbb.asFloatBuffer();
		texBuffer.put(texture);
		texBuffer.position(0);
	}

	public static boolean isTouching() {
		return touchDown;
	}
}
