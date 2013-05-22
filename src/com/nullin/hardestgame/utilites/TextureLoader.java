package com.nullin.hardestgame.utilites;

import static android.opengl.GLES10.GL_CLAMP_TO_EDGE;
import static android.opengl.GLES10.GL_LINEAR;
import static android.opengl.GLES10.GL_NEAREST;
import static android.opengl.GLES10.GL_REPLACE;
import static android.opengl.GLES10.GL_TEXTURE0;
import static android.opengl.GLES10.GL_TEXTURE_2D;
import static android.opengl.GLES10.GL_TEXTURE_ENV;
import static android.opengl.GLES10.GL_TEXTURE_ENV_MODE;
import static android.opengl.GLES10.GL_TEXTURE_MAG_FILTER;
import static android.opengl.GLES10.GL_TEXTURE_MIN_FILTER;
import static android.opengl.GLES10.GL_TEXTURE_WRAP_S;
import static android.opengl.GLES10.GL_TEXTURE_WRAP_T;
import static android.opengl.GLES10.glBindTexture;
import static android.opengl.GLES10.glEnable;
import static android.opengl.GLES10.glGenTextures;
import static android.opengl.GLES10.glTexEnvf;
import static android.opengl.GLES10.glTexParameterf;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES10;
import android.opengl.GLUtils;

public class TextureLoader {

	private static Context mContext;
	private static int[] TEXTURE = new int[6];
	private TextureLoader() {

	}

	public static void init(Context context) {
		mContext = context;

		glEnable(GL_TEXTURE_2D);

		glGenTextures(6, TEXTURE, 0);

		glBindTexture(GL_TEXTURE_2D, TEXTURE[0]);
		
		// int[] textureIds = new int[] { R.drawable.pad_margin,
		// R.drawable.pad_margin_active,
		// R.drawable.pad_center_normal, R.drawable.pad_center_active };
		// for (int i = 0; i < textureIds.length; i++) {
		// bindImageToTexture(TEXTURE[i + 4], textureIds[i]);
		// }

	}

	public static void bindImageToTexture(int index, int resId) {
		Bitmap bitmap = getBitmap(resId);
		glBindTexture(GL_TEXTURE_2D, TEXTURE[index]);
		glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

		glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
		glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);

		glTexEnvf(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_REPLACE);
		GLUtils.texImage2D(GL_TEXTURE_2D, 0, bitmap, 0);
		bitmap.recycle();
	}

	public static void bindImageToTexture(int index, Bitmap bitmap) {
		if (bitmap == null) {
			throw new IllegalArgumentException("bitmap is null");
		}
		glBindTexture(GL_TEXTURE_2D, TEXTURE[index]);
		
		glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

		glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
		glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);

		glTexEnvf(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_REPLACE);
		GLUtils.texImage2D(GL_TEXTURE_2D, 0, bitmap, 0);
		bitmap.recycle();
	}

	public static void activeTexture(int index) {
		GLES10.glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, TEXTURE[index]);
//		GLES10.glTexParameterx(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GLES10.GL_REPEAT);
//		GLES10.glTexParameterx(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GLES10.GL_REPEAT);
	}

	private static Bitmap getBitmap(int id) {
		Bitmap bitmap = null;
		InputStream is = mContext.getResources().openRawResource(id);
		try {
			bitmap = BitmapFactory.decodeStream(is);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return bitmap;
	}

	private static Bitmap getBitmap(String path) {
		Bitmap bitmap = null;
		InputStream is = null;
		try {
			is = mContext.getAssets().open(path);
			bitmap = BitmapFactory.decodeStream(is);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return bitmap;
	}

	private static Bitmap getBitmapWithPath(String path) {
		FileInputStream fis = null;
		Bitmap bitmap = null;
		File file = new File(path);
		try {
			fis = new FileInputStream(file);
			bitmap = BitmapFactory.decodeStream(fis);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return bitmap;
	}
}
