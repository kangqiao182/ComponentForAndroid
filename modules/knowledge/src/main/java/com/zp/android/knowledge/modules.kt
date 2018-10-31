package com.zp.android.knowledge

import com.zp.android.net.RetrofitHelper
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

/**
 * Created by zhaopan on 2018/10/28.
 */

val viewModule = module {
    // ViewModel for ViewModel
    viewModel { ViewModel(get()) }

}

val dataModule = module(createOnStart = true) {
    // HomeApi 网络请求
    single<ServerAPI> { RetrofitHelper.createService(ServerAPI::class.java) }

}

val moduleList = listOf(viewModule, dataModule)
