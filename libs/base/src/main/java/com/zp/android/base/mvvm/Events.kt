package com.zp.android.base.mvvm

/**
 * Created by zhaopan on 2018/10/21.
 */

/**
 * Abstract Event from ViewModel
 */
open class ViewModelEvent

/**
 * Generic Loading Event
 */
object LoadingEvent : ViewModelEvent()

/**
 * Generic Success Event
 */
object SuccessEvent : ViewModelEvent()

/**
 * Generic Failed Event
 */
data class FailedEvent(val error: Throwable) : ViewModelEvent()
