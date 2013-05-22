package com.nullin.hardestgame.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageButton;

import com.nullin.hardestgame.R;

public class CheckImageButton extends ImageButton {

	private boolean mChecked = false;
	private Drawable srcChecked;
	private Drawable srcNormal;
	private int typeId;

	public CheckImageButton(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public CheckImageButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.chk_ib, defStyle, 0);
		typeId = a.getInt(R.styleable.chk_ib_typeId, 0);
		srcChecked = a.getDrawable(R.styleable.chk_ib_checkedSrc);
		srcNormal = a.getDrawable(R.styleable.chk_ib_normalSrc);
		if (srcNormal != null) {
			setImageDrawable(srcNormal);
		}
		a.recycle();
	}

	public int getTypeId() {
		return typeId;
	}

	public boolean isChecked() {
		return mChecked;
	}

	public void setChecked(boolean checked) {
		this.setImageDrawable(checked ? srcChecked : srcNormal);
		this.mChecked = checked;
	}

	public void doClick() {
		setChecked(!mChecked);
	}
}
