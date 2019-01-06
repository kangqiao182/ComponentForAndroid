package com.zp.android.component

import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.launcher.ARouter
import com.zp.android.component.service.EmptyUserService
import com.zp.android.component.service.ITestService
import com.zp.android.component.service.IUserService

/**
 * Created by zhaopan on 2018/8/19.
 */

object ServiceManager {

    init {
        //ARouter.getInstance().inject(this)
    }

    @Autowired
    lateinit var testService: ITestService


    /**
     * 两种设置UserService的方式都可用, 各有优缺点. 主要创建时机不同."
     */
    @JvmStatic
    fun getUserService(): IUserService {
        return userService2 ?: userService1 ?: EmptyUserService()
    }

    //通过ARouter注入UserService, 由ARouter来创建并管理. 注: 首次使用时ARouter创建.
    private val userService2: IUserService? by lazy {
        ARouter.getInstance().build(RouterPath.User.SERVICE).navigation() as? IUserService
    }

    //通过AppConfig配置启动的方式, 在ModuleApp初始化时创建UserService并赋值到ServiceManger中.
    // user模块内部使用koin注入单实例的UserService.
    var userService1: IUserService? = null

}