package com.iyuba.camstory.widget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

import com.iyuba.camstory.R;
import com.iyuba.camstory.view.NoScrollGridView;
public class ShareDialog {
	private AlertDialog dialog;
	private NoScrollGridView gridView;
	private RelativeLayout cancelButton;
	private SimpleAdapter saImageItems;
	private int[] image = { R.drawable.ssdk_oks_logo_qq,
			R.drawable.ssdk_oks_logo_qzone, R.drawable.ssdk_oks_logo_wechat,
			R.drawable.ssdk_oks_logo_wechatmoments,
			R.drawable.ssdk_oks_logo_wechatfavorite,
			R.drawable.ssdk_oks_logo_sinaweibo };
	private String[] name = { "QQ", "QQ空间", "微信好友", "微信朋友圈", "微信收藏", "新浪微博" };

	public ShareDialog(Context context) {
		dialog = new AlertDialog.Builder(context).create();
		dialog.show();
		Window window = dialog.getWindow();
		window.setContentView(R.layout.share_dialog);
		gridView = window.findViewById(R.id.share_gridView);
		cancelButton = window.findViewById(R.id.share_cancel);
		List<HashMap<String, Object>> shareList = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < image.length; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("ItemImage", image[i]);// 添加图像资源的ID
			map.put("ItemText", name[i]);// 按序号做ItemText
			shareList.add(map);
		}
		saImageItems = new SimpleAdapter(context, shareList,
				R.layout.share_item, new String[] { "ItemImage", "ItemText" },
				new int[] { R.id.imageView1, R.id.textView1 });
		gridView.setAdapter(saImageItems);
	}

	public void setCancelButtonOnClickListener(OnClickListener Listener) {
		cancelButton.setOnClickListener(Listener);
	}

	public void setOnItemClickListener(OnItemClickListener listener) {
		gridView.setOnItemClickListener(listener);
	}

	/**
	 * 关闭对话框
	 */
	public void dismiss() {
		dialog.dismiss();
	}
}