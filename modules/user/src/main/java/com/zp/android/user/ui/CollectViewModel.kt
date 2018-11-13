package com.zp.android.user.ui

import android.arch.lifecycle.MutableLiveData
import com.zp.android.base.mvvm.*
import com.zp.android.base.utils.RxUtil
import com.zp.android.net.exception.ExceptionHandle
import com.zp.android.user.CollectionArticle
import com.zp.android.user.CollectionResponseBody
import com.zp.android.user.ServerAPI

/**
 * Created by zhaopan on 2018/11/9.
 */

class CollectViewModel(
    val server: ServerAPI
) : RxViewModel() {

    val events = SingleLiveEvent<ViewModelEvent>()
    val collectBody = MutableLiveData<CollectionResponseBody<CollectionArticle>>()

    fun getCollectList(page: Int) {
        events.value =  LoadingEvent
        launch {
            server.getCollectList(page)
                .compose(RxUtil.applySchedulersToObservable())
                .subscribe({
                    if(it.isSuccess()){
                        collectBody.value = it.data
                        events.value = SuccessEvent
                    } else {
                        events.value = FailedEvent(it.errorMsg)
                    }
                }, {
                    events.value = FailedEvent(ExceptionHandle.handleException(it))
                })
        }
    }

    fun removeCollectArticle(id: Int, originId: Int) {
        events.value =  LoadingEvent
        launch {
            server.removeCollectArticle(id, originId)
                .compose(RxUtil.applySchedulersToObservable())
                .subscribe({
                    if(it.isSuccess()){
                        events.value = SuccessEvent
                    } else {
                        events.value = FailedEvent(it.errorMsg)
                    }
                }, {
                    events.value = ExceptionEvent(it)
                })
        }
    }
}