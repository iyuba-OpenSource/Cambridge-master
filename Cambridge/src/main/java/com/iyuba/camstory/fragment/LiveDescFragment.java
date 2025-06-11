package com.iyuba.camstory.fragment;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.iyuba.camstory.R;
import com.iyuba.camstory.WebActivity;
import com.iyuba.camstory.listener.ILiveDescListener;
import com.iyuba.camstory.lycam.manager.ConstantManager;
import com.iyuba.configation.Constant;

/**
 * 作者：renzhy on 16/7/21 19:34
 * 邮箱：renzhongyigoo@gmail.com
 */
public class LiveDescFragment extends Fragment implements ILiveDescListener,View.OnClickListener {

	/*@BindView(R.id.tv_live_content_desc)
	TextView tvContentDesc;
	@BindView(R.id.tv_live_teacher_desc)
	TextView tvTeacherDesc;
	@BindView(R.id.tv_live_suitable_people)
	TextView tvSuitablePeople;
	@BindView(R.id.ll_course_normal_problem)
	View mViewNormalProblem;
	@BindView(R.id.ll_course_qq_consulation)
	View mViewQQConsulation;*/

	private Context mContext;
	private String mContentDesc = null;
	private String mTeacherDesc = null;
	private String mSuitablePeople = null;
	private TextView tvContentDesc;
	private TextView tvTeacherDesc;
	private TextView tvSuitablePeople;
	private View mViewNormalProblem;
	private View mViewQQConsulation;

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
//		((LiveContentActivity)getActivity()).setFragmentListener(this);

	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getActivity();
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_course_desc,container,false);
		findView(view);
		//ButterKnife.bind(this,view);
		return view;
	}

	private void findView(View view) {
		tvContentDesc = view.findViewById(R.id.tv_live_content_desc);
		tvTeacherDesc = view.findViewById(R.id.tv_live_teacher_desc);
		tvSuitablePeople = view.findViewById(R.id.tv_live_suitable_people);
		mViewNormalProblem = view.findViewById(R.id.ll_course_normal_problem);
		mViewQQConsulation = view.findViewById(R.id.ll_course_qq_consulation);

	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initWidget();
	}

	public void initWidget(){
		if(mContentDesc != null){
			tvContentDesc.setText(mContentDesc);
		}
		if(mTeacherDesc != null){
			tvTeacherDesc.setText(mTeacherDesc);
		}
		if(mSuitablePeople != null){
			tvSuitablePeople.setText(mSuitablePeople);
		}
		mViewNormalProblem.setOnClickListener(this);
		mViewQQConsulation.setOnClickListener(this);
	}

	public void setContentDesc(String mContentDesc) {
		this.mContentDesc = mContentDesc;
	}

	public void setTeacherDesc(String mTeacherDesc) {
		this.mTeacherDesc = mTeacherDesc;
	}

	public void setSuitablePeople(String mSuitablePeople) {
		this.mSuitablePeople = mSuitablePeople;
	}

	@Override
	public void onFragmentDescUpdate(String contentDesc, String teacherDesc, String suitablePeople) {
		if(contentDesc != null && tvContentDesc != null){
			tvContentDesc.setText(contentDesc);
		}
		if(teacherDesc != null && tvTeacherDesc != null){
			tvTeacherDesc.setText(teacherDesc);
		}
		if(suitablePeople != null && tvSuitablePeople != null){
			tvSuitablePeople.setText(suitablePeople);
		}
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()){
			case R.id.ll_course_normal_problem:
				Intent intent = new Intent();
				intent.setClass(mContext, WebActivity.class);
				intent.putExtra(
						"url",
						"http://www."+Constant.IYBHttpHead+"/cq.jsp");
				intent.putExtra("title", "常见问题");
				startActivity(intent);
				break;
			case R.id.ll_course_qq_consulation:
				String url = "mqqwpa://im/chat?chat_type=wpa&uin="+ ConstantManager.QQ_CONSULT+"&version=1";
				try {
					startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
				} catch (ActivityNotFoundException e) {
					// TODO Auto-generated catch block
					Toast.makeText(mContext, "您的设备尚未安装QQ客户端", Toast.LENGTH_SHORT).show();
					e.printStackTrace();
				}
				break;
		}
	}
}
