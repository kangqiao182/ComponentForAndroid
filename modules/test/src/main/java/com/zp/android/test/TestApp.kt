package com.zp.android.test

import android.app.Application
import com.zp.android.base.ModuleApp
import org.koin.standalone.StandAloneContext

/**
 * Created by zhaopan on 2018/11/07.
 */

class TestApp : ModuleApp() {


    override fun onCreate() {
        super.onCreate()
        //todo 独立运行时, 测试环境设置. 此处可设置测试网络.
        initModuleApp(this)
        initModuleData(this)
    }

    override fun initModuleApp(application: Application) {
        //startKoin(application.applicationContext, moduleList)
        StandAloneContext.loadKoinModules(moduleList)
    }

    override fun initModuleData(application: Application) {

    }
}
