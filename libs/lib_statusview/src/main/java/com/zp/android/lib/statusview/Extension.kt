package com.zp.android.lib.statusview

import android.app.Activity
import android.support.annotation.IdRes
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.view.View

/**
 * Created by zhaopan on 2018/12/6.
 * https://github.com/SheHuan/StatusView
 * 当 Fragment 布局文件的根 View 使用 StatusView 时，为避免出现的异常问题，建议在 XML 中初始化！
 * 当直接在 Fragment 中使用时，init()方法需要在onCreateView()之后的生命周期方法中执行！
 */

inline fun Activity.initStatusView() = StatusView.init(this)
inline fun Activity.initStatusView(@IdRes id: Int) = StatusView.init(this, id)
@Deprecated("慎用, 必须保证view是Activity布局中的子View")
fun Activity.initStatusView(view: View) = StatusView.init(view)
inline fun Fragment.initStatusView(@IdRes id: Int) = StatusView.init(this, id)
@Deprecated("慎用, 必须保证view为Fragment布局中的子View, 且不能是Fragment的根布局View")
fun Fragment.initStatusView(view: View) = StatusView.init(view)


//自定义状态页面配置
fun showStatusView(statusView: StatusView, index: Int, @LayoutRes layoutId: Int, listener: StatusViewConvertListener) {
    statusView.setStatusView(index, layoutId)
    statusView.setOnStatusViewConvertListener(index, listener)
    statusView.showStatusView(index)
}

fun Activity.showStatusView(index: Int, @LayoutRes layoutId: Int,  @IdRes id: Int? = null, listener: StatusViewConvertListener) {
    val statusView: StatusView = if(null == id) StatusView.init(this) else StatusView.init(this, id)
    showStatusView(statusView, index, layoutId, listener)
}

fun Fragment.showStatusView(index: Int, @LayoutRes layoutId: Int, @IdRes id: Int, listener: StatusViewConvertListener) {
    val statusView: StatusView = StatusView.init(this, id)
    showStatusView(statusView, index, layoutId, listener)
}

//显示加载中页面
fun showLoadingView(statusView: StatusView, @LayoutRes layoutId: Int? = R.layout.sv_empty_layout, listener: StatusViewConvertListener? = null){
    if (null != layoutId) statusView.setLoadingView(layoutId)
    statusView.setOnLoadingViewConvertListener(listener)
    statusView.showLoadingView()
}

fun Activity.showLoadingView(@IdRes id: Int, @LayoutRes layoutId: Int? = null, listener: StatusViewConvertListener? = null ) {
    val statusView = StatusView.init(this, id)
    showLoadingView(statusView, layoutId, listener)
}

fun Fragment.showLoadingView(@IdRes id: Int, @LayoutRes layoutId: Int? = null, listener: StatusViewConvertListener? = null ) {
    val statusView = StatusView.init(this, id)
    showLoadingView(statusView, layoutId, listener)
}

//显示空页面
fun showEmptyView(statusView: StatusView, @LayoutRes layoutId: Int? = R.layout.sv_empty_layout, listener: StatusViewConvertListener? = null){
    if (null != layoutId) statusView.setEmptyView(layoutId)
    statusView.setOnEmptyViewConvertListener(listener)
    statusView.showEmptyView()
}

fun Activity.showEmptyView(@IdRes id: Int, @LayoutRes layoutId: Int? = null, listener: StatusViewConvertListener? = null ) {
    val statusView = StatusView.init(this, id)
    showEmptyView(statusView, layoutId, listener)
}

fun Fragment.showEmptyView(@IdRes id: Int, @LayoutRes layoutId: Int? = null, listener: StatusViewConvertListener? = null ) {
    val statusView = StatusView.init(this, id)
    showEmptyView(statusView, layoutId, listener)
}

//显示错误页面
fun showErrorView(statusView: StatusView, @LayoutRes layoutId: Int? = R.layout.sv_empty_layout, listener: StatusViewConvertListener? = null){
    if (null != layoutId) statusView.setErrorView(layoutId)
    statusView.setOnErrorViewConvertListener(listener)
    statusView.showErrorView()
}

fun Activity.showErrorView(@IdRes id: Int, @LayoutRes layoutId: Int? = null, listener: StatusViewConvertListener? = null ) {
    val statusView = StatusView.init(this, id)
    showErrorView(statusView, layoutId, listener)
}

fun Fragment.showErrorView(@IdRes id: Int, @LayoutRes layoutId: Int? = null, listener: StatusViewConvertListener? = null ) {
    val statusView = StatusView.init(this, id)
    showErrorView(statusView, layoutId, listener)
}