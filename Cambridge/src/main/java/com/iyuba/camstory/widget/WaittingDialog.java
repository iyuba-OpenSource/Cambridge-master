package com.iyuba.camstory.widget;

import java.util.Random;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.iyuba.camstory.R;

/**
 * 自定义等待窗口
 *
 * @author 李京蔚
 *
 */
public class WaittingDialog  {
	private TextView mTextView;
	private String topString ;
	private String hint[];
	//private AdView adView;

	public WaittingDialog() {

	}

	/**
	 * 等待窗口
	 */
	public CustomDialog wettingDialog(Context context) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View layout = inflater.inflate(R.layout.wetting, null);
		topString=context.getResources().getString(R.string.hint_top);
		hint=context.getResources().getStringArray(R.array.hint);
		mTextView = layout.findViewById(R.id.hint);
		mTextView.setText(getText());
		CustomDialog.Builder customBuilder = new CustomDialog.Builder(context);
		CustomDialog cDialog = customBuilder.setContentView(layout).create();
		return cDialog;
	}

	private String getText() {
		int i = new Random().nextInt(100) % hint.length;
		return topString + hint[i];
	}
}
