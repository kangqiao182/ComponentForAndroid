package com.zp.android.base

import android.content.Context
import android.support.v4.app.Fragment
import com.zp.android.base.widget.CustomToast

/**
 * Created by zhaopan on 2018/11/9.
 */

fun Fragment.showToast(content: String) {
    CustomToast(this?.activity?.applicationContext, content).show()
}

fun Context.showToast(content: String) {
    CustomToast(this, content).show()
}