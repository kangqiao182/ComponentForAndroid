package com.zp.android.test

import com.zp.android.net.RetrofitHelper
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

/**
 * Created by zhaopan on 2018/11/7.
 */


val viewModule = module(path = "Test") {
    // ViewModel for ViewModel
    viewModel { ViewModel(get()) }

}

val dataModule = module(path = "Test", createOnStart = true) {
    // Test 网络请求
    single<ServerAPI> { RetrofitHelper.createService(ServerAPI::class.java) }

}

val moduleList = listOf(viewModule, dataModule)
