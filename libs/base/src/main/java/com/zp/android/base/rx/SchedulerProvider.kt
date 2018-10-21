package com.zp.android.base.rx

import kotlinx.coroutines.experimental.CoroutineDispatcher

/**
 * Created by zhaopan on 2018/10/21.
 * Rx Scheduler Provider
 */
interface SchedulerProvider {
    fun ui(): CoroutineDispatcher
}