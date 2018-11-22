package com.zp.android.project.ui

import com.zp.android.base.mvp.RxPresenter
import com.zp.android.base.utils.RxUtil
import com.zp.android.project.ServerAPI

/**
 * Created by zhaopan on 2018/11/17.
 */

class CategoryListPresenter(
    val serverAPI: ServerAPI
) : RxPresenter<CategoryListContract.View>(), CategoryListContract.Presenter {

    override fun getCategoryList() {
        launch {
            serverAPI.getProjectTree()
                .compose(RxUtil.applySchedulersToObservable())
                .subscribe({
                    if (it.isSuccess()) {
                        view?.setCategoryList(it.data)
                    } else {
                        view?.setCategoryList(null)
                    }
                }, { error ->
                    view?.showError(error)
                })
        }
    }

}