package com.zp.android.knowledge

import android.arch.lifecycle.MutableLiveData
import com.zp.android.base.mvvm.*
import com.zp.android.base.utils.RxUtil

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
                    events.value = SuccessEvent
                    knowledgeTreeList.value = it.data
                }, {
                    events.value = FailedEvent(it)
                })
        }
    }

    fun requestArticleData(page: Int, cid: Int){
        events.value = LoadingEvent
        launch {
            server.getKnowledgeList(page, cid)
                .compose(RxUtil.applySchedulersToObservable())
                .subscribe({
                    events.value = SuccessEvent
                    articleData.value = it.data
                }, {
                    events.value = FailedEvent(it)
                })
        }
    }

}