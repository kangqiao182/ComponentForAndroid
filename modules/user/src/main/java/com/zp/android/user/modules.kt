package com.zp.android.user

import com.zp.android.base.BaseApp
import com.zp.android.base.utils.SPStorage
import com.zp.android.net.RetrofitHelper
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

/**
 * Created by zhaopan on 2018/11/7.
 */


val viewModule = module(path = "User") {
    // ViewModel for ViewModel
    viewModel { ViewModel(get(), get()) }

}

val dataModule = module(path = "User", createOnStart = true) {
    // User 模块专属ShardPreferences配置.
    single<SPStorage> { SPStorage(BaseApp.application, "User") }
    // HomeApi 网络请求
    single<ServerAPI> { RetrofitHelper.createService(ServerAPI::class.java) }
    // UserService
    single<UserService> { UserService(get(), get())}

}

val moduleList = listOf(viewModule, dataModule)
