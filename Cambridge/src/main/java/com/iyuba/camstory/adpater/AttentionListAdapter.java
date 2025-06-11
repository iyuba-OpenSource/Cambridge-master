package com.iyuba.camstory.adpater;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.iyuba.camstory.R;
import com.iyuba.camstory.bean.AttentionBean;
import com.iyuba.camstory.sqlite.mode.Attention;
import com.iyuba.camstory.utils.GitHubImageLoader;


/**
 * 关注适配器
 *
 * @author yq
 * @version 1.0
 */
public class AttentionListAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<AttentionBean.DataBean> mList = new ArrayList<AttentionBean.DataBean>();
    private ViewHolder viewHolder;

    /**
     * @param mContext
     */
    public AttentionListAdapter(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * @param mContext
     * @param mList
     */
    public AttentionListAdapter(Context mContext, ArrayList<AttentionBean.DataBean> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    public void addList(ArrayList<AttentionBean.DataBean> replyList) {
        this.mList = replyList;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        if (position == 0) {
            return 0;
        } else {
            return mList.get(position);
        }
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public void setData(ArrayList<AttentionBean.DataBean> List) {
        this.mList = List;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        final AttentionBean.DataBean curAttention = mList.get(position);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.item_fans, null);
            viewHolder = new ViewHolder();
            viewHolder = new ViewHolder();
            viewHolder.username = convertView
                    .findViewById(R.id.username);
            viewHolder.message = convertView
                    .findViewById(R.id.content);
            viewHolder.userImageView = convertView
                    .findViewById(R.id.pic);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.username.setText(curAttention.getFusername());
        viewHolder.message.setText(curAttention.getDoing());
        GitHubImageLoader.Instace(mContext).setCirclePic(curAttention.getFollowuid(),
                viewHolder.userImageView);
        return convertView;
    }


    public class ViewHolder {
        TextView username;
        TextView message;// 当前状态
        ImageView userImageView;
    }
}
