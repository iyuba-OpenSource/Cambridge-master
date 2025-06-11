package com.iyuba.camstory.manager;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.iyuba.camstory.bean.AttentionBean;
import com.iyuba.camstory.bean.FansBean;
import com.iyuba.camstory.frame.CrashApplication;
import com.iyuba.camstory.listener.CommonCallBack;
import com.iyuba.camstory.listener.RequestAttentionList;
import com.iyuba.camstory.listener.RequestCallBack;
import com.iyuba.camstory.listener.RequestFansList;
import com.iyuba.camstory.listener.RequestMutualAttentionList;
import com.iyuba.camstory.listener.RequestNotificationInfo;
import com.iyuba.camstory.sqlite.mode.Attention;
import com.iyuba.camstory.sqlite.mode.BlogContent;
import com.iyuba.camstory.sqlite.mode.BlogInfo;
import com.iyuba.camstory.sqlite.mode.DoingsCommentInfo;
import com.iyuba.camstory.sqlite.mode.DoingsInfo;
import com.iyuba.camstory.sqlite.mode.Fans;
import com.iyuba.camstory.sqlite.mode.FeedInfo;
import com.iyuba.camstory.sqlite.mode.GuessFriendInfo;
import com.iyuba.camstory.sqlite.mode.MessageLetter;
import com.iyuba.camstory.sqlite.mode.MutualAttention;
import com.iyuba.camstory.sqlite.mode.NearFriendInfo;
import com.iyuba.camstory.sqlite.mode.NotificationInfo;
import com.iyuba.camstory.sqlite.mode.POIInfo;
import com.iyuba.camstory.sqlite.mode.PublicAccountInfo;
import com.iyuba.camstory.sqlite.mode.RequestSameAppFriendsList;
import com.iyuba.camstory.sqlite.mode.SameAppFriendInfo;
import com.iyuba.camstory.sqlite.mode.SearchItem;
import com.iyuba.camstory.sqlite.mode.UserInfo;
import com.iyuba.camstory.utils.NetStatusUtil;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SocialDataManager {
	Context mContext;
	public static SocialDataManager instance;

	public SocialDataManager() {
	}

	public static synchronized SocialDataManager getInstance() {
		if (instance == null) {
			instance = new SocialDataManager();
			instance.mContext = CrashApplication.getInstance();
		}
		return instance;
	}

	public int currFansPage = 1;
	public int currAttentionsPage = 1;
	public boolean attentionRequesting = false;// 正在刷新
	public boolean fansRequesting = false;// 正在刷新

	public boolean sameDataRefreshed = false;// 判断是否刷新过了
	public boolean mattentionDataRefreshed = false;// 判断是否刷新过了
	public boolean notifitionRefreshed = false;
	public List<NearFriendInfo> nearFriendInfos = new ArrayList<NearFriendInfo>();
	public List<SameAppFriendInfo> sameAppFriendInfos = new ArrayList<SameAppFriendInfo>();
	public ArrayList<AttentionBean.DataBean> attentions = new ArrayList<AttentionBean.DataBean>();
	public ArrayList<FansBean.DataBean> fans = new ArrayList<FansBean.DataBean>();
	public List<MutualAttention> mattentions = new ArrayList<MutualAttention>();
	public List<Fans> notificationInfos = new ArrayList<Fans>();

	public void getSameAppDatas(final CommonCallBack ccb) {
		RequestSameAppFriendsList requestNearFriendsList = new RequestSameAppFriendsList(
				AccountManager.getInstance().userId, 1, new RequestCallBack() {
					@Override
					public void requestResult(Request result) {
						RequestSameAppFriendsList rs = (RequestSameAppFriendsList) result;
						if (rs.isRequestSuccessful()) {
							sameAppFriendInfos.clear();
							sameAppFriendInfos.addAll(rs.list);
							for (Iterator iterator = sameAppFriendInfos.iterator(); iterator.hasNext();) {
								SameAppFriendInfo sa = (SameAppFriendInfo) iterator.next();
								Log.d("sameapp", sa.username + ";" + sa.uid + ";" + sa.appname);
								sameDataRefreshed = true;
							}
							if (ccb != null) {
								ccb.onPositive(null);
							}
						} else {
							if (ccb != null) {
								ccb.onNegative(null);
							}
						}
					}
				});
		CrashApplication.getInstance().getQueue().add(requestNearFriendsList);
	}

	public void getAttentionDatas(final CommonCallBack ccb) {
		if (!NetStatusUtil.isConnected(mContext)) {
			Toast.makeText(mContext, "网络无法连接，无法获取关注列表", 1000).show();
			return;
		}
		if (!attentionRequesting) {
			attentionRequesting = true;
			RequestAttentionList requestNearFriendsList = new RequestAttentionList(
					AccountManager.getInstance().userId, currAttentionsPage, new RequestCallBack() {
						@Override
						public void requestResult(Request result) {
							RequestAttentionList rs = (RequestAttentionList) result;
							if (rs.isRequestSuccessful()) {
								attentions.addAll(rs.fansList);
								currAttentionsPage++;
								for (Iterator iterator = rs.fansList.iterator(); iterator.hasNext();) {
									Attention at = (Attention) iterator.next();
									Log.d("Attention", at.fusername + ";" + at.followuid + ";" + at.mutual);
								}
								if (ccb != null) {
									ccb.onPositive(null);
								}
							} else {
								if (ccb != null) {
									ccb.onNegative(null);
								}
							}
							attentionRequesting = false;
						}
					});
			CrashApplication.getInstance().getQueue().add(requestNearFriendsList);
		}
	}

	public void getMultiAttenDatas(final CommonCallBack ccb) {
		RequestMutualAttentionList requestNearFriendsList = new RequestMutualAttentionList(
				AccountManager.getInstance().userId, 1 + "", new RequestCallBack() {
					@Override
					public void requestResult(Request result) {
						RequestMutualAttentionList rs = (RequestMutualAttentionList) result;
						if (rs.isRequestSuccessful()) {
							mattentions.clear();
							mattentions.addAll(rs.list);
							mattentionDataRefreshed = true;
							if (ccb != null) {
								ccb.onPositive(null);
							}
						} else {
							if (ccb != null) {
								ccb.onNegative(null);
							}
						}
					}
				});
		CrashApplication.getInstance().getQueue().add(requestNearFriendsList);
	}

	public void getFansList(final CommonCallBack ccb) {
		if (!NetStatusUtil.isConnected(mContext)) {
			Toast.makeText(mContext, "网络无法连接，无法获取粉丝列表", 1000).show();
			return;
		}
		Log.e("fansRequesting", fansRequesting + "");
		if (!fansRequesting) {
			fansRequesting = true;
			RequestFansList requestNearFriendsList = new RequestFansList(
					AccountManager.getInstance().userId, currFansPage, new RequestCallBack() {
						@Override
						public void requestResult(Request result) {
							RequestFansList rs = (RequestFansList) result;
							if (rs.isRequestSuccessful()) {
								fans.addAll(rs.fansList);
								currFansPage++;
								if (ccb != null) {
									ccb.onPositive(null);
								}
							} else {
								if (ccb != null) {
									ccb.onNegative(null);
								}
							}
							fansRequesting = false;
						}
					});
			CrashApplication.getInstance().getQueue().add(requestNearFriendsList);
		}
	}

	public void getNotifitionInfos(final CommonCallBack ccb) {
		RequestNotificationInfo requestNearFriendsList = new RequestNotificationInfo(
				AccountManager.getInstance().userId, 1 + "", "system,app,follow", new RequestCallBack() {
					@Override
					public void requestResult(Request result) {
						RequestNotificationInfo rs = (RequestNotificationInfo) result;
						if (rs.isRequestSuccessful()) {
							notificationInfos.clear();
							notificationInfos.addAll(rs.list);
							notifitionRefreshed = true;
							if (ccb != null) {
								ccb.onPositive(null);
							}
						} else {
							if (ccb != null) {
								ccb.onNegative(null);
							}
						}
					}
				});
		CrashApplication.getInstance().getQueue().add(requestNearFriendsList);
	}

	public BlogInfo blogInfo;
	public BlogContent blogContent;
	public DoingsInfo doingsInfo;
	public DoingsCommentInfo doingsCommentInfo;
	public AttentionBean.DataBean attention;
	public MutualAttention mutualAttention;
	public List<BlogInfo> blogList = new ArrayList<BlogInfo>();
	public List<DoingsInfo> doingsList = new ArrayList<DoingsInfo>();
	public List<DoingsCommentInfo> doingsCommentInfoList = new ArrayList<DoingsCommentInfo>();
	public MessageLetter letter = new MessageLetter();
	public List<MessageLetter> letterlist = new ArrayList<MessageLetter>();
	public NotificationInfo replyInfo = new NotificationInfo();
	public List<NotificationInfo> nofiList = new ArrayList<NotificationInfo>();
	public FeedInfo feed = new FeedInfo();
	public List<FeedInfo> feedList = new ArrayList<FeedInfo>();
	public UserInfo userInfo = new UserInfo();
	public NearFriendInfo nearFriendInfo = new NearFriendInfo();
	public GuessFriendInfo guessFriendInfo = new GuessFriendInfo();
	public SameAppFriendInfo sameAppFriendInfo = new SameAppFriendInfo();
	public PublicAccountInfo publicAccountInfo = new PublicAccountInfo();
	public SearchItem searchItem = new SearchItem();
	public POIInfo poiInfo = new POIInfo();

}
