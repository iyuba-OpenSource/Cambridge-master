package com.iyuba.camstory.fragment;

import java.io.IOException;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.iyuba.camstory.LoginActivity;
import com.iyuba.camstory.R;
import com.iyuba.camstory.adpater.NewCardListAdapter;
import com.iyuba.camstory.adpater.SentenceAdapter;
import com.iyuba.camstory.frame.CrashApplication;
import com.iyuba.camstory.listener.RequestCallBack;
import com.iyuba.camstory.listener.SentenceRequest;
import com.iyuba.camstory.listener.SentenceUploadRequest;
import com.iyuba.camstory.manager.AccountManager;
import com.iyuba.camstory.manager.VoaDataManager;
import com.iyuba.camstory.sqlite.mode.Sentence;
import com.iyuba.camstory.sqlite.mode.Voa;
import com.iyuba.camstory.sqlite.op.VoaDetailOp;
import com.iyuba.camstory.sqlite.op.VoaOp;
import com.iyuba.camstory.widget.CustomDialog;
import com.iyuba.camstory.widget.CustomToast;
import com.iyuba.camstory.widget.WaittingDialog;
import com.iyuba.multithread.FileService;
import com.iyuba.voa.frame.components.ConfigManagerVOA;
import com.umeng.analytics.MobclickAgent;
/**
 * 收藏至本地
 * 
 * @author chentong
 */
public class LocalDownloadFragment extends Fragment {
	private View backView;
	private Context mContext;
	private ArrayList<Voa> voaList = new ArrayList<Voa>();
	private ArrayList<Sentence> sList = new ArrayList<Sentence>();
	private ArrayList<Sentence> temp;// 用于同步时的比较
	private ListView newsList, sentenceList;
	private NewCardListAdapter nla;
	private SentenceAdapter sta;
	private Button uploadButton, downloadButton;
	private boolean isDelStart = false, isSelectAll = false;
	private VoaOp vop;
	private CustomDialog wettingDialog;
	// private RadioGroup radioGroup;
	private int collect_which;// 0是已下载，1是句子,2是未完成下载
	private int ready;
	// private RadioButton videoButton,sentenceButton;
	private View cloudControl;
	private View view;
	FileService fileService;

	public void onAttach(android.app.Activity activity) {

		mContext = activity;
		super.onAttach(activity);
	}

    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		view = inflater.inflate(R.layout.news_collection_list, container, false);
		initView(view);
		return view;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	/*@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO 自动生成的方法存根
		if (collect_which == 0) {
			menu.add(Menu.NONE, Menu.FIRST, Menu.NONE, "编辑")
					.setIcon(R.drawable.button_edit)
					.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		} else if (collect_which == 2) {
			menu.add(Menu.NONE, 2, Menu.NONE, "开始全部")
					.setIcon(R.drawable.news_download)
					.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
			menu.add(Menu.NONE, Menu.FIRST, Menu.NONE, "删除")
					.setIcon(R.drawable.button_edit)
					.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);

		} else {

		}
	}*/

	//private ActionMode mode;

