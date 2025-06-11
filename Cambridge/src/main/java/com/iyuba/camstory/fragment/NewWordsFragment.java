
package com.iyuba.camstory.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.iyuba.camstory.R;
import com.iyuba.camstory.adpater.WordListAdapter;
import com.iyuba.camstory.frame.CrashApplication;
import com.iyuba.camstory.listener.RequestCallBack;
import com.iyuba.camstory.listener.WordSynRequest;
import com.iyuba.camstory.listener.WordUpdateRequest;
import com.iyuba.camstory.manager.AccountManager;
import com.iyuba.camstory.sqlite.mode.Word;
import com.iyuba.camstory.sqlite.op.WordOp;
import com.iyuba.camstory.utils.NetWorkStateUtil;
import com.iyuba.camstory.widget.CustomDialog;
import com.iyuba.camstory.widget.CustomToast;
import com.iyuba.camstory.widget.WaittingDialog;
import com.iyuba.camstory.widget.WordDialog;
import com.iyuba.voa.activity.setting.Constant;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;


/**
 * 添加单词至单词本Activity
 * 
 * @author lijingwei
 * 
 */

public class NewWordsFragment extends Fragment {
	private Context mContext;
	private ArrayList<Word> words = new ArrayList<Word>();
	private ArrayList<Word> tryToDeleteWords = new ArrayList<Word>();
	private WordOp wo;
	private int page = 0;
	private ListView wordList;
	private WordListAdapter nla;
	private boolean isDelStart = false, isSelectAll = false;
	private WordDialog wordDialog;
	private String userId;
	private CustomDialog wettingDialog;
	private View backView;
	private View view;

