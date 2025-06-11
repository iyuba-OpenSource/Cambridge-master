package com.iyuba.camstory.adpater;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap.Config;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.iyuba.camstory.R;
import com.iyuba.camstory.VipCenterGoldActivity;
import com.iyuba.camstory.bean.VoaDetail;
import com.iyuba.camstory.frame.CrashApplication;
import com.iyuba.camstory.listener.AnimateFirstDisplayListener;
import com.iyuba.camstory.listener.CommonCallBack;
import com.iyuba.camstory.listener.IObserver;
import com.iyuba.camstory.listener.RequestCallBack;
import com.iyuba.camstory.listener.UseCreditsRequest;
import com.iyuba.camstory.manager.AccountManager;
import com.iyuba.camstory.manager.UserInfoManager;
import com.iyuba.camstory.manager.VoaDataManager;
import com.iyuba.camstory.sqlite.mode.UserInfo;
import com.iyuba.camstory.sqlite.mode.Voa;
import com.iyuba.camstory.sqlite.op.VoaDetailOp;
import com.iyuba.camstory.sqlite.op.VoaOp;
import com.iyuba.camstory.utils.NetWorkStateUtil;
import com.iyuba.camstory.widget.CustomToast;
import com.iyuba.camstory.widget.ProgressWheel;
import com.iyuba.multithread.DownloadProgressListener;
import com.iyuba.multithread.FileDownloader;
import com.iyuba.multithread.FileService;
import com.iyuba.multithread.MultiThreadDownloadManager;
import com.iyuba.voa.activity.setting.Constant;
import com.iyuba.voa.frame.components.ConfigManagerVOA;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
public class NewCardListAdapter extends BaseAdapter implements IObserver {
	private static final String TAG = NewCardListAdapter.class.getSimpleName();
	private static final int FREE_DOWNLOAD_LIMIT = 5;
	private Context mContext;

	private List<Voa> mList;
	private LayoutInflater inflater;
	public boolean modeDelete = false;
	public boolean modedownload = false;
	private AnimateFirstDisplayListener animateFirstListener;
	private DisplayImageOptions mDisplayImageOptions;
	private VoaOp vop;
	private VoaDetailOp vdop;
	private Voa voa;
	private int isConnect;
	private AlertDialog alertDialog;
	private CheckBox checkBox;
	private FileService fileService;

	public NewCardListAdapter(Context context) {
		this(context, new ArrayList<Voa>());
	}

	public NewCardListAdapter(Context context, ArrayList<Voa> list) {
		mContext = context;
		inflater = LayoutInflater.from(mContext);
		mList = list;
		init();
	}

	private void init() {
		vop = new VoaOp();
		vdop = new VoaDetailOp();
		fileService = new FileService(mContext);
		animateFirstListener = new AnimateFirstDisplayListener();
		alertDialog = buildDownloadHintDialog();

		// 初始化列表图片所需的displayImageOptions
		if (Constant.getAppid().equals("215") || Constant.getAppid().equals("221")) {
			mDisplayImageOptions = CrashApplication.getInstance()
					.getDefaultDisplayImageOptionsBuilder()
					.displayer(new FadeInBitmapDisplayer(0))
					// .imageScaleType(ImageScaleType.NONE)
					.bitmapConfig(Config.RGB_565)
					// .showImageOnLoading(R.drawable.nearby_no_icon)//默认图片
					.build();
		} else {
			mDisplayImageOptions = CrashApplication
					.getInstance()
					.getDefaultDisplayImageOptionsBuilder()
					.displayer(new RoundedBitmapDisplayer(15))
					// 设置圆角
					// .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
					.bitmapConfig(Config.RGB_565)
					.showImageOnLoading(R.mipmap.nearby_no_icon)// 默认图片
					.build();
		}
	}

