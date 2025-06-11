package com.iyuba.camstory.protocol.view.drawable;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.Log;

public class SwitchTextDrawable extends Drawable{
private static final String TAG = SwitchTextDrawable.class.getSimpleName();
	
	public static final int MODE_ENG = 1;
	public static final int MODE_CHN = 2;
	private static final String chnStr = "双";
	private static final String engStr = "英" ;
	
	private int mDrawableMode;
	private int mLength;
	
	private Paint mPaint;
	private Paint mDividerPaint;
	Path mDivider;
	private String upstr;
	private String downstr;
	private Rect upRect;
	private Rect downRect;
	
	public SwitchTextDrawable(int length){
		this.mLength = length;
		setDrawableMode(MODE_ENG);
		
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setColor(0xffffffff);
		
		mDividerPaint = new Paint();
		mDividerPaint.setAntiAlias(true);
		mDividerPaint.setColor(0xffffffff);
		mDividerPaint.setStyle(Paint.Style.FILL_AND_STROKE);
		mDividerPaint.setStrokeWidth(3.0f);
		
	}

	@Override
	public void draw(Canvas canvas) {
		upRect = new Rect();
		float upsize = (mLength * 5.0f) / 16;
		mPaint.setTextSize(upsize);
		mPaint.getTextBounds(upstr, 0, upstr.length(), upRect);
		Log.e(TAG, "upRect.width : " + upRect.width() + " upRect.height : " + upRect.height());
		canvas.drawText(upstr, (mLength * 1.0f) / 8, (mLength * 4.0f) / 8, mPaint);

		mDivider = new Path();
		mDivider.moveTo((mLength * 6.0f) / 16 , (mLength * 14.0f) / 16);
		mDivider.lineTo((mLength * 14.0f) / 16, (mLength * 6.0f) / 16);
		mDivider.close();
		canvas.drawPath(mDivider, mDividerPaint);
		
		downRect = new Rect();
		float downsize = (mLength * 3.0f) / 16;
		mPaint.setTextSize(downsize);
		mPaint.getTextBounds(downstr, 0, downstr.length(), downRect);
		Log.e(TAG, "downRect.width : " + downRect.width() + " downRect.height : " + downRect.height());
		canvas.drawText(downstr, (mLength * 5.0f) / 8, (mLength * 7.0f) / 8, mPaint);
		
	}

	@Override
	public void setAlpha(int alpha) {
		mPaint.setAlpha(alpha);
	}

	@Override
	public void setColorFilter(ColorFilter colorFilter) {
		mPaint.setColorFilter(colorFilter);
	}

	@Override
	public int getOpacity() {
		return PixelFormat.TRANSLUCENT;
	}
	
	public void setDrawableMode(int mode){
		this.mDrawableMode = mode;
		switch (mDrawableMode) {
		case MODE_ENG:
			this.upstr = engStr;
			this.downstr = chnStr;
			break;
		case MODE_CHN:
			this.upstr = chnStr;
			this.downstr = engStr;
			break;
		default:
			this.mDrawableMode = MODE_ENG;
			this.upstr = engStr;
			this.downstr = chnStr;
			break;
		}
	}
	
	public void switchmode(){
		if(mDrawableMode == MODE_CHN){
			setDrawableMode(MODE_ENG);
		} else {
			setDrawableMode(MODE_CHN);
		}
	}

}
