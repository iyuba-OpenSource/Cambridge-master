package com.iyuba.camstory.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import com.iyuba.camstory.R;
import com.iyuba.camstory.utils.DensityUtil;


public class CustomTimeImageView extends ImageView {
	private static final String TAG = CustomTimeImageView.class.getSimpleName();

	// 显示的学习时间
	private int mHour, mMinute, mSecond;
	// 显示的学习时间排名和百分比
	private int mRate, mPercentage;

	public CustomTimeImageView(Context context) {
		super(context);
	}

	public CustomTimeImageView(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		setAttrs(context, attrs);
	}

	public CustomTimeImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setAttrs(context, attrs);
	}

	private void setAttrs(Context context, AttributeSet attrs) {
		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.CustomtimeImageView);
		mHour = a.getInt(R.styleable.CustomtimeImageView_customtime_hour, 0);
		mMinute = a.getInt(R.styleable.CustomtimeImageView_customtime_minute, 0);
		mSecond = a.getInt(R.styleable.CustomtimeImageView_customtime_second, 0);
		a.recycle();
		Log.e(TAG, "H: " + mHour + " M: " + mMinute + " S:" + mSecond);
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
		// 写时间信息
		Paint paint = new Paint();
		paint.setColor(0xEEFD5460);
		paint.setAntiAlias(true);
		paint.setTypeface(Typeface.DEFAULT_BOLD);
		paint.setTextSize(DensityUtil.dip2px(getContext(), 20));
		if (mHour % 1000 != 0) {
			canvas.drawText(mHour + "", (int) (width * 0.37), (int) (heigh * 0.2989),
					paint);
		} else if (mHour % 100 != 0) {
			canvas.drawText(mHour + "", (int) (width * 0.385),
					(int) (heigh * 0.2989), paint);
		} else {
			canvas.drawText(mHour + "", (int) (width * 0.4), (int) (heigh * 0.2989),
					paint);
		}
		canvas.drawText(mMinute + "", (int) (width * 0.6053),
				(int) (heigh * 0.2989), paint);
		canvas.drawText(mSecond + "", (int) (width * 0.8), (int) (heigh * 0.2989),
				paint);
		// 写排名信息
		paint.setColor(0xEE07BDAA);
		paint.setTextSize(DensityUtil.dip2px(getContext(), 24));
		canvas.drawText(mRate + "", (int) (width * 0.1974), (int) (heigh * 0.9655),
				paint);
		paint.setTextSize(DensityUtil.dip2px(getContext(), 20));
		canvas.drawText(mPercentage + "%", (int) (width * 0.58),
				(int) (heigh * 0.9655), paint);
	}

	public void setData(int hour, int minute, int second, int ranking,
			int percentage) {
		mHour = hour;
		mMinute = minute;
		mSecond = second;
		mRate = ranking;
		mPercentage = percentage;
		invalidate();
	}

}
