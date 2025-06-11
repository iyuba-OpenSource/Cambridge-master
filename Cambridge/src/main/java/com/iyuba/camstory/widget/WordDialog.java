package com.iyuba.camstory.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.iyuba.camstory.LoginActivity;
import com.iyuba.camstory.R;
import com.iyuba.camstory.frame.CrashApplication;
import com.iyuba.camstory.listener.DictRequest;
import com.iyuba.camstory.listener.RequestCallBack;
import com.iyuba.camstory.listener.WordUpdateRequest;
import com.iyuba.camstory.manager.AccountManager;
import com.iyuba.camstory.sqlite.mode.Word;
import com.iyuba.camstory.sqlite.op.WordOp;

public class WordDialog extends Dialog {
	private Context mContext;
	LayoutInflater layoutInflater;
	private Button add_word, close_word;
	private ProgressBar progressBar_translate;
	private String selectText;
	private TextView key, pron, def, example;
	private Typeface mFace;
	private Word selectCurrWordTemp;
	private ImageView speaker;

	private void initGetWordMenu() {
		progressBar_translate = findViewById(R.id.progressBar_get_Interperatatior);
		key = findViewById(R.id.word_key);
		pron = findViewById(R.id.word_pron);
		def = findViewById(R.id.word_def);
		example = findViewById(R.id.example);
		/*
		 * mFace = Typeface.createFromAsset(mContext.getAssets(),
		 * "font/SEGOEUI.TTF");
		 */
		speaker = findViewById(R.id.word_speaker);
		add_word = findViewById(R.id.word_add);
		add_word.setOnClickListener(new View.OnClickListener() { // 添加到生词本

					@Override
					public void onClick(View v) {
						saveNewWords(selectCurrWordTemp);
					}
				});
		close_word = findViewById(R.id.word_close);
		close_word.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				WordDialog.this.dismiss();
			}
		});
	}

	/**
	 * 获取单词释义
	 */
	private void getNetworkInterpretation() {
		if (selectText != null && selectText.length() != 0) {
			DictRequest request = new DictRequest(selectText, new RequestCallBack() {

				@Override
				public void requestResult(Request result) {
					DictRequest dictResponse = (DictRequest) result;
					selectCurrWordTemp = dictResponse.word;
					if (selectCurrWordTemp != null) {
						selectCurrWordTemp.userid = AccountManager.getInstance().userId;
					}
					if (selectCurrWordTemp != null) {
						if (selectCurrWordTemp.def != null
								&& selectCurrWordTemp.def.length() != 0) {
							handler.sendEmptyMessage(1);
						} else {
							handler.sendEmptyMessage(2);
						}
					} else {
					}
				}
			});
			CrashApplication.getInstance().getQueue().add(request);
		} else {
			CustomToast.showToast(mContext, R.string.play_please_take_the_word, 1000);
		}
	}

	public void showWordDefInfo() {
		key.setText(selectCurrWordTemp.key);
		// pron.setTypeface(mFace);
		if (selectCurrWordTemp.pron != null
				&& selectCurrWordTemp.pron.length() != 0) {
			pron.setText(Html.fromHtml("[" + selectCurrWordTemp.pron + "]"));
		}
		def.setText(selectCurrWordTemp.def);
		example.setText(Html.fromHtml(selectCurrWordTemp.examples));
		example.setMovementMethod(ScrollingMovementMethod.getInstance());
		example.setText(Html.fromHtml(selectCurrWordTemp.examples));
		if (selectCurrWordTemp.audioUrl != null
				&& selectCurrWordTemp.audioUrl.length() != 0) {
			speaker.setVisibility(View.VISIBLE);
		} else {
			speaker.setVisibility(View.GONE);
		}
		/*speaker.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Player player = new Player(mContext, null, null);
				String url = selectCurrWordTemp.audioUrl;
				player.playUrl(url);
			}
		});*/
		add_word.setVisibility(View.VISIBLE); // 选词的同事隐藏加入生词本功能
		progressBar_translate.setVisibility(View.GONE); // 显示等待
	}

	private void saveNewWords(Word wordTemp) {
		if (!AccountManager.getInstance().checkUserLogin()) {
			Intent intent = new Intent();
			intent.setClass(mContext, LoginActivity.class);
			mContext.startActivity(intent);
		} else {
			try {
				WordOp wo = new WordOp();
				wo.saveData(wordTemp);
				CustomToast.showToast(mContext, R.string.play_ins_new_word_success,
						1000);
				WordDialog.this.dismiss();
				addNetwordWord(wordTemp.key);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void addNetwordWord(String wordTemp) {
		WordUpdateRequest request = new WordUpdateRequest(
				AccountManager.getInstance().userId, WordUpdateRequest.MODE_INSERT,
				wordTemp, new RequestCallBack() {

					@Override
					public void requestResult(Request result) {
						// TODO 自动生成的方法存根
					}
				});
		CrashApplication.getInstance().getQueue().add(request);
	}

	public void searchWord(String word) {
		selectText = word;
		getNetworkInterpretation();
	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				showWordDefInfo();
				break;
			case 2:
				CustomToast.showToast(mContext, R.string.play_no_word_interpretation,
						1000);
				WordDialog.this.dismiss();
				break;
			}
		}
	};

	public WordDialog(Context context, int theme) {
		super(context, theme);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.wordcard);
		mContext = context;
		this.setCancelable(true);
		initGetWordMenu();
	}
}
