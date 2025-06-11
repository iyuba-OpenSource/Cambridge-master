package com.iyuba.camstory.fragment;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.iyuba.camstory.R;
import com.iyuba.camstory.adpater.HistoryAdapter;
import com.iyuba.camstory.manager.VoaDataManager;
import com.iyuba.camstory.sqlite.mode.Voa;
import com.iyuba.camstory.sqlite.op.VoaDetailOp;
import com.iyuba.camstory.sqlite.op.VoaOp;
import com.quentindommerc.superlistview.SuperListview;
import com.quentindommerc.superlistview.SwipeDismissListViewTouchListener;

public class HistoryListFragment extends Fragment implements OnItemClickListener{
	
	private Context mContext;
	private SuperListview historyListView;
	private HistoryAdapter historyAdapter;
	private ArrayList<Voa> historys;
	private VoaOp voaOp;
	private VoaDetailOp vdop;
	private Voa voa ;
	@Override
	public void onAttach(Activity activity) {
		// TODO 自动生成的方法存根
		super.onAttach(activity);
		mContext = activity;
		voaOp = new VoaOp();
		vdop = new VoaDetailOp();
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		View view = inflater.inflate(R.layout.readhistory,null);
		initView(view);
		return view;
	}
	
/*	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO 自动生成的方法存根
		inflater.inflate(R.menu.history_menu, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int itemId = item.getItemId();
		if (itemId == R.id.history_clearall) {
			if (historys!=null) {
				CustomDialog dialog = new CustomDialog(mContext);
				dialog.setTitle(R.string.alert);
				dialog.setContent("确定清空历史记录吗？");
				dialog.setCancel("取消");
				dialog.setConfirm("确定");
				dialog.setClickListener(new ClickListener() {
					@Override
					public void onConfirmClick() {
						// TODO 自动生成的方法存根
						for (Iterator iterator = historys.iterator(); iterator
								.hasNext();) {
							Voa voa = (Voa) iterator.next();
								voa.isRead = 0;
								
						}
						voaOp.deleteHistory(historys);
						historys.clear();
						if (historyAdapter!=null) {
							historyAdapter.notifyDataSetChanged();
						}
					}
					@Override
					public void onCancelClick() {
						// TODO 自动生成的方法存根
						
					}
				});
				dialog.show();
			}
		} else {
		}
		return super.onOptionsItemSelected(item);
	}*/

	private void initView(View view) {
		// TODO 自动生成的方法存根
		historyListView = view.findViewById(R.id.history_list);
		historyListView.hideProgress();
		historys = voaOp.findReadDataAll();
		if (historys!=null) {
			historyAdapter = new HistoryAdapter(mContext, historys);
			historyListView.setAdapter(historyAdapter);
			historyListView.setOnItemClickListener(this);
			historyListView.setupSwipeToDismiss(new SwipeDismissListViewTouchListener.DismissCallbacks() {
				
				@Override
				public void onDismiss(ListView listView, int[] reverseSortedPositions) {
					// TODO 自动生成的方法存根
					for (int position : reverseSortedPositions) {
						
						voaOp.deleteOneHistory(historys.get(position));
                        historys.remove(position);
                        
                    }
                    historyAdapter = new HistoryAdapter(mContext, historys);
                    historyListView.setAdapter(historyAdapter);
                    
				}
				
				@Override
				public boolean canDismiss(int position) {
					// TODO 自动生成的方法存根
					return true;
					
				}
			}, false);
		}
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO 自动生成的方法存根
		voa = historys.get(position);
		//nla.getView(arg2 - 1, arg1, arg0);
		voa.isRead=System.currentTimeMillis();
		historyAdapter.notifyDataSetChanged();
		VoaDataManager.getInstance().voaTemp = voa;
		handler.sendEmptyMessage(0);
//		VoaDataManager.Instance().setCurrDetail(voa,null);
	}
	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
		/*	Intent intent = new Intent();
			intent.setClass(mContext, StudyActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
			intent.putExtra("source", voa.category);
			mContext.startActivity(intent);
			getActivity().overridePendingTransition(R.anim.popup_enter,R.anim.popup_exit);*/
		}
    };
	
}
