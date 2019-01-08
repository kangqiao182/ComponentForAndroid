package com.zp.android.base.common

import android.app.Application
import android.util.Log
import com.zp.android.base.utils.CrashHandler
import com.zp.android.common.Supplier
import org.jetbrains.annotations.NonNls
import timber.log.Timber


/**
 * Created by zhaopan on 2019/1/8.
 */

fun Application.initLogger(isDebug: Boolean = true) {
    if (isDebug)
        Timber.plant(Timber.DebugTree())
    else
        Timber.plant(CrashReportingTree())

    log { "initLogger successfully, isDebug = $isDebug" }
}

inline fun log(supplier: Supplier<String>) = logd(supplier)

inline fun logd(supplier: Supplier<String>) = Timber.d(supplier())

inline fun logi(supplier: Supplier<String>) = Timber.i(supplier())

inline fun logw(supplier: Supplier<String>) = Timber.w(supplier())

inline fun loge(supplier: Supplier<String>) = Timber.e(supplier())

inline fun log(@NonNls message: String, vararg args: Any) = logd(message, args)
inline fun logd(@NonNls message: String, vararg args: Any) = Timber.d(message, args)
inline fun logi(@NonNls message: String, vararg args: Any) = Timber.i(message, args)
inline fun logw(@NonNls message: String, vararg args: Any) = Timber.w(message, args)
inline fun loge(@NonNls message: String, vararg args: Any) = Timber.e(message, args)


class CrashReportingTree : Timber.Tree() {

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        if (priority == Log.VERBOSE || priority == Log.DEBUG) {
            return
        }

        FakeCrashLibrary.log(priority, tag, message)

        if (t != null) {
            if (priority == Log.ERROR) {
                FakeCrashLibrary.logError(t)
            } else if (priority == Log.WARN) {
                FakeCrashLibrary.logWarning(t)
            }
        }
    }
}


object FakeCrashLibrary {
    fun log(priority: Int, tag: String?, message: String) {

    }

    fun logWarning(t: Throwable) {

    }

    fun logError(t: Throwable) {
        CrashHandler.uncaughtException(Thread.currentThread(), t)
    }
}