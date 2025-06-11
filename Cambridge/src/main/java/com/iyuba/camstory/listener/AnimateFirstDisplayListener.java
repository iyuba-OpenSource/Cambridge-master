package com.iyuba.camstory.listener;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class AnimateFirstDisplayListener implements ImageLoadingListener {

	static final List<String> displayedImages = Collections
			.synchronizedList(new LinkedList<String>());

	@Override
	public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
		if (loadedImage != null) {
			ImageView imageView = (ImageView) view;
			boolean firstDisplay = !displayedImages.contains(imageUri);
			if (firstDisplay) {
				FadeInBitmapDisplayer.animate(imageView, 600);
				displayedImages.add(imageUri);
			}
		}
	}

	@Override
	public void onLoadingCancelled(String arg0, View arg1) {
		// TODO 自动生成的方法存根

	}

	@Override
	public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
		// TODO 自动生成的方法存根

	}

	@Override
	public void onLoadingStarted(String arg0, View arg1) {
		// TODO 自动生成的方法存根

	}
}
