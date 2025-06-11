package com.iyuba.camstory.sqlite.mode;

import com.iyuba.camstory.bean.BookContentResponse;
import com.iyuba.camstory.bean.VoaDetail;
import com.iyuba.camstory.manager.AccountManager;
import com.iyuba.voa.activity.setting.Constant;
//import com.iyuba.voa.manager.AccountManager;

/**
 * CommentList generated after reading in the voice evaluating part
 */
public class ReadVoiceComment extends Comment implements Shareable{
	private static final String TAG = ReadVoiceComment.class.getSimpleName();

	private Voa voaRef;
	private BookContentResponse.Texts texts;

	public ReadVoiceComment() {
		super();
		shuoshuoType = 2;
	}

	public ReadVoiceComment(BookContentResponse.Texts texts) {
		this();
		this.texts = texts;
	}

	public Voa getVoaRef() {
		return this.voaRef;
	}

	public BookContentResponse.Texts getVoaDetailRef() {
		return this.texts;
	}


	@Override
	public String getShareUrl() {
		// return "http://voa."+Constant.IYBHttpHead+"/voa/play.jsp?id=" + id + "&appid=" + Constant.getAppid();
		return "http://voa."+Constant.IYBHttpHead+"/voa/play.jsp?id="+id;
	}

	@Override
	public String getShareImageUrl() {
		//return voaRef.pic;
		return "http://app."+Constant.IYBHttpHead+"/android/images/camstory/camstory.png";
	}

	@Override
	public String getShareAudioUrl() {
		return "http://voa."+Constant.IYBHttpHead+"/voa/" + shuoshuo;
	}

	@Override
	public String getShareTitle() {
		String userName = (AccountManager.getInstance().checkUserLogin()) ? "["
				+ AccountManager.getInstance().userName + "]" : "";
		return userName + "在" + "爱语吧语音评测中获得了" + texts.getReadScore() + "分";
	}

	@Override
	public String getShareLongText() {
		return texts.getTextEN() + " " + texts.getTextCH();
	}

	@Override
	public String getShareShortText() {
		return texts.getTextEN();
	}

	@Override
	public String toString() {
		return TAG + "[id=" + id + "shuoshuo=" + shuoshuo + "voaRef.id="
				+ voaRef.voaid + "detailRef.sentence=" + texts.getTextEN() + "]";
	}


}
