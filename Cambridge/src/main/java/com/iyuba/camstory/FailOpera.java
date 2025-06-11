package com.iyuba.camstory;

import java.io.File;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class FailOpera {
	private static FailOpera instance;

	private FailOpera() {
	}

	public static synchronized FailOpera getInstance(Context context) {
		if (instance == null) {
			instance = new FailOpera();
		}
		return instance;
	}

	public void openFile(Context context, String filePath) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(new File(filePath)),
				"application/vnd.android.package-archive");
		context.startActivity(intent);
	}
}
