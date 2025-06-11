package com.iyuba.camstory.adpater;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.iyuba.camstory.R;
import com.iyuba.camstory.bean.AttentionBean;
import com.iyuba.camstory.bean.FansBean;
import com.iyuba.camstory.utils.GitHubImageLoader;

import java.util.ArrayList;


/**
 * 粉丝适配器
 *
 * @author yq
 * @version 1.0
 */
public class FansListAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<FansBean.DataBean> mList = new ArrayList<FansBean.DataBean>();
    private ViewHolder viewHolder;

    /**
     * @param mContext
     */
    public FansListAdapter(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * @param mContext
     * @param mList
     */
    public FansListAdapter(Context mContext, ArrayList<FansBean.DataBean> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    public void addList(ArrayList<FansBean.DataBean> replyList) {
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

    public void setData(ArrayList<FansBean.DataBean> List) {
        this.mList = List;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        final FansBean.DataBean curAttention = mList.get(position);
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
        viewHolder.username.setText(curAttention.getUsername());
        viewHolder.message.setText(curAttention.getDoing());
        GitHubImageLoader.Instace(mContext).setCirclePic(curAttention.getUid(),
                viewHolder.userImageView);
        return convertView;
    }


    public class ViewHolder {
        TextView username;
        TextView message;// 当前状态
        ImageView userImageView;
    }
}
