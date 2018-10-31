package com.zp.android.common

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import me.yokeyword.fragmentation.SupportFragment

/**
 * Created by zhaopan on 2018/10/30.
 * https://www.jianshu.com/p/3d68d6ec9468
 * 使用这种Adapter，我们的Fragment在切换的时候，不会销毁，而只是调用事务中的detach方法，
 * 这种方法，我们只会把我们的Fragment的view销毁，而保留了以前的Fragment对象。
 * 所以通过这种方式创建的Fragment一直不会被销毁。
 */

class CommonFragmentPageAdapter(
    fm: FragmentManager,
    private val mTabList: List<String>,
    private val mFragmentList: List<SupportFragment>
) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return mFragmentList[position % mFragmentList.size]
    }

    override fun getCount(): Int {
        return mTabList.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return mTabList[position % mTabList.size]
    }
}