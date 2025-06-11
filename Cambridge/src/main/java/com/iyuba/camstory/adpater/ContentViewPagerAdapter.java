package com.iyuba.camstory.adpater;



import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.iyuba.camstory.MainActivity;
import com.iyuba.camstory.fragment.ContentFragment;

/**
 * Description :
 *
 * @author Hao Zhao
 * @date 2020/2/5
 */

//为该界面的viewpager设置adapter因为是添加的fragment,所以继承自该adapter
public class ContentViewPagerAdapter extends FragmentStatePagerAdapter {
    private String[] mTitles;

    private String type;

    public ContentViewPagerAdapter(FragmentManager fm, String[] titles, MainActivity.BookType bookType) {
        super(fm);
        mTitles=titles;
        type = bookType.name();
    }

    @Override
    public int getCount() {
        return mTitles.length;
    }

    @Override
    //设置要添加到那一个position
    public Fragment getItem(int position) {
        //ContentFragment用来添加Fragment对应每本小说
        return ContentFragment.newInstance(position,type);
    }

    @Override
    public CharSequence getPageTitle(int position) {

        return mTitles[position];
    }
}
