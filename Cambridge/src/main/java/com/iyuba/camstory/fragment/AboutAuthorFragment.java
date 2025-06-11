package com.iyuba.camstory.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.iyuba.camstory.BookDetailActivity;
import com.iyuba.camstory.R;
import com.iyuba.camstory.bean.BookDetailResponse;
import com.iyuba.camstory.bean.BookListResponse;
import com.iyuba.camstory.manager.StoryDataManager;
import com.umeng.analytics.MobclickAgent;

public class AboutAuthorFragment extends Fragment {
	private TextView aboutAuthor;
	private View info;
	private Context mContext;
	private boolean refresh = false;
	private BookListResponse.BookInfo bookInfo;

	public AboutAuthorFragment(BookListResponse.BookInfo bookInfo) {
		this.bookInfo = bookInfo;
	}

	@Override
	public void onAttach(Activity paramActivity) {
		this.mContext = paramActivity;
		super.onAttach(paramActivity);
	}

	@Override
	public View onCreateView(LayoutInflater paramLayoutInflater,
			ViewGroup paramViewGroup, Bundle paramBundle) {
		this.info = paramLayoutInflater.inflate(R.layout.fragment_aboutauthor,
				paramViewGroup, false);
		initView(this.info);
		return this.info;
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("AboutAuthorFragment");
	}

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("AboutAuthorFragment");
	}

	@Override
	public void setUserVisibleHint(boolean paramBoolean) {
		super.setUserVisibleHint(paramBoolean);
		if ((paramBoolean) && (this.refresh) && (this.info != null)) {
			initView(this.info);
			this.refresh = false;
		}
	}

	private void initView(View paramView) {
		this.aboutAuthor = paramView
				.findViewById(R.id.aboutauthor_textview);
		this.aboutAuthor.setText(bookInfo.getAuthor()
				+","
				+ bookInfo.getAbout_author() + "\n"
				+ bookInfo.getInterpreter()
				+","
				+ bookInfo.getAbout_interpreter());
	}
}
