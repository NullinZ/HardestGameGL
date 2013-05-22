package com.nullin.hardestgame.engine;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

import com.nullin.hardestgame.HGActivity;
import com.nullin.hardestgame.R;

public class ResourceManager {
	private static ResourceManager mResourceManager = null;
	private static HGActivity mHGActivity;
	public Bitmap rPadBorder;
	public Bitmap rPadBorderA;
	public Bitmap rPadNormal;
	public Bitmap rPadActive;

	public Bitmap rLevelIndexNomal;
	public Bitmap rLevelIndexActive;

	public Bitmap rGameBg;

	public String[] rStatusText;

	private ResourceManager(HGActivity HGActivity) {

	}

	public static ResourceManager getManager() {
		if (mResourceManager == null) {
			mResourceManager = new ResourceManager(mHGActivity);
		}
		return mResourceManager;
	}

	public void init(HGActivity hgActivity) {
		mHGActivity = hgActivity;
		rStatusText=mHGActivity.getResources().getStringArray(R.array.levelStr);
	}

	/**
	 * 初始化 pad 资源
	 */
	public void initPadRes() {
		rPadBorder = getBitmap(R.drawable.pad_margin);
		rPadBorderA = getBitmap(R.drawable.pad_margin_active);
		rPadNormal = getBitmap(R.drawable.pad_center_normal);
		rPadActive = getBitmap(R.drawable.pad_center_active);

	}

	public void initLevelRes() {
		rLevelIndexNomal = getBitmap(R.drawable.level_index_nomal);
		rLevelIndexActive = getBitmap(R.drawable.level_index_active);
	}

	public void initGameRes() {
		rGameBg = getBitmap(R.drawable.game_bg);
	}

	public static Bitmap getBitmap(int id) {
		return ((BitmapDrawable) mHGActivity.getResources().getDrawable(id)).getBitmap();
	}
	public Context getContext(){
		return mHGActivity;
	}
}
