package com.nullin.hardestgame.sprit;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BlurMaskFilter;
import android.graphics.BlurMaskFilter.Blur;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.nullin.hardestgame.map.ILocus;

public class Ball extends Sprite {
	private ILocus l;
	public float r;
	private Paint paint;
	private Bitmap circle;
	private int blurW = 2;
	private Paint mPaint;

	public Ball(ILocus l, float r) {
		this(0, 0, r, l, -1);
	}

	public Ball(float x, float y, float r, int color) {
		this(x, y, r, null, color);
	}

	public Ball(float x, float y, float r, ILocus l) {
		this(x, y, r, l, -1);
	}

	public Ball(float x, float y, float r, ILocus l, int color) {
		super(color != -1 ? color : Color.BLUE);
		this.x = x;
		this.y = y;
		this.r = r;
		this.l = l;
		circle = Bitmap.createBitmap((int) r << 2, (int) r << 2, Config.ARGB_8888);
		paint = new Paint();
		paint.setColor(this.color);
		paint.setAntiAlias(true);
		genBall();
		mPaint = new Paint();
		initBall();
	}

	private void genBall() {
		Canvas c = new Canvas(circle);
		paint.setMaskFilter(new BlurMaskFilter(blurW, Blur.SOLID));
		c.drawColor(Color.TRANSPARENT);
		c.drawCircle(2 * r, 2 * r, r, paint);
	}

	@Override
	public void updatePosition() {
		if (l != null) {
			float[] position = l.getCoordinates();
			moveTo(position[0], position[1]);
		}
	}

	@Override
	public void draw(Canvas canvas) {
		// canvas.drawCircle(x, y, r, paint);
		canvas.drawBitmap(circle, x - 2 * r, y - 2 * r, mPaint);
	}

	public void draw(GL10 gl) {
		gl.glPushMatrix();
		{
			gl.glTranslatef(30 * x / 800 - 15, -18 * y / 480 + 9, 0);
			gl.glColorPointer(4, GL10.GL_FLOAT, 0, colorBuffer);
			gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
			gl.glDrawArrays(GL10.GL_POINTS, 0, 1);
		}
		gl.glPopMatrix();
	}
	protected FloatBuffer vertexBuffer;
	protected FloatBuffer colorBuffer;

	public void initBall() {
		float[] vertex = new float[] { 0, 0, -2.8f };
		ByteBuffer vbb = ByteBuffer.allocateDirect(vertex.length * 4);
		vbb.order(ByteOrder.nativeOrder());
		vertexBuffer = vbb.asFloatBuffer();
		vertexBuffer.put(vertex);
		vertexBuffer.position(0);

		float[] colors = new float[] { Color.red(color) / 255, Color.green(color) / 255, Color.blue(color) / 255, 1 };
		ByteBuffer tbb = ByteBuffer.allocateDirect(colors.length * 4);
		tbb.order(ByteOrder.nativeOrder());
		colorBuffer = tbb.asFloatBuffer();
		colorBuffer.put(colors);
		colorBuffer.position(0);
	}
	@Override
	public boolean isHit(float x, float y) {
		return (r * r >= (x - this.x) * (x - this.x) + (y - this.y) * (y - this.y)) ? true : false;
	}

	public boolean isHit(Rect rect) {
		return isHit(rect.left, rect.top) || isHit(rect.right, rect.top) || isHit(rect.left, rect.bottom)
				|| isHit(rect.right, rect.bottom) || rect.contains((int) this.x, (int) this.y);
	}

	public int getBlurW() {
		return blurW;
	}

	public void setAlpha(int alpha) {
		mPaint.setAlpha(alpha);
	}

	public void setBlurW(int blurW) {
		this.blurW = blurW;
		genBall();
	}
}
