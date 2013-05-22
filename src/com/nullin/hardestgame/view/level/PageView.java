package com.nullin.hardestgame.view.level;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.nullin.hardestgame.engine.ResourceManager;

public class PageView extends ImageView {

	ResourceManager mRM = ResourceManager.getManager();

	private Bitmap page = null;
	private Bitmap curPage = null;

	private int mCurrentPage = 0;
	private int mPrePage = 0;
	private int pageCount = 5;
	private int margin_left, margin_top;

	private Paint inPaint;
	private Paint outPaint;

	public PageView(Context context) {
		this(context, null);
	}

	public PageView(Context context, AttributeSet attr) {
		super(context, attr);
		mRM.initLevelRes();
		inPaint = new Paint();
		outPaint = new Paint();
		outPaint.setAlpha(0);
		page = mRM.rLevelIndexNomal;
		curPage = mRM.rLevelIndexActive;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		margin_left = (getMeasuredWidth() - pageCount * curPage.getWidth()) >> 1;
		margin_top = (getMeasuredHeight() - curPage.getHeight()) >> 1;

	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		// canvas.drawColor(Color.WHITE);
		for (int i = 0; i < pageCount; i++) {
			canvas.drawBitmap(page, margin_left + i * curPage.getWidth(), margin_top, null);
		}
		canvas.drawBitmap(curPage, margin_left + mCurrentPage * curPage.getWidth(), margin_top, inPaint);
		canvas.drawBitmap(curPage, margin_left + mPrePage * curPage.getWidth(), margin_top, outPaint);

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

	public void animation() {
		new Thread() {
			public void run() {
				for (int i = 0; i <= 10; i++) {
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					inPaint.setAlpha(250 * i / 10);
					outPaint.setAlpha(250 * (10 - i) / 10);
					postInvalidate();
				}
			};
		}.start();
	}
}
