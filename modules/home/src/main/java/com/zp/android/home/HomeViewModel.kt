package com.zp.android.home

import android.arch.lifecycle.MutableLiveData
import com.zp.android.base.mvvm.*
import com.zp.android.base.utils.RxUtil

/**
 * Created by zhaopan on 2018/10/21.
 */

class HomeViewModel(
    val homeApi: HomeApi
) : RxViewModel() {

    val states =  SingleLiveEvent<ViewModelEvent>()
    val articleList = MutableLiveData<ArticleResponseBody>()
    val bannerList = MutableLiveData<List<BannerItem>>()

    fun getArticleData(num: Int){
        states.value = LoadingEvent
        launch {
            homeApi.getArticles(num)
                .compose(RxUtil.applySchedulersToObservable())
                .subscribe({
                    states.value = SuccessEvent
                    articleList.value = it.data
                }, {
                    states.value = FailedEvent(it)
                })
        }
    }

    fun getBannerList(){
        states.value = LoadingEvent
        launch {
            homeApi.getBanners()
                //.retryWhen(RxUtil.retryAndDelay())
                .compose(RxUtil.applySchedulersToObservable())
                .subscribe({
                    bannerList.value = it.data
                }, {
                    states.value = FailedEvent(it)
                })
        }
    }


}