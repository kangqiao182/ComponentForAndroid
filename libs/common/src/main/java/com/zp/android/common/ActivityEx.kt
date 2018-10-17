package com.zp.android.common

import android.support.v4.app.FragmentActivity

/**
 * Created by zhaopan on 2018/10/10.
 */

fun <T : Any> FragmentActivity.argument(key: String) =
        lazy { intent.extras[key] as? T ?: error("Intent Argument $key is missing") }