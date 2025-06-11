package com.iyuba.camstory.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;

import androidx.appcompat.widget.AppCompatTextView;


/**
 * Created by lee on 2018/8/13.
 */

public class CustomTextView  extends AppCompatTextView {
    // Attributes
    private Paint testPaint;
    private float cTextSize;
    private Rect rect;

    public CustomTextView (Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Re size the font so the specified text fits in the text box * assuming
     * the text box is the specified width.
     * 在此方法中学习到：getTextSize返回值是以像素(px)为单位的，而setTextSize()是以sp为单位的，
     * 因此要这样设置setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
     */
    private void refitText(String text, int textWidth) {
        if (textWidth > 0) {
            testPaint = new Paint();
            rect = new Rect();
            testPaint.set(this.getPaint());
            //获得当前TextView这个控件的有效宽度
            int availableWidth = textWidth - this.getPaddingLeft() - this.getPaddingRight();
            //所有字符串所占像素宽度
            int textWidths = rect.width();

            float[] widths = new float[text.length()];

            testPaint.getTextBounds(text, 0, text.length(), rect);
            cTextSize = this.getTextSize();//这个返回的单位为px
            while(textWidths > availableWidth){
                cTextSize = cTextSize - 1;
                testPaint.setTextSize(cTextSize);//这里传入的单位是px
                textWidths = testPaint.getTextWidths(text, widths);
            }
            this.setTextSize(TypedValue.COMPLEX_UNIT_PX, cTextSize);//这里制定传入的单位是px
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        refitText(getText().toString(), this.getWidth());
    }
}
