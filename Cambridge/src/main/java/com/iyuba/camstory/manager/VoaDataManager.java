package com.iyuba.camstory.manager;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.util.Log;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.iyuba.camstory.bean.Subtitle;
import com.iyuba.camstory.bean.VoaDetail;
import com.iyuba.camstory.frame.CrashApplication;
import com.iyuba.camstory.listener.DetailRequest;
import com.iyuba.camstory.listener.IObserver;
import com.iyuba.camstory.listener.RequestCallBack;
import com.iyuba.camstory.sqlite.mode.Voa;
import com.iyuba.camstory.sqlite.op.VoaDetailOp;
import com.iyuba.camstory.sqlite.op.VoaOp;
import com.iyuba.camstory.widget.CustomToast;
import com.iyuba.camstory.widget.SubtitleSum;

/**
 * 视频管理
 * 
 * @author chentong
 * 
 */
public class VoaDataManager {
	private static final String TAG = VoaDataManager.class.getSimpleName();
	public static final String ACTION_DETAIL_GET = "VoaDataManager_getdetail";

	private static VoaDataManager instance;

	private VoaDataManager() {
		vdop = new VoaDetailOp();
		vOp = new VoaOp();
	}

	public static synchronized VoaDataManager getInstance() {
		if (instance == null) {
			instance = new VoaDataManager();
		}
		return instance;
	}

	public List<IObserver> observerList = new ArrayList<IObserver>();

	// 当前在播放的VOA
	public Voa voaTemp;

	// 当前在播放的VOA的detail信息
	public List<VoaDetail> voaDetailsTemp = new ArrayList<VoaDetail>();

	// 当前在播放的VOA的subtitleSum
	public SubtitleSum subtitleSum;

	// 当前在新闻列表中显示的所有VOA
	public List<Voa> voasTemp = new ArrayList<Voa>();

	// 用于原文详细信息的数据库操作类
	private VoaDetailOp vdop;

	// 用于标题信息的数据库操作类
	private VoaOp vOp;

	// 用于收藏列表的播放
	public List<Voa> collectVoas;

	// 收藏列表的播放标志
	public boolean collectPlay;

	public void setSubtitleSum(Voa voa, List<VoaDetail> voaDetailsTemp) {
		if (voaDetailsTemp == null) {
			return;
		}
		this.voaDetailsTemp = voaDetailsTemp;
		subtitleSum = new SubtitleSum();
		subtitleSum.voaid = voa.voaid;
		subtitleSum.articleTitle = voa.title;
		subtitleSum.articleDesc = voa.desccn;
		subtitleSum.favorites = false; // 查询是否被收藏
		subtitleSum.photoUrl = voa.pic;
		subtitleSum.mp3Url = voa.sound;
		// subtitleSum.photoImg = voa.picBitmap;
		if (subtitleSum.subtitles == null) {
			subtitleSum.subtitles = new ArrayList<Subtitle>();
			subtitleSum.subtitles.clear();
		}
		for (int i = 0; i < voaDetailsTemp.size(); i++) {
			Subtitle st = new Subtitle();
			st.articleTitle = voa.title;
			if (voaDetailsTemp.get(i).sentence_cn.equals("")) {
				st.content = voaDetailsTemp.get(i).sentence;
			} else {
				st.content = voaDetailsTemp.get(i).sentence + "\n"
						+ voaDetailsTemp.get(i).sentence_cn;
			}
			st.pointInTime = voaDetailsTemp.get(i).startTime;
			subtitleSum.subtitles.add(st);
		}
	}

	public void changeLanguage(boolean isOnlyEnglish) {
		Log.e("changeLanguage", isOnlyEnglish + "*** *");
		if (voaDetailsTemp == null || subtitleSum == null) {
			return;
		}
		for (int i = 0; i < voaDetailsTemp.size(); i++) {
			Subtitle st = subtitleSum.subtitles.get(i);
			if (isOnlyEnglish) {// 只有英文
				st.content = voaDetailsTemp.get(i).sentence;
			} else {
				st.content = voaDetailsTemp.get(i).sentence + "\n"
						+ voaDetailsTemp.get(i).sentence_cn;
			}
		}
	}

