package com.iyuba.camstory.adpater;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap.Config;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.iyuba.camstory.R;
import com.iyuba.camstory.VipCenterGoldActivity;
import com.iyuba.camstory.bean.VoaDetail;
import com.iyuba.camstory.frame.CrashApplication;
import com.iyuba.camstory.listener.AnimateFirstDisplayListener;
import com.iyuba.camstory.manager.AccountManager;
import com.iyuba.camstory.manager.VoaDataManager;
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
import com.mcxiaoke.popupmenu.PopupMenuCompat;
import com.mcxiaoke.popupmenu.PopupMenuCompat.OnMenuItemClickListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class HistoryAdapter extends BaseAdapter {
	
	private Context mContext;
	private ArrayList<Voa> mList;
	private LayoutInflater inflater ;
	public boolean modeDelete = false;
	public boolean modedownload = false;
	private AnimateFirstDisplayListener animateFirstListener;
	private DisplayImageOptions mDisplayImageOptions;
	private VoaOp vOp;
	private Voa voa;
	private int isConnect;
	private AlertDialog alertDialog;
	private CheckBox checkBox;
	private VoaDetailOp vdop;
	private FileService fileService;
	private FileDownloader fDownloader;

	private PopupMenuCompat popupMenuCompat;

	public HistoryAdapter(Context context) {
		mContext = context;
		init();
	}
	
	public HistoryAdapter(Context context, ArrayList<Voa> list) {
		mContext = context;
		if (list != null) {
			mList = list;
		}
		init();
	}
	private void init() {
		// TODO 自动生成的方法存根
		vOp = new VoaOp();
		vdop= new VoaDetailOp();
		fileService = new FileService(mContext);
		inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		animateFirstListener = new AnimateFirstDisplayListener();
		alertDialog = new AlertDialog.Builder(mContext).setPositiveButton(R.string.ok,new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO 自动生成的方法存根
				download();
			}
		})
		.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO 自动生成的方法存根
				dialog.cancel();
			}
		}).create();
		View layout = inflater.inflate(R.layout.mydialog, null);
		alertDialog.setView(layout);
		alertDialog.setTitle(R.string.alert);
		checkBox = layout.findViewById(R.id.checkBox_remember);
		//初始化列表图片所需的displayImageOptions
				if (Constant.getAppid().equals("215")
						||Constant.getAppid().equals("221")) {
					mDisplayImageOptions = 
							CrashApplication.getInstance().getDefaultDisplayImageOptionsBuilder()
							.displayer(new FadeInBitmapDisplayer(0))//设置圆角
							//.imageScaleType(ImageScaleType.NONE)
							.bitmapConfig(Config.RGB_565)
							//.showImageOnLoading(R.drawable.nearby_no_icon)//默认图片
							.build();
							//进行加载
				}
				else {
					mDisplayImageOptions = 
							CrashApplication.getInstance().getDefaultDisplayImageOptionsBuilder()
							.displayer(new RoundedBitmapDisplayer(15))//设置圆角
							//.imageScaleType(ImageScaleType.IN_SAMPLE_INT)
							.bitmapConfig(Config.RGB_565)
							.showImageOnLoading(R.drawable.nearby_no_icon)//默认图片
							.build();
							//进行加载
				}
	}
	
	public void addList(List<Voa> voasTemps) {
		mList.addAll(voasTemps);
	}
	
	public void replaceList(List<Voa> voasTemps) {
		mList.clear();
		mList.addAll(voasTemps);
	}
	
	public void deleteAllCancle() {
		for (Iterator iterator = mList.iterator(); iterator.hasNext();) {
			Voa type = (Voa) iterator.next();
			type.isDelete  = false;
			notifyDataSetChanged();
		}
	}
	
	public void deleteAll() {
		for (Iterator iterator = mList.iterator(); iterator.hasNext();) {
			Voa type = (Voa) iterator.next();
			type.isDelete  = true;
			notifyDataSetChanged();
		}
	}

	@Override
	public int getCount() {
		// TODO 自动生成的方法存根
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO 自动生成的方法存根
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO 自动生成的方法存根
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO 自动生成的方法存根
		final Voa curVoa = mList.get(position);
		final ViewHolder viewHolder;
		if (convertView == null) {
			
			convertView = inflater.inflate(R.layout.listitem_history2, null);
			viewHolder = new ViewHolder();
			viewHolder.imageView = convertView.findViewById(R.id.image);
			viewHolder.name = convertView.findViewById(R.id.name);
			viewHolder.readTimeLabelText = convertView.findViewById(R.id.read_time_label);
			viewHolder.readTimeText = convertView.findViewById(R.id.read_time);
			viewHolder.name = convertView.findViewById(R.id.name);
			viewHolder.collectImageButton = convertView.findViewById(R.id.collectbutton);
			viewHolder.downloadImageButton = convertView.findViewById(R.id.downloadbutton);
			viewHolder.progressWheel = convertView.findViewById(R.id.videodownload_wheel);
			convertView.setTag(viewHolder);
			
		}
		else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		//显示图片
		ImageLoader.getInstance().displayImage(
				curVoa.pic,
				viewHolder.imageView,
				mDisplayImageOptions,
				animateFirstListener
				);
		//显示标题
		if (ConfigManagerVOA.Instance(mContext).loadString("applanguage").equals("zh")) {
			viewHolder.name.setText(curVoa.title_cn);
		}
		else {
			viewHolder.name.setText(curVoa.title);
		}
		
		viewHolder.readTimeText.setText(getTime(curVoa));
		viewHolder.readTimeLabelText.setText(mContext.getResources().getString(R.string.last_read_time));
		//设置点击下载
		OnClickListener menuClickListener = new OnClickListener() {
			@Override
			public void onClick(final View v) {
				// TODO 自动生成的方法存根
				popupMenuCompat = new PopupMenuCompat(mContext, v);
						popupMenuCompat.inflate(R.menu.newlist_);
				popupMenuCompat.setOnMenuItemClickListener(new OnMenuItemClickListener() {
					@Override
					public boolean onMenuItemClick(MenuItem item) {
						// TODO 自动生成的方法存根
						if (item.getItemId() == R.id.newlist_menu_download) {
							voa = curVoa;
							if (AccountManager.getInstance().isVip(mContext)) {
								checkNetWork();
							} else{
								List<Voa> temp = vOp.findDataByDownload();
								if (temp == null) {
									checkNetWork();
								} else if (temp.size() > 9) {
									AlertDialog alert = new AlertDialog.Builder(mContext)
											.create();
									alert.setTitle(mContext.getResources().getString(
											R.string.alert));
									alert.setMessage(mContext.getResources().getString(
											R.string.nladapter_notvip));
									alert.setIcon(android.R.drawable.ic_dialog_alert);
									alert.setButton(
											AlertDialog.BUTTON_POSITIVE,
											mContext.getResources().getString(
													R.string.alert_btn_buy),
											new DialogInterface.OnClickListener() {

												@Override
												public void onClick(DialogInterface dialog,
														int which) {
													Intent intent = new Intent();
													intent.setClass(mContext,
															VipCenterGoldActivity.class);
													mContext.startActivity(intent);
												}
											});
									alert.setButton(
											AlertDialog.BUTTON_NEGATIVE,
											mContext.getResources().getString(
													R.string.alert_btn_cancel),
											new DialogInterface.OnClickListener() {
												@Override
												public void onClick(DialogInterface dialog,
														int which) {
												}
											});
									alert.show();
								} else {
									checkNetWork();
								}
							}
						}
						else if (item.getItemId() == R.id.newlist_menu_collect) {
							collect(curVoa.voaid);
						}
						else if (item.getItemId() == R.id.newlist_menu_bell) {
							new AlertDialog.Builder(mContext)
							.setTitle(mContext.getResources().getString(R.string.alert))
							.setMessage(R.string.setting_wakeup_set)
							.setPositiveButton(mContext.getResources().getString(R.string.alert_btn_ok),
									new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface dialog,
												int whichButton) {
											// TODO Auto-generated method stub
											//如果所选文章已经下载到本地了（有这篇文章的音频文件）
										if (new File(Constant.getVideoAddr()+curVoa.voaid+Constant.getAppend()).exists()) {
												String bellvoaid=Constant.getVideoAddr()+curVoa.voaid;
												ConfigManagerVOA.Instance(mContext).putString("belladdress",bellvoaid+Constant.getAppend());
												ConfigManagerVOA.Instance(mContext).putString("bellurl",Constant.getAudiourl()+curVoa.sound);
												ConfigManagerVOA.Instance(mContext).putString("bellvoaid",curVoa.voaid+"");
												Toast.makeText(mContext,R.string.setting_wakeup_bellok, 2000).show();
										}
											else {
												//弹出未下载的提示
												Toast.makeText(mContext,R.string.setting_wakeup_notdownload, 2000).show();
											}
									
										}
									}).show();
						}
						return false;
					}});
				popupMenuCompat.show();
			}};
			if (curVoa.favourite) {
				viewHolder.collectImageButton.setImageResource(R.mipmap.news_collected);
			}
			else {
				viewHolder.collectImageButton.setImageResource(R.mipmap.news_uncollect);
			}
		viewHolder.collectImageButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				if (curVoa.favourite) {
					curVoa.favourite = false;
					uncollect(curVoa.voaid);
				}
				else {
					curVoa.favourite = true;
					collect(curVoa.voaid);
				}
			}
		});
		OnClickListener downListener =new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				voa = curVoa;
				if (AccountManager.getInstance().isVip(mContext)) {
					checkNetWork();
				} else{
					List<Voa> temp = vOp.findDataByDownload();
					if (temp == null) {
						checkNetWork();
					} else if (temp.size() > 9) {
						AlertDialog alert = new AlertDialog.Builder(mContext)
								.create();
						alert.setTitle(mContext.getResources().getString(
								R.string.alert));
						alert.setMessage(mContext.getResources().getString(
								R.string.nladapter_notvip));
						alert.setIcon(android.R.drawable.ic_dialog_alert);
						alert.setButton(
								AlertDialog.BUTTON_POSITIVE,
								mContext.getResources().getString(
										R.string.alert_btn_buy),
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										Intent intent = new Intent();
										intent.setClass(mContext,
												VipCenterGoldActivity.class);
										mContext.startActivity(intent);
									}
								});
						alert.setButton(
								AlertDialog.BUTTON_NEGATIVE,
								mContext.getResources().getString(
										R.string.alert_btn_cancel),
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
									}
								});
						alert.show();
					} else {
						checkNetWork();
					}
				}
			}
		};
		viewHolder.downloadImageButton.setOnClickListener(downListener);
		viewHolder.progressWheel.setOnClickListener(downListener);
		voa  = curVoa;
		if (isAudioFileExist(curVoa)
				&&!isAudioFileComplete(curVoa)) {
			viewHolder.progressWheel.setVisibility(View.VISIBLE);
			//progress是0-360度
			viewHolder.progressWheel.setProgress((int) ((MultiThreadDownloadManager.getTaskPercentage(curVoa.voaid)/100f)*360));
			viewHolder.progressWheel.setText(MultiThreadDownloadManager.getTaskPercentage(curVoa.voaid)+"%");
		}
		else{
			viewHolder.progressWheel.setVisibility(View.GONE);
		}
		if (isAudioFileExist(curVoa)
				&&isAudioFileComplete(curVoa)) {
			MultiThreadDownloadManager.removeTask(curVoa.voaid);
			viewHolder.downloadImageButton.setImageResource(R.mipmap.news_downloaded);
			viewHolder.downloadImageButton.setVisibility(View.VISIBLE);
		}
		else if (isAudioFileExist(curVoa)) {
			viewHolder.downloadImageButton.setVisibility(View.INVISIBLE);
		}
		else {
			viewHolder.downloadImageButton.setImageResource(R.mipmap.news_download);
			viewHolder.downloadImageButton.setVisibility(View.VISIBLE);
		}
		return convertView;
	}
	
	private CharSequence getTime(Voa voa) {
		Calendar c1=Calendar.getInstance();
		Calendar c2=Calendar.getInstance();
		c1.setTimeInMillis(Long.valueOf(voa.isRead));
		c2.setTimeInMillis(System.currentTimeMillis());
		 if (isSameDay(c1,c2)) {
			 DateFormat format1 = new SimpleDateFormat("HH:mm");
			 return format1.format(c1.getTime());  
			}
		 else {
			 DateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			 return format2.format(c1.getTime());  
		}  
	}
	
	private boolean isSameDay(Calendar c1, Calendar c2) {
		// TODO 自动生成的方法存根
        return c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)
                && (c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH))
                && c1.get(Calendar.DAY_OF_MONTH) == c2.get(Calendar.DAY_OF_MONTH);
	}
	
	private void checkNetWork() {
		isConnect = NetWorkStateUtil.getAPNType();
		if (isConnect == 0) {
			CustomToast.showToast(mContext, R.string.category_check_network,
					1000);
		} else if (isConnect == 1) {
			if (!ConfigManagerVOA.Instance(mContext).loadBoolean("remember_download_ok"))
				alertDialog.show();
			else if (isAudioFileExist(voa)
				&&isAudioFileComplete(voa)) {
					vOp.updateDataByDownload(voa.voaid);
					voa.isDownloaded = true;
					CustomToast.showToast(mContext, "已下载", 1000);
				}
				else {
					vOp.deleteDataByDownload(voa.voaid);
					voa.isDownloaded = false;
					download();
				}
		} else if (isConnect == 2) {
			if(isAudioFileExist(voa)
					&&isAudioFileComplete(voa)) {
				vOp.updateDataByDownload(voa.voaid);
				voa.isDownloaded = true;
				CustomToast.showToast(mContext, "已下载", 1000);
				}
			else {
				vOp.deleteDataByDownload(voa.voaid);
				voa.isDownloaded = false;
				download();
			}
		}
	}
	
	private void download() {
		//下载地址、存储路径
		final Voa downVoa = voa;
		final String url;
		final String fileSaveDir;
		if (fileService.isInDownloadDB(voa.voaid)) {
			url = fileService.findDownloadUrl(voa.voaid);
		}
		else {
			url = getAudioUrl(downVoa);
		}
		
		Log.e("url", url);
		
		File file = new File(ConfigManagerVOA.Instance(mContext).loadString("media_saving_path"));
		if (!file.exists()) {
			file.mkdirs();
		}
		//存原文
		if (vdop.findDataByVoaId(downVoa.voaid).size() == 0) {
			VoaDataManager.getInstance().getDetail(downVoa,new VoaDataManager.DetailCallback() {
				@Override
				public void onDetail(ArrayList<VoaDetail> details) {
					// TODO 自动生成的方法存根
					if (details == null 
							|| details.size()==0) {
						((Activity)mContext).runOnUiThread(new Runnable() {
							@Override
							public void run() {
								// TODO 自动生成的方法存根
								CustomToast.showToast(mContext, "文章内容获取失败..请在播放页重新获取", 1000);
							}
						});
					}
					else{
						vdop.saveData(details);
					}
				}
			});
			}
		fileSaveDir = mergeAudioPath(downVoa.voaid);
		Log.e("mergeAudioPath", fileSaveDir);
		MultiThreadDownloadManager.enQueue(
				mContext,
				downVoa.voaid,
				url,
				new File(fileSaveDir),
				2,new DownloadProgressListener() {
					@Override
					public void onProgressUpdate(int id,String url,int fileDownloadSize) {
						// TODO 自动生成的方法存根
						handler.sendEmptyMessage(0);
//						Log.e("onProgressUpdate", url+"  : "+MultiThreadDownloadManager.getTaskPercentage(id));
					}
					
					@Override
					public void onDownloadStart(FileDownloader fileDownloader,int id,int fileTotalSize) {
						// TODO 自动生成的方法存根
						handler.sendEmptyMessage(0);
						
					}
					
					@Override
					public void onDownloadError(int id,Exception e) {
						// TODO 自动生成的方法存根
						File file = new File(fileSaveDir);
						if (file.exists()) {
							file.delete();
						}
						((Activity)mContext).runOnUiThread(new Runnable() {
							@Override
							public void run() {
								// TODO 自动生成的方法存根
								CustomToast.showToast(mContext, "文章下载失败，请重试", 2000);
							}
						});
					}
					
					@Override
					public void onDownloadComplete(final int id,String string) {
						// TODO 自动生成的方法存根
						((Activity)mContext).runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								// TODO 自动生成的方法存根
								if (modedownload) {
									for (int i = 0;i<mList.size();i++) {
										if (mList.get(i).voaid == id) {
											mList.remove(i);
											break;
										}
									}
								}
								vOp.updateDataByDownload(id);
								notifyDataSetChanged();
							}
						});
					}

					@Override
					public void onDownloadStoped(int id) {
						// TODO 自动生成的方法存根
						
					}
				});
	}
	
	
	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				notifyDataSetChanged();
				break;

			default:
				break;
			}
		}
    };
	
	private String getAudioUrl(Voa voa){
		String url;
		if (!AccountManager.getInstance().isVip(mContext)
				||!ConfigManagerVOA.Instance(mContext).loadBoolean("highspeed_download")) {
			url = getVipAudioUrl(voa);
		}
		else{
			url = getUnVipAudioUrl(voa);
		}
		return url;
	}
	private String getVipAudioUrl(Voa voa){
		String url;
			 if (Constant.getAppid().equals("213")
						||Constant.getAppid().equals("217")) {
				 url = Constant.getAudiourl() + voa.voaid + Constant.getAppend();
			}
			 else {
				 url = Constant.getAudiourl() + voa.sound;
			}
		return url;
	}
	private String getUnVipAudioUrl(Voa voa){
		String url;
		 if (Constant.getAppid().equals("213")
					||Constant.getAppid().equals("217")) {
			 url = Constant.getAudiourlVip() + voa.voaid + Constant.getAppend();
		}
		 else {
			 url = Constant.getAudiourlVip() + voa.sound;
		}
		 return url;
	}
	
	private String mergeAudioPath(int voaid){
		return ConfigManagerVOA.Instance(mContext).loadString("media_saving_path")+File.separator+
				"temp_audio_"+voa.voaid +Constant.getAppend();
	}
	
	
	/**
	 * 是否存在文件
	 * @param voa
	 * @return
	 */
	private boolean isAudioFileExist(Voa voa){
		File downFile = new File(mergeAudioPath(voa.voaid));
        return downFile.exists();
    }

    /**
	 * 文件是否完整（下载完成状态）
	 * @param voa
	 * @return
	 */
	private boolean isAudioFileComplete(Voa voa){
        return !(fileService.isInDownloadDB(getUnVipAudioUrl(voa))
                || fileService.isInDownloadDB(getVipAudioUrl(voa)));
    }


    private void collect(int voaid){
		vOp.updateDataByCollection(voaid);
		Toast.makeText(mContext,"收藏成功，您可在个人中心查看收藏", 1000).show();
		notifyDataSetChanged();
	}
	private void uncollect(int voaid){
		vOp.deleteDataByCollection(voaid);
		Toast.makeText(mContext,"取消收藏成功", 1000).show();
		notifyDataSetChanged();
	}
	
	public class ViewHolder {
		public ImageView imageView;
		public TextView name;
		public TextView readTimeLabelText;
		public TextView readTimeText;
		public ImageButton collectImageButton;
		public ImageButton downloadImageButton;
		public ProgressWheel progressWheel;
	}
}
