package com.nullin.hardestgame.sprit;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.BlurMaskFilter.Blur;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

import com.nullin.hardestgame.C;
import com.nullin.hardestgame.R;
import com.nullin.hardestgame.engine.ControlPad;
import com.nullin.hardestgame.engine.ResourceManager;
import com.nullin.hardestgame.map.Map;

public class Hero extends Sprite {
	private float r;
	private Paint mPaint;
	private int direction = 0;
	private Rect border;
	private int dieTimes = 0;
	private boolean dying = false;
	private int blurW = 2;
	private float velocity = C.HERO_VELOCITY;
	private Bitmap ship[];
	private int shipDirection = 0;

	
	protected FloatBuffer vertexBuffer;
	protected FloatBuffer colorBuffer;
	protected ShortBuffer indexBuffer;

	Matrix mDirection = new Matrix();

	public Hero(float x, float y, float r) {
		super(Color.RED);
		startX = this.x = x;
		startY = this.y = y;
		this.r = r;
		mPaint = new Paint();
		mPaint.setColor(color);
		mPaint.setMaskFilter(new BlurMaskFilter(blurW, Blur.NORMAL));
		border = new Rect();
		ship = new Bitmap[2];
		ship[0] = ResourceManager.getBitmap(R.drawable.ship);
		ship[1] = ResourceManager.getBitmap(R.drawable.shipr);
		initHero();
	}

	@Override
	public boolean isHit(float x, float y) {
		RectF rect = new RectF(this.x - r, this.y - r, this.x + r, this.y + r);
		return rect.contains(x, y);
	}

	public boolean isHit(RectF rect) {
		RectF rectf = new RectF(this.x - r, this.y - r, this.x + r, this.y + r);
		return rectf.contains(rect);
	}

	public void resetBorder() {
		border.left = (int) (this.x - r);
		border.right = (int) (this.x + r);
		border.top = (int) (this.y - r);
		border.bottom = (int) (this.y + r);
	}

