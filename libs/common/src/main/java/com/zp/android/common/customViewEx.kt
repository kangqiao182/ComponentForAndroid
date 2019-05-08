package com.zp.android.common

import android.view.ViewManager
import com.just.agentweb.NestedScrollAgentWebView
import com.youth.banner.Banner
import org.jetbrains.anko.custom.ankoView

/**
 * Created by zhaopan on 2018/9/5.
 */

//Banner 轮播图
inline fun ViewManager.banner(theme: Int = 0) = banner(theme) {}
inline fun ViewManager.banner(theme: Int = 0, init: Banner.() -> Unit): Banner {
    return ankoView({ Banner(it) }, theme = theme, init = init)
}

////设置ItemView
//inline fun ViewManager.settingItem(theme: Int = 0) = settingItem(theme) {}
//inline fun ViewManager.settingItem(theme: Int = 0, init: LSettingItem.() -> Unit) = ankoView({ LSettingItem(it) }, theme, init)


inline fun ViewManager.nestedScrollAgentWebView(): NestedScrollAgentWebView = nestedScrollAgentWebView() {}
inline fun ViewManager.nestedScrollAgentWebView(init: (NestedScrollAgentWebView).() -> Unit): NestedScrollAgentWebView {
    return ankoView({NestedScrollAgentWebView(it)}, theme = 0) { init() }
}


