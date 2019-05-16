package com.zp.android.home.ui

import androidx.lifecycle.MutableLiveData
import com.zp.android.base.mvvm.*
import com.zp.android.base.utils.RxUtil
import com.zp.android.home.ArticleResponseBody
import com.zp.android.home.BannerItem
import com.zp.android.home.HomeApi

/**
 * Created by zhaopan on 2018/10/21.
 */

class HomeViewModel(
    val homeApi: HomeApi
) : RxViewModel() {

    val events =  SingleLiveEvent<ViewModelEvent>()
    val articleData = MutableLiveData<ArticleResponseBody>()
    val bannerList = MutableLiveData<List<BannerItem>>()

    fun getArticleData(num: Int){
        if (0 == num) events.value = LoadingEvent
        launch {
            homeApi.getArticles(num)
                .compose(RxUtil.applySchedulersToObservable())
                .subscribe({
                    if(it.isSuccess()){
                        events.value = SuccessEvent
                        articleData.value = it.data
                    } else {
                        events.value = FailedEvent(it.errorMsg)
                    }
                }, {
                    events.value = ExceptionEvent(it)
                })
        }
    }

    fun getBannerList(){
        launch {
            homeApi.getBanners()
                //.retryWhen(RxUtil.retryAndDelay())
                .compose(RxUtil.applySchedulersToObservable())
                .subscribe({
                    if(it.isSuccess()){
                        bannerList.value = it.data
                    }
                }, {
                    events.value = ExceptionEvent(it)
                })
        }
    }


}