package com.zp.android.base.mvp

import android.os.Bundle
import com.zp.android.base.BaseFragment

/**
 * Created by zhaopan on 2018/12/8.
 */

abstract class BaseMVPFragment<T: BasePresenter<BaseView<T>>>: BaseFragment(), BaseView<T> {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onStart() {
        super.onStart()
        presenter?.subscribe(this)  // Bind View
    }

    override fun onStop() {
        presenter?.unSubscribe()
        super.onStop()
    }


}