	/*@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO 自动生成的方法存根
		if (item.getItemId() == Menu.FIRST) {
			mode = getSherlockActivity().startActionMode(
					new CollectionEditActionMode());
			if (nla != null)
				nla.notifyDataSetChanged();
			isDelStart = true;
			changeItemDeleteStart(true);
		} else if (item.getItemId() == 2) {
			ArrayList<Voa> tempVoas = new ArrayList<Voa>();
			ArrayList<Integer> integers = fileService.findFileId();
			for (Iterator iterator = integers.iterator(); iterator.hasNext();) {
				Integer integer = (Integer) iterator.next();
				tempVoas.add(vop.findDataById(integer.intValue()));
			}
			if (tempVoas.size() == 0) {
				CustomToast.showToast(mContext, "没有下载中的任务~", 2000);
			}
			// else {
			// if (NetStatusUtil.isConnected(mContext)) {
			// for (Iterator iterator = tempVoas.iterator(); iterator
			// .hasNext();) {
			// Voa voa = (Voa) iterator.next();
			// nla.download(voa);
			// }
			// CustomToast.showToast(mContext, "已开始继续下载", 2000);
			// }
			// else {
			// CustomToast.showToast(mContext, "无网络连接..", 2000);
			// }
			// }
		}
		return true;
	}

	class CollectionEditActionMode implements ActionMode.Callback {
		@Override
		public boolean onCreateActionMode(ActionMode mode, Menu menu) {
			// TODO 自动生成的方法存根
			menu.add(Menu.NONE, Menu.FIRST, Menu.NONE, "删除")
					.setIcon(android.R.drawable.ic_menu_delete)
					.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
			menu.add(Menu.NONE, 2, Menu.NONE, "全选").setShowAsAction(
					MenuItem.SHOW_AS_ACTION_ALWAYS);
			return true;
		}

		@Override
		public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
			// TODO 自动生成的方法存根
			return false;
		}

		@Override
		public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
			// TODO 自动生成的方法存根
			switch (item.getItemId()) {
			case Menu.FIRST:
				Log.e("Menu.FIRST", "Menu.FIRST");
				boolean hasDelete = false;
				if (voaList == null) {
					changeItemDeleteStart(false);
					if (mode != null) {
						mode.finish();
					}
					break;
				}

				Iterator<Voa> iteratorVoa = voaList.iterator();
				while (iteratorVoa.hasNext()) {
					Voa voaTemp = iteratorVoa.next();
					if (voaTemp.isDelete) {
						hasDelete = true;
						vop.deleteDataByDownload(voaTemp.voaid);
						new ClearBuffer(Constant.getFileDownloadTag() + +voaTemp.voaid
								+ Constant.getAppend()).Delete();
						iteratorVoa.remove();
						MultiThreadDownloadManager.removeTask(voaTemp.voaid);
					}
				}
				if (hasDelete) {
					nla.notifyDataSetChanged();
					isDelStart = false;
					cancelDelete();
					isDelStart = false;
					changeItemDeleteStart(false);
				} else {
					CustomToast.showToast(mContext,
							R.string.collection_please_select_art, 1000);
				}
				// deleteDel.setText(R.string.delete);
				cancelDelete();
				isDelStart = false;
				// selectAll.setVisibility(View.GONE);
				changeItemDeleteStart(false);
				if (mode != null) {
					mode.finish();
				}
				break;
			case 2:
				if (isSelectAll) {
					if (nla != null) {
						nla.modeDelete = isDelStart;
						nla.deleteAllCancel();
						isSelectAll = !isSelectAll;
					}

				} else {
					if (nla != null) {
						nla.modeDelete = isDelStart;
						nla.deleteAll();
						isSelectAll = !isSelectAll;
					}
				}
				break;
			default:
				break;
			}
			return true;
		}

		@Override
		public void onDestroyActionMode(ActionMode mode) {
			// TODO 自动生成的方法存根
			cancelDelete();
			isDelStart = false;
			changeItemDeleteStart(false);
		}

	}*/

