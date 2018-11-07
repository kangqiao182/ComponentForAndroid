package com.zp.android.user

import com.zp.android.net.RetrofitHelper
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

/**
 * Created by zhaopan on 2018/11/7.
 */


val viewModule = module(path = "User") {
    // ViewModel for ViewModel
    viewModel { ViewModel(get()) }

}

val dataModule = module(path = "User", createOnStart = true) {
    // HomeApi 网络请求
    single<ServerAPI> { RetrofitHelper.createService(ServerAPI::class.java) }

}

val moduleList = listOf(viewModule, dataModule)
