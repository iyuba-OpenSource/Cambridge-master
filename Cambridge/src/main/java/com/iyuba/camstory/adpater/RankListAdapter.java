package com.iyuba.camstory.adpater;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.iyuba.camstory.utils.LogUtils;
import com.iyuba.camstory.CommentActivity;
import com.iyuba.camstory.R;
import com.iyuba.camstory.bean.RankUser;
import com.iyuba.camstory.lycam.widget.imageview.CircleImageView;
import com.iyuba.camstory.manager.StoryDataManager;
import com.iyuba.camstory.utils.GitHubImageLoader;
import com.iyuba.configation.Constant;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017/1/4.
 */

public class RankListAdapter extends BaseAdapter {

    private Context mContext;
    public List<RankUser> rankUserList = new ArrayList<>();
    private LayoutInflater mInflater;
    private Pattern p;
    private Matcher m;
    private String rankType;

    public RankListAdapter(Context mContext, List<RankUser> rankUserList, String rankType) {
        this.mContext = mContext;
        this.rankUserList.addAll(rankUserList);
        this.rankType = rankType;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return rankUserList.size();
    }

    @Override
    public Object getItem(int position) {
        return rankUserList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return Integer.valueOf(rankUserList.get(position).getSort());
    }

    public void resetList(List<RankUser> list) {
        rankUserList.clear();
        rankUserList.addAll(list);
        notifyDataSetChanged();
    }

