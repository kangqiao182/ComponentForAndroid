package com.zp.android.base.flutter

import io.flutter.plugin.common.MethodChannel.Result
/**
 * Created by zhaopan on 2019/2/18.
 */

interface ResultCallback: Result {
    override fun success(var1: Any?) {}

    override fun error(var1: String, var2: String?, var3: Any?)

    override fun notImplemented() {}
}