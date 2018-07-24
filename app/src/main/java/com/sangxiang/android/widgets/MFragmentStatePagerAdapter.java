package com.sangxiang.android.widgets;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MFragmentStatePagerAdapter extends FragmentStatePagerAdapter {
    public static final String KEY_CATEGORY_NAME="key_category_name";
    final List<Fragment> items = new ArrayList<>();
    private String[] tabTitles;
    SparseArray<WeakReference<Fragment>> registeredFragments = new SparseArray<>();

    public MFragmentStatePagerAdapter(FragmentManager fm) {
        super(fm);
    }
    public MFragmentStatePagerAdapter(FragmentManager fm,List<Fragment> list,String[] tabTitles) {
        super(fm);
        this.items.addAll(list);
        this.tabTitles=tabTitles;
    }
    @Override
    public Fragment getItem(int position) {
        return items.get(position);
    }

    public List<Fragment> getItems() {
        return items;
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        registeredFragments.put(position, new WeakReference<>(fragment));
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        registeredFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    // Only return fragments that are still instantiated
    public List<Fragment> getRegisteredFragment() {
        List<Fragment> fragments = new ArrayList<>();
        for (int i = 0; i < registeredFragments.size(); i++) {
            fragments.add(registeredFragments.valueAt(i).get());
        }
        return fragments;
    }

    public void add(Fragment... fragments) {
        Collections.addAll(items, fragments);
        notifyDataSetChanged();
    }

    public void addWithoutNotify(Fragment fragment) {
        items.add(fragment);
    }

    public void clear() {
        items.clear();
        notifyDataSetChanged();
    }

//    @Override
//    public CharSequence getPageTitle(int position) {
//        Bundle arguments = items.get(position).getArguments();
//        if(arguments != null && arguments.getString(KEY_CATEGORY_NAME) != null){
//            return arguments.getString(KEY_CATEGORY_NAME);
//        } else {
//            return super.getPageTitle(position);
//        }
//    }
        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }

    public void clearWithoutNotify() {
        items.clear();
    }
}
