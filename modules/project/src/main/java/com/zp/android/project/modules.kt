package com.zp.android.project

import com.zp.android.base.BaseApp
import com.zp.android.base.utils.SPStorage
import com.zp.android.net.RetrofitHelper
import com.zp.android.project.ui.CategoryTabContract
import com.zp.android.project.ui.CategoryTabPresenter
import com.zp.android.project.ui.ProjectListContract
import com.zp.android.project.ui.ProjectListPresenter
import fr.ekito.myweatherapp.util.coroutines.ApplicationSchedulerProvider
import fr.ekito.myweatherapp.util.coroutines.SchedulerProvider
import org.koin.dsl.module.module

/**
 * Created by zhaopan on 2018/11/7.
 */

@JvmField
val presenterModule = module(path = "Project") {
    // Presenter for category list
    factory<CategoryTabContract.Presenter> { CategoryTabPresenter(get()) }
    // Presenter for project list
    factory<ProjectListContract.Presenter> { ProjectListPresenter() }



}

@JvmField
val dataModule = module(path = "Project", createOnStart = true) {

    // Rx Schedulers
    single<SchedulerProvider>(createOnStart = true) { ApplicationSchedulerProvider() }
    // provided serverAPI
    single { RetrofitHelper.createService(ServerAPI::class.java) }
    // User 模块专属ShardPreferences配置.
    single { SPStorage(BaseApp.application, "Project") }
}

@JvmField
val moduleList = listOf(presenterModule, dataModule)
