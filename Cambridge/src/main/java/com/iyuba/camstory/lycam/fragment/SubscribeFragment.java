package com.iyuba.camstory.lycam.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
/*
import com.iyuba.live.R;
import com.iyuba.live.bean.IyuStreamInfo;
import com.iyuba.live.lycam.bean.StreamInfoModel;
import com.iyuba.live.util.TimeUtils;*/

import com.iyuba.camstory.R;
import com.iyuba.camstory.bean.living.IyuStreamInfo;

//import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lycamandroid on 16/7/22.
 * 订阅
 */
public class SubscribeFragment extends BaseFragment {

    private static final String KEY_MODEL = "model";
/*
    @BindView(R.id.tv_subscribe)
    TextView tvSubscribe;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_longtime)
    TextView tvLongTime;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.tv_description)
    TextView tvDescription;*/

    private IyuStreamInfo mModel;
    private TextView tvSubscribe;
    private TextView tvTime;
    private TextView tvLongTime;
    private TextView tvPrice;
    private TextView tvDescription;

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_subscribe,container,false);
        ButterKnife.bind(this,view);
        mModel = getArguments().getParcelable(KEY_MODEL);
        findView(view);
        init();
        return view;
    }

    private void findView(View view) {
        tvSubscribe = view.findViewById(R.id.tv_subscribe);
        tvTime = view.findViewById(R.id.tv_time);
        tvLongTime = view.findViewById(R.id.tv_longtime);
        tvPrice = view.findViewById(R.id.tv_price);
        tvDescription = view.findViewById(R.id.tv_description);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void init() {
//        if(mModel.isSubscribe()){
//            tvSubscribe.setText(R.string.subscribed);
//        }else{
//            tvSubscribe.setText(R.string.subscribe);
//        }
//        tvTime.setText(TimeUtils.getInstance().stampToStrByFormat(mModel.getTimeStamp(),new SimpleDateFormat( "yyyy年MM月dd日 HH:mm")));
        tvTime.setText(mModel.getStartTime());
        tvLongTime.setText(mModel.getDuration());
        tvPrice.setText(getString(R.string.much_balance,mModel.getCharge()));
        tvDescription.setText(mModel.getDescription());
    }

    public static SubscribeFragment newInstance(IyuStreamInfo model){
        SubscribeFragment fragment = new SubscribeFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_MODEL,model);
        fragment.setArguments(bundle);
        return fragment;
    }
}