	protected void initView(View view) {
		// TODO Auto-generated method stub
		ready = 0;
		fileService = new FileService(mContext);
		backView = view.findViewById(R.id.backlayout);
		backView.setBackgroundColor(Color.WHITE);
		wettingDialog = new WaittingDialog().wettingDialog(mContext);
		// radioGroup=(RadioGroup)view.findViewById(R.id.collection_change);
		uploadButton = view.findViewById(R.id.cloud_upload);
		downloadButton = view.findViewById(R.id.cloud_download);
		uploadButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				if (AccountManager.getInstance().checkUserLogin()) {
					Dialog dialog = new AlertDialog.Builder(mContext)
							.setIcon(android.R.drawable.ic_dialog_alert)
							.setTitle(getResources().getString(R.string.alert))
							.setMessage(
									getResources().getString(R.string.collection_upload_msg))
							.setPositiveButton(
									getResources().getString(R.string.alert_btn_ok),
									new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface dialog, int whichButton) {
											// TODO Auto-generated method stub
											handleCloud.sendEmptyMessage(0);
										}
									})
							.setNeutralButton(
									getResources().getString(R.string.alert_btn_cancel),
									new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface dialog, int which) {
										}
									}).create();
					dialog.show();
				} else {
					Intent intent = new Intent(mContext, LoginActivity.class);
					startActivity(intent);
				}

			}
		});
		downloadButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				if (AccountManager.getInstance().checkUserLogin()) {
					Dialog dialog = new AlertDialog.Builder(mContext)
							.setIcon(android.R.drawable.ic_dialog_alert)
							.setTitle(getResources().getString(R.string.alert))
							.setMessage(
									getResources().getString(R.string.collection_download_msg))
							.setPositiveButton(
									getResources().getString(R.string.alert_btn_ok),
									new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface dialog, int whichButton) {
											// TODO Auto-generated method stub
											handleCloud.sendEmptyMessage(1);
										}
									})
							.setNeutralButton(
									getResources().getString(R.string.alert_btn_cancel),
									new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface dialog, int which) {
										}
									}).create();
					dialog.show();
				}

				else {
					Intent intent = new Intent(mContext, LoginActivity.class);
					startActivity(intent);// 前往登录界面
				}
			}
		});

		// 下面都是两个LIST相关的event，动画等等。
		newsList = view.findViewById(R.id.news_list);
		sentenceList = view.findViewById(R.id.sentence_list);
		sentenceList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO 自动生成的方法存根
				if (arg1.findViewById(R.id.sentence_cn_text).getVisibility() == View.GONE) {
					arg1.findViewById(R.id.sentence_cn_text).setVisibility(View.VISIBLE);
					arg1.findViewById(R.id.sentence_image).setVisibility(View.VISIBLE);
				} else {
					arg1.findViewById(R.id.sentence_cn_text).setVisibility(View.GONE);
					arg1.findViewById(R.id.sentence_image).setVisibility(View.GONE);
				}

			}

		});
		sentenceList.setOnTouchListener(new OnTouchListener() {
			float x, y, upx, upy;

			public boolean onTouch(View view, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					x = event.getX();
					y = event.getY();
				}
				if (event.getAction() == MotionEvent.ACTION_UP) {
					upx = event.getX();
					upy = event.getY();
					int position1 = ((ListView) view).pointToPosition((int) x, (int) y);
					int position2 = ((ListView) view).pointToPosition((int) upx,
							(int) upy);

					if (position1 == position2 && Math.abs(x - upx) > 80
							&& Math.abs(y - upy) < 30) {
						View v = ((ListView) view).getChildAt(position1
								- ((ListView) view).getFirstVisiblePosition());
						if (upx > x) {
							removeListItem(v, position1, 0);// 0,1代表不同方向的动画
						} else {
							removeListItem(v, position1, 1);
						}
					}
				}
				return false;
			}

		});
		try {
			collect_which = ConfigManagerVOA.Instance(mContext).loadInt("collectwhich");
		} catch (Exception e) {
			// TODO: handle exception
			collect_which = 0;
		}
		cloudControl = view.findViewById(R.id.cloud_control);
		handler.removeMessages(0);
		handler.sendEmptyMessage(0);
	}

	// 动画--删除动作
	/**
	 * @param rowView
	 *          要移除的view
	 * @param positon
	 *          位置
	 * @param i
	 *          0向右动画，1向左
	 * 
	 * 
	 * 
	 */
	protected void removeListItem(View rowView, final int positon, int i) {
		final Animation animation;
		if (rowView != null && i == 0) {
			animation = AnimationUtils.loadAnimation(
					rowView.getContext(), R.anim.collect_delete);
		} else if (rowView != null && i == 1) {
			animation = AnimationUtils.loadAnimation(
					rowView.getContext(), R.anim.collect_delete_left);
		} else {
			return;
		}
		animation.setAnimationListener(new AnimationListener() {
			public void onAnimationStart(Animation animation) {
			}

			public void onAnimationRepeat(Animation animation) {
			}

			public void onAnimationEnd(Animation animation) {
				if (positon >= 0 && positon <= sList.size()) {
					sList.remove(positon);// /※※※※※
					if (sta != null) {
						sta.notifyDataSetChanged();
					}
				}
				try {
					ConfigManagerVOA.Instance(mContext).putString("sentencelist", sList);
				} catch (IOException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
				// sta.notifyDataSetChanged();
				// notifydatasetchanged在这里总是显示不正确，
				// 所以选择了发送广播，，重新加载CollectionActivity,列表刷新才正常
				// 所以需要在oncreate中加入判断选择了哪个list的逻辑
				Intent intent = new Intent();
				intent.setAction("collect");
				mContext.sendBroadcast(intent);
				animation.cancel();

			}
		});

		rowView.startAnimation(animation);

	}

	// 以上均为两个个list切换及相关按键逻辑
	public void changeItemDeleteStart(boolean isDelete) {
		if (nla != null) {
			nla.modeDelete = isDelStart;
			nla.notifyDataSetChanged();
		}
	}

	public void cancelDelete() {
		if (voaList != null) {
			for (int i = 0; i < voaList.size(); i++) {
				voaList.get(i).isDelete = false;
			}
		}
	}

	Handler handleCloud = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO 自动生成的方法存根
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				// 上传句子
				handleCloud.sendEmptyMessage(2);

				if (ready == 0) {
					handleCloud.sendEmptyMessageDelayed(0, 100);
					return;
				}
				for (int i = 0; i < sList.size(); i++) {// 本地有，云端没有，插入
					if (temp.indexOf(sList.get(i)) == -1) {
						handleCloud.obtainMessage(3, sList.get(i)).sendToTarget();
					}

				}
				for (int i = 0; i < temp.size(); i++) {// 云端有，本地没有，删除
					if (sList.indexOf(temp.get(i)) == -1) {
						handleCloud.obtainMessage(4, temp.get(i)).sendToTarget();
					}

				}

				break;
			case 1:
				// 下载句子
				SentenceRequest request = new SentenceRequest(
						AccountManager.getInstance().userId, 1, new RequestCallBack() {
							@Override
							public void requestResult(Request result) {
								// TODO 自动生成的方法存根
								SentenceRequest rs = (SentenceRequest) result;
								// 用hashset去除重复元素，然后恢复
								temp = rs.sList;
								temp.addAll(sList);
								HashSet<Sentence> hs = new HashSet<Sentence>(temp);
								sList.clear();
								sList.addAll(hs);// 恢复slist
								Collections.sort(sList, new CompareSentence());
								if (sta != null) {
									sta.notifyDataSetChanged();
								} else {
									sta = new SentenceAdapter(sList);
									sentenceList.setAdapter(sta);
								}
								if (sList != null) {
									try {
										ConfigManagerVOA.Instance(mContext).putString("sentencelist",
												sList);
									} catch (IOException e) {
										// TODO 自动生成的 catch 块
										e.printStackTrace();
									}
								} else {
									// CustomToast.showToast(mContext,
									// R.string.new_word_sync_fai, 1000);
									// wettingDialog.dismiss();
								}
							}
						});
				CrashApplication.getInstance().getQueue().add(request);
				break;
			case 2:
				// 先获取服务器端的数据
				SentenceRequest request1 = new SentenceRequest(
						AccountManager.getInstance().userId, 1, new RequestCallBack() {
							@Override
							public void requestResult(Request result) {
								// TODO 自动生成的方法存根
								SentenceRequest rs = (SentenceRequest) result;
								temp = rs.sList;
								ready = 1;
							}
						});
				CrashApplication.getInstance().getQueue().add(request1);

				break;
			case 3:
				SentenceUploadRequest request2 = new SentenceUploadRequest(
						AccountManager.getInstance().userId, "insert",
						((Sentence) msg.obj).voaid, (float) ((Sentence) msg.obj).starttime,
						new RequestCallBack() {
							@Override
							public void requestResult(Request result) {
								// TODO 自动生成的方法存根
							}
						});
				CrashApplication.getInstance().getQueue().add(request2);
				break;
			case 4:
				// 删除
				SentenceUploadRequest request3 = new SentenceUploadRequest(
						AccountManager.getInstance().userId, "del",
						((Sentence) msg.obj).voaid, (float) ((Sentence) msg.obj).starttime,
						new RequestCallBack() {
							@Override
							public void requestResult(Request result) {
								// TODO 自动生成的方法存根
							}
						});
				CrashApplication.getInstance().getQueue().add(request3);
				break;
			default:
				break;
			}
		}

	};

	/**
	 * 用于句子的list的排序的比较器
	 * 
	 */
	class CompareSentence implements Comparator<Sentence> {

		@Override
		public int compare(Sentence lhs, Sentence rhs) {
			// TODO 自动生成的方法存根
			int flag;
			if (lhs.voaid < rhs.voaid) {
				flag = -1;
				return flag;
			} else if (lhs.voaid > rhs.voaid) {
				flag = 1;
				return 1;
			} else if (lhs.starttime < rhs.starttime) {
				flag = -1;
				return flag;
			} else if (lhs.starttime > rhs.starttime) {
				flag = 1;
				return flag;
			} else {
				return 0;
			}

		}
	}

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO 自动生成的方法存根
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				// if (MultiThreadDownloadManager.IsDowning()
				// && nla != null) {
				// nla.notifyDataSetChanged();
				// }
				// sendEmptyMessageDelayed(0, 1000);
				break;
			case 1:
				CustomToast.showToast(mContext, R.string.play_check_network, 1000);
				break;

			default:
				break;
			}
		}

	};

	Handler handler_1 = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 8:
				wettingDialog.show();
				break;
			case 9:
				wettingDialog.dismiss();
				break;
			}
		}
	};

	@SuppressWarnings("unchecked")
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onPageStart("CollectionPage"); // 统计页面
		try {
			sList = (ArrayList<Sentence>) ConfigManagerVOA.Instance(mContext)
					.loadObject("sentencelist");
			sta = new SentenceAdapter(sList);
			sentenceList.setAdapter(sta);
		} catch (StreamCorruptedException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			Log.d("LoadObject", "No Object");
			// e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		if (newsList != null) {
			isDelStart = false;

			if (nla != null) {
				cancelDelete();
				isDelStart = false;
				changeItemDeleteStart(false);
			}
			vop = new VoaOp();
			voaList = (ArrayList<Voa>) vop.findDataByDownload();
			// Log.d("list", (voaList==null)+"");
			if (voaList != null && voaList.size() != 0) {
				nla = new NewCardListAdapter(mContext, voaList);
				newsList.setAdapter(nla);
				newsList.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
							long arg3) {
						// TODO Auto-generated method stub

						VoaDataManager.getInstance().voaTemp = voaList.get(arg2);
						if (isDelStart) {
                            voaList.get(arg2).isDelete = !voaList.get(arg2).isDelete;
							nla.notifyDataSetChanged();
						} else {
							handler_1.sendEmptyMessage(8);
							VoaDetailOp vdop = new VoaDetailOp();
							VoaDataManager.getInstance().voaDetailsTemp = vdop
									.findDataByVoaId(voaList.get(arg2).voaid);
							VoaDataManager.getInstance().setSubtitleSum(voaList.get(arg2),
									VoaDataManager.getInstance().voaDetailsTemp);
							handler_1.sendEmptyMessage(9);
							/*Intent intent = new Intent();
							intent.setClass(mContext, StudyActivity.class);
							intent.putExtra("source", 100);
							intent.putExtra("from", "collection");
							startActivity(intent);*/
						}
					}
				});
			} else {

			}
		}

		if (sentenceList != null) {

		}

		if (collect_which == 0 || collect_which == 2) {
			newsList.setVisibility(View.VISIBLE);
			sentenceList.setVisibility(View.GONE);
			cloudControl.setVisibility(View.GONE);
		} else {
			newsList.setVisibility(View.GONE);
			sentenceList.setVisibility(View.VISIBLE);
			cloudControl.setVisibility(View.VISIBLE);
		}

	}

	@Override
	public void onPause() {
		// TODO 自动生成的方法存根
		super.onDestroy();
		MobclickAgent.onPageEnd("CollectionPage");

	}

	@SuppressLint("NewApi")
	public void showUnFinish() {
		// TODO 自动生成的方法存根
		collect_which = 2;
		//getSherlockActivity().invalidateOptionsMenu();
		ArrayList<Voa> tempVoas = new ArrayList<Voa>();
		ArrayList<Integer> integers = fileService.findFileId();
		for (Iterator iterator = integers.iterator(); iterator.hasNext();) {
			Integer integer = (Integer) iterator.next();
			tempVoas.add(vop.findDataById(integer.intValue()));
		}
		if (voaList != null) {
			voaList.clear();
			if (tempVoas != null) {
				voaList.addAll(tempVoas);
			}
		} else {
			voaList = tempVoas;
		}
		if (nla == null && voaList != null) {
			nla = new NewCardListAdapter(mContext, voaList);
			newsList.setAdapter(nla);
			nla.modedownload = true;
		} else if (nla != null) {
			nla.modedownload = true;
			nla.notifyDataSetChanged();
		}
		newsList.setVisibility(View.VISIBLE);
		sentenceList.setVisibility(View.GONE);
		ConfigManagerVOA.Instance(mContext).putInt("collectwhich", 2);// 0是视频
		cloudControl.setVisibility(View.GONE);
	}

	@SuppressLint("NewApi")
	public void showAudio() {
		collect_which = 0;
		//getSherlockActivity().invalidateOptionsMenu();
		ArrayList<Voa> tempVoas = new ArrayList<Voa>();
		tempVoas = (ArrayList<Voa>) vop.findDataByDownload();
		if (voaList != null) {
			voaList.clear();
			if (tempVoas != null) {
				voaList.addAll(tempVoas);
			}
		} else {
			voaList = tempVoas;
		}
		if (nla == null && voaList != null) {
			nla = new NewCardListAdapter(mContext, voaList);
			newsList.setAdapter(nla);
			nla.modedownload = false;
		} else if (nla != null) {
			nla.modedownload = false;
			nla.notifyDataSetChanged();
		}
		newsList.setVisibility(View.VISIBLE);
		sentenceList.setVisibility(View.GONE);
		ConfigManagerVOA.Instance(mContext).putInt("collectwhich", 0);// 0是视频
		cloudControl.setVisibility(View.GONE);
	}

	@SuppressLint("NewApi")
	public void showSentence() {
		// TODO 自动生成的方法存根
		collect_which = 1;
		//getSherlockActivity().invalidateOptionsMenu();
		newsList.setVisibility(View.GONE);
		sentenceList.setVisibility(View.VISIBLE);
		// deleteDel.setVisibility(View.GONE);
		ConfigManagerVOA.Instance(mContext).putInt("collectwhich", 1);// 1是句子
		cloudControl.setVisibility(View.VISIBLE);
	}

}
