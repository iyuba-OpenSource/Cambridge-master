package com.iyuba.camstory.adpater;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.iyuba.camstory.R;
import com.iyuba.camstory.bean.CommentList;
import com.iyuba.camstory.manager.AccountManager;
import com.iyuba.camstory.protocol.AgreeAgainstRequest;
import com.iyuba.camstory.protocol.AgreeAgainstResponse;
import com.iyuba.camstory.protocol.dao.CommentAgreeDao;
import com.iyuba.camstory.utils.GitHubImageLoader;
import com.iyuba.camstory.utils.LogUtils;
import com.iyuba.camstory.widget.CustomToast;
import com.iyuba.camstory.widget.OnPlayStateChangedListener;
import com.iyuba.camstory.widget.Player;
import com.iyuba.configation.ConfigManagerHead;
import com.iyuba.configation.Constant;
import com.iyuba.http.BaseHttpRequest;
import com.iyuba.http.BaseHttpResponse;
import com.iyuba.http.ClientSession;
import com.iyuba.http.IResponseReceiver;

import java.util.ArrayList;

//import com.iyuba.abilitytest.widget.OnPlayStateChangedListener;
//import com.iyuba.abilitytest.widget.Player;

public class CommentAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private ArrayList<CommentList> mList = new ArrayList<CommentList>();
    public Player mediaPlayer;



    public CommentAdapter(Context context) {
        mContext = context;
    }

    public void setDate(ArrayList<CommentList> list) {
        mList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater mInflater = LayoutInflater.from(mContext);

        View view = mInflater.inflate(R.layout.item_comment, viewGroup, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof CommentViewHolder) {
            CommentList commentBean = mList.get(position);
            ((CommentViewHolder) viewHolder).setData(position, commentBean);
            ((CommentViewHolder) viewHolder).setListener(position, mList);
        }

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder {

        private ImageView image;// 头像图片
        private ImageView agreeView;// 点赞按钮
        private ImageView againstView;// 点踩按钮
        private TextView agreeText; // 多少赞
        private TextView againstText; // 多少踩
        private ImageView comment_body_voice_icon;// 显示正在播放的
        private TextView name; // 用户名
        private TextView time; // 发布时间
        private TextView body; // 评论体
        private Button reply; // 回复按钮
        private ImageView deleteBox;
        private TextView tvVoiceStatus;
        private TextView mSenScore, mSenId;
        private ImageView tempVoice;


        public CommentViewHolder(View itemView) {
            super(itemView);

            image = (ImageView) itemView.findViewById(R.id.comment_image);
            body = (TextView) itemView.findViewById(R.id.comment_letter_view);
            name = (TextView) itemView.findViewById(R.id.comment_name);
            time = (TextView) itemView.findViewById(R.id.comment_time);
            comment_body_voice_icon = (ImageView) itemView.findViewById(R.id.comment_body_voice_icon);// 显示正在播放的
            //reply = (Button) itemView.findViewById(R.id.reply);
            deleteBox = (ImageView) itemView.findViewById(R.id.checkBox_isDelete);
            agreeText = (TextView) itemView.findViewById(R.id.agree_text);
            againstText = (TextView) itemView.findViewById(R.id.against_text);
            agreeView = (ImageView) itemView.findViewById(R.id.agree);
            againstView = (ImageView) itemView.findViewById(R.id.against);
            tvVoiceStatus = itemView.findViewById(R.id.tv_voice_statues);
            mSenId = itemView.findViewById(R.id.tv_sentence);
            mSenScore = itemView.findViewById(R.id.tv_score);
        }

        public void setData(int position, CommentList curItem) {
            LogUtils.e("CommentListAdapte 列表类型" + curItem.shuoshuoType);
            int type = curItem.shuoshuoType;
            if (type == 2) {
                Log.e("类型type", type + "");
                tvVoiceStatus.setVisibility(View.VISIBLE);
                if (curItem.shuoshuoType == 2) {
                    tvVoiceStatus.setText("单句");
                    mSenId.setVisibility(View.VISIBLE);
                } else if (curItem.shuoshuoType == 4) {
                    mSenId.setVisibility(View.GONE);
                    tvVoiceStatus.setText("多句合成");
                }
            } else {
                tvVoiceStatus.setVisibility(View.GONE);
            }
            //第几句
            if (curItem.indexId != null && !curItem.indexId.equals("")) {
                int index = Integer.valueOf(curItem.indexId) + 1;
                mSenId.setText("第" + index + "句");
                mSenId.setVisibility(View.VISIBLE);
            } else {
                mSenId.setVisibility(View.GONE);
            }

            //得分
            if (curItem.score != null && !curItem.score.equals("")) {
                mSenScore.setText(curItem.score + "分");
                mSenScore.setVisibility(View.VISIBLE);
            } else {
                mSenScore.setVisibility(View.GONE);
            }

            //刷新播放动画
            if (curItem.isPlay) {
                ((AnimationDrawable) comment_body_voice_icon.getDrawable()).start();
            }else {
                AnimationDrawable animation = (AnimationDrawable) comment_body_voice_icon.getDrawable();
                animation.stop();
                animation.selectDrawable(0);
            }
            String uid="";
            try {
                uid = ConfigManagerHead.Instance().loadString("userId");
            }catch (Exception e){
                e.printStackTrace();
            }

            if (checkAgree(curItem.id, uid) == 1) {
                agreeView.setBackgroundResource(R.drawable.agree_press);
            } else if (checkAgree(curItem.id, uid) == 2) {
                againstView.setBackgroundResource(R.drawable.against_press);
            } else {
                agreeView.setBackgroundResource(R.drawable.agree);
                againstView.setBackgroundResource(R.drawable.against);
            }
            // 点赞部分
            agreeText.setText(String.valueOf(curItem.agreeCount));
            againstText.setText(String.valueOf(curItem.againstCount));

            if (curItem.username != null && !"none".equals(curItem.username) && !"".equals(curItem.username) && !"null".equals(curItem.username))
                name.setText(curItem.username);
            else
                name.setText(curItem.userId);
            time.setText(curItem.createdate);
            body.setText(curItem.shuoshuo);

            GitHubImageLoader.Instace(mContext).setPic(mList.get(position).userId, image);

            if (curItem.shuoshuoType == 0) {
                comment_body_voice_icon.setVisibility(View.INVISIBLE);
                body.setVisibility(View.VISIBLE);
            } else {
                comment_body_voice_icon.setVisibility(View.VISIBLE);
                body.setVisibility(View.INVISIBLE);
            }
        }

        public void setListener(final int position, final ArrayList<CommentList> list) {
            final CommentList curItem =list.get(position);
            final String uid = String.valueOf(AccountManager.getInstance().userId);
            final int type = curItem.shuoshuoType;
            //点赞点击事件
            agreeView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    if (checkAgree(curItem.id, uid) == 0) {
                        ClientSession.getInstance().asynGetResponse(
                                new AgreeAgainstRequest("61001", curItem.id, type),
                                new IResponseReceiver() {
                                    @Override
                                    public void onResponse(
                                            BaseHttpResponse response,
                                            BaseHttpRequest request, int rspCookie) {
                                        AgreeAgainstResponse rs = (AgreeAgainstResponse) response;
                                        if (rs.result.equals("001")) {
                                            handler.sendEmptyMessage(4);
                                            new CommentAgreeDao(mContext).saveData(
                                                    curItem.id, uid, "agree");
                                            mList.get(position).agreeCount = mList
                                                    .get(position) == null ? null
                                                    : (++mList.get(position).agreeCount);
                                            handler.sendEmptyMessage(0);
                                        } else if (rs.result.equals("000")) {
                                            handler.sendEmptyMessage(2);
                                        }
                                    }
                                });
                    } else {
                        handler.sendEmptyMessage(3);
                    }
                }
            });
            //反对
            againstView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    if (checkAgree(curItem.id, uid) == 0) {
                        ClientSession.getInstance().asynGetResponse(
                                new AgreeAgainstRequest("61002", curItem.id, type),
                                new IResponseReceiver() {
                                    @Override
                                    public void onResponse(
                                            BaseHttpResponse response,
                                            BaseHttpRequest request, int rspCookie) {
                                        AgreeAgainstResponse rs = (AgreeAgainstResponse) response;
                                        if (rs.result.equals("001")) {
                                            handler.sendEmptyMessage(5);
                                            new CommentAgreeDao(mContext).saveData(
                                                    curItem.id, uid, "against");
                                            mList.get(position).againstCount = mList
                                                    .get(position) == null ? null
                                                    : (++mList.get(position).againstCount);
                                            handler.sendEmptyMessage(0);
                                        } else if (rs.result.equals("000")) {
                                            handler.sendEmptyMessage(2);
                                        }
                                    }
                                });
                    } else {
                        handler.sendEmptyMessage(3);
                    }
                }
            });
            //播放录音
            comment_body_voice_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mediaPlayer!=null&&mediaPlayer.isPlaying()){
                        mediaPlayer.pause();
                    }
                    if (list.get(position).isPlay){
                        AnimationDrawable animation = (AnimationDrawable) comment_body_voice_icon.getDrawable();
                        animation.stop();
                        animation.selectDrawable(0);
                        list.get(position).isPlay=false;
                        return;
                    }

                    playVoice("http://voa." + Constant.IYBHttpHead + "/voa/" + curItem.shuoshuo);


                    for (int i = 0; i < list.size(); i++) {
                        if (i == position) {
                            list.get(i).isPlay = true;
                        } else {
                            list.get(i).isPlay = false;
                        }
                    }
                    LogUtils.e("播放的是" + position);
                    notifyDataSetChanged();
                }
            });

            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {//没有个人中心
//                    if (AccountManager.getInstance().checkUserLogin()) {
//                        Intent intent = new Intent();
//                        SocialDataManager.getInstance().userInfo.uid = Integer.valueOf(curItem.userId);
//                        intent.setClass(mContext, PersonalHome.class);
//                        mContext.startActivity(intent);
//                    } else {
//                        Intent intent = new Intent();
//                        intent.setClass(mContext, LoginActivity.class);
//                        mContext.startActivity(intent);
//                    }
                }
            });
        }

        // 播放语音
        private void playVoice(String url) {

            if (mediaPlayer == null) {
                mediaPlayer = new Player(mContext,mListener);
            } else {
                stopVoice();
            }
            mediaPlayer.playUrl(url);
            mediaPlayer.mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    AnimationDrawable animation = (AnimationDrawable) comment_body_voice_icon.getDrawable();
                    animation.stop();
                    animation.selectDrawable(0);
                }
            });
        }

        private OnPlayStateChangedListener mListener =new OnPlayStateChangedListener() {

            @Override
            public void playFaild() {
                LogUtils.e("播放失败");
            }

            @Override
            public void playCompletion() { //播放完毕
                for (CommentList comment : mList) {
                    comment.isPlay = false;
                }
            }
        };


        // 播放语音评论之前先在这里reset播放器
        private void stopVoice() {
            if (mediaPlayer != null) {
                mediaPlayer.reset();
            }
        }

        private int checkAgree(String commentId, String uid) {
            return new CommentAgreeDao(mContext).findDataByAll(commentId, uid); //数据库
        }

        @SuppressLint("HandlerLeak")
        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                // TODO Auto-generated method stub
                super.handleMessage(msg);
                switch (msg.what) {
                    case 0:
                        notifyDataSetChanged();
                        break;
                    case 1:
                       //fuck the Animation
                        break;
                    case 2:
                        CustomToast.showToast(mContext, "网络异常或服务器忙，请稍后重试", 1000);
                        break;
                    case 3:
                        CustomToast.showToast(mContext, "您已经评论过该条了...", 1000);
                        break;
                    case 4:
                        CustomToast.showToast(mContext, "点赞成功~", 1000);
                        break;
                    case 5:
                        CustomToast.showToast(mContext, "鄙视成功-_-", 1000);
                        break;
                }
            }
        };
    }

    public void stopVoices() {
        if (mediaPlayer != null) {
            mediaPlayer.reset();
        }
    }
}