	/**
	 * 获取文章内容并设为当前的值，以回调形式处理结果
	 * 
	 * @param voa
	 *          要获取内容的voa
	 * @param dc
	 *          回调函数
	 */
	public void setCurrDetail(final Voa voa, final DetailCallback dc) {
		Log.e(TAG, "setCurrDetail now");
		getDetail(voa, new DetailCallback() {

			@Override
			public void onDetail(ArrayList<VoaDetail> details) {
				voaTemp = voa;
				voaDetailsTemp = details;
				setSubtitleSum(voa, details);
				if (details == null || details.size() == 0) {
					CustomToast.showToast(CrashApplication.getInstance(),
							"文章内容获取失败..请在网络良好时重新获取", 1000);
				} else {
					CrashApplication.getInstance().sendBroadcast(
							new Intent(VoaDataManager.ACTION_DETAIL_GET));
				}
				if (dc != null) {
					dc.onDetail(details);
				}
			}
		});
	}

	/**
	 * 获取文章内容，以回调形式处理结果
	 * 
	 * @param voa
	 *          要获取内容的voa
	 * @param dc
	 *          回调函数
	 */
	public void getDetail(final Voa voa, final DetailCallback dc) {
		// 查询本地
		ArrayList<VoaDetail> tempDetails = (ArrayList<VoaDetail>) vdop
				.findDataByVoaId(voa.voaid);
		// 本地有数据
		if (tempDetails != null && tempDetails.size() != 0) {
			if (dc != null) {
				dc.onDetail(tempDetails);
			}
		} else {
			// 去网络获取
			getNetDetail(voa, dc);
		}
	}

	/**
	 * 从网络获取detail信息，
	 * 
	 * @param voa
	 *          ，所要获取detail的VOA值
	 * @param dc
	 */
	public void getNetDetail(Voa voa, final DetailCallback dc) {
		final Voa voatemp = voa;
		DetailRequest request = new DetailRequest(voatemp.voaid,
				new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						if (error instanceof NoConnectionError) {
							CustomToast.showToast(CrashApplication.getInstance(),
									"无网络连接，请检查网络", 1000);
						} else {
							CustomToast.showToast(CrashApplication.getInstance(),
									"获取原文失败，请稍后重试", 1000);
						}
						if (dc != null) {
							dc.onDetail(new ArrayList<VoaDetail>());
						}
					}
				}, new RequestCallBack() {
					@Override
					public void requestResult(Request result) {
						DetailRequest tr = (DetailRequest) result;
						if (tr.isRequestSuccessful()) {
							if (tr.voaDetailsTemps != null && tr.voaDetailsTemps.size() != 0) {
								vdop.saveData(tr.voaDetailsTemps);
							}
							if (dc != null) {
								dc.onDetail((ArrayList<VoaDetail>) tr.voaDetailsTemps);
							}
						} else {
							if (dc != null) {
								dc.onDetail(new ArrayList<VoaDetail>());
							}
						}
					}
				});
		CrashApplication.getInstance().getQueue().add(request);
	}

	public interface DetailCallback {
		void onDetail(ArrayList<VoaDetail> details);
	}

	public void registObserser(IObserver observer) {
		if (!observerList.contains(observer)) {
			observerList.add(observer);
		}
	}

	public void updateObserverCollect(int voaId, boolean state) {
		for (IObserver observer : observerList) {
			observer.updateCollectState(voaId, state);
		}
	}

	public void deleteDataByCollection(int voaId) {
		vOp.deleteDataByCollection(voaId);
		updateObserverCollect(voaId, false);
	}

	public void addDataToCollection(int voaId) {
		vOp.updateDataByCollection(voaId);
		updateObserverCollect(voaId, true);
		List<Voa> list = vOp.findAllCollection();
	}
}
