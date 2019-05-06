package com.zp.android.user

import com.zp.android.base.BaseApp
import com.zp.android.base.utils.SPStorage
import com.zp.android.net.RetrofitHelper
import com.zp.android.user.ui.CollectViewModel
import com.zp.android.user.ui.UserViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Created by zhaopan on 2018/11/7.
 */

val moduleList = module(createdAtStart = true) {
    // UserViewModel for UserViewModel
    viewModel { UserViewModel(get(), get()) }
    viewModel { CollectViewModel(get()) }

    // User 模块专属ShardPreferences配置.
    single { SPStorage(BaseApp.application, "User") }
    // HomeApi 网络请求
    single { RetrofitHelper.createService(ServerAPI::class.java) }
    // UserService
    single { UserService(get(), get())}
}