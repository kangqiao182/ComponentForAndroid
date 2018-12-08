package com.zp.android.base

import android.os.Bundle
import android.support.annotation.IdRes
import android.support.annotation.Nullable
import android.view.View
import android.view.View.NO_ID
import me.yokeyword.fragmentation.SupportFragment

/**
 * Created by zhaopan on 2018/8/28.
 */

open abstract class BaseFragment : SupportFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
    }

    open fun initView(view: View) {}
}