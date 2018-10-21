package com.zp.android.base.rx

import android.arch.lifecycle.ViewModel
import kotlinx.coroutines.experimental.CoroutineScope
import kotlinx.coroutines.experimental.Job

/**
 * Created by zhaopan on 2018/10/21.
 * ViewModel for Coroutines Jobs
 *
 * launch() - launch a Rx request clear all request on stop
 */
abstract class CoroutineViewModel(private val schedulerProvider: SchedulerProvider) : ViewModel() {
    var jobs = listOf<Job>()

    fun launch(code: suspend CoroutineScope.() -> Unit) {
        jobs += kotlinx.coroutines.experimental.launch(schedulerProvider.ui(), block = code)
    }

    override fun onCleared() {
        super.onCleared()
        jobs.forEach { it.cancel() }
    }
}