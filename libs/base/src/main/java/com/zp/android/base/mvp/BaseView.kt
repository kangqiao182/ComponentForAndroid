package com.zp.android.base.mvp

/**
 * Created by zhaopan on 2018/10/11.
 */

interface BaseView<out T : BasePresenter<*>> {

    fun showError(error: Throwable)

    val presenter: T
}