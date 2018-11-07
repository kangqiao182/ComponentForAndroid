package com.zp.android.app.ui

import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.zp.android.base.BaseActivity
import com.zp.android.component.RouterPath

/**
 * Created by zhaopan on 2018/11/1.
 */

@Route(path = RouterPath.APP.LOGIN, name = "登录")
class LoginActivity: BaseActivity() {
    companion object {
        fun open(){
            ARouter.getInstance().build(RouterPath.APP.LOGIN).navigation()
        }
    }
}