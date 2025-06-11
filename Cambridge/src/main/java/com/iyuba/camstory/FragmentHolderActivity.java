package com.iyuba.camstory;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.iyuba.camstory.fragment.AttentionFragment;
import com.iyuba.camstory.fragment.FansFragment;
import com.iyuba.camstory.fragment.GradeFragment;
import com.iyuba.camstory.fragment.HistoryListFragment;
import com.iyuba.camstory.fragment.LocalDownloadFragment;
import com.iyuba.camstory.fragment.NewWordsFragment;
import com.iyuba.camstory.manager.NightModeManager;

import java.lang.reflect.Field;

public class FragmentHolderActivity extends AppCompatActivity {
    private WindowManager mWindowManager;
    private NightModeManager nightModeManager;

    String fragmentName;
    FragmentManager fm = getSupportFragmentManager();
    //private ActionBar actionBar;
    private Context mContext;
    private LocalDownloadFragment localFragment;
    private GradeFragment achieveFragment;
    private SpannableString ss;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragmentholder);

        Toolbar toolBar = findViewById(R.id.toolbar);
        //getSupportActionBar().setTitle("");
        setSupportActionBar(toolBar);
        getSupportActionBar().setTitle("关注");
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mContext = this;
        mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        nightModeManager = new NightModeManager(mWindowManager, mContext);
        fragmentName = getIntent().getExtras().getString("fragmentname");

        //initActionbar();

        if (fragmentName == null) {
            return;
        }
        if (fragmentName.equals("word")) {
            FragmentTransaction transaction = fm.beginTransaction();
            transaction.replace(R.id.fragmentholder, new NewWordsFragment());

            //actionBar.setTitle(R.string.main_tab_words);
            transaction.commit();
        } else if (fragmentName.equals("fanslist")) {
            FragmentTransaction transaction = fm.beginTransaction();
            transaction.replace(R.id.fragmentholder, new FansFragment());
            ss = new SpannableString("我的粉丝");
            ss.setSpan(new ForegroundColorSpan(Color.WHITE), 0, ss.length(),
                    Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            //actionBar.setTitle(ss);
            transaction.commit();
        } else if (fragmentName.equals("attentionlist")) {
            FragmentTransaction transaction = fm.beginTransaction();
            transaction.replace(R.id.fragmentholder, new AttentionFragment());
            ss = new SpannableString("我的关注");
            ss.setSpan(new ForegroundColorSpan(Color.WHITE), 0, ss.length(),
                    Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            //actionBar.setTitle(ss);
            transaction.commit();
        } else if (fragmentName.equals("localdownload")) {
            FragmentTransaction transaction = fm.beginTransaction();
            localFragment = new LocalDownloadFragment();
            transaction.replace(R.id.fragmentholder, localFragment);
            /*actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
			ArrayAdapter<CharSequence> list = ArrayAdapter.createFromResource(
					mContext, R.array.localarray, R.layout.sherlock_spinner_item);
			list.setDropDownViewResource(R.layout.sherlock_spinner_dropdown_item);
			getSupportActionBar().setListNavigationCallbacks(list,
					new OnNavigationListener() {
						@Override
						public boolean onNavigationItemSelected(int itemPosition,
								long itemId) {
							switch (itemPosition) {
							case 0:
								localFragment.showAudio();
								break;
							case 1:
								localFragment.showUnFinish();
								break;
							case 2:
								localFragment.showSentence();
								break;
							default:
								break;
							}
							return false;
						}
					});
			actionBar.setTitle("本地文章");*/
            transaction.commit();
        } else if (fragmentName.equals("history")) {
            FragmentTransaction transaction = fm.beginTransaction();
            transaction.replace(R.id.fragmentholder, new HistoryListFragment());
            //actionBar.setTitle("历史记录");
            transaction.commit();
        } else
//			if (fragmentName.equals("collect")) {
//			FragmentTransaction transaction = fm.beginTransaction();
//			transaction.replace(R.id.fragmentholder, new CollectionFragment());
//			actionBar.setTitle("本地收藏");
//			transaction.commit();
//		} else 
            if (fragmentName.equals("grade")) {
                FragmentTransaction transaction = fm.beginTransaction();
                achieveFragment = new GradeFragment();
                transaction.replace(R.id.fragmentholder, achieveFragment);
			/*ArrayAdapter<CharSequence> list = ArrayAdapter.createFromResource(
					mContext, R.array.gradearray, R.layout.sherlock_spinner_item);
			list.setDropDownViewResource(R.layout.sherlock_spinner_dropdown_item);
			getSupportActionBar().setListNavigationCallbacks(list,
					new OnNavigationListener() {

						@Override
						public boolean onNavigationItemSelected(int itemPosition,
								long itemId) {
							switch (itemPosition) {
							case 0:
								achieveFragment.showRanking();
								break;
							case 1:
								achieveFragment.showCounting();
								break;

							default:
								break;
							}
							return false;
						}
					});
			actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
			actionBar.setTitle("我的成绩");*/
                transaction.commit();
            }
    }

	/*private void initActionbar() {
		setOverflowShowingAlways();

		actionBar = this.getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayShowHomeEnabled(true);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setDisplayUseLogoEnabled(true);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setBackgroundDrawable(getResources().getDrawable(
				R.color.camstorygreen));
	}*/

    private void setOverflowShowingAlways() {
        ViewConfiguration config = ViewConfiguration.get(mContext);
        try {
            Field menuKeyField = ViewConfiguration.class
                    .getDeclaredField("sHasPermanentMenuKey");
            menuKeyField.setAccessible(true);
            menuKeyField.setBoolean(config, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	/*@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;
		default:
			break;
		}
		return false;
	}*/

    public void onResume() {
        super.onResume();
        nightModeManager.checkMode();
    }

    public void finish() {
        super.finish();
        nightModeManager.remove();
    }
}
