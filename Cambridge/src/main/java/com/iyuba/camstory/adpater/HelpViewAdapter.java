package com.iyuba.camstory.adpater;

import java.util.List;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.view.View;
import android.widget.ImageView;

import com.iyuba.camstory.R;
import com.iyuba.camstory.frame.CrashApplication;

public class HelpViewAdapter extends AbstractViewPagerAdapter<Integer> {
	private Options options = new Options();

	public HelpViewAdapter(List<Integer> data) {
		super(data);
		options.inPreferredConfig = Bitmap.Config.RGB_565;
		options.inPurgeable = true;
		options.inInputShareable = true;
	}

	@Override
	public View newView(int position) {
		View view = View.inflate(CrashApplication.getInstance(),
				R.layout.intro_pic1, null);
		ImageView imageview = view.findViewById(R.id.intro_pic);
		Bitmap bm = BitmapFactory.decodeResource(CrashApplication.getInstance()
				.getResources(), getItem(position), options);
		imageview.setImageBitmap(bm);
		return view;
	}

}
