package com.iyuba.camstory.view;



import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.iyuba.camstory.R;
import com.iyuba.camstory.utils.DensityUtil;

public class TestNumberImageView extends ImageView {
	private int mTestNumber, mRightRate;
	private int mRanking;

	public TestNumberImageView(Context context) {
		super(context);
	}

	public TestNumberImageView(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		setAttrs(context, attrs);
	}

	public TestNumberImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setAttrs(context, attrs);
	}

	private void setAttrs(Context context, AttributeSet attrs) {
		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.TestNumberImageView);
		mTestNumber = a.getInt(
				R.styleable.TestNumberImageView_testimageview_testnumber, 0);
		mRightRate = a.getInt(
				R.styleable.TestNumberImageView_testimageview_rightrate, 0);
		a.recycle();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		int width, heigh;
		super.onDraw(canvas);
		Drawable drawable = getDrawable();
		if (drawable == null) {
			return;
		}
		if ((width = getWidth()) == 0 || (heigh = getHeight()) == 0) {
			return;
		}
		this.measure(0, 0);
		if (drawable.getClass() == NinePatchDrawable.class)
			return;
		Paint paint = new Paint();
		paint.setColor(0xEEF05C91);
		paint.setAntiAlias(true);
		paint.setTypeface(Typeface.DEFAULT_BOLD);
		paint.setTextSize(DensityUtil.dip2px(getContext(), 24));
		canvas.drawText(mTestNumber + "", (int) (width * 0.7237),
				(int) (heigh * 0.33), paint);
		canvas.drawText(mRightRate + "%", (int) (width * 0.7237),
				(int) (heigh * 0.68), paint);
		paint.setTextSize(DensityUtil.dip2px(getContext(), 28));
		canvas.drawText(mRanking + "", (int) (width * 0.6182),
				(int) (heigh * 0.97), paint);
	}

	public void setData(int testNumber, int rightRate, int ranking) {
		mTestNumber = testNumber;
		mRightRate = rightRate;
		mRanking = ranking;
		invalidate();
	}

}
