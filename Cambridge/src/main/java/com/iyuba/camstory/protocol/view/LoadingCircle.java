package com.iyuba.camstory.protocol.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.iyuba.camstory.R;


/**
 * To show a loading circle as a view
 * the color of start point is get as a attribute
 * the color of end point is hard coded to white(0xffffff) now
 * but it could also be changed to a attribute or given by a setter
 *
 */
public class LoadingCircle extends View {
	private int mCircleColor;
	private int mCircleWidth;
	private int mRadius;
	private int mEndColor = 0xffffff;
	
	private int mWidth;
	private int mHeight;
	
	private Paint mPaint;

	public LoadingCircle(Context context) {
		this(context, null);
	}

	public LoadingCircle(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public LoadingCircle(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);

		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LoadingCircle);
		mCircleColor = a.getColor(R.styleable.LoadingCircle_lcborder_color, Color.BLUE);
		mCircleWidth = a.getDimensionPixelSize(R.styleable.LoadingCircle_lcborder_width,
				(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
						.getDisplayMetrics()));
		mRadius = a.getDimensionPixelSize(R.styleable.LoadingCircle_lcradius, (int) TypedValue
				.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics()));
		a.recycle();
		
		mPaint = new Paint();
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);
		if(widthMode == MeasureSpec.EXACTLY){
			mWidth = widthSize;
		} else {
			mWidth = (mRadius + mCircleWidth) * 2 + getPaddingLeft() + getPaddingRight();
		}
		if(heightMode == MeasureSpec.EXACTLY){
			mHeight = heightSize;
		} else {
			mHeight = (mRadius + mCircleWidth) * 2 + getPaddingTop() + getPaddingBottom();
		}
		setMeasuredDimension(mWidth, mHeight);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		int center = getWidth() / 2;
		Shader s = new SweepGradient(center, center, mCircleColor, mEndColor);
		mPaint.setAntiAlias(true);
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeWidth(mCircleWidth);
		mPaint.setShader(s);
		canvas.drawCircle(center, center, mRadius, mPaint);
	}

}
