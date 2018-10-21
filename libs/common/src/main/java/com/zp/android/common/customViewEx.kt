package com.zp.android.common

import android.view.ViewManager
import com.rengwuxian.materialedittext.MaterialEditText
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

////设置MaterialEditText
inline fun ViewManager.mdEditText(theme: Int = 0) = mdEditText(theme) {}
inline fun ViewManager.mdEditText(theme: Int = 0, init: MaterialEditText.() -> Unit) = ankoView({ MaterialEditText(it) }, theme, init)


