package com.zp.android.knowledge.ui

import android.arch.lifecycle.MutableLiveData
import com.zp.android.base.mvvm.*
import com.zp.android.base.utils.RxUtil
import com.zp.android.knowledge.ArticleResponseBody
import com.zp.android.knowledge.KnowledgeTreeBody
import com.zp.android.knowledge.ServerAPI

/**
 * Created by zhaopan on 2018/10/28.
 */

class ViewModel(
    val server: ServerAPI
) : RxViewModel() {

    val events = SingleLiveEvent<ViewModelEvent>()
    val knowledgeTreeList = MutableLiveData<List<KnowledgeTreeBody>>()
    val articleData = MutableLiveData<ArticleResponseBody>()

    fun loadKnowledgeTree() {
        events.value = LoadingEvent
        launch {
            server.getKnowledgeTree()
                .compose(RxUtil.applySchedulersToObservable())
                .subscribe({
                    if(it.isSuccess()) {
                        knowledgeTreeList.value = it.data
                        events.value = SuccessEvent
                    } else {
                        events.value = FailedEvent(it.errorMsg)
                    }
                }, {
                    events.value = ExceptionEvent(it)
                })
        }
    }

    fun requestArticleData(page: Int, cid: Int){
        events.value = LoadingEvent
        launch {
            server.getKnowledgeList(page, cid)
                .compose(RxUtil.applySchedulersToObservable())
                .subscribe({
                    if(it.isSuccess()){
                        articleData.value = it.data
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