package com.zp.android.user

import android.app.Application
import com.zp.android.base.ModuleApp
import com.zp.android.component.ServiceFactory
import org.koin.android.ext.android.inject
import org.koin.standalone.StandAloneContext

/**
 * Created by zhaopan on 2018/11/07.
 */

class UserApp : ModuleApp() {

    //@Deprecated("采用ARouter的@Autowired方式注入到ServiceFactory中. 暂不在ModuleApp中手动注入到ServiceFactory.")
    // 通过Koin单例注入userService, 并在initModuleData中设置到ServiceFactory中.
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
        ServiceFactory.userService = userService
    }
}
