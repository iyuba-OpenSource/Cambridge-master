package com.iyuba.camstory.widget;

import android.util.Log;

import com.iyuba.camstory.sqlite.mode.Shareable;
import com.iyuba.voa.activity.setting.Constant;


import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.Platform.ShareParams;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.favorite.WechatFavorite;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

public class CallPlatform1 {
	private static final String TAG = CallPlatform1.class.getSimpleName();

	private HashMap<String, Object> item;
	private Shareable stuff;
	private ShareParams sp;
	private PlatformActionListener pal;
	private String imagePath,text,url,title;

	public CallPlatform1(HashMap<String, Object> item, String imagePath,
						 String text, String url, String title, PlatformActionListener pal) {
		this.item = item;
		this.imagePath=imagePath;
		this.text=text;
		this.url=url;
		this.title=title;
		this.pal = pal;
		try {
			setParams();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	private void setParams() throws UnsupportedEncodingException {
		sp = new ShareParams();
		if (item.get("ItemText").equals("QQ")) {
			sp.setTitle(title);
			sp.setTitleUrl(url);
			sp.setText("123123");
			sp.setImageUrl(imagePath);
            Log.v(TAG+"SetTitleUrl",url);
			Platform qq = ShareSDK.getPlatform(QQ.NAME);
			qq.setPlatformActionListener(pal);
			qq.share(sp);
		} else if (item.get("ItemText").equals("微信好友")) {
			sp.setShareType(Platform.SHARE_MUSIC);
//			sp.setUrl(url);
			sp.setTitle(title);
			sp.setTitleUrl(url);
			sp.setText(text);
			sp.setImageUrl(imagePath);

			Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
			wechat.setPlatformActionListener(pal);
			wechat.share(sp);
		} else if (item.get("ItemText").equals("微信朋友圈")) {
			sp.setShareType(Platform.SHARE_MUSIC);
			sp.setTitle(title);
			sp.setTitleUrl(url);
			sp.setText(text);
			sp.setImageUrl(imagePath);
//			sp.setUrl(url);
//			sp.setMusicUrl(stuff.getShareAudioUrl());
			Platform wechatMoments = ShareSDK.getPlatform(WechatMoments.NAME);
			wechatMoments.setPlatformActionListener(pal);
			wechatMoments.share(sp);
		} else if (item.get("ItemText").equals("新浪微博")) {
			sp.setText(text);
//			sp.setImageUrl(imagePath);
			sp.setTitle(title);
			sp.setTitleUrl(url);
			Platform sinaWeibo = ShareSDK.getPlatform(SinaWeibo.NAME);
			sinaWeibo.SSOSetting(true);
			sinaWeibo.setPlatformActionListener(pal);
			sinaWeibo.share(sp);
		} else if (item.get("ItemText").equals("QQ空间")) {
			sp.setTitle(title);
			sp.setTitleUrl(url);
			sp.setText(text);
			sp.setImageUrl(imagePath);
			sp.setSite(Constant.getAppname());
			sp.setSiteUrl(url);
			Platform qZone = ShareSDK.getPlatform(QZone.NAME);
			qZone.setPlatformActionListener(pal);
			qZone.share(sp);
		} else if (item.get("ItemText").equals("微信收藏")) {
			sp.setShareType(Platform.SHARE_MUSIC);
			sp.setTitle(title);
			sp.setTitleUrl(url);
			sp.setText(text);
			sp.setImageUrl(imagePath);
//			sp.setUrl(url);
//			sp.setMusicUrl(url);
			Platform wechatFavorite = ShareSDK.getPlatform(WechatFavorite.NAME);
			wechatFavorite.setPlatformActionListener(pal);
			wechatFavorite.share(sp);
		}

	}

}