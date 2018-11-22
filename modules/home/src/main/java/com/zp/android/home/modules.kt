package com.zp.android.home

import com.zp.android.net.RetrofitHelper
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

/**
 * Created by zhaopan on 2018/10/21.
 */

val homeModule = module {
    // ViewModel for HomeFragment
    viewModel { HomeViewModel(get()) }

}

val dataModule = module(createOnStart = true) {
    // HomeApi 网络请求
    single<HomeApi> { RetrofitHelper.createService(HomeApi::class.java) }

}

val moduleList = listOf(homeModule, dataModule)
