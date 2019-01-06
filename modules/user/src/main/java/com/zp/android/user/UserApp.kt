package com.zp.android.user

import android.app.Application
import com.alibaba.android.arouter.launcher.ARouter
import com.zp.android.base.ModuleApp
import com.zp.android.component.ServiceManager
import org.koin.android.ext.android.inject
import org.koin.standalone.StandAloneContext

/**
 * Created by zhaopan on 2018/11/07.
 */

class UserApp : ModuleApp() {

    // 通过Koin单例注入userService, 并在initModuleData中手动设置到ServiceFactory中.
    private val userService: UserService by inject()

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
        ServiceManager.userService1 = userService
    }
}