    public void addList(List<RankUser> list) {

        if (list.size() > 0) {

            int old_sort = 0;
            if (rankUserList.size() > 0) {
                old_sort = Integer.valueOf(rankUserList.get(rankUserList.size() - 1).getRanking());
            }
            int new_sore = Integer.valueOf(list.get(0).getRanking());
            if (new_sore <= old_sort) {
                LogUtils.e("列表新旧排名" + "old_sort" + old_sort + "new_sore" + new_sore + "");
                return;
            }
        }
        LogUtils.e("列表数量1" + rankUserList.size());
        rankUserList.addAll(list);
        LogUtils.e("列表数量2" + rankUserList.size());


        notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.rank_info_item, null);
        }
        RankUser ru = rankUserList.get(position);
        String firstChar;
        if (ru.getName() == null || "".equals(ru.getName()))
            ru.setName(ru.getUid());
        ImageView rankLogoImage = ViewHolder.get(convertView, R.id.rank_logo_image);
        TextView rankLogoText = ViewHolder.get(convertView, R.id.rank_logo_text);
        CircleImageView userImage = ViewHolder.get(convertView, R.id.user_image);
        TextView userImageText = ViewHolder.get(convertView, R.id.user_image_text);
        TextView userName = ViewHolder.get(convertView, R.id.rank_user_name);
        TextView userInfo = ViewHolder.get(convertView, R.id.rank_user_info);
        TextView userWords = ViewHolder.get(convertView, R.id.rank_user_words);

        convertView.setOnClickListener(v -> {
            String mVoaId="" ;
            if (StoryDataManager.Instance().getCurChapterInfo()!=null){
                mVoaId = StoryDataManager.Instance().getCurChapterInfo().getVoaid();
            }else {
                return;
            }
            Intent intent = new Intent();
            intent.putExtra("uid", rankUserList.get(position).getUid());
            intent.putExtra("voaId", String.valueOf(mVoaId));
            intent.putExtra("userName", rankUserList.get(position).getName());
            intent.putExtra("userPic", rankUserList.get(position).getImgSrc());
            intent.setClass(mContext, CommentActivity.class);
            mContext.startActivity(intent);
        });

        firstChar = getFirstChar(ru.getName());
        switch (ru.getRanking()) {
            case "1":
                rankLogoText.setVisibility(View.INVISIBLE);
                rankLogoImage.setVisibility(View.VISIBLE);
                rankLogoImage.setImageResource(R.drawable.rank_gold);

                if (ru.getImgSrc().equals("http://static1." + Constant.IYBHttpHead + "/uc_server/images/noavatar_middle.jpg")) {
                    userImage.setVisibility(View.INVISIBLE);
                    userImageText.setVisibility(View.VISIBLE);
                    p = Pattern.compile("[a-zA-Z]");
                    m = p.matcher(firstChar);
                    if (m.matches()) {
//                        Toast.makeText(Main.this,"输入的是字母", Toast.LENGTH_SHORT).show();
                        userImageText.setBackgroundResource(R.drawable.rank_blue);
                        userImageText.setText(firstChar);
                        userName.setText(ru.getName());

                        setinfo(userInfo, userWords, ru);
//                        userInfo.setText("News:" + ru.getCnt() + "\nWPM:" + ru.getWpm());
//                        userWords.setText("Words:" +ru.getWords());
                    } else {
                        userImageText.setBackgroundResource(R.drawable.rank_green);
                        userImageText.setText(firstChar);
                        userName.setText(ru.getName());

                        setinfo(userInfo, userWords, ru);
//                        userInfo.setText("News:" + ru.getCnt() + "\nWPM:" + ru.getWpm());
//                        userWords.setText("Words:" +ru.getWords());
                    }
                } else {
                    userImage.setVisibility(View.VISIBLE);
                    userImageText.setVisibility(View.INVISIBLE);
                    GitHubImageLoader.Instace(mContext).setRawPic(ru.getImgSrc(), userImage,
                            R.drawable.noavatar_small);
                    userName.setText(ru.getName());

                    setinfo(userInfo, userWords, ru);
//                    userInfo.setText( "News:" + ru.getCnt() + "\nWPM:" + ru.getWpm());
//                    userWords.setText("Words:" +ru.getWords());
                }
                break;
            case "2":
                rankLogoText.setVisibility(View.INVISIBLE);
                rankLogoImage.setVisibility(View.VISIBLE);
                rankLogoImage.setImageResource(R.drawable.rank_silvery);

                if (ru.getImgSrc().equals("http://static1." + Constant.IYBHttpHead + "/uc_server/images/noavatar_middle.jpg")) {
                    userImage.setVisibility(View.INVISIBLE);
                    userImageText.setVisibility(View.VISIBLE);
                    p = Pattern.compile("[a-zA-Z]");
                    m = p.matcher(firstChar);
                    if (m.matches()) {
//                        Toast.makeText(Main.this,"输入的是字母", Toast.LENGTH_SHORT).show();
                        userImageText.setBackgroundResource(R.drawable.rank_blue);
                        userImageText.setText(firstChar);
                        userName.setText(ru.getName());
                        setinfo(userInfo, userWords, ru);
//                        userInfo.setText( "News:" + ru.getCnt() + "\nWPM:" + ru.getWpm());
//                        userWords.setText("Words:" +ru.getWords());
                    } else {
                        userImageText.setBackgroundResource(R.drawable.rank_green);
                        userImageText.setText(firstChar);
                        userName.setText(ru.getName());

                        setinfo(userInfo, userWords, ru);
//                        userInfo.setText( "News:" + ru.getCnt() + "\nWPM:" + ru.getWpm());
//                        userWords.setText("Words:" +ru.getWords());
                    }
                } else {
                    userImage.setVisibility(View.VISIBLE);
                    userImageText.setVisibility(View.INVISIBLE);
                    GitHubImageLoader.Instace(mContext).setRawPic(ru.getImgSrc(), userImage,
                            R.drawable.noavatar_small);
                    userName.setText(ru.getName());

                    setinfo(userInfo, userWords, ru);
//                    userInfo.setText("News:" + ru.getCnt() + "\nWPM:" + ru.getWpm());
//                    userWords.setText("Words:" +ru.getWords());
                }
                break;
            case "3":
                rankLogoText.setVisibility(View.INVISIBLE);
                rankLogoImage.setVisibility(View.VISIBLE);
                rankLogoImage.setImageResource(R.drawable.rank_copper);

                if (ru.getImgSrc().equals("http://static1." + Constant.IYBHttpHead + "/uc_server/images/noavatar_middle.jpg")) {
                    userImage.setVisibility(View.INVISIBLE);
                    userImageText.setVisibility(View.VISIBLE);
                    p = Pattern.compile("[a-zA-Z]");
                    m = p.matcher(firstChar);
                    if (m.matches()) {
//                        Toast.makeText(Main.this,"输入的是字母", Toast.LENGTH_SHORT).show();
                        userImageText.setBackgroundResource(R.drawable.rank_blue);
                        userImageText.setText(firstChar);
                        userName.setText(ru.getName());

                        setinfo(userInfo, userWords, ru);
//                        userInfo.setText(  "News:" + ru.getCnt() + "\nWPM:" + ru.getWpm());
//                        userWords.setText("Words:" +ru.getWords());
                    } else {
                        userImageText.setBackgroundResource(R.drawable.rank_green);
                        userImageText.setText(firstChar);
                        userName.setText(ru.getName());
                        setinfo(userInfo, userWords, ru);

//                        userInfo.setText("News:" + ru.getCnt() + "\nWPM:" + ru.getWpm());
//                        userWords.setText("Words:" + ru.getWords());
                    }
                } else {
                    userImageText.setVisibility(View.INVISIBLE);
                    userImage.setVisibility(View.VISIBLE);
                    GitHubImageLoader.Instace(mContext).setRawPic(ru.getImgSrc(), userImage,
                            R.drawable.noavatar_small);
                    userName.setText(ru.getName());

                    setinfo(userInfo, userWords, ru);
//                    userInfo.setText("News:" + ru.getCnt() + "\nWPM:" + ru.getWpm());
//                    userWords.setText("Words:" +ru.getWords());
                }
                break;
            case "4":
            case "5":
                rankLogoImage.setVisibility(View.INVISIBLE);
                rankLogoText.setVisibility(View.VISIBLE);
                rankLogoText.setText(ru.getRanking());
                rankLogoText.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                rankLogoText.setSingleLine(true);
                //rankLogoText.setSelected(true);
                //rankLogoText.setFocusable(true);
                // rankLogoText.setFocusableInTouchMode(true);

                if (ru.getImgSrc().equals("http://static1." + Constant.IYBHttpHead + "/uc_server/images/noavatar_middle.jpg")) {
                    userImage.setVisibility(View.INVISIBLE);
                    userImageText.setVisibility(View.VISIBLE);
                    p = Pattern.compile("[a-zA-Z]");
                    m = p.matcher(firstChar);
                    if (m.matches()) {
//                        Toast.makeText(Main.this,"输入的是字母", Toast.LENGTH_SHORT).show();
                        userImageText.setBackgroundResource(R.drawable.rank_blue);
                        userImageText.setText(firstChar);
                        userName.setText(ru.getName());


                        setinfo(userInfo, userWords, ru);
//                        userInfo.setText("News:" + ru.getCnt() + "\nWPM:" + ru.getWpm());
//                        userWords.setText("Words:" +ru.getWords());
                    } else {
                        userImageText.setBackgroundResource(R.drawable.rank_green);
                        userImageText.setText(firstChar);
                        userName.setText(ru.getName());

                        setinfo(userInfo, userWords, ru);
//                        userInfo.setText( "News:" + ru.getCnt() + "\nWPM:" + ru.getWpm());
//                        userWords.setText("Words:" +ru.getWords());
                    }
                } else {
                    userImageText.setVisibility(View.INVISIBLE);
                    userImage.setVisibility(View.VISIBLE);
                    GitHubImageLoader.Instace(mContext).setRawPic(ru.getImgSrc(), userImage,
                            R.drawable.noavatar_small);
                    userName.setText(ru.getName());

                    setinfo(userInfo, userWords, ru);
//                    userInfo.setText("News:" + ru.getCnt() + "\nWPM:" + ru.getWpm());
//                    userWords.setText("Words:" +ru.getWords());
                }
                break;
            default:
                rankLogoImage.setVisibility(View.INVISIBLE);
                rankLogoText.setVisibility(View.VISIBLE);
                rankLogoText.setText(ru.getRanking());
                rankLogoText.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                rankLogoText.setSingleLine(true);
                rankLogoText.setSelected(true);
                rankLogoText.setFocusable(true);
                rankLogoText.setFocusableInTouchMode(true);

                if (ru.getImgSrc().equals("http://static1." + Constant.IYBHttpHead + "/uc_server/images/noavatar_middle.jpg")) {
                    userImage.setVisibility(View.INVISIBLE);
                    userImageText.setVisibility(View.VISIBLE);
                    p = Pattern.compile("[a-zA-Z]");
                    m = p.matcher(firstChar);
                    if (m.matches()) {
//                        Toast.makeText(Main.this,"输入的是字母", Toast.LENGTH_SHORT).show();
                        userImageText.setBackgroundResource(R.drawable.rank_blue);
                        userImageText.setText(firstChar);
                        userName.setText(ru.getName());

                        setinfo(userInfo, userWords, ru);
//                        userInfo.setText("News:" + ru.getCnt() + "\nWPM:" + ru.getWpm());
//                        userWords.setText("Words:" +ru.getWords());
                    } else {
                        userImageText.setBackgroundResource(R.drawable.rank_green);
                        userImageText.setText(firstChar);
                        userName.setText(ru.getName());

                        setinfo(userInfo, userWords, ru);
//                        userInfo.setText("News:" + ru.getCnt() + "\nWPM:" + ru.getWpm());
//                        userWords.setText("Words:" +ru.getWords());
                    }
                } else {
                    userImageText.setVisibility(View.INVISIBLE);
                    userImage.setVisibility(View.VISIBLE);
                    GitHubImageLoader.Instace(mContext).setRawPic(ru.getImgSrc(), userImage,
                            R.drawable.noavatar_small);
                    userName.setText(ru.getName());

                    setinfo(userInfo, userWords, ru);
//                    userInfo.setText("News:" + ru.getCnt() + "\nWPM:" + ru.getWpm());
//                    userWords.setText("Words:" +ru.getWords());
                }
                break;

        }
        return convertView;
    }

    private String getFirstChar(String name) {
        String subString;
        for (int i = 0; i < name.length(); i++) {
            subString = name.substring(i, i + 1);

            p = Pattern.compile("[0-9]*");
            m = p.matcher(subString);
            if (m.matches()) {
//                Toast.makeText(Main.this,"输入的是数字", Toast.LENGTH_SHORT).show();
                return subString;
            }

            p = Pattern.compile("[a-zA-Z]");
            m = p.matcher(subString);
            if (m.matches()) {
//                Toast.makeText(Main.this,"输入的是字母", Toast.LENGTH_SHORT).show();
                return subString;
            }

            p = Pattern.compile("[\u4e00-\u9fa5]");
            m = p.matcher(subString);
            if (m.matches()) {
//                Toast.makeText(Main.this,"输入的是汉字", Toast.LENGTH_SHORT).show();
                return subString;
            }
        }

        return "A";
    }


    private void setinfo(TextView tv1, TextView tv2, RankUser rankUser) {


        switch (rankType) {
            case "阅读":

                tv1.setText("文章数:" + rankUser.getCnt() + "\n每分钟单词阅读数:" + rankUser.getWpm());
                tv2.setText("单词数:" + rankUser.getWords());

                break;
            case "听力":
                tv1.setText("文章数:" + rankUser.getTotalEssay() + "\n单词数:" + rankUser.getTotalWord());

                int min = Integer.valueOf(rankUser.getTotalTime()) / 60;
                tv2.setText(min + "分钟"); //学习时间
                break;
            case "口语":
                double scores_avg, scores;
                if (rankUser.getScores() == null || rankUser.getCount() == null) {
                    scores_avg = 0.0;
                    scores = 0.0;
                } else {
                    scores_avg = Double.valueOf(rankUser.getScores()) / Double.valueOf(rankUser.getCount());
                    scores = Double.valueOf(rankUser.getScores());
                }
                DecimalFormat df = new DecimalFormat("0.00");

                tv1.setText("总分:" + df.format(scores) + "\n平均分:" + df.format(scores_avg));
                tv2.setText("排名：" + rankUser.getRanking());
                break;
            case "学习":

                tv1.setText("文章数:" + rankUser.getTotalEssay() + "\n单词数:" + rankUser.getTotalWord());

                int hour = Integer.valueOf(rankUser.getTotalTime()) / 3600;
                DecimalFormat df2 = new DecimalFormat("0.00");

                tv2.setText(df2.format(hour) + "小时"); //学习时间
                break;
            case "测试":
                double right = 0;
                if (rankUser.getTotalRight() != null && rankUser.getTotalTest() != null) {
                    right = Double.valueOf(rankUser.getTotalRight()) / Double.valueOf(rankUser.getTotalTest());
                }
                DecimalFormat df3 = new DecimalFormat("0.00");

                tv2.setText("总题数：" + rankUser.getTotalTest());

                tv1.setText("正确数:" + rankUser.getTotalRight() + "\n正确率:" + df3.format(right));
                break;
            case "语音":
                tv1.setText("句子总数:" + rankUser.getCount() + "\n总分数:" + rankUser.getScores());
                break;

        }
    }

}
