package com.zp.android.common;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.Arrays;
import java.util.List;

import me.yokeyword.fragmentation.SupportFragment;

/**
 * Created by zhaopan on 2016/4/12.
 */

public class CommonFragmentPageAdapter extends FragmentPagerAdapter {
    private List<String> mTabList;
    private List<SupportFragment> mFragmentList;

    public CommonFragmentPageAdapter(FragmentManager fm, List<String> tabList, List<SupportFragment> fragmentList) {
        super(fm);
        mTabList = tabList;
        mFragmentList = fragmentList;
    }

    public CommonFragmentPageAdapter(FragmentManager fm, String[] tabs, SupportFragment[] fragments) {
        this(fm, Arrays.asList(tabs), Arrays.asList(fragments));
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position % mFragmentList.size());
    }

    @Override
    public int getCount() {
        return mTabList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTabList.get(position % mTabList.size());
    }
}