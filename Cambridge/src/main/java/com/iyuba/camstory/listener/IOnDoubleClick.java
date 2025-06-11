package com.iyuba.camstory.listener;

import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;


/**
 * Created by 10202 on 2015/12/21.
 */
public class IOnDoubleClick implements View.OnTouchListener {
	private int count;
	private long firClick, secClick;
	private String failMsg;
	Handler handler = new Handler(new Handler.Callback() {
		@Override
		public boolean handleMessage(Message msg) {
			switch (msg.what) {
				case 0:
					if (count == 1) {
						count = 0;
						firClick = 0;
						secClick = 0;
					}
					break;
			}
			return false;
		}
	});
	private IOnClickListener onClickListener;

	public IOnDoubleClick(IOnClickListener onClickListener, String msg) {
		this.onClickListener = onClickListener;
		this.failMsg = msg;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (MotionEvent.ACTION_DOWN == event.getAction()) {
			count++;
			if (count == 1) {
				firClick = System.currentTimeMillis();
				handler.sendEmptyMessageDelayed(0, 600);
			} else if (count == 2) {
				secClick = System.currentTimeMillis();
				if (secClick - firClick < 590) {
					onClickListener.onClick(v, "click");
				}
				count = 0;
				firClick = 0;
				secClick = 0;
			}
		}
		return true;
	}
}
