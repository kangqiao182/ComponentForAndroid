package com.zp.android.test

import android.app.Application
import com.zp.android.base.ModuleApp
import org.koin.core.context.loadKoinModules

/**
 * Created by zhaopan on 2018/11/07.
 */

class TestApp : ModuleApp() {

    override fun onCreate() {
        super.onCreate()
        initModuleApp(this)
        initModuleData(this)
        //todo 独立运行时, 测试环境设置. 此处可设置测试网络.
    }

    override fun initModuleApp(application: Application) {
        loadKoinModules(moduleList)
    }

    override fun initModuleData(application: Application) {

    }
}