	public void die() {
		dying = true;
		for (int i = 10; i > 0; i--) {
			mPaint.setAlpha(i * 25);
			try {
				Thread.sleep(C.SPF);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		mPaint.setAlpha(255);
		this.x = startX;
		this.y = startY;
		dieTimes++;
		dying = false;

	}

	public int dieTimes() {
		return dieTimes;
	}

	@Override
	public void updatePosition() {

	}

	public void move(Map map, int direction) {
		if (dying)
			return;
		float dx = 0, dy = 0;
		if ((direction & C.TOUCH_UP) != 0 && map.canPass(this, x, y - velocity - r)) {// 上
			dy = -velocity;
		} else if ((direction & C.TOUCH_DOWN) != 0 && map.canPass(this, x, y + velocity + r)) {// 下
			dy = velocity;
		}

		if ((direction & C.TOUCH_LEFT) != 0 && map.canPass(this, x - velocity - r, y)) {// 左
			dx = -velocity;
		} else if ((direction & C.TOUCH_RIGHT) != 0 && map.canPass(this, x + velocity + r, y)) {// 右
			dx = velocity;
		}
		if (ControlPad.isTouching())
			redirectShip(direction);
		else
			redirectShip(C.TOUCH_RIGHT);
		move(dx, dy);
		resetBorder();
	}

	private void redirectShip(int direction) {
		if ((direction | (C.TOUCH_UP | C.TOUCH_LEFT)) == direction) {// 上
			shipDirection = 5;
		} else if ((direction | (C.TOUCH_DOWN | C.TOUCH_LEFT)) == direction) {// 下
			shipDirection = 3;
		} else if ((direction | (C.TOUCH_UP | C.TOUCH_RIGHT)) == direction) {// 上
			shipDirection = 7;
		} else if ((direction | (C.TOUCH_DOWN | C.TOUCH_RIGHT)) == direction) {// 下
			shipDirection = 1;
		} else if ((direction | C.TOUCH_DOWN) == direction) {// 下
			shipDirection = 2;
		} else if ((direction | C.TOUCH_UP) == direction) {// 下
			shipDirection = 6;
		} else if ((direction | C.TOUCH_LEFT) == direction) {// 左
			shipDirection = 4;
		} else if ((direction | C.TOUCH_RIGHT) == direction) {// 右
			shipDirection = 0;
		}
		shipDirection *= 45;
	}

	@Override
	public void draw(Canvas canvas) {
		canvas.drawRect(x - r, y - r, x + r, y + r, mPaint);
//		mDirection.reset();
//		mDirection.setRotate(shipDirection, 11, 11);
//		mDirection.postTranslate(x - r, y - r);
//		if (ControlPad.isTouching()) {
//			canvas.drawBitmap(ship[1], mDirection, mPaint);
//		} else {
//			canvas.drawBitmap(ship[0], mDirection, mPaint);
//
//		}
	}

	public void draw(GL10 gl) {
		gl.glPushMatrix();
		{
			gl.glTranslatef(30 * x / 800 - 15, -18 * y / 480 + 9, 0);
			gl.glColorPointer(4, GL10.GL_FLOAT, 0, colorBuffer);
			gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
			gl.glDrawElements(GL10.GL_TRIANGLES, 36, GL10.GL_UNSIGNED_SHORT, indexBuffer);
		}
		gl.glPopMatrix();
	}

	public void initHero() {
		final float r = C.HERO_RADIUS * .03f ;
		float vertex[] = { //
				-r, -r, -r/2 - 3f,//
				r, -r, -r/2 - 3f,//
				r, r, -r/2 - 3f,//
				-r, r, -r/2 - 3f,//
				-r, -r, r/2 - 3f,//
				r, -r, r/2 - 3f,//
				r, r, r/2 - 3f,//
				-r, r, r/2 - 3f,//
		};

        short indices[] = {
                0, 4, 5,    0, 5, 1,
                1, 5, 6,    1, 6, 2,
                2, 6, 7,    2, 7, 3,
                3, 7, 4,    3, 4, 0,
                4, 7, 6,    4, 6, 5,
                3, 0, 1,    3, 1, 2
        };
	

        ByteBuffer vbb = ByteBuffer.allocateDirect(vertex.length * 4 );
        vbb.order(ByteOrder.nativeOrder());
        vertexBuffer = vbb.asFloatBuffer();
        vertexBuffer.put(vertex);
        vertexBuffer.position(0);
        
		ByteBuffer ibb = ByteBuffer.allocateDirect(indices.length * 2);
		ibb.order(ByteOrder.nativeOrder());
		indexBuffer = ibb.asShortBuffer();
		indexBuffer.put(indices);
		indexBuffer.position(0);

        float colors[] = {
                1,    1,    1,  1,
                Color.red(color) / 255,    1,    1,  1,
                1,  Color.green(color) / 255,    1,  1,
                Color.red(color) / 255,  1,    1,  1,
                Color.red(color) / 255,    1,  Color.blue(color) / 255,  1,
                Color.red(color) / 255,  Color.green(color) / 255,  Color.blue(color) / 255,  1,
                1,  Color.green(color) / 255,  Color.blue(color) / 255,  1,
                1,    1,  1,  1,
        };
		ByteBuffer tbb = ByteBuffer.allocateDirect(colors.length * 4);
		tbb.order(ByteOrder.nativeOrder());
		colorBuffer = tbb.asFloatBuffer();
		colorBuffer.put(colors);
		colorBuffer.position(0);
	}

	public Rect getBorder() {
		return border;
	}

	public float getR() {
		return r;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public int getDirection() {
		return direction;
	}

	public int getBlurW() {
		return blurW;
	}

	public void setAlpha(int alpha) {
		mPaint.setAlpha(alpha);
	}

	public void setBlurW(int blurW) {
		this.blurW = blurW;
		mPaint.setMaskFilter(new BlurMaskFilter(blurW, Blur.NORMAL));
	}

}
