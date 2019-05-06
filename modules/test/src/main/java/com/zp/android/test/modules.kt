package com.zp.android.test

import com.zp.android.net.RetrofitHelper
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Created by zhaopan on 2018/11/7.
 */

val moduleList = module(createdAtStart = true) {
    // ViewModel for ViewModel
    viewModel { ViewModel(get()) }

    // Test 网络请求
    single<ServerAPI> { RetrofitHelper.createService(ServerAPI::class.java) }

}
