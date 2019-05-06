package com.zp.android.home

import com.zp.android.home.ui.HomeViewModel
import com.zp.android.net.RetrofitHelper
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Created by zhaopan on 2018/10/21.
 */

val moduleList = module {
    // ViewModel for HomeFragment
    viewModel { HomeViewModel(get()) }

    // HomeApi 网络请求
    single<HomeApi> { RetrofitHelper.createService(HomeApi::class.java) }
}
