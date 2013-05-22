package com.nullin.hardestgame.view;

import static android.opengl.GLES10.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES10.GL_DEPTH_BUFFER_BIT;
import static android.opengl.GLES10.GL_DEPTH_TEST;
import static android.opengl.GLES10.GL_DITHER;
import static android.opengl.GLES10.GL_SMOOTH;
import static android.opengl.GLES10.glClear;
import static android.opengl.GLES10.glClearColor;
import static android.opengl.GLES10.glDisable;
import static android.opengl.GLES10.glEnable;
import static android.opengl.GLES10.glLoadIdentity;
import static android.opengl.GLES10.glShadeModel;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLU;

import com.nullin.hardestgame.C;
import com.nullin.hardestgame.R;
import com.nullin.hardestgame.engine.GameEngine;
import com.nullin.hardestgame.engine.ResourceManager;
import com.nullin.hardestgame.utilites.Configuration;
import com.nullin.hardestgame.utilites.DClickRocker;
import com.nullin.hardestgame.utilites.IRocker;
import com.nullin.hardestgame.utilites.IRocker.IRockerHolder;
import com.nullin.hardestgame.utilites.TextureLoader;

public class GameView extends GLSurfaceView implements Renderer, IRockerHolder {

	public static boolean nextGate = false;
	private GameEngine mGameEngine;
	private Context mContext;
	private IRocker rocker;

	private ResourceManager mRM;

	public GameView(Context context) {
		super(context);
		mContext = context;
		mGameEngine = GameEngine.getEngine();
		mRM = ResourceManager.getManager();
		setKeepScreenOn(true);
		setRenderer(this);
	}

	public void draw(GL10 gl) {

		glEnable(GL10.GL_TEXTURE_2D);
		glDisable(GL10.GL_DEPTH_TEST);
		glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_ONE, GL10.GL_DST_ALPHA);
		if (Configuration.isRockerOn) {
			mGameEngine.getPad().draw(gl);
		}
		rocker.transfer(gl);
		mGameEngine.getMap().drawMapWithBg(gl);
		glDisable(GL10.GL_BLEND);
		glEnable(GL10.GL_DEPTH_TEST);

		glDisable(GL10.GL_TEXTURE_2D);
		
		gl.glEnable(GL10.GL_POINT_SMOOTH);
		gl.glEnableClientState(GL10.GL_COLOR_ARRAY);

		gl.glPointSize(C.BALL_RADIUS * (DClickRocker.farFormEye ? 1.5f : 2));
		for (int i = 0; i < mGameEngine.getBalls().length; i++) {
			if (mGameEngine.getBalls()[i] != null)
				mGameEngine.getBalls()[i].draw(gl);
		}
		for (int i = 0; i < mGameEngine.getCoins().length; i++) {
			if (mGameEngine.getCoins()[i] != null)
				mGameEngine.getCoins()[i].draw(gl);
		}
		mGameEngine.getHero().draw(gl);
		gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
	}

	@Override
	public void onDrawFrame(GL10 gl) {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		glLoadIdentity();
		GLU.gluLookAt(gl, 0, 0, 6, 0, 0, 0, 0, 1, 0);
		if (GameEngine.gameState != C.GAME_STATE_OVER) {
			if (GameEngine.gameState == C.GAME_STATE_RUNNING) {
				draw(gl);
			}
		}
		if (nextGate) {
			change();
		}
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		float ratio = (float) width / height;
		gl.glViewport(0, 0, width, height);
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glFrustumf(-ratio, ratio, -1, 1, 1, 20);
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {

		glDisable(GL_DITHER);
		glClearColor(0f, 0f, 0f, 1);
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);
		// glEnable(GL10.GL_LINE_SMOOTH);
		// glEnable(GL10.GL_POINT_SMOOTH);
		glShadeModel(GL_SMOOTH);
		TextureLoader.init(mContext);

		Bitmap b = Bitmap.createBitmap(1024, 512, Config.ARGB_4444);
		Canvas cc = new Canvas(b);
		mGameEngine.getMap().drawMapWithBg(cc);
		TextureLoader.bindImageToTexture(0, b);

		b = Bitmap.createBitmap(256, 256, Config.ARGB_4444);
		cc = new Canvas(b);
		cc.drawBitmap(mRM.rPadBorder, 8, 8, null);
		TextureLoader.bindImageToTexture(1, b);
		b = Bitmap.createBitmap(256, 256, Config.ARGB_4444);
		cc = new Canvas(b);
		cc.drawBitmap(mRM.rPadBorderA, 8, 8, null);
		TextureLoader.bindImageToTexture(2, b);
		b = Bitmap.createBitmap(256, 256, Config.ARGB_4444);
		cc = new Canvas(b);
		cc.drawBitmap(mRM.rPadNormal, 93, 93, null);
		TextureLoader.bindImageToTexture(3, b);
		b = Bitmap.createBitmap(256, 256, Config.ARGB_4444);
		cc = new Canvas(b);
		cc.drawBitmap(mRM.rPadActive, 93, 93, null);
		TextureLoader.bindImageToTexture(4, b);

		TextureLoader.bindImageToTexture(5, R.drawable.game_bg_seg);

	}

	public void change() {
		Bitmap b = Bitmap.createBitmap(1024, 512, Config.ARGB_4444);
		Canvas cc = new Canvas(b);
		mGameEngine.getMap().drawMapWithBg(cc);
		TextureLoader.bindImageToTexture(0, b);
		nextGate = false;
	}

	@Override
	public void setRocker(IRocker rocker) {
		this.rocker = rocker;
	}
}
