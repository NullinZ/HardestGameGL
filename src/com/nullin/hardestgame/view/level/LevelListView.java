package com.nullin.hardestgame.view.level;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;

public class LevelListView extends LinearLayout {

	private int mCurrentPage = 0;
	private int mPrePage = 0;
	private int pageCount = 5;

	public LevelListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void toNextPage() {
		mPrePage = mCurrentPage;
		if (mCurrentPage >= pageCount - 1) {
			return;
		}
		mCurrentPage++;

		animation();
	}

	public void toPrePage() {
		mPrePage = mCurrentPage;
		if (mCurrentPage <= 0) {
			return;
		}
		mCurrentPage--;
		animation();
	}

	public int getCurrentPage() {
		return mCurrentPage;
	}

	public void animation() {
		Animation mAnimNext = new TranslateAnimation(-mPrePage * 600, -mCurrentPage * 600, 0, 0);
		mAnimNext.setDuration(500);
		mAnimNext.setFillAfter(true);
		mAnimNext.setInterpolator(new AccelerateDecelerateInterpolator());
		this.startAnimation(mAnimNext);
	}
}
