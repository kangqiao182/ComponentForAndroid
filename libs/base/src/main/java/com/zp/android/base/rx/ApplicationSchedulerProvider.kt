package com.zp.android.base.rx

import kotlinx.coroutines.experimental.android.UI

/**
 * Created by zhaopan on 2018/10/21.
 * Application providers
 */

class ApplicationSchedulerProvider : SchedulerProvider {
    override fun ui() = UI
}