package com.zp.android.lib.statusview

import android.app.Activity
import android.support.annotation.IdRes
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment

/**
 * Created by zhaopan on 2018/12/6.
 */

inline fun Activity.initStatusView() = StatusView.init(this)
inline fun Activity.initStatusView(@IdRes id: Int) = StatusView.init(this, id)
inline fun Fragment.initStatusView(@IdRes id: Int) = StatusView.init(this, id)

inline fun StatusView.setStatusView(index: Int, @LayoutRes layoutId: Int, listener: StatusViewConvertListener) {
    this.setStatusView(index, layoutId)
    this.setOnStatusViewConvertListener(index, listener)
}