	public void onAttach(android.app.Activity activity) {
		mContext = activity;
		super.onAttach(activity);
	}

    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.word_collection_list, container, false);
		initView(view);
		return view;
	}

	protected void initView(View view) {
		backView = view.findViewById(R.id.backlayout);
		backView.setBackgroundColor(Constant.getBackgroundColor());
		wettingDialog = new WaittingDialog().wettingDialog(mContext);
		userId = AccountManager.getInstance().userId + "";
		wordDialog = new WordDialog(mContext, R.style.MyDialogStyle);
		wordList = view.findViewById(R.id.news_list);
		wo = new WordOp();
		try {
			words = (ArrayList<Word>) wo.findDataByPage(
					AccountManager.getInstance().userId, page);
		}catch (Exception e){
			e.printStackTrace();
		}

		if (words != null) {
			page = 1;
		} else {
			page = 0;
			Log.e("words null", "null!!!");
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	/*@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.newwords_menu, menu);
		((SearchView) menu.getItem(0).getActionView())
				.setOnQueryTextListener(new OnQueryTextListener() {

					@Override
					public boolean onQueryTextSubmit(String query) {
						searchAppointWord(query);
						return true;
					}

					@Override
					public boolean onQueryTextChange(String newText) {
						return false;
					}
				});
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.wordmenu_sync) {
			if (AccountManager.getInstance().checkUserLogin()) {
				handler.sendEmptyMessage(0);
			} else {
				Intent intent = new Intent();
				intent.setClass(mContext, LoginActivity.class);
				startActivity(intent);
			}
		}

		else if (item.getItemId() == R.id.wordmenu_edit) {

			getSherlockActivity().startActionMode(new WordEditActionMode());
			// selectAll.setVisibility(View.VISIBLE);
			// delButton.setText(R.string.complete);
			if (nla != null)
				nla.notifyDataSetChanged();
			isDelStart = true;
			changeItemDeleteStaut(true);
			// translateButton.setVisibility(View.GONE);
		}
		return super.onOptionsItemSelected(item);
	}

	class WordEditActionMode implements ActionMode.Callback {

		@Override
		public boolean onCreateActionMode(ActionMode mode, Menu menu) {
			menu.add("删除").setIcon(android.R.drawable.ic_menu_delete)
					.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
			return true;
		}

		@Override
		public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
			return false;
		}

		@Override
		public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
			StringBuffer ids = new StringBuffer("");
			Iterator<Word> iteratorVoa = null;
			try {
				iteratorVoa = words.iterator();
				ArrayList<Word> delWordTemp = new ArrayList<Word>();
				while (iteratorVoa.hasNext()) {
					Word word = iteratorVoa.next();
					if (word.isDelete) {
						delWordTemp.add(word);
						ids.append(",").append("\'" + word.key + "\'");
						iteratorVoa.remove();
					}
				}
				if (ids.toString() != null && ids.toString().length() != 0) {
					isDelStart = false;
					cancelDelete();
					isDelStart = false;
					// delButton.setText(R.string.collection_del);
					changeItemDeleteStaut(false);
					delNetwordWord(delWordTemp);
					wo.tryToDeleteItemWord(ids.toString().substring(1), userId);
					nla.notifyDataSetChanged();
				} else {
					CustomToast.showToast(mContext, R.string.newword_please_select_word,
							1000);
				}
			} catch (Exception e) {// 当初始化单词表中尚无单词是出现异常处理
				CustomToast.showToast(mContext, R.string.no_new_word, 1000);
			}
			// delButton.setText(R.string.collection_del);
			cancelDelete();
			isDelStart = false;
			changeItemDeleteStaut(false);
			// selectAll.setVisibility(View.GONE);
			// translateButton.setVisibility(View.VISIBLE);
			// Toast.makeText(mContext, "Got click: " + item,
			// Toast.LENGTH_SHORT).show();
			mode.finish();
			return false;
		}

		@Override
		public void onDestroyActionMode(ActionMode mode) {
			// TODO 自动生成的方法存根
			cancelDelete();
			isDelStart = false;
			changeItemDeleteStaut(false);
		}

	}
*/
	private void searchAppointWord(String word) {
		// card.setVisibility(View.VISIBLE);
		// card.searchWord(word);
		wordDialog.searchWord(word);
		wordDialog.show();
		// InputMethodManager imm = (InputMethodManager)
		// mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
		// imm.hideSoftInputFromWindow(inputWordTextView.getWindowToken(), 0);
	}

	public void changeItemDeleteStaut(boolean isDelete) {
		if (nla != null) {
			nla.modeDelete = isDelStart;
			nla.notifyDataSetChanged();
		}
	}

	public void cancelDelete() {
		if (words != null && words.size() != 0) {
			for (int i = 0; i < words.size(); i++) {
				words.get(i).isDelete = false;
			}
		}
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("WordsNotePage");
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onPageStart("WordsNotePage"); // 统计页面
		userId = AccountManager.getInstance().userId + "";
		if (wordList != null) {
			isDelStart = false;
			// selectAll.setVisibility(View.GONE);
			// delButton.setText(R.string.collection_del);
			if (nla != null) {
				cancelDelete();
				isDelStart = false;
				changeItemDeleteStaut(false);
			}
			tryToDeleteWords = (ArrayList<Word>) wo.findDataByDelete(userId);
			if (tryToDeleteWords != null && NetWorkStateUtil.isConnectingToInternet()) {
				for (int i = 0; i < tryToDeleteWords.size(); i++) {
					String wordTemp = tryToDeleteWords.get(i).key;
					WordUpdateRequest request = new WordUpdateRequest(
							AccountManager.getInstance().userId,
							WordUpdateRequest.MODE_DELETE, wordTemp, new RequestCallBack() {
								@Override
								public void requestResult(Request result) {
									// TODO 自动生成的方法存根
									WordUpdateRequest rs = (WordUpdateRequest) result;
									wo.deleteItemWord(userId);
								}
							});
					CrashApplication.getInstance().getQueue().add(request);
				}
			}
			if (words != null) {
				nla = new WordListAdapter(mContext, words);
				wordList.setAdapter(nla);
			}
			wordList.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
						long arg3) {
					if (isDelStart) {
                        words.get(arg2).isDelete = !words.get(arg2).isDelete;
						nla.notifyDataSetChanged();
					} else {
						nla.getView(arg2, arg1, arg0);
						nla.viewHolder.def.setText(words.get(arg2).def);
						/*Player player = new Player(mContext, null, null);
						String url = words.get(arg2).audioUrl;
						if (url != null) {

							player.playUrl(url);

						}*/

					}
				}
			});
			wordList.setOnScrollListener(new OnScrollListener() {

				@Override
				public void onScrollStateChanged(AbsListView view, int scrollState) {
					if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
							&& wordList.getLastVisiblePosition() == wordList.getCount() - 1) {
						List<Word> wArrayList = wo.findDataByPage(
								AccountManager.getInstance().userId, page);
						if (wArrayList == null) {
							SynchoWordThread();
						} else {
							words.addAll(wArrayList);
							nla.notifyDataSetChanged();
							page++;
						}
					}
				}

				@Override
				public void onScroll(AbsListView view, int firstVisibleItem,
						int visibleItemCount, int totalItemCount) {
					// TODO 自动生成的方法存根

				}
			});
		}
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				wettingDialog.show();
				SynchoWordThread();
				break;
			case 1:
				wettingDialog.dismiss();
				CustomToast.showToast(mContext, R.string.play_check_network, 1000);
				break;
			case 2:
				wettingDialog.dismiss();
				if (words != null) {
					if (nla != null) {
						nla.notifyDataSetChanged();
					} else {
						nla = new WordListAdapter(mContext, words);
						wordList.setAdapter(nla);
					}
					// wordList.setAdapter(nla);
				}
				CustomToast.showToast(mContext, R.string.new_word_sync_suc, 1000);
				break;
			}
		}
	};

	public void delNetwordWord(ArrayList<Word> wordss) {
		for (int i = 0; i < wordss.size(); i++) {
			String wordTemp = wordss.get(i).key;
			WordUpdateRequest request = new WordUpdateRequest(
					AccountManager.getInstance().userId, WordUpdateRequest.MODE_DELETE,
					wordTemp, new RequestCallBack() {
						@Override
						public void requestResult(Request result) {
							WordUpdateRequest rs = (WordUpdateRequest) result;
							wo.deleteItemWord(userId);
						}
					});
			CrashApplication.getInstance().getQueue().add(request);
		}
	}

	private void SynchoWordThread() {
		WordSynRequest request = new WordSynRequest(
				AccountManager.getInstance().userId, page == 0 ? page = 1 : page,
				new RequestCallBack() {
					@Override
					public void requestResult(Request result) {
						final WordSynRequest wsr = (WordSynRequest) result;
						// words = (ArrayList<Word>) wsr.wordList;
						if (wsr.hasWord()) {
							for (Word word : wsr.wordList) {
								wo.saveData(word);
							}
							if (words == null) {
								words = new ArrayList<Word>();
							}
							getActivity().runOnUiThread(new Runnable() {
								@Override
								public void run() {
									words.clear();
									words.addAll(wsr.wordList);
								}
							});
							handler.sendEmptyMessage(2);
							page++;
						} else {
							CustomToast.showToast(mContext, R.string.new_word_sync_fai, 1000);
							wettingDialog.dismiss();
						}
					}
				});
		CrashApplication.getInstance().getQueue().add(request);
	}
}

