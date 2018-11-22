package com.zp.android.base.mvp

import android.support.annotation.CallSuper
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * Created by zhaopan on 2018/10/11.
 */

abstract class RxPresenter<V> : BasePresenter<V> {

    override var view: V? = null
    private val disposables = CompositeDisposable()

    fun launch(job: () -> Disposable) {
        disposables.add(job())
    }

    override fun subscribe(view: V) {
        this.view = view
    }

    @CallSuper
    override fun unSubscribe() {
        disposables.clear()
        view = null
    }
}