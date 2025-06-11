package com.iyuba.camstory.lycam.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;


import com.iyuba.camstory.R;

import tv.lycam.gift.adapter.PublicMessageAdapter;


/**
 * Created by lycamandroid on 16/7/18.
 * 消息
 */
public class MessageListFragment extends BaseFragment {

    ListView listMessage;

    protected PublicMessageAdapter mMsgAdapter;

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_messagelist,container,false);
        listMessage = view.findViewById(R.id.list_message);
        mMsgAdapter = new PublicMessageAdapter(context, null);
        listMessage.setAdapter(mMsgAdapter);
        return view;
    }
    //添加数据
    public void addString(String text){
        mMsgAdapter.addString(text);
    }

    public void addData(String data){
        mMsgAdapter.addData(data, new PublicMessageAdapter.OnMessageDataChanged() {
            @Override
            public void onChanged() {
                //滑动到最后一条数据处
                listMessage.smoothScrollToPosition(mMsgAdapter.getCount() - 1);
            }
        });
    }
    //获取实例
    public static MessageListFragment newInstance() {
        MessageListFragment fragment = new MessageListFragment();
        return fragment;
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }
}
