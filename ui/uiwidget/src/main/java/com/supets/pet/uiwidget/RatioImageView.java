package com.supets.pet.uiwidget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.facebook.drawee.view.SimpleDraweeView;

public class RatioImageView extends SimpleDraweeView {

	private static final double DEFAULT_RATIO_WIDTH = 1.0;
	private static final double DEFAULT_RATIO_HEIGHT = 1.0;

	private double mRatioWidth = DEFAULT_RATIO_WIDTH;
	private double mRatioHeight = DEFAULT_RATIO_HEIGHT;

	private int mHeightRes;

	public RatioImageView(Context context) {
		this(context, null);
	}

	public RatioImageView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public RatioImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setScaleType(ImageView.ScaleType.CENTER_CROP);
	}

	public void setHeight(int dimenRes) {
		mHeightRes = dimenRes;
	}

	public void setRatio(double ratioWidth, double ratioHeight) {
		mRatioWidth = ratioWidth;
		mRatioHeight = ratioHeight;
		requestLayout();
		invalidate();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int widthSpecMode = View.MeasureSpec.getMode(widthMeasureSpec);

		int widthSize = 0;
		int heightSize = 0;

		if (mHeightRes > 0) { // 根据高度计算
			heightSize = mHeightRes;
			widthSize = (int) (1.0 * mRatioWidth * heightSize / mRatioHeight);
		} else {
			if (widthSpecMode == View.MeasureSpec.EXACTLY) {
				widthSize = View.MeasureSpec.getSize(widthMeasureSpec);
			} else {
				widthSize = getResources().getDisplayMetrics().widthPixels;
			}
			heightSize = (int) (1.0 * mRatioHeight * widthSize / mRatioWidth);
		}
		setMeasuredDimension(widthSize, heightSize);
	}
}
