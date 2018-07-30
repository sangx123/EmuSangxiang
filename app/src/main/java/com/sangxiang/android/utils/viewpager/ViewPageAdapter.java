package com.sangxiang.android.utils.viewpager;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class ViewPageAdapter extends PagerAdapter {
    private final Context context;
    protected List<View> mViewList;
    private int[] tabTitles;

    public ViewPageAdapter(Context context, List<View> mViewList, int[] tabTitles) {
        this.mViewList = mViewList;
        this.context = context;
        this.tabTitles = tabTitles;
    }

    @Override
    public int getCount() {
        return mViewList.size(); //页卡数
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(mViewList.get(position));
        return mViewList.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mViewList.get(position));
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return context.getString(tabTitles[position]);
    }
}
