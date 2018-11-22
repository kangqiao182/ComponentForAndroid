package com.zp.android.project.ui

import com.zp.android.base.mvp.BasePresenter
import com.zp.android.base.mvp.BaseView
import com.zp.android.project.ProjectTreeBean

/**
 * Created by zhaopan on 2018/11/17.
 */

interface CategoryListContract{

    interface View : BaseView<Presenter> {
        fun setCategoryList(list: List<ProjectTreeBean>?)
    }

    interface Presenter : BasePresenter<View> {
        fun getCategoryList()
    }
}