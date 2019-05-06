package com.zp.android.knowledge

import com.zp.android.knowledge.ui.ViewModel
import com.zp.android.net.RetrofitHelper
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Created by zhaopan on 2018/10/28.
 */

val moduleList = module(createdAtStart = true) {
    // ViewModel for ViewModel
    viewModel { ViewModel(get()) }

    // HomeApi 网络请求
    single<ServerAPI> { RetrofitHelper.createService(ServerAPI::class.java) }

}