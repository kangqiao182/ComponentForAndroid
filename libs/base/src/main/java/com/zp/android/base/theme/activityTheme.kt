package com.zp.android.base.theme

import android.app.Activity
import android.os.Build
import androidx.core.content.ContextCompat
import android.view.View
import com.zp.android.base.R
import com.zp.android.common.StatusBarUtil

/**
 * Created by zhaopan on 2018/9/7.
 */


@Deprecated("参考")
private inline fun setStatusBarStyle(activity: Activity, statusBarColor: Int, uiFlag: Int){
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
        return
    }
    StatusBarUtil.setColorNoTranslucent(activity, statusBarColor)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        activity.window.decorView.systemUiVisibility = uiFlag // if(isDefaultStyle) View.SYSTEM_UI_FLAG_LAYOUT_STABLE else  View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    }
}

inline fun Activity.whiteStatusbarTheme(){
    StatusBarUtil.setColorNoTranslucent(this, ContextCompat.getColor(this, R.color.base_white))
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    }
}

inline fun Activity.blueStatusbarTheme(){
    StatusBarUtil.setColorNoTranslucent(this, ContextCompat.getColor(this, R.color.base_blue))
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
    }
}