	private AlertDialog buildDownloadHintDialog() {
		AlertDialog dialog = new AlertDialog.Builder(mContext).create();
		View layout = inflater.inflate(R.layout.mydialog, null);
		dialog.setView(layout);
		dialog.setTitle(R.string.alert);
		dialog.setButton(AlertDialog.BUTTON_POSITIVE,
				mContext.getString(R.string.ok), new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						download();
					}
				});
		dialog.setButton(AlertDialog.BUTTON_NEGATIVE,
				mContext.getString(R.string.cancel),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
		checkBox = layout.findViewById(R.id.checkBox_remember);
		return dialog;
	}

	public void setList(List<Voa> mList) {
		this.mList = mList;
	}

	public void addList(List<Voa> voasTemps) {
		mList.addAll(voasTemps);
	}

	public void replaceList(List<Voa> voasTemps) {
		mList.clear();
		mList.addAll(voasTemps);
	}

	public void deleteAllCancel() {
		Iterator<Voa> iterator = mList.iterator();
		while (iterator.hasNext()) {
			Voa voa = iterator.next();
			voa.isDelete = false;
			notifyDataSetChanged();
		}
	}

	public void deleteAll() {
		Iterator<Voa> iterator = mList.iterator();
		while (iterator.hasNext()) {
			Voa voa = iterator.next();
			voa.isDelete = true;
			notifyDataSetChanged();
		}
	}

	public Voa getFirstItem() {
		return (mList.size() != 0) ? mList.get(0) : new Voa();
	}

	public Voa getLastItem() {
		return (mList.size() != 0) ? mList.get(mList.size() - 1) : new Voa();
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	private void checkPoint2() {
		Log.e(TAG, "run checkPoint");
		if (AccountManager.getInstance().checkUserLogin()) {
			UserInfoManager.getMyInfo(new CommonCallBack() {
				@Override
				public void onPositive(Object object) {
					UserInfoManager.myInfo.uid = AccountManager.getInstance().userId;
					if (UserInfoManager.myInfo.credits < Constant.coin) {
						handler.sendEmptyMessage(1);
					} else {
						handler.sendEmptyMessage(2);
					}
				}

				@Override
				public void onNegative(Object object) {
					// TODO 自动生成的方法存根
				}
			});
		} else {
			CustomToast.showToast(mContext, "您还没有登录，请先登录", 2000);
		}
	}

	private void showAlertDialog() {
		AlertDialog alert = new AlertDialog.Builder(mContext).create();
		alert.setTitle(mContext.getResources().getString(R.string.alert));
		alert.setMessage(mContext.getResources().getString(
				R.string.nladapter_noPoint));
		alert.setIcon(android.R.drawable.ic_dialog_alert);
		alert.setButton(AlertDialog.BUTTON_POSITIVE, mContext.getResources()
				.getString(R.string.alert_btn_buy),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						Intent intent = new Intent();
						intent.setClass(mContext, VipCenterGoldActivity.class);
						mContext.startActivity(intent);
					}
				});
		alert.setButton(AlertDialog.BUTTON_NEGATIVE, mContext.getResources()
				.getString(R.string.alert_btn_cancel),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				});
		alert.show();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final Voa curVoa = mList.get(position);
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.listitem_newlist_card, null);
		}
		ImageView voaImage = ViewHolder.get(convertView, R.id.image);
		TextView readCount = ViewHolder.get(convertView, R.id.readcount);
		TextView name = ViewHolder.get(convertView, R.id.name);
		TextView time = ViewHolder.get(convertView, R.id.time);
		ImageView deleteBox = ViewHolder.get(convertView, R.id.checkBox_isDelete);
		ImageButton collectImageButton = ViewHolder.get(convertView, R.id.collectbutton);
		ImageButton downloadImageButton = ViewHolder.get(convertView, R.id.downloadbutton);
		ProgressWheel progressWheel = ViewHolder.get(convertView,	R.id.videodownload_wheel);

		// 显示删除
		if (modeDelete) {
			deleteBox.setVisibility(View.VISIBLE);
			if (curVoa.isDelete) {
				deleteBox.setImageResource(R.drawable.check_box_checked);
			} else {
				deleteBox.setImageResource(R.drawable.check_box);
			}
		} else {
			deleteBox.setVisibility(View.GONE);
		}

		// 显示图片
		ImageLoader.getInstance().displayImage(curVoa.pic, voaImage,
				mDisplayImageOptions, animateFirstListener);
		// 显示标题
		if (ConfigManagerVOA.Instance(mContext).loadString("applanguage").equals("zh")) {
			name.setText(curVoa.title_cn);
		} else {
			name.setText(curVoa.title);
		}
		if (curVoa.isReaded()) {
			name.setTextColor(Constant.getReadColor());
			time.setTextColor(Constant.getReadColor());
			readCount.setTextColor(Constant.getReadColor());
		} else {
			name.setTextColor(Constant.getUnreadColor());
			time.setTextColor(0xaa000000);
			readCount.setTextColor(0xaa000000);
		}
		// 显示阅读数
		readCount.setText(curVoa.readcount
				+ mContext.getString(R.string.voa_readcount));
		// 显示时间
		String[] dateTime = curVoa.creattime.split(" ");
		if (dateTime.length != 2) {
			time.setText(curVoa.creattime);
		} else {
			time.setText(dateTime[0]);
		}

		if (curVoa.favourite) {
			collectImageButton.setImageResource(R.mipmap.news_collected);
		} else {
			collectImageButton.setImageResource(R.mipmap.news_uncollect);
		}
		collectImageButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (curVoa.favourite) {
					curVoa.favourite = false;
					uncollect(curVoa.voaid);
				} else {
					curVoa.favourite = true;
					collect(curVoa.voaid);
				}
			}
		});
		OnClickListener downBtnOCL = new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isAudioFileExist(curVoa) && isAudioFileComplete(curVoa)) {
					vop.updateDataByDownload(curVoa.voaid);
					curVoa.isDownloaded = true;
					CustomToast.showToast(mContext, "已下载", 1000);
				} else {
					vop.deleteDataByDownload(curVoa.voaid);
					curVoa.isDownloaded = false;
					if (!curVoa.isClickDownload) {
						if (AccountManager.getInstance().checkUserLogin()) {
							if (AccountManager.getInstance().isVip(mContext)) {
								checkNetworkAndDownload(curVoa);
							} else {
								if (isOverFreeDownloadLimit()) {
									checkUserCreditThenDownload(curVoa);
								} else {
									checkNetworkAndDownload(curVoa);
								}
							}
						} else {
							if (isOverFreeDownloadLimit()) {
								CustomToast.showToast(mContext, "您还没有登录，请先登录", 2000);
							} else {
								checkNetworkAndDownload(curVoa);
							}
						}
					}
				}
			}
		};

		OnClickListener downListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				voa = curVoa;
				if (!voa.isClickDownload) {
					if (AccountManager.getInstance().isVip(mContext)) {
						if (checkNetWork()) {
							download();
						}
					} else {
						int downloadNumWithNoInetgeral = ConfigManagerVOA.Instance(mContext)
								.loadInt("download_num_with_no_integral");
						if (downloadNumWithNoInetgeral < FREE_DOWNLOAD_LIMIT) {
							if (checkNetWork()) {
								CustomToast.showToast(mContext,
										R.string.download_with_no_integral_tip, 2000);
								download();
							}
						} else {
							if (checkNetWork()) {
								checkPoint2();
							}
						}
					}
				}
			}
		};
		// downloadImageButton.setOnClickListener(downListener);
		// progressWheel.setOnClickListener(downListener);
		downloadImageButton.setOnClickListener(downBtnOCL);
		progressWheel.setOnClickListener(downBtnOCL);
		voa = curVoa;
		Log.e(TAG, "isAudioFileExist(curVoa) : " + isAudioFileExist(curVoa)
				+ "\nisAudioFileComplete(curVoa) : " + isAudioFileComplete(curVoa));
		if (isAudioFileExist(curVoa) && !isAudioFileComplete(curVoa)) {
			Log.e(TAG, "set progress wheel content");
			progressWheel.setVisibility(View.VISIBLE);
			// progress是0-360度
			progressWheel.setProgress((int) ((MultiThreadDownloadManager
					.getTaskPercentage(curVoa.voaid) / 100f) * 360));
			progressWheel.setText(MultiThreadDownloadManager
					.getTaskPercentage(curVoa.voaid) + "%");
		} else {
			Log.e(TAG, "hide progress wheel");
			progressWheel.setVisibility(View.GONE);
		}
		if (isAudioFileExist(curVoa) && isAudioFileComplete(curVoa)) {
			MultiThreadDownloadManager.removeTask(curVoa.voaid);
			downloadImageButton.setImageResource(R.mipmap.news_downloaded);
			downloadImageButton.setVisibility(View.VISIBLE);
		} else if (isAudioFileExist(curVoa)) {
			downloadImageButton.setVisibility(View.INVISIBLE);
		} else {
			downloadImageButton.setImageResource(R.mipmap.news_download);
			downloadImageButton.setVisibility(View.VISIBLE);
		}
		return convertView;
	}

	private boolean checkNetWork() {
		boolean result = false;
		isConnect = NetWorkStateUtil.getAPNType();
		if (isConnect == 0) {
			CustomToast.showToast(mContext, R.string.category_check_network, 1000);
		} else if (isConnect == 1) {
			if (!ConfigManagerVOA.Instance(mContext).loadBoolean("remember_download_ok"))
				alertDialog.show();
			else if (isAudioFileExist(voa) && isAudioFileComplete(voa)) {
				vop.updateDataByDownload(voa.voaid);
				voa.isDownloaded = true;
				CustomToast.showToast(mContext, "已下载", 1000);
			} else {
				vop.deleteDataByDownload(voa.voaid);
				voa.isDownloaded = false;
				result = true;
			}
		} else if (isConnect == 2) {
			if (isAudioFileExist(voa) && isAudioFileComplete(voa)) {
				vop.updateDataByDownload(voa.voaid);
				voa.isDownloaded = true;
				CustomToast.showToast(mContext, "已下载", 1000);
			} else {
				vop.deleteDataByDownload(voa.voaid);
				voa.isDownloaded = false;
				result = true;
			}
		}
		Log.e(TAG, "check result==> type : " + isConnect + " result : " + result);
		return result;
	}

	private void checkNetworkAndDownload(final Voa downVoa) {
		int nettype = NetWorkStateUtil.getAPNType();
		switch (nettype) {
		case 0:
			CustomToast.showToast(mContext, R.string.category_check_network, 1000);
			break;
		case 1:
			AlertDialog dialog = new AlertDialog.Builder(mContext).create();
			View layout = inflater.inflate(R.layout.mydialog, null);
			dialog.setView(layout);
			dialog.setTitle(R.string.alert);
			dialog.setButton(AlertDialog.BUTTON_POSITIVE,
					mContext.getString(R.string.ok),
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							directlyDownload(downVoa);
						}
					});
			dialog.setButton(AlertDialog.BUTTON_NEGATIVE,
					mContext.getString(R.string.cancel),
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();
						}
					});
			dialog.show();
			break;
		case 2:
			directlyDownload(downVoa);
			break;
		default:
			break;
		}
	}

	private void directlyDownload(final Voa downVoa) {
		Log.e(TAG, "first downVoa.isClickDownload : " + downVoa.isClickDownload);
		final String url;
		final String fileSaveDir;
		downVoa.isClickDownload = true;
		Log.e(TAG, "second downVoa.isClickDownload : " + downVoa.isClickDownload);
		if (fileService.isInDownloadDB(downVoa.voaid)) {
			url = fileService.findDownloadUrl(downVoa.voaid);
		} else {
			url = getAudioUrl(downVoa);
		}
		File file = new File(ConfigManagerVOA.Instance(mContext).loadString(
				"media_saving_path"));
		if (!file.exists()) {
			file.mkdirs();
		}
		fileSaveDir = mergeAudioPath(downVoa.voaid);
		MultiThreadDownloadManager.enQueue(mContext, downVoa.voaid, url, new File(
				fileSaveDir), 2, new DownloadProgressListener() {

			@Override
			public void onDownloadStart(FileDownloader fileDownloader, int id,
					int fileTotalSize) {
				Log.e(TAG, "on download start totalSize : " + fileTotalSize);
				handler.sendEmptyMessage(0);
			}

			@Override
			public void onProgressUpdate(int id, String url, int fileDownloadSize) {
				Log.e(TAG, "on download progress downedSize : " + fileDownloadSize);
				handler.sendEmptyMessage(0);
			}

			@Override
			public void onDownloadComplete(final int id, String string) {
				Log.e(TAG, "on download complete! id : " + id + " string : " + string);
				((Activity) mContext).runOnUiThread(new Runnable() {

					@Override
					public void run() {
						int downloadNumWithNoInetgeral = ConfigManagerVOA.Instance(mContext)
								.loadInt("download_num_with_no_integral");
						if (!AccountManager.getInstance().isVip(mContext)) {
							if (downloadNumWithNoInetgeral < FREE_DOWNLOAD_LIMIT) {
								ConfigManagerVOA.Instance(mContext).putInt(
										"download_num_with_no_integral",
										downloadNumWithNoInetgeral + 1);
							} else {
								Message msg = handler.obtainMessage(3);
								msg.obj = downVoa.voaid;
								handler.sendMessage(msg);
							}
						}

						if (modedownload) {
							for (int i = 0; i < mList.size(); i++) {
								if (mList.get(i).voaid == id) {
									mList.remove(i);
									break;
								}
							}
						}
						vop.updateDataByDownload(id);
						notifyDataSetChanged();
					}
				});
				downVoa.isClickDownload = false;
				Log.e(TAG, "on Download complete downVoa.isClickDownload : " + downVoa.isClickDownload);
			}

			@Override
			public void onDownloadStoped(int id) {
				Log.e(TAG, "on download stop! id : " + id);
			}

			@Override
			public void onDownloadError(int id, Exception e) {
				Log.e(TAG, "on download error! id : " + id);
				File file = new File(fileSaveDir);
				if (file.exists()) {
					file.delete();
				}
				((Activity) mContext).runOnUiThread(new Runnable() {
					@Override
					public void run() {
						CustomToast.showToast(mContext, "文章下载失败，请重试", 2000);
					}
				});
				Log.e(TAG, "on Download error downVoa.isClickDownload : " + downVoa.isClickDownload);
				downVoa.isClickDownload = false;
			}

		});
	}

	private void download() {
		final Voa downVoa = voa;
		final String url;
		final String fileSaveDir;
		downVoa.isClickDownload = true;
		if (fileService.isInDownloadDB(voa.voaid)) {
			url = fileService.findDownloadUrl(voa.voaid);
		} else {
			url = getAudioUrl(downVoa);
		}

		File file = new File(ConfigManagerVOA.Instance(mContext).loadString(
				"media_saving_path"));
		if (!file.exists()) {
			file.mkdirs();
		}
		// 存原文
		if (vdop.findDataByVoaId(downVoa.voaid).size() == 0) {
			VoaDataManager.getInstance().getDetail(downVoa, new VoaDataManager.DetailCallback() {
				@Override
				public void onDetail(ArrayList<VoaDetail> details) {
					if (details == null || details.size() == 0) {
						((Activity) mContext).runOnUiThread(new Runnable() {
							@Override
							public void run() {
								CustomToast.showToast(mContext, "文章内容获取失败..请在播放页重新获取", 1000);
							}
						});
					} else {
						vdop.saveData(details);
					}
				}
			});
		}
		fileSaveDir = mergeAudioPath(downVoa.voaid);
		MultiThreadDownloadManager.enQueue(mContext, downVoa.voaid, url, new File(
				fileSaveDir), 2, new DownloadProgressListener() {
			@Override
			public void onProgressUpdate(int id, String url, int fileDownloadSize) {
				handler.sendEmptyMessage(0);
			}

			@Override
			public void onDownloadStart(FileDownloader fileDownloader, int id,
					int fileTotalSize) {
				handler.sendEmptyMessage(0);
			}

			@Override
			public void onDownloadError(int id, Exception e) {
				File file = new File(fileSaveDir);
				if (file.exists()) {
					file.delete();
				}
				((Activity) mContext).runOnUiThread(new Runnable() {
					@Override
					public void run() {
						CustomToast.showToast(mContext, "文章下载失败，请重试", 2000);
					}
				});
				downVoa.isClickDownload = false;
			}

			@Override
			public void onDownloadComplete(final int id, String string) {
				((Activity) mContext).runOnUiThread(new Runnable() {

					@Override
					public void run() {
						int downloadNumWithNoInetgeral = ConfigManagerVOA.Instance(mContext)
								.loadInt("download_num_with_no_integral");
						if (!AccountManager.getInstance().isVip(mContext)) {
							if (downloadNumWithNoInetgeral < FREE_DOWNLOAD_LIMIT) {
								ConfigManagerVOA.Instance(mContext).putInt(
										"download_num_with_no_integral",
										downloadNumWithNoInetgeral + 1);
							} else {
								Message msg = handler.obtainMessage(3);
								msg.obj = downVoa.voaid;
								handler.sendMessage(msg);
							}
						}

						if (modedownload) {
							for (int i = 0; i < mList.size(); i++) {
								if (mList.get(i).voaid == id) {
									mList.remove(i);
									break;
								}
							}
						}
						vop.updateDataByDownload(id);
						notifyDataSetChanged();
					}
				});
				downVoa.isClickDownload = false;
			}

			@Override
			public void onDownloadStoped(int id) {
				// TODO 自动生成的方法存根

			}
		});
	}

	private void reduceCredit(int voaid) {
		UseCreditsRequest request = new UseCreditsRequest(
				Integer.valueOf(AccountManager.getInstance().userId), voaid,
				new RequestCallBack() {
					@Override
					public void requestResult(Request result) {
						UseCreditsRequest rs = (UseCreditsRequest) result;
						if (rs.result.equals("1")) {
							CustomToast.showToast(mContext, "下载扣除" + Constant.coin
									+ "积分，您还剩下" + rs.totalcredit + "积分", 2000);
						} else {
							CustomToast.showToast(mContext, "oops~,服务器跪了", 2000);
						}
					}
				});
		CrashApplication.getInstance().getQueue().add(request);
	}

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				notifyDataSetChanged();
				break;
			case 1:
				CustomToast.showToast(mContext, "您的积分不够，您可以通过完成任务获取积分或购买vip！", 2000);
				break;
			case 2:
				download();
				break;
			case 3:
				int voaid = (Integer) msg.obj;
				reduceCredit(voaid);
				break;
			default:
				break;
			}
		}
    };

	private String getAudioUrl(Voa voa) {
		String url;
		if (!AccountManager.getInstance().isVip(mContext)
				|| !ConfigManagerVOA.Instance(mContext).loadBoolean("highspeed_download")) {
			url = getVipAudioUrl(voa);
		} else {
			url = getUnVipAudioUrl(voa);
		}
		return url;
	}

	private String getVipAudioUrl(Voa voa) {
		String url;
		if (Constant.getAppid().equals("213") || Constant.getAppid().equals("217")
				|| Constant.getAppid().equals("229")) {
			url = Constant.getAudiourl() + voa.voaid + Constant.getAppend();
		} else {
			url = Constant.getAudiourl() + voa.sound;
		}
		return url;
	}

	private String getUnVipAudioUrl(Voa voa) {
		String url;
		if (Constant.getAppid().equals("213") || Constant.getAppid().equals("217")
				|| Constant.getAppid().equals("229")) {
			url = Constant.getAudiourlVip() + voa.voaid + Constant.getAppend();
		} else {
			url = Constant.getAudiourlVip() + voa.sound;
		}
		return url;
	}

	private String mergeAudioPath(int voaid) {
		return ConfigManagerVOA.Instance(mContext).loadString("media_saving_path")
				+ "temp_audio_" + voa.voaid + Constant.getAppend();
	}

	/**
	 * 是否存在文件
	 * 
	 * @param voa
	 * @return
	 */
	private boolean isAudioFileExist(Voa voa) {
		File downFile = new File(mergeAudioPath(voa.voaid));
        return downFile.exists();
    }

	/**
	 * 文件是否完整（下载完成状态）
	 * 
	 * @param voa
	 * @return
	 */
	private boolean isAudioFileComplete(Voa voa) {
        return !(fileService.isInDownloadDB(getUnVipAudioUrl(voa))
                || fileService.isInDownloadDB(getVipAudioUrl(voa)));
    }

	private void collect(int voaid) {
		VoaDataManager.getInstance().addDataToCollection(voaid);
		CustomToast.showToast(mContext, "收藏成功，您可在个人中心查看收藏", 1000);
		notifyDataSetChanged();
	}

	private void uncollect(int voaid) {
		VoaDataManager.getInstance().deleteDataByCollection(voaid);
		CustomToast.showToast(mContext, "取消收藏成功", 1000);
		notifyDataSetChanged();
	}

	public void updateCollectState(int voaId, boolean state) {
		for (Voa voa : mList) {
			if (voa.voaid == voaId) {
				voa.favourite = state;
				break;
			}
		}
		notifyDataSetChanged();
	}

	private boolean isOverFreeDownloadLimit() {
		int usedFreeCount = ConfigManagerVOA.Instance(mContext).loadInt(
				"download_num_with_no_integral");
		return usedFreeCount >= FREE_DOWNLOAD_LIMIT;
	}

	private void checkUserCreditThenDownload(final Voa voa) {
		UserInfoManager.getUserInfo(AccountManager.getInstance().userId,
				new CommonCallBack() {

					@Override
					public void onPositive(Object object) {
						UserInfo uinfo = (UserInfo) object;
						if (uinfo.credits < Constant.coin) {
							CustomToast.showToast(mContext, "您的积分不够，您可以通过完成任务获取积分或购买vip！",
									2000);
						} else {
							checkNetworkAndDownload(voa);
						}
					}

					@Override
					public void onNegative(Object object) {
						Log.e(TAG, "on Negative");
					}
				});
	}

}
