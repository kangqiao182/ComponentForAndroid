package com.zp.android.component

import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.launcher.ARouter
import com.zp.android.component.service.EmptyUserService
import com.zp.android.component.service.ITestService
import com.zp.android.component.service.IUserService

/**
 * Created by zhaopan on 2018/8/19.
 */

object ServiceFactory {

    init {
        ARouter.getInstance().inject(this)
    }

    @Autowired
    lateinit var testService: ITestService


    //@Autowired
    var userService: IUserService = EmptyUserService()
}