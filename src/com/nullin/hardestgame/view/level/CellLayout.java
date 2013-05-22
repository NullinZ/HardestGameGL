package com.nullin.hardestgame.view.level;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nullin.hardestgame.C;

public class CellLayout extends ViewGroup {

	final public static int GATE_LINE_COUNT = 3;
	final public static int GATE_ROW_COUNT = 2;

	Drawable drawable;

	public CellLayout(Context context, AttributeSet attr) {
		super(context, attr);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
	}

	ImageView iv;

	@Override
	protected void onLayout(boolean arg0, int arg1, int arg2, int arg3, int arg4) {
		int marginLeft = ((getWidth() / GATE_LINE_COUNT) - C.GATE_LIST_W) / 2;
		int marginTop = ((getHeight() / GATE_ROW_COUNT) - C.GATE_LIST_H) / 2;
		for (int i = 0; i < getChildCount(); i++) {
			iv = (ImageView) getChildAt(i);
			if (iv.getVisibility() != GONE)
				iv.layout(i % GATE_LINE_COUNT * (getWidth() / GATE_LINE_COUNT) + marginLeft, i / GATE_LINE_COUNT
						* (getHeight() / GATE_ROW_COUNT) + marginTop, i % GATE_LINE_COUNT
						* (getWidth() / GATE_LINE_COUNT) + marginLeft + C.GATE_LIST_W, i / GATE_LINE_COUNT
						* (getHeight() / GATE_ROW_COUNT) + marginTop + C.GATE_LIST_H);
		}
	}
}
